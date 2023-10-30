package org.proxy;

import org.Access;
import org.Type;
import org.bytecode.attributes.code.instruction.InstructionSet;
import org.modify.ClassModifier;
import org.modify.CodeModifier;
import org.modify.FieldModifier;
import org.modify.MethodModifier;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.Access.*;
import static org.proxy.ProxyConstant.*;

public class Exchanger {
    private final static Map<Class<?>, Class<?>> transforms = new HashMap<>();

    static {
        transforms.put(Boolean.TYPE, Boolean.class);
        transforms.put(Character.TYPE, Character.class);
        transforms.put(Byte.TYPE, Byte.class);
        transforms.put(Short.TYPE, Short.class);
        transforms.put(Integer.TYPE, Integer.class);
        transforms.put(Float.TYPE, Float.class);
        transforms.put(Long.TYPE, Long.class);
        transforms.put(Double.TYPE, Double.class);
    }

    ClassModifier proxyModel;
    String thisClassName;
    Class<?> target;
    Method[] targetMethods;
    short codeIndex;
    short objectIndex;
    short handlerIndex;
    short thisClassIndex;
    short getMethodIndex;
    short counter;
    short klassIndex;
    short catchIndex;
    short throwIndex;
    short throwInitIndex;
    ArrayList<MethodInfo> methods = new ArrayList<>();
    ArrayList<Short> fields = new ArrayList<>();

    public Exchanger(Class<?> target, byte[] model) {
        this.target = target;
        this.proxyModel = new ClassModifier(model);
        this.targetMethods = target.getDeclaredMethods();
        proxy();
    }

    private void proxy() {
        initClassInfo();
        initConstantPool();
        for (Method targetMethod : targetMethods) {
            creatProxyMethod(targetMethod);
        }
        initClInit();
    }

    private void initConstantPool() {
        codeIndex = proxyModel.addUtf8Info(CODE_ATTRIBUTE_NAME);
        objectIndex = proxyModel.addClassInfo(OBJECT_CLASS_INFO);
        thisClassIndex = proxyModel.getThisClass();
        short name, desc, nameAndType;
        name = proxyModel.addUtf8Info(HANDLE_METHOD_NAME);
        desc = proxyModel.addUtf8Info(HANDLE_METHOD_DESC);
        nameAndType = proxyModel.addNameAndTypeInfo(name, desc);
        handlerIndex = proxyModel.addMethodRefInfo(thisClassIndex, nameAndType);
        klassIndex = proxyModel.addClassInfo(KLASS_CLASS_INFO);
        name = proxyModel.addUtf8Info(GETMETHOD_METHOD_NAME);
        desc = proxyModel.addUtf8Info(GETMETHOD_METHOD_DESC);
        nameAndType = proxyModel.addNameAndTypeInfo(name, desc);
        getMethodIndex = proxyModel.addMethodRefInfo(klassIndex, nameAndType);
        catchIndex = proxyModel.addClassInfo(CATCH_CLASS_INFO);
        throwIndex = proxyModel.addClassInfo(THROW_CLASS_INFO);
        name = proxyModel.addUtf8Info(INIT_METHOD_NAME);
        desc = proxyModel.addUtf8Info(THROW_INIT_DESC);
        nameAndType = proxyModel.addNameAndTypeInfo(name, desc);
        throwInitIndex = proxyModel.addMethodRefInfo(throwIndex, nameAndType);
    }

    public void initClassInfo() {
        String targetClassInfo = Type.getType(target).getClassInfo();
        thisClassName = SYNTHETIC_CLASS_NAME_PREFIX + target.getSimpleName();
        proxyModel.modifyThisClassName(thisClassName, false);
        proxyModel.addInterface(targetClassInfo);
    }

