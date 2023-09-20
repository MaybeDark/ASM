package org.bytecode.method;

import org.bytecode.ByteCodeWriter;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.clazz.bootstrapmethod.CallSite;
import org.bytecode.attributes.method.code.BranchCondition;
import org.bytecode.attributes.method.code.instruction.Operator;
import org.bytecode.attributes.method.code.instruction.InstructionSet;
import org.Type;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.attributes.method.Exceptions;
import org.bytecode.attributes.method.code.Code;
import org.bytecode.constantpool.ConstantPool;
import com.sun.istack.internal.Nullable;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;
import java.util.*;

public class MethodWriter implements ByteCodeWriter {
    public final ClassWriter classWriter;
    private final int access;
    private final String methodName;
    private final String methodDesc;
    private Type[] parameterTypes;
    private Type returnType;
    private final Code code;
    private int callSiteCount;
    private final Map<String, Attribute> attributes;

    public MethodWriter(ClassWriter classWriter,int access, String methodName, @Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        this.classWriter = classWriter;
        this.access = access;
        this.methodName = methodName;
        this.code = new Code(this, returnType, parameters);
        this.returnType = returnType;
        attributes = new HashMap<>();
        if (ArrayTool.notNull(parameters)) {
            parameterTypes = new Type[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getType();
            }
            this.methodDesc = Type.getMethodDescriptor(returnType, parameterTypes);
        } else {
            this.methodDesc = Type.getMethodDescriptor(returnType);
        }
        attributes.put("Code", code);
    }

    public MethodWriter loadNull() {
        code.loadNull();
        return this;
    }

    public MethodWriter loadInt(int num) {
        code.loadInt(num);
        return this;
    }

    public MethodWriter loadDouble(double num) {
        code.loadDouble(num);
        return this;
    }

    public MethodWriter loadLong(long num) {
        code.loadLong(num);
        return this;
    }

    public MethodWriter loadFloat(float num) {
        code.loadFloat(num);
        return this;
    }

    public MethodWriter loadString(String str) {
        code.loadString(str);
        return this;
    }

    public MethodWriter loadLocal(String localName) {
        code.loadLocal(localName);
        return this;
    }

    public MethodWriter loadArrayElement() {
        code.loadArrayElement();
        return this;
    }

    public MethodWriter loadField(FieldWrapper fieldWrapper, boolean isStatic) {
        code.loadField(fieldWrapper, isStatic);
        return this;
    }

     void loadCallSite(CallSite callSite) {
        code.invokeDynamic(callSite);
        String proxyMethodName = "lambda$"+(this instanceof ConstructorWriter ?methodName:"new")+"$"+callSiteCount;
        callSite.setProxyMethod(classWriter.addMethod(4106,proxyMethodName,callSite.getTargetType()));
        callSiteCount++;
    }

    public MethodWriter storeLocal(String localName) {
        code.storeLocal(localName);
        return this;
    }

    public MethodWriter storeArrayElement() {
        code.storeArrayElement();
        return this;
    }

    public MethodWriter storeField(FieldWrapper fieldWrapper, boolean isStatic) {
        code.storeField(fieldWrapper, isStatic);
        return this;
    }

    public MethodWriter checkCast(ClassWrapper classWrapper) {
        code.checkCast(classWrapper);
        return this;
    }

    public MethodWriter stackBehavior(InstructionSet inst) {
        code.stackBehavior(inst);
        return this;
    }

    public MethodWriter stackBracket() {
        code.startBracket();
        return this;
    }

    public MethodWriter endBracket() {
        code.endBracket();
        return this;
    }

    public MethodWriter startIf(BranchCondition branchCondition) {
        code.startIf(branchCondition);
        return this;
    }

    public MethodWriter endIf() {
        code.endIf();
        return this;
    }

    public MethodWriter newObject(ClassWrapper classWrapper) {
        code.newObject(classWrapper);
        return this;
    }

    public MethodWriter newArray(Type elementType, int length) {
        code.newArray(elementType, length);
        return this;
    }

    public MethodWriter arrayLength() {
        code.arrayLength();
        return this;
    }

    public MethodWriter throwException() {
        code.throwException();
        return this;
    }

    public MethodWriter monitorEnter() {
        code.monitorEnter();
        return this;
    }

