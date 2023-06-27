package org.bytecode.method.attribute.code;

import org.InstructionSet;
import org.Type;
import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.*;
import org.bytecode.method.attribute.code.localvariabletable.LocalVariableTable;
import org.bytecode.method.attribute.code.operandstack.OperandStack;
import org.exception.ErrorInstruction;
import org.exception.TypeErrorException;
import org.bytecode.method.attribute.code.instruction.Instruction;
import com.sun.istack.internal.Nullable;
import org.bytecode.method.attribute.code.bracket.IfBracket;
import org.bytecode.method.attribute.code.bracket.Bracket;
import org.bytecode.method.attribute.code.bracket.BracketStack;
import org.tools.ArrayTool;
import org.tools.ConvertTool;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Code extends Attribute {
    private Type returnType;
    private final LocalVariableWrapper[] parameters;
    private final BracketStack bracketStack = new BracketStack();
    private final Map<String, Attribute> attributes = new HashMap<>();
    InstructionSet returnInstruction;
    LinkedList<Instruction> instructions = new LinkedList<>();
    private OperandStack stack;
    private LocalVariableTable locals;
    private int codeLength;
    private boolean needStackMapTable = false;
    private boolean isEnd = false;

    private StackLoader stackLoader;
    public Code(@Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        this(null,returnType,parameters);
    }

    /**
     * 如果非静态方法使用此构造器
     *
     * @param owner      类
     * @param returnType 返回类型
     * @param parameters 参数
     */
    public Code(@Nullable Type owner, @Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        this.returnType = returnType;
        this.parameters = parameters;
        bracketStack.put(new Bracket(0));
        attributeLength = 8;
        setReturnInstruction(returnType);
        initStackAndLocal(owner);
        attributes.put("LocalVariableTable",locals);
    }

    public StackLoader stackLoader(){
        if (stackLoader == null){
            stackLoader = new StackLoader(this);
        }
        return stackLoader;
    }










    private void startBracket(Bracket bracket) {
        bracketStack.put(bracket);
    }

    private int addInstruction(Instruction instruction){
        InstructionSet instructionSet = instruction.getInstructionSet();
        if (instructionSet.unknownOperandLength()){
            throw new RuntimeException(instructionSet + " is unknownOperandLength");
        }
        if (instructionSet.isJumpInstruction()) needStackMapTable = true;
        codeLength += 1 + instructionSet.getOperand();;
        instructions.add(instruction);
        return codeLength;
    }

    public Bracket startBracket(){
        Bracket bracket = new Bracket(codeLength);
        bracketStack.put(bracket);
        return bracket;
    }

    public Bracket endBracket() {
        Bracket bracket = bracketStack.pop(codeLength);
        locals.endLocalVariableScope(bracket);
        return bracket;
    }

    /**
     * 开始if语句
     * 然后需要增加else语句可以在endIf之前增加goto指令
     * @param instructionSet if类型指令集
     */
    public void startIf(InstructionSet instructionSet) {
        if (!instructionSet.isIfInstruction()) {
            throw new ErrorInstruction(instructionSet + " not a ifInstruction");
        }
        stack.pop(instructionSet.getPop());
        Instruction instruction = new Instruction(instructionSet);
        startBracket(new IfBracket(codeLength, instruction));
        addInstruction(instruction);
        stack.startIfScope();
    }

    /**
     * 结束if语句
     */
    public void endIf() {
        Bracket ifBracket = endBracket();
        if (!(ifBracket instanceof IfBracket)) {
            throw new RuntimeException("ifBracket mismatching");
        }
        stack.endIfScope();
        ifBracket.setLength((short) (codeLength - ifBracket.getStartPc()));
        Instruction instruction = ((IfBracket) ifBracket).getInstruction();
        instruction.setOperand(ConvertTool.S2B((short) ifBracket.getLength()));
    }


    public void newObject(ClassWrapper classWrapper,ConstantPool cp,Type... constructorParameterTypes){
        if (!classWrapper.isLoaded())
            classWrapper.load(cp);
        stack.put(classWrapper.getType());
        addInstruction(new Instruction(InstructionSet.NEW,ConvertTool.S2B(classWrapper.getCpIndex())));
        stackBehavior(InstructionSet.DUP);
        MethodWrapper methodWrapper = new MethodWrapper(classWrapper.getFullClassName(),"<init>",null, constructorParameterTypes);
        methodWrapper.load(cp);
        invokeMethod(InstructionSet.INVOKESPECIAL,methodWrapper);
    }


    /**
     * 添加跳转语句
     * 为code属性添加goto指令（因jsr和ret都已过时，跳转指令使用goto)
     * @param offset 偏移
     */
    public void jumpTo(short offset){
        Instruction instruction = new Instruction(InstructionSet.GOTO, ConvertTool.S2B(offset));
        addInstruction(instruction);
    }

    /**
     * 操作数比较
     *
     * @param instructionSet 指令集
     */
    public void operandCompare(InstructionSet instructionSet){
        if (!instructionSet.isCompareInstruction()){
            throw new RuntimeException(instructionSet + " not a compareOperationInstruction");
        }
        Type pop1 = stack.pop();
        Type pop2 = stack.pop();
        if (pop1.equals(pop2))
            throw new TypeErrorException("compareInstruction need to pop two values of the same type");
        stack.put(Type.INT);
    }

    /**
     * 操作数运算
     *
     * @param instructionSet 运算指令集
     */
    public void mathOperandOperation(InstructionSet instructionSet){
        if (!instructionSet.ismathOperandOperationInstruction()){
            throw new RuntimeException(instructionSet + " not a mathOperandOperationInstruction");
        }
        Type pop1 = stack.pop();
        Type pop2 = stack.pop();
        if (!pop1.equals(pop2))
            throw new TypeErrorException("operationInstruction need to pop two values of the same type");
        stack.put(pop1);
       addInstruction(new Instruction(instructionSet));
    }

    private void negOperandOperation(){
        //do nothing
    }

    public void logicalOperandOperation(InstructionSet instructionSet){
        if (!instructionSet.islogicalOperandOperationInstrucion()){
            throw new RuntimeException(instructionSet + " not a logicalOperandOperationInstrucion");
        }
        if (instructionSet.getOpcode()>=116 && instructionSet.getOpcode()<=119){
            negOperandOperation();
            return;
        }
        Type pop1 = stack.pop();
        stack.pop();
        stack.put(pop1);
        addInstruction(new Instruction(instructionSet));
    }
    /**
     * 局部变量表自增
     *
     * @param tableIndex 局部变量表表索引
     * @param num        自增数
     */
    public void iinc(byte tableIndex,byte num){
        LocalVariableWrapper localVariable = locals.getLocalVariableByIndex(tableIndex);
        if (!localVariable.getType().isIntType()) {
            throw new TypeErrorException("The local variable at index must contain an int");
        }
        Instruction instruction = new Instruction(InstructionSet.IINC, new byte[]{tableIndex, num});
        addInstruction(instruction);
    }

    public void iinc(String name,byte num){
        LocalVariableWrapper localVariable = locals.getLocalVariableByName(name);
        iinc((byte) localVariable.getTableIndex(),num);
    }


    /**
     * 操作数转换类型
     *
     * @param instructionSet 类型转化指令集
     */
    public void operandConvert(InstructionSet instructionSet){
        if (!instructionSet.isConvertInstruction()){
            throw new RuntimeException(instructionSet + " not a convertInstruction");
        }
        Type oType,nType;
        String s = instructionSet.toString();
        {
            char oTypeDesc = s.charAt(0);
            oType = Type.getType(oTypeDesc);
        }
        {
            char nTypeDesc = s.charAt(2);
            nType = Type.getType(nTypeDesc);
        }
        {
            Type pop = stack.pop();
            if (!pop.equals(oType)) {
                throw new TypeErrorException(instructionSet + " need to pop an " + oType.getDescriptor() +
                        " type, but unexpectedly popped up a " + pop.getDescriptor());
            }
        }
        stack.put(nType);
        addInstruction(new Instruction(instructionSet));
    }

    public void loadFromConstant(InstructionSet instructionSet){
        if (!instructionSet.isLoadFromConstantInstruction()){
            throw new RuntimeException(instructionSet + " not a loadConstantInstruction");
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
        addInstruction(new Instruction(instructionSet));
    }

    public void loadFromConstantPool(ConstantPool cp,short cpIndex){
        AbsConstantPoolInfo absConstantPoolInfo = cp.get(cpIndex);
        if (absConstantPoolInfo instanceof ConstantPoolIntegerInfo){
            stack.put(Type.INT);
            if (cpIndex < 256){
                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
            }else {
                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
            }
        }else if (absConstantPoolInfo instanceof ConstantPoolFloatInfo){
            stack.put(Type.FLOAT);
            if (cpIndex < 256){
                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
            }else {
                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
            }
        }else if (absConstantPoolInfo instanceof ConstantPoolStringInfo){
            stack.put(Type.getType(String.class));
            if (cpIndex < 256){
                addInstruction(new Instruction(InstructionSet.LDC,new byte[]{(byte) cpIndex}));
            }else {
                addInstruction(new Instruction(InstructionSet.LDC_W,ConvertTool.S2B(cpIndex)));
            }
        }else if(absConstantPoolInfo instanceof ConstantPoolDoubleInfo) {
            stack.put(Type.DOUBLE);
            addInstruction(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
        }else if(absConstantPoolInfo instanceof ConstantPoolLongInfo){
            stack.put(Type.LONG);
            addInstruction(new Instruction(InstructionSet.LDC2_W,ConvertTool.S2B(cpIndex)));
        } else{
            throw new TypeErrorException("Can only download int, long, double, String, float from the constant pool");
        }
    }

    public void loadFromShortNum(short num) {
        if (num <= Byte.MAX_VALUE && num >= Byte.MIN_VALUE) {
            addInstruction(new Instruction(InstructionSet.BIPUSH, new byte[]{(byte) num}));
        } else {
            addInstruction(new Instruction(InstructionSet.SIPUSH, ConvertTool.S2B(num)));
        }
    }

    public void loadFromLocals(String localVariableName) {
        LocalVariableWrapper localVariable = locals.getLocalVariableByName(localVariableName);
        if (localVariable == null) {
            throw new RuntimeException(localVariableName + "not in locals,The scope may be ended or undefined");
        }
        Type type = localVariable.getType();
        short tableIndex = localVariable.getTableIndex();
        addInstruction(buildLoadInstruction(tableIndex, type));
        stack.put(type);
    }

    public void loadFromLocals(short tableIndex){
        LocalVariableWrapper localVariable = locals.getLocalVariableByIndex(tableIndex);
        if (localVariable == null) {
            throw new RuntimeException("error local variable table index");
        }
        Type type = localVariable.getType();
        addInstruction(buildLoadInstruction(tableIndex, type));
        stack.put(type);
    }

    public void loadOrSetField(InstructionSet instructionSet, FieldWrapper field) {
        if (!field.isLoaded()) {
            throw new RuntimeException(field.getFieldName() + "must be load before use");
        }
        if (!instructionSet.isFieldInstruction()) {
            throw new RuntimeException(instructionSet + " not a fieldInstruction");
        }
        Instruction instruction = new Instruction(instructionSet, ConvertTool.S2B(field.getFieldInfoIndex()));
        addInstruction(instruction);
        switch (instructionSet.getOpcode()) {
            case -78:
                stack.put(field.getType());
                break;
            case -76:
                popAndPut(1,field.getType());
                break;
            case -77:
                stack.pop();
                break;
            case -75:
                stack.pop(2);
                break;
        }
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
        addInstruction(buildArrayLoadInstruction(Type.getComponentType(ref)));
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
        addInstruction(buildArrayStoreInstruction(valueType));
    }

    public short storeToLocals(String localVariableName) {
        Type type = stack.pop();
        short tableIndex = locals.searchByName(localVariableName);
        if (tableIndex == -1) {
            LocalVariableWrapper localVariable = new LocalVariableWrapper(localVariableName,type);
            localVariable.setStartPc(codeLength);
            tableIndex = locals.put(localVariable);
        }
        Instruction instruction = buildStoreInstruction(tableIndex, type);
        addInstruction(instruction);
        return tableIndex;
    }

    public void invokeMethod(InstructionSet instructionSet, MethodWrapper method) {
        if (!instructionSet.isInvokeInstruction()) {
            throw new RuntimeException(instructionSet + " not a invokeInstruction");
        }
        if (!method.isLoaded()) {
            throw new RuntimeException(method.getMethodName() + "need to load before use");
        }
        stack.pop(method.getPop());
        byte operand = instructionSet.getOpcode();
        if (operand == (byte) 0xB9 || operand == (byte) 0xB7 || operand == (byte)0xB6)
            stack.pop();
        Instruction newInstruction = new Instruction(instructionSet, ConvertTool.S2B(method.getMethodInfoIndex()));
        addInstruction(newInstruction);
        stack.put(method.getReturnType());
    }

    /**
     * 堆栈操作指令例如pop,dup等指令
     *
     * @param instructionSet 指令集
     */
    public void stackBehavior(InstructionSet instructionSet){
        switch (instructionSet.getOpcode()){
            case 87: {
                Type pop = stack.pop();
                if (pop.isLongType() || pop.isDoubleType())
                    throw new TypeErrorException("The pop instruction must not be used unless value is a value of a category 1 computational type");
                break;
            }
            case 88:
                stack.pop(2);
                break;
            case 89: {
                Type pop = stack.pop();
                if (pop.isLongType() || pop.isDoubleType())
                    throw new TypeErrorException("The dup instruction must not be used unless value is a value of a category 1 computational type");
                stack.put(pop);
                stack.put(pop);
            }
                break;
            case 90: {
                Type pop1 = stack.pop();
                Type pop2 = stack.pop();
                if (pop1.isDoubleType() || pop1.isLongType())
                    throw new TypeErrorException("The dup_x1 instruction must not be used unless value1 is a value of a category 1 computational type");
                if (pop2.isDoubleType() || pop2.isLongType())
                    throw new TypeErrorException("The dup_x1 instruction must not be used unless value2 is a value of a category 1 computational type");
                stack.put(pop1);
                stack.put(pop2);
                stack.put(pop1);
            }
                break;
            case 91: {
                Type pop1 = stack.pop();
                Type pop2 = stack.pop();
                if (pop1.isDoubleType() || pop1.isLongType())
                    throw new TypeErrorException("The dup_x2 instruction must not be used unless value1 is a value of a category 1 computational type");
                if (!(pop2.isDoubleType() || pop2.isLongType()))
                    throw new TypeErrorException("The dup_x2 instruction must not be used unless value2 not is a value of a category 2 computational type");
                stack.put(pop1);
                stack.put(pop2);
                stack.put(pop1);
            }
                break;
            case 92:{
                Type pop1 = stack.pop();
                if (pop1.isDoubleType() || pop1.isLongType()) {
                    stack.put(pop1);
                }else {
                    Type pop2 = stack.pop();
                    if (pop2.isDoubleType() || pop2.isLongType())
                        throw new TypeErrorException("The dup2 instruction must not be used unless value2 is a value of a category 1 computational type");
                    stack.put(pop2);
                    stack.put(pop1);
                    stack.put(pop2);
                }
                stack.put(pop1);
            }
                break;
            case 93: {
                Type pop1 = stack.pop();
                Type pop2 = stack.pop();
                if (pop1.isLongType() || pop1.isDoubleType()){
                    if (pop2.isDoubleType() || pop2.isLongType())
                        throw new TypeErrorException("The dup2_x1 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 2 computational type");
                    stack.put(pop1);
                }else {
                    if (pop2.isDoubleType() || pop2.isLongType())
                        throw new TypeErrorException("The dup2_x1 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 1 computational type");
                    Type pop3 = stack.pop();
                    if (pop3.isDoubleType() || pop3.isLongType())
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
            case 94:{
                Type pop1 = stack.pop();
                Type pop2 = stack.pop();
                //2
                if (pop1.isDoubleType() || pop1.isLongType()){
                    //2 2
                    if (pop2.isDoubleType() || pop2.isLongType()){
                        stack.put(pop1);
                        stack.put(pop2);
                        stack.put(pop1);
                    }else{
                        //2 1 1
                        Type pop3 = stack.pop();
                        if (pop3.isDoubleType() || pop3.isLongType()) {
                            throw new TypeErrorException("The dup2_x2 instruction must not be used unless value3 is a value of a category 1 computational type " +
                                    "when value1 is a value of a category 2 computational type and value2 is a value of a category 1 computational type");
                        }
                        stack.put(pop1);
                        stack.put(pop3);
                        stack.put(pop2);
                        stack.put(pop1);
                    }
                //1
                }else {
                    if (pop2.isDoubleType() || pop2.isLongType()) {
                        throw new TypeErrorException("The dup2_x2 instruction must not be used unless value2 is a value of a category 1 computational type " +
                                "when value1 is a value of a category 1 computational type");
                    //1 1
                    }else{
                        Type pop3 = stack.pop();
//                        1 1 2
                        if (pop3.isLongType() || pop3.isDoubleType()){
                            stack.put(pop2);
                            stack.put(pop1);
                            stack.put(pop3);
                            stack.put(pop2);
                            stack.put(pop1);
//                      1 1 1 1
                        }else {
                            Type pop4 = stack.pop();
                            if (pop4.isDoubleType() || pop4.isLongType()) {
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
            case 95:{
                Type pop1 = stack.pop();
                Type pop2 = stack.pop();
                if (pop1.isDoubleType() || pop1.isLongType())
                    throw new TypeErrorException("The swap instruction must not be used unless value1 is a value of a category 1 computational type ");
                if (pop2.isDoubleType() || pop2.isLongType())
                    throw new TypeErrorException("The swap instruction must not be used unless value2 is a value of a category 1 computational type ");
                stack.put(pop2);
                stack.put(pop1);
            }
            break;
            default:
                throw new RuntimeException(instructionSet + " not a stackBehaviorInstruction");

        }
        addInstruction(new Instruction(instructionSet));
    }

    public void checkCast(ConstantPool constantPool,ClassWrapper classWrapper){
        if (!classWrapper.isLoaded()) {
          classWrapper.load(constantPool);
        }

        addInstruction(new Instruction(InstructionSet.CHECKCAST,ConvertTool.S2B(classWrapper.getCpIndex())));
    }

    private void popAndPut(int popCount, Type type) {
        stack.pop(popCount);
        if (type != null) {
            stack.put(type);
        }
    }

    private void initStackAndLocal(Type obj) {
        this.stack = new OperandStack();
        this.locals = new LocalVariableTable();

//        StackMapTable stackMapTable = new StackMapTable();
//        attributes.put("StackMapTable",stackMapTable);
        if (obj != null) {
            locals.put(new LocalVariableWrapper("this", obj));
        }
        if (parameters != null && parameters.length != 0){
            for (LocalVariableWrapper localVariableWrapper : parameters) {
                locals.put(localVariableWrapper);
            }
//            TODO stackMapTableInit
//            InitFrame initFrame = new InitFrame(parameters.length,);
//            stackMapTable.addStackMapFrame(initFrame);
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

    public void endCode() {
        if (!isEnd) {
            checkReturnType();
            addInstruction(new Instruction(returnInstruction));
            while(!bracketStack.isEmpty()){
                endBracket();
            }
            if (!stack.isEmpty()){
                System.err.println("The final operand stack is not empty,maybe use stackOperation instruction make it empty or check you instructions is correct");
            }
            isEnd = true;
        }
    }

    private void checkReturnType(){
        if (returnType != null && !returnType.isVoidType()) {
            Type pop = stack.pop();
            if (pop.equals(returnType)) {
                throw new TypeErrorException("need to return a value of " + returnType.getDescriptor());
            }
        }
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Code");
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            attribute.load(cp);
        }
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!isEnd){
            endCode();
        }
        attributeLength += codeLength;
        attributeLength += 2;       //异常表长度
        Collection<Attribute> attr = attributes.values();
        byte[] attrBytes = new byte[0];
        short attrCount = 0;
        for (Attribute attribute : attr) {
            if (attribute!=null){
                attrCount ++;
                byte[] bytes = attribute.toByteArray();
                attrBytes = ArrayTool.join(attrBytes, bytes);
            }
        }
        attributeLength += 2;   //attribute长度
        attributeLength += attrBytes.length;
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(stack.getMax()),0,result,6,2);
        System.arraycopy(ConvertTool.S2B(locals.getMax()),0,result,8,2);
        System.arraycopy(ConvertTool.I2B(codeLength),0,result,10,4);
        int index = 14;
        for (Instruction instruction : instructions) {
            byte[] bytes = instruction.toByteArray();
            System.arraycopy(bytes,0,result,index,bytes.length);
            index += bytes.length;
        }
        System.arraycopy(ConvertTool.S2B((short) 0),0,result,index,2);//异常表
        index += 2;
        System.arraycopy(ConvertTool.S2B(attrCount),0,result,index,2);
        index += 2;
        System.arraycopy(attrBytes,0,result,index,attrBytes.length);
        return result;
    }

    public boolean isEmpty(){
        return instructions.isEmpty();
    }

    public String list(){
        endCode();
        StringBuilder sb = new StringBuilder();
        int index = 1;
        int pcIndex = 0;
        for (Instruction instruction : instructions) {
            String instructionStr = instruction.getInstructionSet().toString().toLowerCase();
            sb.append(String.format("%- 2d  %- 3d  %-10s",
                    index,pcIndex,instructionStr));
            if (instruction.getOperand() != null)
                sb.append(String.format("% -2d",ConvertTool.B2S(instruction.getOperand())));
            sb.append('\n');
            pcIndex += instruction.getLength();
        }
        return sb.toString();
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType){
        if (isEnd)
            throw new RuntimeException("code is end cannot modify returnType");
        this.returnType = returnType;
        setReturnInstruction(returnType);
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
}