    public void initClInit() {
        if (counter == 0) {
            return;
        }
        MethodModifier clinit = proxyModel.addMethod(ACC_STATIC, CLINIT_METHOD_NAME, CLINIT_METHOD_DESC);
        ByteVectors codeValue = new ByteVectors();
        MethodInfo method;
        Class<?> argType;
        for (int i = 0; i < methods.size(); i++) {
            method = methods.get(i);
            codeValue.putByte(18) //ldc
                    .putByte(thisClassIndex) //classInfo
                    .putByte(18) //ldc
                    .putByte(proxyModel.addStringInfo(method.name)); //methodName
            //方法有参
            if (ArrayTool.notNull(method.args)) {
                int length = method.args.length;
                (length < 5 ? codeValue.putByte(3 + length) : codeValue.putByte(16).putByte(length))
                        .putByte(189) //anewarray
                        .putShort(klassIndex)   //Class
                        .putByte(89); //dup
                for (int j = 0; length > 0 && j < 6; j++, length--) { //处理六个参数以内
                    codeValue.putByte(3 + j); //iconst_n
                    if ((argType = method.args[j]).isPrimitive()) { //基本类型
                        short classInfo = proxyModel.addClassInfo(Type.getType(transforms.get(argType)).getClassInfo());
                        short name = proxyModel.addUtf8Info("TYPE");
                        short desc = proxyModel.addUtf8Info(Type.getClassDescriptor(KLASS_CLASS_INFO));
                        short nameAndType = proxyModel.addNameAndTypeInfo(name, desc);
                        short ref = proxyModel.addFieldRefInfo(classInfo, nameAndType); //包装类的TYPE（fieldref)
                        codeValue.putByte(178) //getstatic
                                .putShort(ref); //fieldref
                    } else { //引用类型
                        codeValue.putByte(18) //ldc
                                .putByte(proxyModel.addClassInfo(Type.getType(argType).getClassInfo())); //classInfo
                    }
                    codeValue.putByte(83) //aastore
                            .putByte(89); //dup
                }
                for (int j = 0; length > 0; j++, length--) { //处理六个参数以上
                    codeValue.putByte(16) //bipush
                            .putByte(6 + j); //num
                    if ((argType = method.args[j]).isPrimitive()) { //基本类型
                        short classInfo = proxyModel.addClassInfo(Type.getType(transforms.get(argType)).getClassInfo());
                        short name = proxyModel.addUtf8Info("TYPE");
                        short desc = proxyModel.addUtf8Info(Type.getClassDescriptor(KLASS_CLASS_INFO));
                        short nameAndType = proxyModel.addNameAndTypeInfo(name, desc);
                        short ref = proxyModel.addFieldRefInfo(classInfo, nameAndType); //包装类的TYPE（fieldref)
                        codeValue.putByte(178) //getstatic
                                .putShort(ref); //fieldref
                    } else { //引用类型
                        codeValue.putByte(18) //ldc
                                .putByte(proxyModel.addClassInfo(Type.getType(argType).getClassInfo())); //classInfo
                    }
                    codeValue.putByte(83) //aastore
                            .putByte(89); //dup
                }
                //TODO 可能引发异常
                codeValue.tryRemove(1); //删除最后一个dup
            } else { // no args
                codeValue.putByte(3)
                        .putByte(189)
                        .putShort(klassIndex);
            }

            codeValue.putByte(182) //invokevirual
                    .putShort(getMethodIndex) //getMethod
                    .putByte(179)  //putstatic
                    .putShort(fields.get(i)); //fieldref
        }
        int mark = codeValue.getLength();
        //goto return
        codeValue.putByte(167) //goto
                .putShort(13); //catch语句后
        //catch 固定长度10
        codeValue.putByte(75) //astore_0
                .putByte(187) //new
                .putShort(throwIndex) //UndeclaredThrowableException
                .putByte(89) //dup
                .putByte(42) //aload_0
                .putByte(183) //invokespecial
                .putShort(throwInitIndex) //UndeclaredThrowableException.init()
                .putByte(191) //athrow
                .putByte(177); //return
        int codeLength = codeValue.getLength(); //code 结束
        //exceptionTable
        codeValue.putShort(1) //一条
                .putShort(0) //起始 0
                .putShort(mark) //goto指令以前
                .putShort(mark + 3) //处理指令起始
                .putShort(catchIndex); //捕获类型Throwable
        //StackMapTable
        codeValue.putShort(1) //attributeCount
                .putShort(proxyModel.addUtf8Info("StackMapTable")) //nameIndex
                .putInt(7) //attributeLength
                .putShort(2) //两条
                .putByte(mark + 3 + 64) //same_locals_1_stack mark+3+64
                .putByte(7) //stack varifications_object
                .putShort(catchIndex) //Throwable
                .putByte(9); //same +9


        ByteVector byteVector = new ByteVector(14 + codeValue.getLength());
        byteVector.putShort(codeIndex) //attributeNameIndex
                .putInt(codeValue.getLength() + 8) //attributeLength
                .putShort(6) //stackMax
                .putShort(1) //localMax
                .putInt(codeLength) //codeLength
                .putArray(codeValue.toByteArray()); //code + exceptionTable + stackMapTable

        clinit.addAttribute(CODE_ATTRIBUTE_NAME, CodeModifier.visit(byteVector));
    }

