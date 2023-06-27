package org.wrapper;

import org.Loadable;
import org.Type;
import org.bytecode.constantpool.ConstantPool;
import com.sun.istack.internal.Nullable;
import org.exception.TypeErrorException;

public class MethodWrapper implements Loadable<ConstantPool> {
    private final String fullClassName;
    private final String methodName;
    private String methodDesc;
    private int    parameterCount;
    private int    pop;
    private int    put;
    private boolean loaded = false;
    private short  methodInfoIndex;
    private Type returnType;
    public MethodWrapper(Type classType, String methodName, @Nullable Type returnType, Type... parameterType){
        this(classType.getFullClassName(),methodName,returnType,parameterType);
    }

    public MethodWrapper(Type classType, String methodName,Type methodType){
        this(classType.getFullClassName(),methodName,methodType);
    }

    public MethodWrapper(String fullClassName, String methodName, @Nullable Type returnType, Type... parameterType){
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.returnType = returnType;
        parseMethod(returnType,parameterType);
    }

    public MethodWrapper(String fullClassName, String methodName,Type methodType){
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        if (methodType == null) {
            parseMethod(null, null);
            return;
        }
        if (!methodType.isMethodType()) {
            throw new TypeErrorException("type must be is a method type");
        }
        parseMethod(methodType);
    }

    private void parseMethod(Type methodType){
        parseMethod(Type.getReturnType(methodType),Type.getArgumentTypes(methodType));
    }

    private void parseMethod(@Nullable Type returnType,@Nullable Type[] parameterType){
        this.methodDesc =Type.getMethodDescriptor(returnType,parameterType);
        if (returnType != null && !returnType.isVoidType()){
            put = 1;
        }

        if (parameterType != null && parameterType.length != 0){
            if (parameterType.length == 1 && parameterType[0].equals(Type.VOID)){
                return;
            }
            parameterCount = parameterType.length;
            pop = parameterCount;
        }
    }

    public short load(ConstantPool constantPool){
        loaded = true;
        methodInfoIndex = constantPool.putMethodrefInfo(fullClassName, methodName, methodDesc);
        return methodInfoIndex;
    }

    public static MethodWrapper buildConstructor(String fullClassName,Type... parameterType){
        return new MethodWrapper(fullClassName,"<init>",Type.VOID,parameterType);
    }


    public String getFullClassName() {
        return fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public short getMethodInfoIndex() {
        return methodInfoIndex;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public int getPop() {
        return pop;
    }

    public int getPut() {
        return put;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Type getReturnType() {
        return returnType;
    }
}
