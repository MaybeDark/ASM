package org.bytecode.attributes.code;

import org.Type;
import org.bytecode.MethodWriter;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.LocalVariableTable;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.attributes.bootstrapmethods.CallSite;
import org.bytecode.attributes.code.bracket.Bracket;
import org.bytecode.attributes.code.bracket.BracketStack;
import org.bytecode.attributes.code.bracket.IfBracket;
import org.bytecode.attributes.code.instruction.CodeHelper;
import org.bytecode.attributes.code.instruction.Instruction;
import org.bytecode.attributes.code.instruction.InstructionSet;
import org.bytecode.attributes.code.instruction.Operator;
import org.bytecode.attributes.stackmaptable.StackMapTable;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolClassInfo;
import org.exception.TypeErrorException;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ByteVectors;
import org.tools.ConvertTool;
import org.visitor.AttributeHelper;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.*;

public class Code extends VariableLengthAttribute {
    private Map<String, Attribute> attributes = new HashMap<>();
    private BracketStack bracketStack = new BracketStack();

    private OperandStack stack = new OperandStack();

    private LocalVariableTable locals = new LocalVariableTable();

    private int jumpPoint = - 1;

    private boolean isEnd = false;

    private boolean needStackMapTable = false;

    private ExceptionTable exceptionTable = new ExceptionTable();

    private int codeLength = 0;

    private List<Instruction> instructions = new LinkedList<>();

    private CodeHelper codeHelper = CodeHelper.getHelper();

    private Instruction returnInst;

    public Code() {
        super(Target.method_info);
        attributeName = "Code";
    }

    /**
     * 如果不是通过visitor新建则需通过此方法初始化
     */
    public Code initByWriter(MethodWriter owner) {
        if (! owner.isStatic()) {
            locals.put(new LocalVariableWrapper("this", Type.getClassDescriptor(owner.getClassName())));
        }

        if (ArrayTool.notNull(owner.getParameterTypes())) {
            for (LocalVariableWrapper localVariableWrapper : owner.getParameterTypes()) {
                locals.put(localVariableWrapper);
            }
        }

        bracketStack.put(new Bracket(0));
        returnInst = codeHelper.return0(owner.getReturnType());
        attributes.put("LocalVariableTable", locals);
        initStackMapTable();
        return this;
    }

    private void initStackMapTable() {
        //TODO
    }

    public void appendInstruction(Instruction instruction) {
        if (isEnd) {
            throw new RuntimeException("code is end can't modify");
        }
        instructions.add(instruction);
        codeLength += instruction.length;
    }

    public void loadNull() {
        stack.put(Type.NULL);
        appendInstruction(codeHelper.loadNull());
    }

    public void loadInt(int num) {
        stack.put(Type.INT);
        appendInstruction(codeHelper.loadInt(num));
    }

    public void loadDouble(double num) {
        stack.put(Type.DOUBLE);
        appendInstruction(codeHelper.loadDouble(num));
    }

    public void loadLong(long num) {
        stack.put(Type.LONG);
        appendInstruction(codeHelper.loadLong(num));
    }

    public void loadFloat(float num) {
        stack.put(Type.FLOAT);
        appendInstruction(codeHelper.loadFloat(num));
    }

    public void loadString(String str) {
        stack.put(Type.STRING);
        appendInstruction(codeHelper.loadString(str));
    }

    public void loadLocal(String localName) {
        LocalVariableWrapper local = locals.getLocalVariableByName(localName);
        Type type = local.getType();
        stack.put(type);
        appendInstruction(codeHelper.loadLocal(type, local.getTableIndex()));
    }

    public void loadArrayElement() {
        stack.pop(); //index
        Type arrayType = stack.pop();
        Type componentType = Type.getElementType(arrayType);
        stack.put(componentType);
        appendInstruction(codeHelper.loadArrayElement(componentType));
    }

    public void loadField(FieldWrapper fieldWrapper, boolean isStatic) {
        if (! isStatic) {
            stack.pop(); //objectRef
        }
        stack.put(fieldWrapper.getType());
        appendInstruction(codeHelper.loadField(fieldWrapper, isStatic));
    }

    public void loadClass(ClassWrapper classWrapper) {
        stack.put(Type.getType(Class.class));
        appendInstruction(codeHelper.loadClass(classWrapper));
    }

