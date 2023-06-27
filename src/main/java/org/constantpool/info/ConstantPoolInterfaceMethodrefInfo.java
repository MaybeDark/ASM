package org.constantpool.info;

import org.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolInterfaceMethodrefInfo extends SymbolicReferenceConstantPoolInfo{
    private final String fullInterfaceName;
    private final String methodName;
    private final String methodDesc;

    public ConstantPoolInterfaceMethodrefInfo(String fullInterfaceName, String methodName, String methodDesc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InterfaceMethodref_info);
        this.fullInterfaceName = fullInterfaceName;
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }
    public ConstantPoolInterfaceMethodrefInfo(String fullInterfaceName, String methodName, String methodDesc) {
        this(fullInterfaceName,methodName,methodDesc,null);
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getFullInterfaceName() {
        return fullInterfaceName;
    }

    public String getMethodName() {
        return methodName;
    }
}
