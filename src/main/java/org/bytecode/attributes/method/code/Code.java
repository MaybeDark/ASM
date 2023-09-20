package org.bytecode.attributes.method.code;

import org.bytecode.attributes.method.code.localvariabletable.LocalVariableTable;
import org.Access;
import org.bytecode.attributes.clazz.bootstrapmethod.CallSite;
import org.bytecode.attributes.method.code.instruction.InstructionFactory;
import org.bytecode.attributes.method.code.instruction.InstructionSet;
import org.Type;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.attributes.method.code.instruction.Operator;
import org.bytecode.attributes.method.code.operandstack.OperandStack;
import org.bytecode.method.MethodWriter;
import org.exception.TypeErrorException;
import org.bytecode.attributes.method.code.instruction.Instruction;
import com.sun.istack.internal.Nullable;
import org.bytecode.attributes.method.code.bracket.IfBracket;
import org.bytecode.attributes.method.code.bracket.Bracket;
import org.bytecode.attributes.method.code.bracket.BracketStack;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ConvertTool;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Code extends Attribute {
    private final LocalVariableWrapper[] parameters;
    private final BracketStack bracketStack;
    private final Map<String, Attribute> attributes;
    private Type returnType;
    private OperandStack stack;
    private LocalVariableTable locals;
    private int jumpPoint = -1;
    private boolean isEnd = false;
    private boolean needStackMapTable = false;

    private short handCount = 0;
    private InstructionFactory instructionFactory;

    public Code(@Nullable MethodWriter methodWriter, @Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        attributeName = "Code";
        this.returnType = returnType;
        this.parameters = parameters;
        this.attributes = new HashMap<>();
        this.bracketStack = new BracketStack();
        this.instructionFactory = new InstructionFactory(methodWriter.classWriter.getConstantPool());
        bracketStack.put(new Bracket(0));
        instructionFactory.setReturnType(returnType);
        if (Access.isStatic(methodWriter.getAccess())) {
            initStackAndLocal(null);
        }else{
            initStackAndLocal(methodWriter.classWriter.thisClass.getType());
        }
        attributes.put("LocalVariableTable", locals);
    }

    public void loadNull() {
        stack.put(Type.NULL);
        instructionFactory.loadNull();
    }

    public void loadInt(int num) {
        stack.put(Type.INT);
        instructionFactory.loadInt(num);
    }

    public void loadDouble(double num) {
        stack.put(Type.DOUBLE);
        instructionFactory.loadDouble(num);
    }

    public void loadLong(long num) {
        stack.put(Type.LONG);
        instructionFactory.loadLong(num);
    }

    public void loadFloat(float num) {
        stack.put(Type.FLOAT);
        instructionFactory.loadFloat(num);
    }

    public void loadString(String str) {
        instructionFactory.loadString(str);
        stack.put(Type.STRING);
    }

    public void loadLocal(String localName) {
        LocalVariableWrapper local = locals.getLocalVariableByName(localName);
        Type type = local.getType();
        stack.put(type);
        instructionFactory.loadLocal(type, local.getTableIndex());
    }

    public void loadArrayElement() {
        stack.pop(); //index
        Type arrayType = stack.pop();
        Type componentType = Type.getElementType(arrayType);
        stack.put(componentType);
        instructionFactory.loadArrayElement(componentType);
    }

    public void loadField(FieldWrapper fieldWrapper, boolean isStatic) {
        if (!isStatic) {
            stack.pop(); //objectRef
        }
        stack.put(fieldWrapper.getType());
        instructionFactory.loadField(fieldWrapper, isStatic);
    }

    public void storeLocal(String localName) {
        Type type = stack.pop();
        short index = locals.put(new LocalVariableWrapper(localName, type));
        instructionFactory.storeLocal(type, index);
    }

    public void storeArrayElement() {
        Type valueType = stack.pop();
        stack.pop(); //index
        stack.pop(); //arrayRef
        instructionFactory.storeArrayElement(valueType);
    }

    public void storeField(FieldWrapper fieldWrapper, boolean isStatic) {
        if (!isStatic) {
            stack.pop(); //objectRef
        }
        stack.pop(); //value
        instructionFactory.storeField(fieldWrapper, isStatic);
    }

    public void checkCast(ClassWrapper classWrapper) {
        stack.pop(); //objectRef
        stack.put(classWrapper.getType()); //converted
        instructionFactory.checkCast(classWrapper);
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
        instructionFactory.stackBehavior(instructionSet);
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
        startBracket(new IfBracket(getCodeLength(), instructionFactory.branch(branchCondition.parallel)));
        stack.startIfScope();
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
        instructionFactory.newObject(classWrapper);
    }

    public void newArray(Type elementType, int length) {
        loadInt(length);
        stack.pop();
        instructionFactory.newArray(elementType);
    }

    public void arrayLength() {
        stack.pop();
        stack.put(Type.INT);
        instructionFactory.arrayLength();
    }

    public void throwException() {
        instructionFactory.throwException();
    }

    public void instanceOf(ClassWrapper classWrapper) {
        stack.pop();
        instructionFactory.instanceOf(classWrapper);
    }

    public void monitorEnter() {
        stack.pop();
        instructionFactory.monitorEnter();
    }

    public void monitorExit() {
        stack.pop();
        instructionFactory.monitorExit();
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
            instructionFactory.newArray(Type.getElementType(elementType));
            return;
        }
        instructionFactory.multiANewArray(new ClassWrapper(elementType), (byte) dimensions);
    }

    public void jumpUp() {
        if (jumpPoint != -1) {
            throw new RuntimeException("You need to fall before jumping again");
        }
        jumpPoint = instructionFactory.getCodeLength();
    }

    public void jumpDown() {
        int offset = instructionFactory.getCodeLength() - jumpPoint;
        instructionFactory.jump((short) offset);
        jumpPoint = -1;
    }

    public void compare() {
        Type right = stack.pop();
        stack.pop();
        instructionFactory.compare(right);
        stack.put(Type.INT);
    }

    public void mathOperation(Operator operator) {
        Type right = stack.pop();
        if (!right.isPrimitiveType())
            throw new TypeErrorException();
        stack.pop();
        instructionFactory.mathOperation(right, operator);
        stack.put(right);
    }

    private void neg() {
        Type type = stack.pop();
        instructionFactory.neg(type);
        stack.put(type);
    }

    public void logicalOperation(Operator operator) {
        if (operator.equals(Operator.NEG)) {
            neg();
            return;
        }
        stack.pop();
        Type left = stack.pop();
        instructionFactory.logicalOperation(left, operator);
        stack.put(left);
    }

    public void increment(String localName) {
        instructionFactory.iinc(locals.searchByName(localName), 1);
    }

    public void convert(Type nType) {
        Type oType = stack.pop();
        if (!nType.isPrimitiveType() || !oType.isPrimitiveType()) {
            throw new TypeErrorException("newType must be a primitiveType,you should use the 'checkcast' method to convert it");
        }
        instructionFactory.convert(oType, nType);
        stack.put(nType);
    }

    public void invokeVirtual(MethodWrapper target) {
        stack.pop(); //objectref
        stack.pop(target.getPop()); //agrs
        instructionFactory.invokeVirtual(target);
        stack.put(target.getReturnType());
    }

    public void invokeSpecial(MethodWrapper target) {
        stack.pop();
        stack.pop(target.getPop());
        instructionFactory.invokeSpecial(target);
        stack.put(target.getReturnType());
    }
    public void addAttribute(Attribute attribute){
        attributes.put(attribute.getAttributeName(),attribute);
    }

    public void invokeStatic(MethodWrapper target) {
        stack.pop(target.getPop());
        instructionFactory.invokeStatic(target);
        stack.put(target.getReturnType());
    }

    public void invokeInterface(MethodWrapper target) {
        stack.pop();
        stack.pop(target.getPop());
        instructionFactory.invokeInterface(target);
        stack.put(target.getReturnType());
    }

    public void invokeDynamic(CallSite callSite) {
        instructionFactory.invokeDynamic(callSite);
        stack.put(callSite.getTargetType());
    }
    public void return0(){
        instructionFactory.return0();
        stack.pop();
    }

    public void end() {
        if (isEnd)
            return;
        instructionFactory.return0();
        while (!bracketStack.isEmpty()) {
            endBracket();
        }
        isEnd = true;
    }

    public Type getReturnType() {
        return returnType;
    }

    public LocalVariableWrapper[] getParameters() {
        return parameters;
    }

    public boolean isNeedStackMapTable() {
        return needStackMapTable;
    }

    public OperandStack getStack() {
        return stack;
    }

    public LocalVariableTable getLocals() {
        return locals;
    }

    public boolean isEmpty() {
        return instructionFactory.isEmpty();
    }

    public int getCodeLength() {
        return instructionFactory.getCodeLength();
    }

    @Override
    public short load(ConstantPool cp) {
        if (!isEnd) { end(); }
        super.load(cp);
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            if (attribute != null)
                attribute.load(cp);
        }
        return cpIndex;
    }


    public String print(){
       return instructionFactory.print();
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        short attributeCount = 0;
        byte[] attrBytes = new byte[0];
        attributeLength += 4; //stackMax localsMax
        attributeLength += 4; //codeLength
        attributeLength += getCodeLength();
        attributeLength += 2; //handleCount
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
                .putInt(getCodeLength())
                .putArray(instructionFactory.toByteArray())
                .putShort(handCount)
                .putShort(attributeCount)
                .putArray(attrBytes);
        attributeLength = 0;
        return byteVector.end();


//
//        attributeLength += getCodeLength();
//        attributeLength += 2;       //异常表长度
//        Collection<Attribute> attr = attributes.values();
//        byte[] attrBytes = new byte[0];
//        short attrCount = 0;
//        for (Attribute attribute : attr) {
//            if (attribute!=null){
//                attrCount ++;
//                byte[] bytes = attribute.toByteArray();
//                attrBytes = ArrayTool.join(attrBytes, bytes);
//            }
//        }
//        attributeLength += 2;   //attribute长度
//        attributeLength += attrBytes.length;
//        byte[] result = new byte[2 + 4 + attributeLength];
//        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
//        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
//        System.arraycopy(ConvertTool.S2B(stack.getMax()),0,result,6,2);
//        System.arraycopy(ConvertTool.S2B(locals.getMax()),0,result,8,2);
//        System.arraycopy(ConvertTool.I2B(getCodeLength()),0,result,10,4);
//        int index = 14;
//        for (Instruction instruction : instructions) {
//            byte[] bytes = instruction.toByteArray();
//            System.arraycopy(bytes,0,result,index,bytes.length);
//            index += bytes.length;
//        }
//        System.arraycopy(ConvertTool.S2B((short) 0),0,result,index,2);//异常表
//        index += 2;
//        System.arraycopy(ConvertTool.S2B(attrCount),0,result,index,2);
//        index += 2;
//        System.arraycopy(attrBytes,0,result,index,attrBytes.length);
//        return result;
    }

    private void initStackAndLocal(Type obj) {
        this.stack = new OperandStack();
        this.locals = new LocalVariableTable();

        if (obj != null) {
            locals.put(new LocalVariableWrapper("this", obj));
        }
        if (parameters != null && parameters.length != 0) {
            for (LocalVariableWrapper localVariableWrapper : parameters) {
                locals.put(localVariableWrapper);
            }
        }
        initStackMapTable();
    }

    private void initStackMapTable() {
//        StackMapTable stackMapTable = new StackMapTable();
//        attributes.put("StackMapTable",stackMapTable);
        //TODO initFrame
    }