    public MethodWriter monitorExit() {
        code.monitorExit();
        return this;
    }

    public MethodWriter multiANewArray(Type elementType, int cow, int rol) {
        return multiANewArray(elementType, 2, cow, rol);
    }

    public MethodWriter multiANewArray(Type elementType, int dimensions, int... size) {
        code.multiANewArray(elementType, dimensions, size);
        return this;
    }

    public MethodWriter jumpUp() {
        code.jumpUp();
        return this;
    }

    public MethodWriter jumpDown() {
        code.jumpDown();
        return this;
    }

    public MethodWriter increment(String localName) {
        code.increment(localName);
        return this;
    }

    public MethodWriter invokeSpecial(MethodWrapper target) {
        code.invokeSpecial(target);
        return this;
    }

    public MethodWriter invokeDynamic(CallSite callSite) {
        loadCallSite(callSite);
        return this;
    }

    public MethodWriter invokeInterface(MethodWrapper target) {
        code.invokeInterface(target);
        return this;
    }

    public MethodWriter invokeStatic(MethodWrapper target) {
        code.invokeStatic(target);
        return this;
    }

    public MethodWriter invokeVirtual(MethodWrapper target) {
        code.invokeVirtual(target);
        return this;
    }

    public MethodWriter convert(Type newType) {
        code.convert(newType);
        return this;
    }

    public MethodWriter compare() {
        code.compare();
        return this;
    }

    public MethodWriter operation(Operator operator) {
        if (operator.isCalculateOperator())
            code.mathOperation(operator);
        else
            code.logicalOperation(operator);
        return this;
    }

    public MethodWriter addAttribute(Attribute attribute){
        attributes.put(attribute.getAttributeName(),attribute);
        return this;
    }

    @Override
    public Attribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public ClassWriter endMethod() {
        code.end();
        return classWriter;
    }

    public MethodWriter addException(Class<? extends Exception> exception) {
        Exceptions exc = new Exceptions(this);
//        Attribute exceptions = attributes.putIfAbsent("Exceptions", exc);
//        if (exceptions != null) {
//            ((Exceptions) exceptions).addException(exception);
//            return this;
//        }
        exc.addException(exception);
        return this;
    }

    public int getAccess() {
        return access;
    }

    public String getClassName() {
        return classWriter.thisClass.getClassName();
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] toByteArray() {
        code.end();
        ConstantPool constantPool = classWriter.getConstantPool();
        short methodNameCpIndex = constantPool.putUtf8Info(methodName);
        short methodDescCpIndex = constantPool.putUtf8Info(methodDesc);
        byte[] attrBytes = new byte[0];
        short attrCount = 0;
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            if (attribute != null) {
                attrCount++;
                attribute.load(constantPool);
                byte[] bytes = attribute.toByteArray();
                attrBytes = ArrayTool.join(attrBytes, bytes);
            }
        }

        ByteVector result = new ByteVector(8 + attrBytes.length);
        result.putShort(access)
                .putShort(methodNameCpIndex)
                .putShort(methodDescCpIndex)
                .putShort(attrCount)
                .putArray(attrBytes);
        return result.end();
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public MethodWrapper wrapper(){
        return new MethodWrapper(getClassName(), methodName, returnType, parameterTypes);
    }

    public Code getCode() {
        return code;
    }

