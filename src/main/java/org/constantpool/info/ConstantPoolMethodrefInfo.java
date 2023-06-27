package org.constantpool.info;

import org.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolMethodrefInfo extends SymbolicReferenceConstantPoolInfo{
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;

    public ConstantPoolMethodrefInfo(String fullClassName, String methodName, String methodDesc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Methodref_info);
        this.fullClassName = fullClassName;
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }
    public ConstantPoolMethodrefInfo(String fullClassName, String methodName, String methodDesc) {
        this(fullClassName,methodName,methodDesc,null);
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }
}