//    private int addInstruction(Instruction instruction){
//        InstructionSet instructionSet = instruction.getInstructionSet();
//        if (instructionSet.unknownOperandLength()){
//            throw new RuntimeException(instructionSet + " is unknownOperandLength");
//        }
//        if (instructionSet.isJumpInstruction()) needStackMapTable = true;
//        getCodeLength( += 1 + instructionSet.getOperand();;
//        instructions.add(instruction);
//        return getCodeLength(;
//    }


//    /**
//     * 添加跳转语句
//     * 为code属性添加goto指令（因jsr和ret都已过时，跳转指令使用goto)
//     * @param offset 偏移
//     */
//    @Deprecated
//    public void jumpTo(short offset){
//        Instruction instruction = new Instruction(InstructionSet.GOTO, ConvertTool.S2B(offset));
//        addInstruction(instruction);
//    }

//    /**
//     * 操作数比较
//     *
//     * @param instructionSet 指令集
//     */
//    @Deprecated
//    public void operandCompare(InstructionSet instructionSet){
//        if (!instructionSet.isCompareInstruction()){
//            throw new RuntimeException(instructionSet + " not a compareOperationInstruction");
//        }
//        Type pop1 = stack.pop();
//        Type pop2 = stack.pop();
//        if (pop1.equals(pop2))
//            throw new TypeErrorException("compareInstruction need to pop two values of the same type");
//        stack.put(Type.INT);
//    }

