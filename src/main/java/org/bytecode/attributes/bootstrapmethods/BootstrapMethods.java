package org.bytecode.attributes.bootstrapmethods;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.innerclass.InnerClass;
import org.bytecode.attributes.innerclass.InnerClasses;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.Parameterizable;
import org.bytecode.constantpool.ReferenceKind;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.bytecode.constantpool.info.ConstantPoolMethodTypeInfo;
import org.exception.TypeErrorException;
import org.tools.ByteVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BootstrapMethods extends Attribute {
    public static final String LAMBDAMETAFACTORY_CLASSNAME = "java/lang/invoke/LambdaMetafactory";
    public static final String LAMBDAMETAFACTORY_METHODNAME = "metafactory";
    public static final String LAMBDAMETAFACTORY_METHODDESC = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
    private final List<BootstrapMethod> pool = new ArrayList<>();
    private final Map<Integer, Short> hash2Index = new HashMap<>();
    private short bootStrapMethodCount = 0;

    public BootstrapMethods() {
        super(Target.class_info);
        attributeLength = 2;
        attributeName = "BootstrapMethods";
    }

    private short putBootStrapMethod(BootstrapMethod bootStrapMethod) {
        int key = bootStrapMethod.hashCode();
        Short value = hash2Index.putIfAbsent(key, bootStrapMethodCount);
        if (value != null) {
            return value;
        }
        pool.add(bootStrapMethod);
        attributeLength += bootStrapMethod.getLength();
        return bootStrapMethodCount++;
    }

    public CallSite newLambdaMethod(ClassWriter writer, Class<?> functionInterface) {
        if (! functionInterface.isInterface() || functionInterface.getDeclaredMethods().length != 1) {
            throw new TypeErrorException("functionInterface must be a FunctionInterface");
        }
        String lambdaMethodDesc = Type.getMethodDescriptor(functionInterface.getDeclaredMethods()[0]);
        ConstantPoolMethodHandleInfo handleInfo = new ConstantPoolMethodHandleInfo(ReferenceKind.REF_invokeStatic, writer.getClassInfo(), "", lambdaMethodDesc);
        BootstrapMethod bootStrapMethod = new BootstrapMethod(
                ReferenceKind.REF_invokeStatic
                , LAMBDAMETAFACTORY_CLASSNAME
                , LAMBDAMETAFACTORY_METHODNAME
                , LAMBDAMETAFACTORY_METHODDESC
                , new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
                , handleInfo
                , new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
        );
        short index = putBootStrapMethod(bootStrapMethod);
        InnerClasses innerClasses = new InnerClasses();
        innerClasses.addInnerClass(InnerClass.innerClassOfLambda());
        writer.addAttribute(innerClasses);
        return new CallSite(functionInterface, handleInfo, index);
    }

    public boolean isEmpty() {
        return bootStrapMethodCount == 0;
    }

    @Override
    public short load(ConstantPool constantPool) {
        if (isEmpty()) {
            System.err.println("empty attribute:" + getAttributeName());
        }
        loadAttributeName(constantPool);
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            bootstrapMethod.load(constantPool);
        }
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        BootstrapMethod newBootstrapMethod;
        byteVector.skip(4);
        short count = byteVector.getShort(), argCount;
        for (int i = 0; i < count; i++) {
            newBootstrapMethod = new BootstrapMethod((ConstantPoolMethodHandleInfo) constantPool.get(byteVector.getShort()));
            argCount = byteVector.getShort();
            for (int j = 0; j < argCount; j++) {
                newBootstrapMethod.addArgs((Parameterizable) constantPool.get(byteVector.getShort()));
            }
            putBootStrapMethod(newBootstrapMethod);
        }
        return this;
    }

    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6 + attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(bootStrapMethodCount);
        for (int i = 0; i < bootStrapMethodCount; i++) {
            result.putArray(pool.get(i).getValue());
        }
        return result.end();
    }

//    public String print() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bootStrapMethodCount; i++) {
//            BootstrapMethod bootstrapMethod = pool.get(i);
//            sb.append(String.format("[%02d]", i));
//            sb.append(String.format("%-15s ", bootstrapMethod.getReferenceKind()));
//            sb.append(String.format("%-15s \n", bootstrapMethod.getClassInfo()));
//            sb.append(String.format("%-15s \n", bootstrapMethod.getMethodName()+":"+bootstrapMethod.getMethodDesc()));
//            for (Parameterizable arg : bootstrapMethod.getArgs()) {
//                sb.append(arg.getType());
//            }
//        }
//        return sb.toString();
//    }
}

