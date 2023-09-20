package org.bytecode.attributes.clazz.bootstrapmethod;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.clazz.ClassAttribute;
import org.bytecode.attributes.clazz.innerclass.InnerClass;
import org.bytecode.attributes.clazz.innerclass.InnerClasses;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ReferenceKind;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.bytecode.constantpool.info.ConstantPoolMethodTypeInfo;
import org.exception.TypeErrorException;
import org.tools.ByteVector;

import java.util.*;

public class BootstrapMethods extends ClassAttribute {
    public static final String LAMBDAMETAFACTORY_CLASSNAME = "java/lang/invoke/LambdaMetafactory";
    public static final String LAMBDAMETAFACTORY_METHODNAME = "metafactory";
    public static final String LAMBDAMETAFACTORY_METHODDESC = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
    private final List<BootstrapMethod> pool = new ArrayList<>();
    private final Map<Integer,Short> hash2Index = new HashMap<>();
    private short bootStrapMethodCount = 0;

    public BootstrapMethods(ClassWriter classWriter){
        super(classWriter);
        attributeLength = 2;
        attributeName = "BootstrapMethods";
        setOrSwap();
    }

    private short putBootStrapMethod(BootstrapMethod bootStrapMethod){
        int key = bootStrapMethod.hashCode();
        Short value = hash2Index.putIfAbsent(key, bootStrapMethodCount);
        if (value != null) {
            return value;
        }
        pool.add(bootStrapMethod);
        attributeLength += bootStrapMethod.getLength();
        return bootStrapMethodCount++;
    }

    public CallSite newLambdaMethod(Class<?> target){
        if (!target.isInterface() || target.getDeclaredMethods().length != 1){
            throw new TypeErrorException("target must be a FunctionInterface");
        }
        String lambdaMethodDesc = Type.getMethodDescriptor(target.getDeclaredMethods()[0]);
        ConstantPoolMethodHandleInfo handleInfo = new ConstantPoolMethodHandleInfo(ReferenceKind.REF_invokeStatic, ((ClassWriter)writer).getClassName(), "", lambdaMethodDesc);
        BootstrapMethod bootStrapMethod = new BootstrapMethod(
                ReferenceKind.REF_invokeStatic
                ,LAMBDAMETAFACTORY_CLASSNAME
                ,LAMBDAMETAFACTORY_METHODNAME
                ,LAMBDAMETAFACTORY_METHODDESC
                ,new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
                ,handleInfo
                ,new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
        );
        short index = ((BootstrapMethods)continuation).putBootStrapMethod(bootStrapMethod);
        InnerClasses innerClasses = new InnerClasses((ClassWriter) writer);
        innerClasses.addInnerClass(InnerClass.innerClassOfLambdaMethod());
        writer.addAttribute(innerClasses);
        return new CallSite(target,handleInfo,index);
    }

    public boolean isEmpty(){
        return bootStrapMethodCount == 0;
    }

    @Override
    public short load(ConstantPool constantPool) {
        super.load(constantPool);
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            bootstrapMethod.load(constantPool);
        }
        return cpIndex;
    }

    public byte[] toByteArray(){
        checkLoaded();
        ByteVector result = new ByteVector(6+attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(bootStrapMethodCount);
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            result.putArray(bootstrapMethod.getValue());
        }
        return result.end();
    }

//    public String print() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bootStrapMethodCount; i++) {
//            BootstrapMethod bootstrapMethod = pool.get(i);
//            sb.append(String.format("[%02d]", i));
//            sb.append(String.format("%-15s ", bootstrapMethod.getReferenceKind()));
//            sb.append(String.format("%-15s \n", bootstrapMethod.getFullClassName()));
//            sb.append(String.format("%-15s \n", bootstrapMethod.getMethodName()+":"+bootstrapMethod.getMethodDesc()));
//            for (Parameterizable arg : bootstrapMethod.getArgs()) {
//                sb.append(arg.getType());
//            }
//        }
//        return sb.toString();
//    }
}
