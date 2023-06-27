package org.attribute.com;

import org.Target;
import org.Type;
import org.attribute.Attribute;
import org.constantpool.ConstantPool;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.tools.ArrayTool;
import org.tools.ConvertTool;
import org.tools.DescTool;
import org.wrapper.GenericWrapper;

/**
 * 任何类、接口、初始化成员或者方法的泛型签名如果包含了类型变量或参数化类型，
 * 则Signature会为它记录泛型签名信息
 */
//TODO‘参数化类泛型’参数和属性定义(Class<T>);多范围泛型定义(Class<T extends A&B)
public class Signature extends Attribute {
    private GenericWrapper[] generics;
    private final String signatureStr;
    private short signatureStrCpIndex;
    private final Target target;

    private Signature(Target target, GenericWrapper[] generics, Type superClass, Type... implementsClasses) {
        this.generics = generics;
        this.target = target;
        this.attributeLength = 2;
        this.signatureStr = getClassSignatureStr(generics,superClass,implementsClasses);
    }

    private Signature(Target target, @Nullable Signature definedGenerics,
                      @Nullable GenericWrapper[] generics,
                      @Nullable Object returnType,
                      @Nullable Object... parametersType){
        this.target = target;
        this.generics = generics;
        this.attributeLength = 2;
        this.signatureStr = getMethodSignatureStr(definedGenerics,generics,returnType,parametersType);
    }

    public Signature(Target target,
                     @NotNull Signature definedGenerics,
                    @NotNull GenericWrapper fieldType){
        this.target = target;
        this.attributeLength = 2;
        this.signatureStr = getFieldSignatureStr(definedGenerics,fieldType);
    }
    public static Signature creatClassSignature(@NotNull GenericWrapper[] generics,
                                                @Nullable Type... implementsClasses) {
        return creatClassSignature(generics, Type.getType(Object.class),implementsClasses);
    }

    public static Signature creatClassSignature(@NotNull GenericWrapper[] generics,
                                                @Nullable Type superClass,
                                                @Nullable Type... implementsClasses) {
        return new Signature(Target.Class, generics, superClass, implementsClasses);
    }

    public static Signature creatMethodSignature(@Nullable Signature definedGenerics,
                                                 @Nullable GenericWrapper[] generics,
                                                 @Nullable Object returnType,
                                                 @Nullable Object... parametersType){
        return new Signature(Target.Method,definedGenerics,generics,returnType,parametersType);
    }

    public static Signature creatMethodSignature(@NotNull GenericWrapper[] generics,
                                                 @Nullable Object returnType,
                                                 @Nullable Object... parametersType){
        return creatMethodSignature(null,generics,returnType,parametersType);
    }

    public static Signature creatMethodSignature(@NotNull Signature definedGenerics,
                                                 @Nullable Object returnType,
                                                 @Nullable Object... parametersType){
        return creatMethodSignature(definedGenerics,null,returnType,parametersType);
    }
    public static Signature creatFieldSignature(@NotNull Signature definedGenerics,
                                                @NotNull GenericWrapper fieldType) {
        return new Signature(Target.Field,definedGenerics,fieldType);
    }


    private String getClassSignatureStr(@NotNull GenericWrapper[] generics, @Nullable Type superClass,@Nullable Type... implementsClasses){
        if (ArrayTool.notNull(generics)){
            throw new RuntimeException("generics must be not null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        for (GenericWrapper generic : generics) {
            if(generic != null){
                sb.append(generic.getGenericName());
                sb.append(':');
                sb.append(generic.getExtendsBy().getDescriptor());
            }
        }
        sb.append('>');
        if (superClass != null) {
            sb.append(superClass.getDescriptor());
        } else {
            sb.append(Type.getClassDescriptor(Object.class));
        }
        if (implementsClasses != null && implementsClasses.length != 0) {
            for (Type clazz : implementsClasses) {
                if (clazz != null && !clazz.isObjectType())
                    sb.append(clazz.getDescriptor());
            }
        }
        return sb.toString();
    }


    private String getMethodSignatureStr(@Nullable Signature definedGenerics,
                                      @Nullable GenericWrapper[] generics,
                                      @Nullable Object returnType,
                                      @Nullable Object[] parametersType){
        GenericWrapper[] allGeneric;
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        if (definedGenerics != null){
            allGeneric = ArrayTool.join(definedGenerics.generics, generics);
        }else {
            allGeneric = generics;
        }
        if (ArrayTool.notNull(generics)){
            for (GenericWrapper generic : generics) {
                sb.append(generic.getGenericName());
                sb.append(':');
                sb.append(generic.getExtendsBy().getDescriptor());
            }
        }
        sb.append('>');
        sb.append('(');
        if (ArrayTool.notNull(parametersType)){
            for (Object o : parametersType) {
                if (o == null)
                    break;
                else if (o instanceof GenericWrapper){
                    GenericWrapper generic = (GenericWrapper) o;
                     if (defined(allGeneric,generic)) {
                        sb.append('T');
                        sb.append(((GenericWrapper) o).getGenericName());
                        sb.append(';');
                    }else {
                         throw new RuntimeException("Before using generics, you must first define");
                     }
                }else if (o instanceof Type){
                    sb.append(((Type) o).getDescriptor());
                }
            }
        }
        sb.append(')');
        if (returnType == null){
            sb.append('V');
        }else if (returnType instanceof GenericWrapper){
            if (defined(allGeneric,(GenericWrapper)returnType)) {
                sb.append('T');
                sb.append(((GenericWrapper) returnType).getGenericName());
                sb.append(';');
            }
        }else if(returnType instanceof Type){
            sb.append(((Type) returnType).getDescriptor());
        }
        return sb.toString();
    }

    private String getFieldSignatureStr(@NotNull Signature definedGenerics,
                                        @NotNull GenericWrapper fieldType){
        StringBuilder sb = new StringBuilder();
        if (defined(definedGenerics.generics,fieldType)) {
            sb.append('T');
            sb.append(fieldType.getGenericName());
            sb.append(';');
        }else {
            throw new RuntimeException("Before using generics, you must first define");
        }
        return sb.toString();
    }
    private boolean defined(GenericWrapper[] definedGenerics,GenericWrapper generic){
        if (!ArrayTool.notNull(definedGenerics)) {
            return false;
        }
        for (GenericWrapper definedGeneric : definedGenerics) {
            if (definedGeneric.getGenericName().equals(generic.getGenericName())) {
                return true;
            }
        }
        return false;
    }

    public String getSignatureStr() {
        return signatureStr;
    }

    public boolean isClassSignature(){
        return target.equals(Target.Class);
    }

    public boolean isMethodSignature(){
        return target.equals(Target.Method);
    }

    public boolean isFieldSignature(){
        return target.equals(Target.Field);
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Signature");
        signatureStrCpIndex = cp.putUtf8Info(signatureStr);
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("Signature attribute need load before use");
        }
        byte[] result = new byte[8];
        System.arraycopy(ConvertTool.S2B((short) cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(getSignatureStrCpIndex()),0,result,6,2);
        return result;
    }

    public GenericWrapper[] getSignature() {
        return generics;
    }

    public short getCpIndex() {
        return cpIndex;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public short getSignatureStrCpIndex() {
        return signatureStrCpIndex;
    }


}
