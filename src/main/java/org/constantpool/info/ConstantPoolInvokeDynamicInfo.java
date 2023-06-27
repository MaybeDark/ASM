package org.constantpool.info;

import org.constantpool.ConstantPoolTag;

public class ConstantPoolInvokeDynamicInfo extends SymbolicReferenceConstantPoolInfo{

    private final String methodName;
    private final String methodDesc;
    public ConstantPoolInvokeDynamicInfo(String methodName, String methodDesc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InvokeDynamic_info);
        this.methodDesc = methodDesc;
        this.methodName =methodName;
        setValue(ref);
    }

    public ConstantPoolInvokeDynamicInfo(String methodName, String methodDesc) {
        this(methodName,methodDesc,null);
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getMethodName() {
        return methodName;
    }
}