//    /**
//     * 操作数运算
//     *
//     * @param instructionSet 运算指令集
//     */
//    @Deprecated
//    public void mathOperandOperation(InstructionSet instructionSet){
//        if (!instructionSet.ismathOperandOperationInstruction()){
//            throw new RuntimeException(instructionSet + " not a mathOperandOperationInstruction");
//        }
//        Type pop1 = stack.pop();
//        Type pop2 = stack.pop();
//        if (!pop1.equals(pop2))
//            throw new TypeErrorException("operationInstruction need to pop two values of the same type");
//        stack.put(pop1);
//       addInstruction(new Instruction(instructionSet));
//    }
//
//    private void negOperandOperation(){
//        //do nothing
//    }
//
//    public void logicalOperandOperation(InstructionSet instructionSet){
//        if (!instructionSet.islogicalOperandOperationInstrucion()){
//            throw new RuntimeException(instructionSet + " not a logicalOperandOperationInstrucion");
//        }
//        if (instructionSet.getOpcode()>=116 && instructionSet.getOpcode()<=119){
//            negOperandOperation();
//            return;
//        }
//        Type pop1 = stack.pop();
//        stack.pop();
//        stack.put(pop1);
//        addInstruction(new Instruction(instructionSet));
//    }
//    /**
//     * 局部变量表自增
//     *
//     * @param tableIndex 局部变量表表索引
//     * @param num        自增数
//     */
//    public void iinc(byte tableIndex,byte num){
//        LocalVariableWrapper localVariable = locals.getLocalVariableByIndex(tableIndex);
//        if (!localVariable.getType().isIntType()) {
//            throw new TypeErrorException("The local variable at index must contain an int");
//        }
//        Instruction instruction = new Instruction(InstructionSet.IINC, new byte[]{tableIndex, num});
//        addInstruction(instruction);
//    }
//
//    public void iinc(String name,byte num){
//        LocalVariableWrapper localVariable = locals.getLocalVariableByName(name);
//        iinc((byte) localVariable.getTableIndex(),num);
//    }


