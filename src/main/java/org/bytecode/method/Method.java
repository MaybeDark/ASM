package org.bytecode.method;

import org.Access;
import org.InstructionSet;
import org.Type;
import org.bytecode.com.Attribute;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.info.ConstantPoolMethodrefInfo;
import org.bytecode.method.attribute.Exceptions;
import org.bytecode.method.attribute.code.Code;
import org.bytecode.constantpool.ConstantPool;
import com.sun.istack.internal.Nullable;
import org.exception.TypeErrorException;
import org.tools.ArrayTool;
import org.tools.ConvertTool;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.*;

public class Method {
    private short cpIndex;
    private final int access;
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;
    private Map<String,Attribute> attributes;
    private short methodNameCpIndex;
    private short methodDescCpIndex;
    private final Code code;
    private ConstantPool constantPool;

    public Method(int access,String fullClassName, String methodName, @Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        this.access = access;
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        if (ArrayTool.notNull(parameters)) {
            Type[] parameterTypes = new Type[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getType();
            }
            this.methodDesc = Type.getMethodDescriptor(returnType, parameterTypes);
        }else {
            this.methodDesc = Type.getMethodDescriptor(returnType);
        }
        if (Access.isStatic((short) access)){
            this.code = new Code(null,returnType,parameters);
        }else {
            this.code = new Code(Type.getType(Type.getClassDescriptor(fullClassName)),returnType,parameters);
        }
        attributes = new HashMap<>();
        attributes.put("Code",code);
    }

    /**
     * <div>if语句跳转指令</div>
     * <div>与正常编写代码不同,在指令中条件成立则跳转而不是执行if语句内代码</div>
     * <ul>
     *     <p>IFEQ 当操作数相等零时跳转</p>
     *     <p>IFNE 当操作数不等于零时跳转</p>
     *     <p>IFLT 当操作数小于零时跳转</p>
     *     <p>IFGE 当操作数小于等于零时跳转</p>
     *     <p>IFLE 当操作数大于零时跳转</p>
     *     <p>IFNE 当操作数大于等于零时跳转</p>
     *     <p>IF_ICMPEQ 当两int类型操作数等于时跳转</p>
     *     <p>IF_ICMPNE 当两int类型操作数不等于时跳转</p>
     *     <p>IF_ICMPLT 当两int类型操作数小于零时跳转</p>
     *     <p>IF_ICMPGE 当两int类型操作数大于等于零时跳转</p>
     *     <p>IF_ICMPGT 当两int类型操作数大于零时跳转</p>
     *     <p>IF_ICMPLE 当两int类型操作数小于等于零时跳转</p>
     *     <p>IF_ACMPEQ 当两reference操作数等于时跳转</p>
     *     <p>IF_ACMPNE 当两reference操作数不等于零时跳转</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method startIf(String instruction){
        instruction = instruction.toUpperCase();
        code.startIf(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     *  <div>在使用分支语句中需调用endif方法结束if语句内代码，并生成offset</div>
     */
    public Method endIf(){
        code.endCode();
        return this;
    }

    /**
     * <div>new Object(Types);</div>
     * <div>当new对象时需生成一系列指令</div>
     * @param classWrapper 需要new的类
     * @param constructorParameterTypes 需要new的类对应的构造器参数
     */
    public Method newObject(ClassWrapper classWrapper,Type... constructorParameterTypes){
        code.newObject(classWrapper,constantPool,constructorParameterTypes);
        return this;
    }