    //    public String print(){
//        return code.print();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodWriter methodWriter = (MethodWriter) o;
        return hashCode() == methodWriter.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, methodDesc);
    }


    //    /**
    //     * <div>if语句跳转指令</div>
    //     * <div>与正常编写代码不同,在指令中条件成立则跳转而不是执行if语句内代码</div>
    //     * <ul>
    //     *     <p>IFEQ 当操作数相等零时跳转</p>
    //     *     <p>IFNE 当操作数不等于零时跳转</p>
    //     *     <p>IFLT 当操作数小于零时跳转</p>
    //     *     <p>IFGE 当操作数小于等于零时跳转</p>
    //     *     <p>IFLE 当操作数大于零时跳转</p>
    //     *     <p>IFNE 当操作数大于等于零时跳转</p>
    //     *     <p>IF_ICMPEQ 当两int类型操作数等于时跳转</p>
    //     *     <p>IF_ICMPNE 当两int类型操作数不等于时跳转</p>
    //     *     <p>IF_ICMPLT 当两int类型操作数小于零时跳转</p>
    //     *     <p>IF_ICMPGE 当两int类型操作数大于等于零时跳转</p>
    //     *     <p>IF_ICMPGT 当两int类型操作数大于零时跳转</p>
    //     *     <p>IF_ICMPLE 当两int类型操作数小于等于零时跳转</p>
    //     *     <p>IF_ACMPEQ 当两reference操作数等于时跳转</p>
    //     *     <p>IF_ACMPNE 当两reference操作数不等于零时跳转</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    ////    @Deprecated
    ////    public Method startIf(String instruction){
    ////        instruction = instruction.toUpperCase();
    ////        code.startIf(InstructionSet.valueOf(instruction));
    ////        return this;
    ////    }
    //
    //    /**
    //     *  <div>在使用分支语句中需调用endif方法结束if语句内代码，并生成offset</div>
    //     */
    //    public Method endIf(){
    //        code.endCode();
    //        return this;
    //    }
    //
    //    /**
    //     * <div>new Object(Types);</div>
    //     * <div>当new对象时需生成一系列指令</div>
    //     * @param classWrapper 需要new的类
    //     */
    //    public Method newObject(ClassWrapper classWrapper){
    //        code.newObject(classWrapper);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>有些类型的比较指令</div>
    //     * <ul>
    //     *     <p>LCMP long类型比较</p>
    //     *     <p>FCMPL float类型比较，如果含NaN则返回1</p>
    //     *     <p>FCMPG float类型比较，如果含NaN则返回-1</p>
    //     *     <p>DCMPL double类型比较，如果含NaN则返回1</p>
    //     *     <p>DCMPG double类型比较，如果含NaN则返回-1</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method compare(String instruction){
    //        instruction = instruction.toUpperCase();
    //        code.operandCompare(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //
    //    /**
    //     * <div>两操作数相加指令</div>
    //     * <ul>
    //     *     <p>IADD int类型相加</p>
    //     *     <p>LADD long类型相加</p>
    //     *     <p>FADD float类型相加</p>
    //     *     <p>DADD double类型相加</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method add(String instruction){
    //        code.mathOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>两操作数相减指令</div>
    //     * <div>ISUB int类型相减</div>
    //     * <div>LSUB long类型相减</div>
    //     * <div>FSUB float类型相减</div>
    //     * <div>DSUB double类型相减</div>
    //     * @param instruction 上述指令
    //     */
    //    public Method sub(String instruction){
    //        code.mathOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>两操作数相乘指令</div>
    //     * <ul>
    //     *     <p>IMUL int类型相乘</p>
    //     *     <p>LMUL long类型相乘</p>
    //     *     <p>FMUL float类型相乘</p>
    //     *     <p>DMUL double类型相乘</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method mul(String instruction){
    //        code.mathOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>两操作数相除指令</div>
    //     * <ul>
    //     *     <p>IDIV int类型相除</p>
    //     *     <p>LDIV long类型相除</p>
    //     *     <p>FDIV float类型相除</p>
    //     *     <p>DDIV double类型相除</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method div(String instruction){
    //        code.mathOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>两操作数求余指令</div>
    //     * <ul>
    //     *     <p>>IDIV int类型求余</p>
    //     *     <p>LDIV long类型求余</p>
    //     *     <p>FDIV float类型求余</p>
    //     *     <p>DDIV double类型求余</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method rem(String instruction){
    //        code.mathOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //
    //    /**
    //     * <div>操作数求反指令</div>
    //     * <ul>
    //     *   <p>INEG int类型求反</p>
    //     *   <p>LNEG long类型求反</p>
    //     *   <p>FNEG float类型求反</p>
    //     *   <p>DNEG double类型求反</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method neg(String instruction){
    //        code.logicalOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //
    //    /**
    //     * <div>操作数进行逻辑运算</div>
    //     * <ul>
    //     *     <p>ISHL int类型左移</p>
    //     *     <p>LSHL long类型左移</p>
    //     *     <p>ISHR int类型右移</p>
    //     *     <p>LSHR long类型右移</p>
    //     *     <p>IUSHR int类型含符号位右移</p>
    //     *     <p>LUSHR long类型含符号位右移</p>
    //     *     <p>IAND int类型按位与</p>
    //     *     <p>LAND long类型按位与</p>
    //     *     <p>IOR int类型按位或</p>
    //     *     <p>LOR long类型按位或</p>
    //     *     <p>IXOR int类型按位异或</p>
    //     *     <p>LXOR long类型按位异或</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     */
    //    public Method logicalOperand(String instruction){
    //        code.logicalOperandOperation(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>类型强转指令</div>
    //     * <ul>
    //     *     <p>I2_ 将int类型转换成(L、F、D、B、C、S)类型</p>
    //     *     <p>L2_ 将long类型转换成(I、L、F)类型</p>
    //     *     <p>F2_ 将float类型转换成(I、L、D)类型</p>
    //     *     <p>D2_ 将double类型转换成(I、L、F)类型</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     *
    //     */
    //    public Method convent(String instruction){
    //        code.operandConvert(InstructionSet.valueOf(instruction));
    //        return this;
    //    }
    //
    //    /**
    //     * <div>自增指令</div>
    //     */
    //    public Method increment(String localVariableName){
    //        code.increment(localVariableName);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>加载int,float,double,long,String以及null等<b>常量</b></div>
    //     */
    //    public <T> Method loadConstant(@Nullable T data){
    //        if (!loadConstant0(data)){
    //            throw new TypeErrorException("data not loadable");
    //        }
    //        return this;
    //    }
    //
    //    private <T> boolean loadConstant0(T data) {
    //        if (data == null){
    //            code.loadFromConstant(InstructionSet.ACONST_NULL);
    //        }else if (data instanceof Integer){
    //            loadInt((Integer) data);
    //        }else if(data instanceof Float){
    //            loadFloat((Float) data);
    //        }else if(data instanceof Long){
    //            loadLong((Long) data);
    //        }else if (data instanceof Double){
    //            loadDouble((Double) data);
    //        }else if (data instanceof String){
    //            short index = constantPool.putStringInfo((String) data);
    //            code.loadFromConstantPool(constantPool,index);
    //        }else{
    //            return false;
    //        }
    //        return true;
    //    }
    //
    //    private void loadDouble(Double data) {
    //        if (data == 0D){
    //            code.loadFromConstant(InstructionSet.DCONST_0);
    //        }else if (data == 1D){
    //            code.loadFromConstant(InstructionSet.DCONST_1);
    //        }else{
    //            short index = constantPool.putDoubleInfo(data);
    //            code.loadFromConstantPool(constantPool,index);
    //        }
    //    }
    //
    //    private void loadLong(Long data) {
    //        if (data == 0L){
    //            code.loadFromConstant(InstructionSet.LCONST_0);
    //        }else if (data == 1L){
    //            code.loadFromConstant(InstructionSet.LCONST_1);
    //        }else{
    //            short index = constantPool.putLongInfo(data);
    //            code.loadFromConstantPool(constantPool,index);
    //        }
    //    }
    //
    //
    ////    private void loadFloat(float data){
    ////        if (data == 0F){
    ////            code.loadFromConstant(InstructionSet.FCONST_0);
    ////        }else if (data == 1F){
    ////            code.loadFromConstant(InstructionSet.FCONST_1);
    ////        }else if (data == 2F){
    ////            code.loadFromConstant(InstructionSet.FCONST_2);
    ////        }else{
    ////            short index = constantPool.putFloatInfo(data);
    ////            code.loadFromConstantPool(constantPool,index);
    ////        }
    ////    }
    //
    ////    private void loadInt(int data){
    ////        if (data >= -1 && data <= 5){
    ////            switch (data){
    ////                case -1:
    ////                    code.loadFromConstant(InstructionSet.ICONST_M1);
    ////                    break;
    ////                case 0:
    ////                    code.loadFromConstant(InstructionSet.ICONST_0);
    ////                    break;
    ////                case 1:
    ////                    code.loadFromConstant(InstructionSet.ICONST_1);
    ////                    break;
    ////                case 2:
    ////                    code.loadFromConstant(InstructionSet.ICONST_2);
    ////                    break;
    ////                case 3:
    ////                    code.loadFromConstant(InstructionSet.ICONST_3);
    ////                    break;
    ////                case 4:
    ////                    code.loadFromConstant(InstructionSet.ICONST_4);
    ////                    break;
    ////                case 5:
    ////                    code.loadFromConstant(InstructionSet.ICONST_5);
    ////                    break;
    ////            }
    ////        } else if (data >= Short.MIN_VALUE && data <= Short.MAX_VALUE){
    ////            code.loadFromShortNum((short) data);
    ////        } else{
    ////            short index = constantPool.putIntegerInfo(data);
    ////            code.loadFromConstantPool(constantPool,index);
    ////        }
    ////    }
    //    /**
    //     * <div>从局部变量表中加载</div>
    //     * @param localVariableName 局部变量名
    //     */
    ////    public Method loadLocal(String localVariableName){
    ////        code.loadFromLocals(localVariableName);
    ////        return this;
    ////    }
    //
    //    /**
    //     * <div>加载成员变量或者静态变量</div>
    //     * <ul>
    //     *     <p>GETFIELD  加载成员变量</p>
    //     *     <p>GETSTATIC 加载静态变量</p>
    //     * </ul>
    //     * @param instruction 指令
    //     * @param fieldWrapper 变量描述
    //     */
    //    public Method loadField(String instruction,FieldWrapper fieldWrapper){
    //        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
    //        if (!fieldWrapper.isLoaded()){
    //            fieldWrapper.load(constantPool);
    //        }
    //        code.loadOrSetField(instructionSet,fieldWrapper);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>数组取值</div>
    //     * _ALOAD 将弹出两个操作数，所以调用此方法之前需要先数组引用、索引压入栈内
    //     */
    ////    public Method loadArrayElement(){
    ////        code.loadFromArray();
    ////        return this;
    ////    }
    //
    //
    //    /**
    //     * <div>数组设值</div>
    //     * _ALOAD 将弹出三个操作数,先后分别是数组引用、索引、值
    //     */
    //    public Method storeToArray(){
    //        code.storeToArray();
    //        return this;
    //    }
    //
    //    /**
    //     * <div>将操作数存储到局部变量表</div>
    //     * @param localVariableName 局部变量名
    //     */
    //    public Method storeToLocals(String localVariableName){
    //        code.storeToLocals(localVariableName);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>将操作数存储到成员变量或者静态变量</div>
    //     * <ul>
    //     *     <p>PUTFIELD 成员变量赋值</p>
    //     *     <p>PUTSTATIC 静态变量赋值</p>
    //     * </ul>
    //     * @param instruction  上述指令
    //     * @param field 变量描述
    //     */
    //    public Method storeToField(String instruction,FieldWrapper field){
    //        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
    //        code.loadOrSetField(instructionSet,field);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>调用方法</div>
    //     * <ul>
    //     *     <p>INVOKEVIRTUAL</p>
    //     *     <p>INVOKESPECIAL</p>
    //     *     <p>INVOKESTATIC</p>
    //     *     <p>INVOKEINTERFACE</p>
    //     *     <p>INVOKEDYNAMIC</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     * @param methodWrapper 方法描述
    //     * @return {@link Method}
    //     */
    //    public Method invokeMethod(String instruction, MethodWrapper methodWrapper){
    //        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
    //        code.invokeMethod(instructionSet,methodWrapper);
    //        return this;
    //    }
    //
    //    /**
    //     * <div>操作数栈相关操作</div>
    //     * <ul>
    //     *     <p>pop</p>
    //     *     <p>POP2</p>
    //     *     <p>DUP</p>
    //     *     <p>DUP_X1</p>
    //     *     <p>DUP_X2</p>
    //     *     <p>DUP2</p>
    //     *     <p>DUP2_X1</p>
    //     *     <p>DUP2_X2</p>
    //     *     <p>SWAP</p>
    //     * </ul>
    //     * @param instruction 上述指令
    //     * @return {@link Method}
    //     */
    //    public Method stackBehavior(String instruction){
    //        InstructionSet instructionSet = InstructionSet.valueOf(instruction);
    //        code.stackBehavior(instructionSet);
    //        return this;
    //    }


}