    public void creatProxyMethod(Method targetMethod) {
        if (Access.isStatic(targetMethod.getModifiers()))
            return;

        int argCount = targetMethod.getParameterCount();
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        Class<?> parameterType;
        MethodModifier newMethod = proxyModel.addMethod(ACC_PUBLIC, targetMethod.getName(), Type.getMethodDescriptor(targetMethod));
        methods.add(new MethodInfo(targetMethod.getName(), newMethod.getNameIndex(), parameterTypes));
        ByteVector codeValue = argCount < 8 ? new ByteVector(64) : new ByteVector(128);
        int localMax = argCount + 1; //loacalMax;
        int stackMax;
        codeValue.putByte(42) //aload_0
                .putByte(178) //getstatic
                .putShort(getNextFieldRef()); //freldref and creat field
        if (argCount == 0) {
            stackMax = 3;
            codeValue
                    .putByte(3) //aload_0
                    .putByte(189) //anewarray
                    .putShort(objectIndex) //object
                    .putByte(182) //invokevirtual
                    .putShort(handlerIndex) //handlerMethod
                    .putArray(selectReturnInst(targetMethod.getReturnType())); //returninst
        } else {
            stackMax = 6;
            codeValue.putByte(3 + argCount) //aload_n
                    .putByte(189)   //anewarray
                    .putShort(objectIndex) //object
                    .putByte(89); //dup
            //aload_1 - aload_3
            for (int i = 0; argCount > 0 && i < 3; i++, argCount--) {
                codeValue.putByte(3 + i); //iconst_n
                if ((parameterType = parameterTypes[i]).isPrimitive()) {
                    Type type = Type.getType(transforms.get(parameterType));
                    short classIndex = proxyModel.addClassInfo(type.getClassInfo());
                    short methodName = proxyModel.addUtf8Info("valueOf");
                    short methodDesc = proxyModel.addUtf8Info("(" + Type.getType(parameterType).getDescriptor() + ")" + type.getDescriptor());
                    short nameAndType = proxyModel.addNameAndTypeInfo(methodName, methodDesc);
                    short methodRef = proxyModel.addMethodRefInfo(classIndex, nameAndType); //装箱方法
                    type = Type.getType(parameterType);
                    if (type.isRuntimeIntType()) {
                        codeValue.putByte(27 + i);
                    } else if (type.isLongType()) {
                        codeValue.putByte(31 + i);
                    } else if (type.isDoubleType()) {
                        codeValue.putByte(39 + i);
                    } else if (type.isFloatType()) {
                        codeValue.putByte(35 + i);
                    } else {
                        //void
                    }
                    codeValue.putByte(184)
                            .putShort(methodRef);
                } else {
                    codeValue.putByte(43 + i); //aload_n
                }
                codeValue.putByte(83) //aastore
                        .putByte(89); //dup
            }
            //iconst3 - inconst5
            for (int i = 0; argCount > 0 && i < 3; i++, argCount--) {
                codeValue.putByte(6 + i)
                        .putByte(25)
                        .putByte(4 + i)
                        .putByte(83)
                        .putByte(89);
            }
            for (int i = 0; argCount > 0; i++, argCount--) {
                codeValue.putByte(16)
                        .putByte(6 + i)
                        .putByte(25)
                        .putByte(7 + i)
                        .putByte(83)
                        .putByte(89);
            }
            codeValue.remove(1); //remove last dup
            codeValue.putByte(182) //invokevirtual
                    .putShort(handlerIndex) //handlerMethod
                    .putArray(selectReturnInst(targetMethod.getReturnType())); //returninst
        }
        int codeLength = codeValue.getLength();
        codeValue.putShort(0); //0 exception
        codeValue.putShort(0); //0 attribute
        int attributeLength = 12 + codeLength;
        ByteVector code = new ByteVector(18 + codeLength);
        code.putShort(codeIndex)
                .putInt(attributeLength)
                .putShort(stackMax)
                .putShort(localMax)
                .putInt(codeLength)
                .putArray(codeValue.end());
        newMethod.addAttribute(CODE_ATTRIBUTE_NAME, CodeModifier.visit(code));
    }