    /**
     * <div>有些类型的比较指令</div>
     * <ul>
     *     <p>LCMP long类型比较</p>
     *     <p>FCMPL float类型比较，如果含NaN则返回1</p>
     *     <p>FCMPG float类型比较，如果含NaN则返回-1</p>
     *     <p>DCMPL double类型比较，如果含NaN则返回1</p>
     *     <p>DCMPG double类型比较，如果含NaN则返回-1</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method compare(String instruction){
        instruction = instruction.toUpperCase();
        code.operandCompare(InstructionSet.valueOf(instruction));
        return this;
    }


    /**
     * <div>两操作数相加指令</div>
     * <ul>
     *     <p>IADD int类型相加</p>
     *     <p>LADD long类型相加</p>
     *     <p>FADD float类型相加</p>
     *     <p>DADD double类型相加</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method add(String instruction){
        code.mathOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>两操作数相减指令</div>
     * <div>ISUB int类型相减</div>
     * <div>LSUB long类型相减</div>
     * <div>FSUB float类型相减</div>
     * <div>DSUB double类型相减</div>
     * @param instruction 上述指令
     */
    public Method sub(String instruction){
        code.mathOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>两操作数相乘指令</div>
     * <ul>
     *     <p>IMUL int类型相乘</p>
     *     <p>LMUL long类型相乘</p>
     *     <p>FMUL float类型相乘</p>
     *     <p>DMUL double类型相乘</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method mul(String instruction){
        code.mathOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>两操作数相除指令</div>
     * <ul>
     *     <p>IDIV int类型相除</p>
     *     <p>LDIV long类型相除</p>
     *     <p>FDIV float类型相除</p>
     *     <p>DDIV double类型相除</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method div(String instruction){
        code.mathOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>两操作数求余指令</div>
     * <ul>
     *     <p>>IDIV int类型求余</p>
     *     <p>LDIV long类型求余</p>
     *     <p>FDIV float类型求余</p>
     *     <p>DDIV double类型求余</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method rem(String instruction){
        code.mathOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }


    /**
     * <div>操作数求反指令</div>
     * <ul>
     *   <p>INEG int类型求反</p>
     *   <p>LNEG long类型求反</p>
     *   <p>FNEG float类型求反</p>
     *   <p>DNEG double类型求反</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method neg(String instruction){
        code.logicalOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }


    /**
     * <div>操作数进行逻辑运算</div>
     * <ul>
     *     <p>ISHL int类型左移</p>
     *     <p>LSHL long类型左移</p>
     *     <p>ISHR int类型右移</p>
     *     <p>LSHR long类型右移</p>
     *     <p>IUSHR int类型含符号位右移</p>
     *     <p>LUSHR long类型含符号位右移</p>
     *     <p>IAND int类型按位与</p>
     *     <p>LAND long类型按位与</p>
     *     <p>IOR int类型按位或</p>
     *     <p>LOR long类型按位或</p>
     *     <p>IXOR int类型按位异或</p>
     *     <p>LXOR long类型按位异或</p>
     * </ul>
     * @param instruction 上述指令
     */
    public Method logicalOperand(String instruction){
        code.logicalOperandOperation(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>类型强转指令</div>
     * <ul>
     *     <p>I2_ 将int类型转换成(L、F、D、B、C、S)类型</p>
     *     <p>L2_ 将long类型转换成(I、L、F)类型</p>
     *     <p>F2_ 将float类型转换成(I、L、D)类型</p>
     *     <p>D2_ 将double类型转换成(I、L、F)类型</p>
     * </ul>
     * @param instruction 上述指令
     *
     */
    public Method convent(String instruction){
        code.operandConvert(InstructionSet.valueOf(instruction));
        return this;
    }

    /**
     * <div>自增指令</div>
     */
    public Method increment(String localVariableName){
        code.iinc(localVariableName, (byte) 1);
        return this;
    }

    /**
     * <div>加载int,float,double,long,String以及null等<b>常量</b></div>
     */
    public <T> Method loadConstant(@Nullable T data){
        if (!loadConstant0(data)){
            throw new TypeErrorException("data not loadable");
        }
        return this;
    }

    private <T> boolean loadConstant0(T data) {
        if (data == null){
            code.loadFromConstant(InstructionSet.ACONST_NULL);
        }else if (data instanceof Integer){
            loadInt((Integer) data);
        }else if(data instanceof Float){
            loadFloat((Float) data);
        }else if(data instanceof Long){
            loadLong((Long) data);
        }else if (data instanceof Double){
            loadDouble((Double) data);
        }else if (data instanceof String){
            short index = constantPool.putStringInfo((String) data);
            code.loadFromConstantPool(constantPool,index);
        }else{
            return false;
        }
        return true;
    }

    private void loadDouble(Double data) {
        if (data == 0D){
            code.loadFromConstant(InstructionSet.DCONST_0);
        }else if (data == 1D){
            code.loadFromConstant(InstructionSet.DCONST_1);
        }else{
            short index = constantPool.putDoubleInfo(data);
            code.loadFromConstantPool(constantPool,index);
        }
    }

    private void loadLong(Long data) {
        if (data == 0L){
            code.loadFromConstant(InstructionSet.LCONST_0);
        }else if (data == 1L){
            code.loadFromConstant(InstructionSet.LCONST_1);
        }else{
            short index = constantPool.putLongInfo(data);
            code.loadFromConstantPool(constantPool,index);
        }
    }


    private void loadFloat(float data){
        if (data == 0F){
            code.loadFromConstant(InstructionSet.FCONST_0);
        }else if (data == 1F){
            code.loadFromConstant(InstructionSet.FCONST_1);
        }else if (data == 2F){
            code.loadFromConstant(InstructionSet.FCONST_2);
        }else{
            short index = constantPool.putFloatInfo(data);
            code.loadFromConstantPool(constantPool,index);
        }
    }

    private void loadInt(int data){
        if (data >= -1 && data <= 5){
            switch (data){
                case -1:
                    code.loadFromConstant(InstructionSet.ICONST_M1);
                    break;
                case 0:
                    code.loadFromConstant(InstructionSet.ICONST_0);
                    break;
                case 1:
                    code.loadFromConstant(InstructionSet.ICONST_1);
                    break;
                case 2:
                    code.loadFromConstant(InstructionSet.ICONST_2);
                    break;
                case 3:
                    code.loadFromConstant(InstructionSet.ICONST_3);
                    break;
                case 4:
                    code.loadFromConstant(InstructionSet.ICONST_4);
                    break;
                case 5:
                    code.loadFromConstant(InstructionSet.ICONST_5);
                    break;
            }
        } else if (data >= Short.MIN_VALUE && data <= Short.MAX_VALUE){
            code.loadFromShortNum((short) data);
        } else{
            short index = constantPool.putIntegerInfo(data);
            code.loadFromConstantPool(constantPool,index);
        }
    }
    /**
     * <div>从局部变量表中加载</div>
     * @param localVariableName 局部变量名
     */
    public Method loadLocal(String localVariableName){
        code.loadFromLocals(localVariableName);
        return this;
    }

    /**
     * <div>加载成员变量或者静态变量</div>
     * <ul>
     *     <p>GETFIELD  加载成员变量</p>
     *     <p>GETSTATIC 加载静态变量</p>
     * </ul>
     * @param instruction 指令
     * @param fieldWrapper 变量描述
     */
    public Method loadField(String instruction,FieldWrapper fieldWrapper){
        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
        if (!fieldWrapper.isLoaded()){
            fieldWrapper.load(constantPool);
        }
        code.loadOrSetField(instructionSet,fieldWrapper);
        return this;
    }

    /**
     * <div>数组取值</div>
     * _ALOAD 将弹出两个操作数，所以调用此方法之前需要先数组引用、索引压入栈内
     */
    public Method loadArrayElement(){
        code.loadFromArray();
        return this;
    }


    /**
     * <div>数组设值</div>
     * _ALOAD 将弹出三个操作数,先后分别是数组引用、索引、值
     */
    public Method storeToArray(){
        code.storeToArray();
        return this;
    }

    /**
     * <div>将操作数存储到局部变量表</div>
     * @param localVariableName 局部变量名
     */
    public Method storeToLocals(String localVariableName){
        code.storeToLocals(localVariableName);
        return this;
    }

    /**
     * <div>将操作数存储到成员变量或者静态变量</div>
     * <ul>
     *     <p>PUTFIELD 成员变量赋值</p>
     *     <p>PUTSTATIC 静态变量赋值</p>
     * </ul>
     * @param instruction  上述指令
     * @param field 变量描述
     */
    public Method storeToField(String instruction,FieldWrapper field){
        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
        code.loadOrSetField(instructionSet,field);
        return this;
    }

    /**
     * <div>调用方法</div>
     * <ul>
     *     <p>INVOKEVIRTUAL</p>
     *     <p>INVOKESPECIAL</p>
     *     <p>INVOKESTATIC</p>
     *     <p>INVOKEINTERFACE</p>
     *     <p>INVOKEDYNAMIC</p>
     * </ul>
     * @param instruction 上述指令
     * @param methodWrapper 方法描述
     * @return {@link Method}
     */
    public Method invokeMethod(String instruction, MethodWrapper methodWrapper){
        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
        code.invokeMethod(instructionSet,methodWrapper);
        return this;
    }

    /**
     * <div>操作数栈相关操作</div>
     * <ul>
     *     <p>pop</p>
     *     <p>POP2</p>
     *     <p>DUP</p>
     *     <p>DUP_X1</p>
     *     <p>DUP_X2</p>
     *     <p>DUP2</p>
     *     <p>DUP2_X1</p>
     *     <p>DUP2_X2</p>
     *     <p>SWAP</p>
     * </ul>
     * @param instruction 上述指令
     * @return {@link Method}
     */
    public Method stackBehavior(String instruction){
        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
        code.stackBehavior(instructionSet);
        return this;
    }

    /**
     * 将引用类型进行强转
     * @param classWrapper 强转类描述
     * @return {@link Method}
     */
    public Method checkCast(ClassWrapper classWrapper){
        code.checkCast(constantPool,classWrapper);
        return this;
    }

    public Method addException(Class<? extends Exception> exception){
        Exceptions exc = new Exceptions();
        Attribute exceptions = attributes.putIfAbsent("Exceptions",exc);
        if (exceptions != null){
            ((Exceptions) exceptions).addException(exception);
            return this;
        }
        exc.addException(exception);
        return this;
    }

    public int getAccess() {
        return access;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    byte[] toByteArray(){
        byte[] attrBytes = new byte[0];
        short attrCount = 0;
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            if (attribute!=null){
                attrCount ++;
                byte[] bytes = attribute.toByteArray();
                attrBytes = ArrayTool.join(attrBytes, bytes);
            }
        }
        byte[] result = new byte[8+attrBytes.length];
        System.arraycopy(ConvertTool.S2B((short) access),0,result,0,2);
        System.arraycopy(ConvertTool.S2B(methodNameCpIndex),0,result,2,2);
        System.arraycopy(ConvertTool.S2B(methodDescCpIndex),0,result,4,2);
        System.arraycopy(ConvertTool.S2B(attrCount),0,result,6,2);
        System.arraycopy(attrBytes,0,result,8,attrBytes.length);
        return result;
    }

    public void setConstantPool(ConstantPool constantPool){
        this.constantPool = constantPool;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return hashCode() == method.hashCode();
    }

    short load(ConstantPool constantPool) {
        cpIndex = constantPool.putMethodrefInfo(fullClassName, methodName, methodDesc);
        methodNameCpIndex = constantPool.putUtf8Info(methodName);
        methodDescCpIndex = constantPool.putUtf8Info(methodDesc);
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute :  attr) {
            attribute.load(constantPool);
        }
        return cpIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullClassName, methodName, methodDesc);
    }
}