//    /**
//     * 操作数转换类型
//     *
//     * @param instructionSet 类型转化指令集
//     */
//    public void operandConvert(InstructionSet instructionSet){
//        if (!instructionSet.isConvertInstruction()){
//            throw new RuntimeException(instructionSet + " not a convertInstruction");
//        }
//        Type oType,nType;
//        String s = instructionSet.toString();
//        {
//            char oTypeDesc = s.charAt(0);
//            oType = Type.getType(oTypeDesc);
//        }
//        {
//            char nTypeDesc = s.charAt(2);
//            nType = Type.getType(nTypeDesc);
//        }
//        {
//            Type pop = stack.pop();
//            if (!pop.equals(oType)) {
//                throw new TypeErrorException(instructionSet + " need to pop an " + oType.getDescriptor() +
//                        " type, but unexpectedly popped up a " + pop.getDescriptor());
//            }
//        }
//        stack.put(nType);
//        addInstruction(new Instruction(instructionSet));
//    }

//    public void loadFromConstant(InstructionSet instructionSet){
//        if (!instructionSet.isLoadFromConstantInstruction()){
//            throw new RuntimeException(instructionSet + " not a loadConstantInstruction");
//        }
//        switch (instructionSet.getOpcode()){
//            case 1:
//                stack.put(Type.NULL);
//                break;
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//            case 8:
//                stack.put(Type.INT);
//                break;
//            case 9:
//            case 10:
//                stack.put(Type.LONG);
//                break;
//            case 11:
//            case 12:
//            case 13:
//                stack.put(Type.FLOAT);
//                break;
//            case 14:
//            case 15:
//                stack.put(Type.DOUBLE);
//                break;
//        }
//        addInstruction(new Instruction(instructionSet));
//    }