    public void storeLocal(String localName) {
        Type type = stack.pop();
        short index = locals.put(new LocalVariableWrapper(localName, type));
        appendInstruction(codeHelper.storeLocal(type, index));
    }

    public void storeArrayElement() {
        Type valueType = stack.pop();
        stack.pop(); //index
        stack.pop(); //arrayRef
        appendInstruction(codeHelper.storeArrayElement(valueType));
    }

    public void storeField(FieldWrapper fieldWrapper, boolean isStatic) {
        if (!isStatic) {
            stack.pop(); //objectRef
        }
        stack.pop(); //value
        appendInstruction(codeHelper.storeField(fieldWrapper, isStatic));
    }

    public void checkCast(ClassWrapper classWrapper) {
        stack.pop(); //objectRef
        stack.put(classWrapper.getType()); //converted
        appendInstruction(codeHelper.checkCast(classWrapper));
    }

    public void stackBehavior(InstructionSet instructionSet) {
        Type pop1, pop2, pop3, pop4;
        switch (instructionSet.getOpcode()) {
            case 87: {
                pop1 = stack.pop();
                if (pop1.isLongOrDoubleType())
                    throw new TypeErrorException("The pop instruction must not be used unless value is a value of a category 1 computational type");
                break;
            }
            case 88:
                pop1 = stack.pop();
                if (!pop1.isLongOrDoubleType())
                    stack.pop();
                break;
            case 89: {
                pop1 = stack.pop();
                if (pop1.isLongOrDoubleType())
                    throw new TypeErrorException("The dup instruction must not be used unless value is a value of a category 1 computational type");
                stack.put(pop1);
                stack.put(pop1);
            }
            break;
            case 90: {
                pop1 = stack.pop();
                pop2 = stack.pop();
                if (pop1.isLongOrDoubleType())
                    throw new TypeErrorException("The dup_x1 instruction must not be used unless value1 is a value of a category 1 computational type");
                if (pop2.isLongOrDoubleType())
                    throw new TypeErrorException("The dup_x1 instruction must not be used unless value2 is a value of a category 1 computational type");
                stack.put(pop1);
                stack.put(pop2);
                stack.put(pop1);
            }
            break;
            case 91: {
                pop1 = stack.pop();
                pop2 = stack.pop();
                if (pop1.isLongOrDoubleType())
                    throw new TypeErrorException("The dup_x2 instruction must not be used unless value1 is a value of a category 1 computational type");
                if (!(pop2.isLongOrDoubleType()))
                    throw new TypeErrorException("The dup_x2 instruction must not be used unless value2 not is a value of a category 2 computational type");
                stack.put(pop1);
                stack.put(pop2);
                stack.put(pop1);
            }
            break;
            case 92: {
                pop1 = stack.pop();
                if (pop1.isLongOrDoubleType()) {
                    stack.put(pop1);
                } else {
                    pop2 = stack.pop();
                    if (pop2.isLongOrDoubleType())
                        throw new TypeErrorException("The dup2 instruction must not be used unless value2 is a value of a category 1 computational type");
                    stack.put(pop2);
                    stack.put(pop1);
                    stack.put(pop2);
                }
                stack.put(pop1);
            }
            break;
            case 93: {
                pop1 = stack.pop();
                pop2 = stack.pop();
                if (pop1.isLongOrDoubleType()) {
                    if (pop2.isLongOrDoubleType())
                        throw new TypeErrorException("The dup2_x1 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 2 computational type");
                    stack.put(pop1);
                } else {
                    if (pop2.isLongOrDoubleType())
                        throw new TypeErrorException("The dup2_x1 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 1 computational type");
                    pop3 = stack.pop();
                    if (pop3.isLongOrDoubleType())
                        throw new TypeErrorException("The dup2_x1 instruction must not be used unless value3 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 1 computational type");
                    stack.put(pop2);
                    stack.put(pop1);
                    stack.put(pop3);
                }
                stack.put(pop2);
                stack.put(pop1);
            }
            break;
            case 94: {
                pop1 = stack.pop();
                pop2 = stack.pop();
                //2
                if (pop1.isLongOrDoubleType()) {
                    //2 2
                    if (pop2.isLongOrDoubleType()) {
                        stack.put(pop1);
                        stack.put(pop2);
                        stack.put(pop1);
                    } else {
                        //2 1 1
                        pop3 = stack.pop();
                        if (pop3.isLongOrDoubleType()) {
                            throw new TypeErrorException("The dup2_x2 instruction must not be used unless value3 is a value of a category 1 computational type " +
                                    "when value1 is a value of a category 2 computational type and value2 is a value of a category 1 computational type");
                        }
                        stack.put(pop1);
                        stack.put(pop3);
                        stack.put(pop2);
                        stack.put(pop1);
                    }
                    //1
                } else {
                    if (pop2.isLongOrDoubleType()) {
                        throw new TypeErrorException("The dup2_x2 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 1 computational type");
                        //1 1
                    } else {
                        pop3 = stack.pop();
//                        1 1 2
                        if (pop3.isLongOrDoubleType()) {
                            stack.put(pop2);
                            stack.put(pop1);
                            stack.put(pop3);
                            stack.put(pop2);
                            stack.put(pop1);
//                      1 1 1 1
                        } else {
                            pop4 = stack.pop();
                            if (pop4.isLongOrDoubleType()) {
                                throw new TypeErrorException("The dup2_x2 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                        "when value1,value2,value3 is a value of a category 1 computational type");
                            }
                            stack.put(pop2);
                            stack.put(pop1);
                            stack.put(pop4);
                            stack.put(pop3);
                            stack.put(pop2);
                            stack.put(pop1);
                        }
                    }
                }
            }
            break;
            case 95: {
                pop1 = stack.pop();
                pop2 = stack.pop();
                if (pop1.isLongOrDoubleType())
                    throw new TypeErrorException("The swap instruction must not be used unless value1 is a value of a category 1 computational type ");
                if (pop2.isLongOrDoubleType())
                    throw new TypeErrorException("The swap instruction must not be used unless value2 is a value of a category 1 computational type ");
                stack.put(pop2);
                stack.put(pop1);
            }
            break;
            default:
                throw new RuntimeException(instructionSet + " not a stackBehaviorInstruction");

        }
        appendInstruction(codeHelper.stackBehavior(instructionSet));
    }

    private void startBracket(Bracket bracket) {
        bracketStack.put(bracket);
    }

    public Bracket startBracket() {
        Bracket bracket = new Bracket(getCodeLength());
        bracketStack.put(bracket);
        return bracket;
    }

    public Bracket endBracket() {
        Bracket bracket = bracketStack.pop(getCodeLength());
        locals.endLocalVariableScope(bracket);
        return bracket;
    }

    public void startIf(BranchCondition branchCondition) {
        stack.pop(branchCondition.parallel.pop);
        Instruction branch = codeHelper.branch(branchCondition.parallel);
        startBracket(new IfBracket(getCodeLength(), branch));
        stack.startIfScope();
        appendInstruction(branch);
    }

    public void endIf() {
        Bracket ifBracket = endBracket();
        if (!(ifBracket instanceof IfBracket)) {
            throw new RuntimeException("ifBracket mismatching");
        }
        stack.endIfScope();
        ifBracket.setLength((short) (getCodeLength() - ifBracket.getStartPc()));
        Instruction instruction = ((IfBracket) ifBracket).getInstruction();
        instruction.setOperand(ConvertTool.S2B((short) ifBracket.getLength()));
    }

    public void newObject(ClassWrapper classWrapper) {
        stack.put(classWrapper.getType());
        appendInstruction(codeHelper.newObject(classWrapper));
    }

    public void newArray(Type elementType, int length) {
        loadInt(length);
        stack.pop();
        appendInstruction(codeHelper.newArray(elementType));
    }

    public void arrayLength() {
        stack.pop();
        stack.put(Type.INT);
        appendInstruction(codeHelper.arrayLength());
    }

    public void throwException() {
        appendInstruction(codeHelper.throwException());
    }

    public void instanceOf(ClassWrapper classWrapper) {
        stack.pop();
        appendInstruction(codeHelper.instanceOf(classWrapper));
    }

    public void monitorEnter() {
        stack.pop();
        appendInstruction(codeHelper.monitorEnter());
    }

    public void monitorExit() {
        stack.pop();
        appendInstruction(codeHelper.monitorExit());
    }

    public void multiANewArray(Type elementType, int dimensions, int... size) {
        if (size.length != dimensions) {
            throw new RuntimeException("dimensions need consistent with size length");
        }
        int flag = 0;
        for (int i = dimensions - 1; i >= 0; i--) {
            elementType = Type.getArrayType(elementType);
            int temp = size[i];
            if (temp == -1) {
                flag++;
                continue;
            }
            loadInt(temp);
        }
        if (dimensions - flag == 1) {
            appendInstruction(codeHelper.newArray(Type.getElementType(elementType)));
            return;
        }
        appendInstruction(codeHelper.multiANewArray(new ClassWrapper(elementType), (byte) dimensions));
    }

    public void jumpUp() {
        if (jumpPoint != -1) {
            throw new RuntimeException("You need to fall before jumping again");
        }
        jumpPoint = codeLength;
    }

    public void jumpDown() {
        int offset = codeLength - jumpPoint;
        appendInstruction(codeHelper.jump((short) offset));
        jumpPoint = - 1;
    }

    public void compare() {
        Type right = stack.pop();
        stack.pop();
        stack.put(Type.INT);
        appendInstruction(codeHelper.compare(right));

    }

    public void mathOperation(Operator operator) {
        Type right = stack.pop();
        if (!right.isPrimitiveType())
            throw new TypeErrorException();
        stack.pop();
        appendInstruction(codeHelper.mathOperation(right, operator));
        stack.put(right);
    }

    private void neg() {
        Type type = stack.pop();
        appendInstruction(codeHelper.neg(type));
        stack.put(type);
    }

    public void logicalOperation(Operator operator) {
        if (operator.equals(Operator.NEG)) {
            neg();
            return;
        }
        stack.pop();
        Type left = stack.pop();
        appendInstruction(codeHelper.logicalOperation(left, operator));
        stack.put(left);
    }

    public void increment(String localName) {
        codeHelper.iinc(locals.searchByName(localName), 1);
    }

    public void convert(Type nType) {
        Type oType = stack.pop();
        if (!nType.isPrimitiveType() || !oType.isPrimitiveType()) {
            throw new TypeErrorException("newType must be a primitiveType,you should use the 'checkcast' method to convert it");
        }
        appendInstruction(codeHelper.convert(oType, nType));
        stack.put(nType);
    }

    public void invokeVirtual(MethodWrapper target) {
        stack.pop(); //objectref
        stack.pop(target.getPop()); //agrs
        stack.put(target.getReturnType());
        appendInstruction(codeHelper.invokeVirtual(target));
    }

    public void invokeSpecial(MethodWrapper target) {
        stack.pop();
        stack.pop(target.getPop());
        stack.put(target.getReturnType());
        appendInstruction(codeHelper.invokeSpecial(target));
    }

    public void addAttribute(Attribute attribute){
        attributes.put(attribute.getAttributeName(),attribute);
    }

    public void invokeStatic(MethodWrapper target) {
        stack.pop(target.getPop());
        appendInstruction(codeHelper.invokeStatic(target));
        stack.put(target.getReturnType());
    }

    public void invokeInterface(MethodWrapper target) {
        stack.pop();
        stack.pop(target.getPop());
        appendInstruction(codeHelper.invokeInterface(target));
        stack.put(target.getReturnType());
    }

    public void invokeDynamic(CallSite callSite) {
        appendInstruction(codeHelper.invokeDynamic(callSite));
        stack.put(callSite.getTargetType());
    }
    public void return0() {
        appendInstruction(returnInst);
        if (returnInst.opcode != InstructionSet.RETURN.opcode) {
            stack.pop();
        }
    }

    public void end() {
        if (isEnd)
            return;
        return0();
        while (! bracketStack.isEmpty()) {
            endBracket();
        }
        isEnd = true;
    }

//    public Type getReturnType() {
//        return returnType;
//    }
//
//    public LocalVariableWrapper[] getParameters() {
//        return parameters;
//    }

    public boolean isNeedStackMapTable() {
        return needStackMapTable;
    }

    public OperandStack getStack() {
        return stack;
    }

    public LocalVariableTable getLocals() {
        return locals;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        AttributeHelper helper = AttributeHelper.getHelper();
        attributeLength = byteVector.getInt();
        stack.setMax(byteVector.getShort());
        byteVector.skip(2);
        codeVisit(byteVector);
        handlerVisit(constantPool, byteVector);
        short attrCount = byteVector.getShort();
        for (int i = 0; i < attrCount; i++) {
            addAttribute(helper.visit(constantPool, byteVector));
        }
        isEnd = true;
        return this;
    }

    private void handlerVisit(ConstantPool constantPool, ByteVector byteVector) {
        short count = byteVector.getShort();
        for (int i = 0; i < count; i++) {
            exceptionTable.putHandler(byteVector.getShort(),
                    byteVector.getShort(),
                    byteVector.getShort(),
                    Type.getType(Type.getClassDescriptor(((ConstantPoolClassInfo) constantPool.get(byteVector.getShort())).getClassInfo())));
        }
    }

    private void codeVisit(ByteVector byteVector) {
        int length = byteVector.getInt(), instLength;
        byte[] operand;
        Instruction instruction;
        for (int i = 0; i != length; ) {
            instruction = new Instruction(byteVector.getByte());
            instLength = instruction.length - 1;
            if (instLength < 0) {
                i += visitSwitch(i, instruction, byteVector);
                continue;
            }
            operand = byteVector.getArray(instLength);
            instruction.setOperand(operand);
            appendInstruction(instruction);
            i += instruction.length;
            if (i > length) {
                throw new RuntimeException("visit code error");
            }
        }
    }

    private int visitSwitch(int codeLength, Instruction switchInst, ByteVector byteVector) {
        int low, high;
        ByteVectors operandByteVector = new ByteVectors();
        for (int i = 0; i < 4 - ((codeLength + 1) % 4); i++) {
            operandByteVector.putByte(0);
            byteVector.skip(1);
        }

        switch (switchInst.opcode) {
            case - 86:
                operandByteVector.putInt(byteVector.getInt());
                low = byteVector.getInt();
                high = byteVector.getInt();
                operandByteVector.putInt(low);
                operandByteVector.putInt(high);
                for (int i = 0; i < high - low + 1; i++) {
                    operandByteVector.putInt(byteVector.getInt());
                }
                break;
            case - 87:
                //TODO
//                operandByteVector.putInt(byteVector.getInt());
                break;
        }
        switchInst.setOperand(operandByteVector.toByteArray());
        appendInstruction(switchInst);
        return 1 + operandByteVector.getLength();
    }


    public boolean isEmpty() {
        return codeLength == 0;
    }

    public int getCodeLength() {
        return codeLength;
    }

    private void creatStackMapTable() {

        StackMapTable stackMapTable = new StackMapTable();
        //TODO
        addAttribute(stackMapTable);
    }

    @Override
    public short load(ConstantPool cp) {
        if (! isEnd) {
            end();
        }
        if (needStackMapTable) {
            creatStackMapTable();
        }
        loadAttributeName(cp);
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            if (attribute != null)
                attribute.load(cp);
        }
        return cpIndex;
    }