    private short getNextFieldRef() {
        FieldModifier newField = proxyModel.addField((short) (ACC_PRIVATE | ACC_STATIC), "m" + counter, SYNTHETIC_FIELD_TYPE);
        short nameAndType = proxyModel.addNameAndTypeInfo(newField.getNameIndex(), newField.getDescIndex());
        counter++;
        short ref = proxyModel.addFieldRefInfo(thisClassIndex, nameAndType);
        fields.add(ref);
        return ref;
    }

    private byte[] selectReturnInst(Class<?> returnType) {
        ByteVector byteVector = new ByteVector(16);
        Class<?> klass;
        if (returnType.isPrimitive()) {
            //void
            if ((klass = transforms.get(returnType)) == null) {
                byteVector.putByte(87);
                byteVector.putByte(177);
                return byteVector.end();
            }
            //获取对应的包装类
            Type type = Type.getType(klass);
            //基本类型拆箱
            short classIndex = proxyModel.addClassInfo(type.getClassInfo());
            short methodName = proxyModel.addUtf8Info(returnType.getName() + "Value");
            short methodDesc = proxyModel.addUtf8Info("()" + Type.getType(returnType).getDescriptor());
            short nameAndType = proxyModel.addNameAndTypeInfo(methodName, methodDesc);
            short methodRef = proxyModel.addMethodRefInfo(classIndex, nameAndType); //拆箱方法
            byteVector.putByte(192)
                    .putShort(classIndex)
                    .putByte(182)
                    .putShort(methodRef);
//          char short boolean int byte
            if ((type = Type.getType(returnType)).isRuntimeIntType()) {
                byteVector.putByte(172);
//          double float long
            } else if (type.isPrimitiveType()) {
                byteVector.putByte(InstructionSet.valueOf(type.getDescriptor() + "RETURN").opcode);
            } else {
                throw new RuntimeException("unknown type" + type.getDescriptor());
            }
//      reference return
        } else {
            if (returnType.equals(Object.class)) {
                byteVector.putByte(176);
            } else {
                short classIndex = proxyModel.addClassInfo(Type.getType(returnType).getClassInfo());
                byteVector.putByte(192)
                        .putShort(classIndex)
                        .putByte(176);
            }
        }
        return byteVector.end();
    }

    public String getClassName() {
        return thisClassName;
    }

    public byte[] toByteArray() {
        return proxyModel.toByteArray();
    }

    class MethodInfo {
        short nameIndex;
        String name;
        Class<?>[] args;

        public MethodInfo(String name, short nameIndex, Class<?>[] args) {
            this.nameIndex = nameIndex;
            this.args = args;
            this.name = name;
        }
    }
}