//    public void loadFromConstantPool(ConstantPool cp,short cpIndex){
//        AbsConstantPoolInfo absConstantPoolInfo = cp.get(cpIndex);
//        if (absConstantPoolInfo instanceof ConstantPoolIntegerInfo){
//            stack.put(Type.INT);
//            if (cpIndex < 256){
//                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
//            }else {
//                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
//            }
//        }else if (absConstantPoolInfo instanceof ConstantPoolFloatInfo){
//            stack.put(Type.FLOAT);
//            if (cpIndex < 256){
//                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
//            }else {
//                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
//            }
//        }else if (absConstantPoolInfo instanceof ConstantPoolStringInfo){
//            stack.put(Type.getType(String.class));
//            if (cpIndex < 256){
//                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
//            }else {
//                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
//            }
//        }else if(absConstantPoolInfo instanceof ConstantPoolDoubleInfo) {
//            stack.put(Type.DOUBLE);
//            addInstruction(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
//        }else if(absConstantPoolInfo instanceof ConstantPoolLongInfo){
//            stack.put(Type.LONG);
//            addInstruction(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
//        } else{
//            throw new TypeErrorException("Can only download int, long, double, String, float from the constant pool");
//        }
//    }
//
//    public void loadFromShortNum(short num) {
//        if (num <= Byte.MAX_VALUE && num >= Byte.MIN_VALUE) {
//            addInstruction(new Instruction(InstructionSet.BIPUSH, new byte[]{(byte) num}));
//        } else {
//            addInstruction(new Instruction(InstructionSet.SIPUSH, ConvertTool.S2B(num)));
//        }
//    }
//
//    public void loadFromLocals(String localVariableName) {
//        LocalVariableWrapper localVariable = locals.getLocalVariableByName(localVariableName);
//        if (localVariable == null) {
//            throw new RuntimeException(localVariableName + "not in locals,The scope may be ended or undefined");
//        }
//        Type type = localVariable.getType();
//        short tableIndex = localVariable.getTableIndex();
//        addInstruction(buildLoadInstruction(tableIndex, type));
//        stack.put(type);
//    }
//
//    public void loadFromLocals(short tableIndex){
//        LocalVariableWrapper localVariable = locals.getLocalVariableByIndex(tableIndex);
//        if (localVariable == null) {
//            throw new RuntimeException("error local variable table index");
//        }
//        Type type = localVariable.getType();
//        addInstruction(buildLoadInstruction(tableIndex, type));
//        stack.put(type);
//    }
//
//    public void loadOrSetField(InstructionSet instructionSet, FieldWrapper field) {
//        if (!field.isLoaded()) {
//            throw new RuntimeException(field.getFieldName() + "must be load before use");
//        }
//        if (!instructionSet.isFieldInstruction()) {
//            throw new RuntimeException(instructionSet + " not a fieldInstruction");
//        }
//        Instruction instruction = new Instruction(instructionSet, ConvertTool.S2B(field.getFieldInfoIndex()));
//        addInstruction(instruction);
//        switch (instructionSet.getOpcode()) {
//            case -78:
//                stack.put(field.getType());
//                break;
//            case -76:
//                popAndPut(1,field.getType());
//                break;
//            case -77:
//                stack.pop();
//                break;
//            case -75:
//                stack.pop(2);
//                break;
//        }
//    }
//
//    /**
//     * value = array[index];
//     * 添加数组取值指令
//     * 首先将‘数组引用(arrayref)’压入栈中
//     * 然后将‘需要操作的下标(index)’压入栈中
//     * 最后执行本方法添加对应‘_aload'指令
//     */
//    public void loadFromArray() {
//        {
//            Type index = stack.pop();
//            if (!index.isIntType()) {
//                throw new RuntimeException("array index must be a int type");
//            }
//        }
//        Type ref = stack.pop();
//        if (!ref.isArrayType()) {
//            throw new RuntimeException("Unexpected pop a " + ref.getDescriptor() + ",not a arrayref");
//        }
//        addInstruction(buildArrayLoadInstruction(Type.getElementType(ref)));
//    }
//
//    /**
//     * array[index] = value;
//     * 添加数组存储指令
//     * 首先将‘数组引用(arrayref)’压入栈中
//     * 然后将‘需要操作的下标(index)’压入栈中
//     * 再将需要赋的值(value)压入栈中
//     * 最后执行本方法添加对应'_astore'指令
//     */
//    public void storeToArray() {
//        Type valueType = stack.pop();
//        {
//            Type index = stack.pop();
//            if (!index.isIntType()) {
//                throw new RuntimeException("array index must be a int type");
//            }
//        }
//        {
//            Type ref = stack.pop();
//            if (!ref.isArrayType()) {
//                throw new RuntimeException("Unexpected pop a " + ref.getDescriptor() + ",not a arrayref");
//            } else if (!Type.getElementType(ref).equals(valueType)) {
//                throw new RuntimeException("array type must be consistent with value");
//            }
//        }
//        addInstruction(buildArrayStoreInstruction(valueType));
//    }
//
//    public short storeToLocals(String localVariableName) {
//        Type type = stack.pop();
//        short tableIndex = locals.searchByName(localVariableName);
//        if (tableIndex == -1) {
//            LocalVariableWrapper localVariable = new LocalVariableWrapper(localVariableName,type);
//            localVariable.setStartPc(getCodeLength();
//            tableIndex = locals.put(localVariable);
//        }
//        Instruction instruction = buildStoreInstruction(tableIndex, type);
//        addInstruction(instruction);
//        return tableIndex;
//    }
//
//    public void invokeMethod(InstructionSet instructionSet, MethodWrapper method) {
//        if (!instructionSet.isInvokeInstruction()) {
//            throw new RuntimeException(instructionSet + " not a invokeInstruction");
//        }
//        if (!method.isLoaded()) {
//            throw new RuntimeException(method.getMethodName() + "need to load before use");
//        }
//        stack.pop(method.getPop());
//        byte operand = instructionSet.getOpcode();
//        if (operand == (byte) 0xB9 || operand == (byte) 0xB7 || operand == (byte)0xB6)
//            stack.pop();
//        Instruction newInstruction = new Instruction(instructionSet, ConvertTool.S2B(method.getMethodInfoIndex()));
//        addInstruction(newInstruction);
//        stack.put(method.getReturnType());
//    }
//
//    private void popAndPut(int popCount, Type type) {
//        stack.pop(popCount);
//        if (type != null) {
//            stack.put(type);
//        }
//    }