    public String print() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        int pcIndex = 0;
        for (Instruction instruction : instructions) {
            String instructionStr = InstructionSet.get(instruction.opcode).toString().toLowerCase();
            sb.append(String.format("%- 3d  %- 3d  %s",
                    index++, pcIndex, instructionStr));
            if (instruction.length != 1)
                sb.append(String.format("% -2d", ConvertTool.B2S(instruction.getOperand())));
            sb.append('\n');
            pcIndex += instruction.length;
        }
        return sb.toString();
    }


    @Override
    public byte[] toByteArray() {
        checkLoaded();
        short attributeCount = 0;
        byte[] attrBytes = new byte[0];
        attributeLength += 4; //stackMax localsMax
        attributeLength += 4; //codeLength
        attributeLength += getCodeLength();
        attributeLength += 2; //exceptionCount
        attributeLength += 2; //attributeCount
        for (Attribute attribute : attributes.values()) {
            if (attribute != null) {
                attributeCount++;
                byte[] bytes = attribute.toByteArray();
                attrBytes = ArrayTool.join(attrBytes, bytes);
            }
        }
        attributeLength += attrBytes.length;
        ByteVector byteVector = new ByteVector(2 + 4 + attributeLength);
        byteVector.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(stack.getMax())
                .putShort(locals.getMax())
                .putInt(codeLength)
                .putArray(codeToByteArray())
                .putArray(exceptionTable.toByteArray())
                .putShort(attributeCount)
                .putArray(attrBytes);
        attributeLength = 0;
        return byteVector.end();
    }

    private byte[] codeToByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        for (Instruction instruction : instructions) {
            byteVectors.putArray(instruction.toByteArray());
        }
        return byteVectors.toByteArray();
    }
}
