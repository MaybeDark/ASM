package org.attribute.method;

import org.InstructionSet;
import org.Type;
import org.attribute.Attribute;
import org.attribute.method.code.LocalVariableTable;
import org.constantpool.ConstantPool;
import org.constantpool.info.*;
import org.exception.TypeErrorException;
import org.instruction.Instruction;
import org.method.code.OperandStack;
import com.sun.istack.internal.Nullable;
import org.other.Label.IfLabel;
import org.other.Label.Label;
import org.other.Label.LabelStack;
import org.tools.ConvertTool;
import org.tools.DescTool;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Code extends Attribute {
    private final Type returnType;
    private final LocalVariableWrapper[] parameterType;
    private final LabelStack labelStack = new LabelStack();
    private final Map<String, Attribute> attributes = new HashMap<>();
    InstructionSet returnInstruction;
    LinkedList<Instruction> instructions = new LinkedList<>();
    private OperandStack stack;
    private LocalVariableTable locals;
    private short stackMax;
    private short localsMax;
    private int codeLength;
    private boolean needStackMapTable = false;
    private boolean isEnd = false;

    public Code(@Nullable Type returnType, @Nullable LocalVariableWrapper... parameterType) {
        setReturnInstruction(returnType);
        this.returnType = returnType;
        this.parameterType = parameterType;
        initStackAndLocal();
        labelStack.put(new Label(0));
    }

    private void startLabel(Label label) {
        labelStack.put(label);
    }

    public Label startLabel(){
        Label label = new Label(codeLength);
        labelStack.put(label);
        return label;
    }

    public Label endLabel() {
        Label label = labelStack.pop(codeLength);
        locals.endLocalVariableScope(label);
        return label;
    }

    public void startIf(InstructionSet instructionSet) {
        if (!instructionSet.isIfInstruction()) {
            throw new RuntimeException(instructionSet + " not a ifInstruction");
        }
        Instruction instruction = new Instruction(instructionSet);
        startLabel(new IfLabel(codeLength, instruction));
        instructions.add(instruction);
        needStackMapTable = true;
        codeLength += 1 + instruction.getInstructionSet().getOperand();
    }

    public void endIf() {
        Label ifLabel = endLabel();
        if (!(ifLabel instanceof IfLabel)) {
            throw new RuntimeException("label mismatching");
        }
        ifLabel.setLength((short) (codeLength - ifLabel.getStartPc()));
        Instruction instruction = ((IfLabel) ifLabel).getInstruction();
        instruction.setOperand(ConvertTool.S2B((short) codeLength));
    }

    public void loadFromConstant(InstructionSet instructionSet){
        if (!instructionSet.isLoadConstInstruction()){
            throw new RuntimeException(instructionSet + " not a loadConstInstruction");
        }
        switch (instructionSet.getOpcode()){
            case 1:
                stack.put(Type.NULL);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                stack.put(Type.INT);
                break;
            case 9:
            case 10:
                stack.put(Type.LONG);
                break;
            case 11:
            case 12:
            case 13:
                stack.put(Type.FLOAT);
                break;
            case 14:
            case 15:
                stack.put(Type.DOUBLE);
                break;
        }
        instructions.add(new Instruction(instructionSet));
        codeLength += 1;
    }

    public void loadFromConstantPool(ConstantPool cp,short cpIndex){
        AbsConstantPoolInfo absConstantPoolInfo = cp.get(cpIndex);
        if (absConstantPoolInfo instanceof ConstantPoolIntegerInfo){
            stack.put(Type.INT);
            if (cpIndex > 256){
                instructions.add(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
                codeLength += 2;
            }else {
                instructions.add(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
                codeLength += 3;
            }
        }else if (absConstantPoolInfo instanceof ConstantPoolFloatInfo){
            stack.put(Type.FLOAT);
            if (cpIndex > 256){
                instructions.add(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
                codeLength += 2;
            }else {
                instructions.add(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
                codeLength += 3;
            }
        }else if (absConstantPoolInfo instanceof ConstantPoolStringInfo){
            stack.put(Type.getType(String.class));
            if (cpIndex > 256){
                instructions.add(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
                codeLength += 2;
            }else {
                instructions.add(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
                codeLength += 3;
            }
        }else if(absConstantPoolInfo instanceof ConstantPoolDoubleInfo) {
            stack.put(Type.DOUBLE);
            instructions.add(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
            codeLength += 3;
        }else if(absConstantPoolInfo instanceof ConstantPoolLongInfo){
            stack.put(Type.LONG);
            instructions.add(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
            codeLength += 3;
        } else{
            throw new TypeErrorException("Can only download int, long, double, String, float from the constant pool");
        }
    }

    public void loadFromShortNum(short num) {
        if (num < Byte.MAX_VALUE && num > Byte.MIN_VALUE) {
            instructions.add(new Instruction(InstructionSet.BIPUSH, new byte[]{(byte) num}));
        } else {
            instructions.add(new Instruction(InstructionSet.SIPUSH, ConvertTool.S2B((short) num)));
        }
    }

    public void loadFromLocals(String localVariableName) {
        LocalVariableWrapper localVariable = locals.getLocalVariableByName(localVariableName);
        if (localVariable == null) {
            throw new RuntimeException(localVariableName + "not in locals,The scope may be ended or undefined");
        }
        Type type = localVariable.getType();
        short tableIndex = localVariable.getTableIndex();
        Instruction instruction = buildLoadInstruction(tableIndex, type);
        instructions.add(instruction);
        codeLength += 1 + instruction.getInstructionSet().getOperand();
        stack.put(type);
    }

    public void loadFromLocals(short tableIndex){
        LocalVariableWrapper localVariable = locals.getLocalVariableByIndex(tableIndex);
        if (localVariable == null) {
            throw new RuntimeException("error local variable table index");
        }
        Type type = localVariable.getType();
        Instruction instruction = buildLoadInstruction(tableIndex, type);
        instructions.add(instruction);
        codeLength += 1 + instruction.getInstructionSet().getOperand();
        stack.put(type);
    }

    /**
     * value = array[index];
     * 添加数组取值指令
     * 首先将‘数组引用(arrayref)’压入栈中
     * 然后将‘需要操作的下标(index)’压入栈中
     * 最后执行本方法添加对应‘_aload'指令
     */
    public void loadFromArray() {
        {
            Type index = stack.pop();
            if (!index.isIntType()) {
                throw new RuntimeException("array index must be a int type");
            }
        }
        Type ref = stack.pop();
        if (!ref.isArrayType()) {
            throw new RuntimeException("Unexpected pop a " + ref.getDescriptor() + ",not a arrayref");
        }
        Type componentType = Type.getComponentType(ref);
        instructions.add(buildArrayLoadInstruction(componentType));
        codeLength += 1;
    }

    /**
     * array[index] = value;
     * 添加数组存储指令
     * 首先将‘数组引用(arrayref)’压入栈中
     * 然后将‘需要操作的下标(index)’压入栈中
     * 再将需要赋的值(value)压入栈中
     * 最后执行本方法添加对应'_astore'指令
     */
    public void storeToArray() {
        Type valueType = stack.pop();
        {
            Type index = stack.pop();
            if (!index.isIntType()) {
                throw new RuntimeException("array index must be a int type");
            }
        }
        {
            Type ref = stack.pop();
            if (!ref.isArrayType()) {
                throw new RuntimeException("Unexpected pop a " + ref.getDescriptor() + ",not a arrayref");
            } else if (!Type.getComponentType(ref).equals(valueType)) {
                throw new RuntimeException("array type must be consistent with value");
            }
        }
        Instruction instruction = buildArrayStoreInstruction(valueType);
        instructions.add(instruction);
        codeLength += 1;
    }

    public short storeToLocals(String localVariableName) {
        Type type = stack.pop();
        LocalVariableWrapper localVariable = new LocalVariableWrapper(localVariableName,type);
        short tableIndex = locals.put(localVariable);
        Instruction instruction = buildStoreInstruction(tableIndex, type);
        instructions.add(instruction);
        codeLength += 1 + instruction.getInstructionSet().getOperand();
        localVariable.setStartPc(codeLength);
        return tableIndex;
    }

    public void loadOrSetField(InstructionSet instructionSet, FieldWrapper field) {
        if (!field.isLoaded()) {
            throw new RuntimeException(field.getFieldName() + "must be load before use");
        }
        if (!instructionSet.isFieldInstruction()) {
            throw new RuntimeException(instructionSet + " not a fieldInstruction");
        }
        Instruction instruction = new Instruction(instructionSet, ConvertTool.D2B(field.getFieldInfoIndex()));
        instructions.add(instruction);
        switch (instructionSet.getOpcode()) {
            case -78:
            case -76:
                popAndPut(1, field.getType());
                break;
            case -77:
            case -75:
                popAndPut(2, null);
                break;
        }
        codeLength += 1 + instruction.getInstructionSet().getOperand();
    }

    public void invokeMethod(InstructionSet instructionSet, MethodWrapper method) {
        if (!instructionSet.isInvokeInstruction()) {
            throw new RuntimeException(instructionSet + " not a invokeInstruction");
        }
        if (!method.isLoaded()) {
            throw new RuntimeException(method.getMethodName() + "need to load before use");
        }
        Instruction newInstruction = new Instruction(instructionSet, ConvertTool.S2B(method.getMethodInfoIndex()));
        instructions.add(newInstruction);
        popAndPut(method.getPop(), method.getReturnType());
        codeLength += 1 + newInstruction.getInstructionSet().getOperand();
    }

    public void endCode() {
        if (!isEnd) {
            instructions.add(new Instruction(returnInstruction));
            codeLength++;
            while(!labelStack.isEmpty()){
                endLabel();
            }
            isEnd = true;
        }
    }

    private void popAndPut(int popCount, Type type) {
        stack.pop(popCount);
        if (type != null) {
            stack.put(type);
        }
    }

    private void initStackAndLocal() {
        this.stack = new OperandStack();
        this.locals = new LocalVariableTable();
        if (parameterType != null && parameterType.length != 0){
            for (LocalVariableWrapper localVariableWrapper : parameterType) {
                locals.put(localVariableWrapper);
            }
        }
    }

    private void setReturnInstruction(Type returnType) {
        if (returnType == null) {
            this.returnInstruction = InstructionSet.RETURN;
        } else if (returnType.isIntType()) {
            this.returnInstruction = InstructionSet.IRETURN;
        } else if (returnType.isDoubleType()) {
            this.returnInstruction = InstructionSet.DRETURN;
        } else if (returnType.isLongType()) {
            this.returnInstruction = InstructionSet.LRETURN;
        } else if (returnType.isFloatType()) {
            this.returnInstruction = InstructionSet.FRETURN;
        } else {
            this.returnInstruction = InstructionSet.ARETURN;
        }
    }

    private Instruction buildStoreInstruction(short tableIndex, Type type) {
        Instruction result;
        boolean iType = type.isIntType() || type.isByteType() || type.isShortType() || type.isCharType();
        if (tableIndex > 3) {
            byte[] operand = new byte[]{(byte) tableIndex};
            if (iType) {
                result = new Instruction(InstructionSet.ISTORE, operand);
            } else if (type.isLongType()) {
                result = new Instruction(InstructionSet.LSTORE, operand);
            } else if (type.isFloatType()) {
                result = new Instruction(InstructionSet.FSTORE, operand);
            } else if (type.isDoubleType()) {
                result = new Instruction(InstructionSet.DSTORE, operand);
            } else {
                result = new Instruction(InstructionSet.ASTORE, operand);
            }
        } else {
            switch (tableIndex) {
                case 0:
                    if (iType) {
                        result = new Instruction(InstructionSet.ISTORE_0);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LSTORE_0);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FSTORE_0);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DSTORE_0);
                    } else {
                        result = new Instruction(InstructionSet.ASTORE_0);
                    }
                    break;
                case 1:
                    if (iType) {
                        result = new Instruction(InstructionSet.ISTORE_1);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LSTORE_1);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FSTORE_1);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DSTORE_1);
                    } else {
                        result = new Instruction(InstructionSet.ASTORE_1);
                    }
                    break;
                case 2:
                    if (iType) {
                        result = new Instruction(InstructionSet.ISTORE_2);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LSTORE_2);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FSTORE_2);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DSTORE_2);
                    } else {
                        result = new Instruction(InstructionSet.ASTORE_2);
                    }
                    break;
                case 3:
                    if (iType) {
                        result = new Instruction(InstructionSet.ISTORE_3);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LSTORE_3);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FSTORE_3);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DSTORE_3);
                    } else {
                        result = new Instruction(InstructionSet.ASTORE_3);
                    }
                    break;
                default:
                    throw new RuntimeException("error tableIndex");
            }
        }
        return result;
    }

    private Instruction buildArrayStoreInstruction(Type type) {
        Instruction result;
        if (type.isIntType()) {
            result = new Instruction(InstructionSet.IASTORE);
        } else if (type.isLongType()) {
            result = new Instruction(InstructionSet.LASTORE);
        } else if (type.isFloatType()) {
            result = new Instruction(InstructionSet.FASTORE);
        } else if (type.isDoubleType()) {
            result = new Instruction(InstructionSet.DASTORE);
        } else if (type.isByteType()) {
            result = new Instruction(InstructionSet.BASTORE);
        } else if (type.isCharType()) {
            result = new Instruction(InstructionSet.CASTORE);
        } else if (type.isShortType()) {
            result = new Instruction(InstructionSet.SASTORE);
        } else {
            result = new Instruction(InstructionSet.AASTORE);
        }
        return result;
    }

    private Instruction buildLoadInstruction(short tableIndex, Type type) {
        Instruction result;
        boolean iType = type.isIntType() || type.isByteType() || type.isShortType() || type.isCharType();
        if (tableIndex > 3) {
            byte[] operand = new byte[]{(byte) tableIndex};
            if (iType) {
                result = new Instruction(InstructionSet.ILOAD, operand);
            } else if (type.isLongType()) {
                result = new Instruction(InstructionSet.LLOAD, operand);
            } else if (type.isFloatType()) {
                result = new Instruction(InstructionSet.FLOAD, operand);
            } else if (type.isDoubleType()) {
                result = new Instruction(InstructionSet.DLOAD, operand);
            } else {
                result = new Instruction(InstructionSet.ALOAD, operand);
            }
        } else {
            switch (tableIndex) {
                case 0:
                    if (iType) {
                        result = new Instruction(InstructionSet.ILOAD_0);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LLOAD_0);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FLOAD_0);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DLOAD_0);
                    } else {
                        result = new Instruction(InstructionSet.ALOAD_0);
                    }
                    break;
                case 1:
                    if (iType) {
                        result = new Instruction(InstructionSet.ILOAD_1);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LLOAD_1);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FLOAD_1);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DLOAD_1);
                    } else {
                        result = new Instruction(InstructionSet.ALOAD_1);
                    }
                    break;
                case 2:
                    if (iType) {
                        result = new Instruction(InstructionSet.ILOAD_2);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LLOAD_2);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FLOAD_2);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DLOAD_2);
                    } else {
                        result = new Instruction(InstructionSet.ALOAD_2);
                    }
                    break;
                case 3:
                    if (iType) {
                        result = new Instruction(InstructionSet.ISTORE_3);
                    } else if (type.isLongType()) {
                        result = new Instruction(InstructionSet.LSTORE_3);
                    } else if (type.isFloatType()) {
                        result = new Instruction(InstructionSet.FSTORE_3);
                    } else if (type.isDoubleType()) {
                        result = new Instruction(InstructionSet.DSTORE_3);
                    } else {
                        result = new Instruction(InstructionSet.ASTORE_3);
                    }
                    break;
                default:
                    throw new RuntimeException("error tableIndex");
            }
        }
        return result;
    }

    private Instruction buildArrayLoadInstruction(Type componentType) {
        Instruction result = null;
        if (componentType.isIntType()) {
            result = new Instruction(InstructionSet.IALOAD);
        } else if (componentType.isLongType()) {
            result = new Instruction(InstructionSet.LALOAD);
        } else if (componentType.isFloatType()) {
            result = new Instruction(InstructionSet.FALOAD);
        } else if (componentType.isDoubleType()) {
            result = new Instruction(InstructionSet.DALOAD);
        } else if (componentType.isByteType()) {
            result = new Instruction(InstructionSet.BALOAD);
        } else if (componentType.isCharType()) {
            result = new Instruction(InstructionSet.CALOAD);
        } else if (componentType.isShortType()) {
            result = new Instruction(InstructionSet.SALOAD);
        } else {
            result = new Instruction(InstructionSet.AALOAD);
        }
        return result;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Code");
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public Type getReturnType() {
        return returnType;
    }

    public LocalVariableWrapper[] getParameterType() {
        return parameterType;
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
}