//    private void setReturnInstruction(Type returnType) {
//        if (returnType == null) {
//            this.returnInstruction = InstructionSet.RETURN;
//        } else if (returnType.isIntType()) {
//            this.returnInstruction = InstructionSet.IRETURN;
//        } else if (returnType.isDoubleType()) {
//            this.returnInstruction = InstructionSet.DRETURN;
//        } else if (returnType.isLongType()) {
//            this.returnInstruction = InstructionSet.LRETURN;
//        } else if (returnType.isFloatType()) {
//            this.returnInstruction = InstructionSet.FRETURN;
//        } else {
//            this.returnInstruction = InstructionSet.ARETURN;
//        }
//    }

//    private Instruction buildStoreInstruction(short tableIndex, Type type) {
//        Instruction result;
//        boolean iType = type.isIntType() || type.isByteType() || type.isShortType() || type.isCharType();
//        if (tableIndex > 3) {
//            byte[] operand = new byte[]{(byte) tableIndex};
//            if (iType) {
//                result = new Instruction(InstructionSet.ISTORE, operand);
//            } else if (type.isLongType()) {
//                result = new Instruction(InstructionSet.LSTORE, operand);
//            } else if (type.isFloatType()) {
//                result = new Instruction(InstructionSet.FSTORE, operand);
//            } else if (type.isDoubleType()) {
//                result = new Instruction(InstructionSet.DSTORE, operand);
//            } else {
//                result = new Instruction(InstructionSet.ASTORE, operand);
//            }
//        } else {
//            switch (tableIndex) {
//                case 0:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ISTORE_0);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LSTORE_0);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FSTORE_0);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DSTORE_0);
//                    } else {
//                        result = new Instruction(InstructionSet.ASTORE_0);
//                    }
//                    break;
//                case 1:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ISTORE_1);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LSTORE_1);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FSTORE_1);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DSTORE_1);
//                    } else {
//                        result = new Instruction(InstructionSet.ASTORE_1);
//                    }
//                    break;
//                case 2:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ISTORE_2);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LSTORE_2);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FSTORE_2);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DSTORE_2);
//                    } else {
//                        result = new Instruction(InstructionSet.ASTORE_2);
//                    }
//                    break;
//                case 3:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ISTORE_3);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LSTORE_3);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FSTORE_3);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DSTORE_3);
//                    } else {
//                        result = new Instruction(InstructionSet.ASTORE_3);
//                    }
//                    break;
//                default:
//                    throw new RuntimeException("error tableIndex");
//            }
//        }
//        return result;
//    }
//
//    private Instruction buildArrayStoreInstruction(Type type) {
//        Instruction result;
//        if (type.isIntType()) {
//            result = new Instruction(InstructionSet.IASTORE);
//        } else if (type.isLongType()) {
//            result = new Instruction(InstructionSet.LASTORE);
//        } else if (type.isFloatType()) {
//            result = new Instruction(InstructionSet.FASTORE);
//        } else if (type.isDoubleType()) {
//            result = new Instruction(InstructionSet.DASTORE);
//        } else if (type.isByteType()) {
//            result = new Instruction(InstructionSet.BASTORE);
//        } else if (type.isCharType()) {
//            result = new Instruction(InstructionSet.CASTORE);
//        } else if (type.isShortType()) {
//            result = new Instruction(InstructionSet.SASTORE);
//        } else {
//            result = new Instruction(InstructionSet.AASTORE);
//        }
//        return result;
//    }
//
//    private Instruction buildLoadInstruction(short tableIndex, Type type) {
//        Instruction result;
//        boolean iType = type.isIntType() || type.isByteType() || type.isShortType() || type.isCharType();
//        if (tableIndex > 3) {
//            byte[] operand = new byte[]{(byte) tableIndex};
//            if (iType) {
//                result = new Instruction(InstructionSet.ILOAD, operand);
//            } else if (type.isLongType()) {
//                result = new Instruction(InstructionSet.LLOAD, operand);
//            } else if (type.isFloatType()) {
//                result = new Instruction(InstructionSet.FLOAD, operand);
//            } else if (type.isDoubleType()) {
//                result = new Instruction(InstructionSet.DLOAD, operand);
//            } else {
//                result = new Instruction(InstructionSet.ALOAD, operand);
//            }
//        } else {
//            switch (tableIndex) {
//                case 0:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ILOAD_0);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LLOAD_0);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FLOAD_0);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DLOAD_0);
//                    } else {
//                        result = new Instruction(InstructionSet.ALOAD_0);
//                    }
//                    break;
//                case 1:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ILOAD_1);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LLOAD_1);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FLOAD_1);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DLOAD_1);
//                    } else {
//                        result = new Instruction(InstructionSet.ALOAD_1);
//                    }
//                    break;
//                case 2:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ILOAD_2);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LLOAD_2);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FLOAD_2);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DLOAD_2);
//                    } else {
//                        result = new Instruction(InstructionSet.ALOAD_2);
//                    }
//                    break;
//                case 3:
//                    if (iType) {
//                        result = new Instruction(InstructionSet.ISTORE_3);
//                    } else if (type.isLongType()) {
//                        result = new Instruction(InstructionSet.LSTORE_3);
//                    } else if (type.isFloatType()) {
//                        result = new Instruction(InstructionSet.FSTORE_3);
//                    } else if (type.isDoubleType()) {
//                        result = new Instruction(InstructionSet.DSTORE_3);
//                    } else {
//                        result = new Instruction(InstructionSet.ASTORE_3);
//                    }
//                    break;
//                default:
//                    throw new RuntimeException("error tableIndex");
//            }
//        }
//        return result;
//    }
//
//    private Instruction buildArrayLoadInstruction(Type componentType) {
//        Instruction result = null;
//        if (componentType.isIntType()) {
//            result = new Instruction(InstructionSet.IALOAD);
//        } else if (componentType.isLongType()) {
//            result = new Instruction(InstructionSet.LALOAD);
//        } else if (componentType.isFloatType()) {
//            result = new Instruction(InstructionSet.FALOAD);
//        } else if (componentType.isDoubleType()) {
//            result = new Instruction(InstructionSet.DALOAD);
//        } else if (componentType.isByteType()) {
//            result = new Instruction(InstructionSet.BALOAD);
//        } else if (componentType.isCharType()) {
//            result = new Instruction(InstructionSet.CALOAD);
//        } else if (componentType.isShortType()) {
//            result = new Instruction(InstructionSet.SALOAD);
//        } else {
//            result = new Instruction(InstructionSet.AALOAD);
//        }
//        return result;
//    }



//    public void setReturnType(Type returnType){
//        if (isEnd)
//            throw new RuntimeException("code is end cannot modify returnType");
//        this.returnType = returnType;
//        setReturnInstruction(returnType);
//    }


}
