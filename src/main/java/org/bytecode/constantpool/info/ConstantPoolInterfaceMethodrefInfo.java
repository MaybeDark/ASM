package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolInterfaceMethodrefInfo extends SymbolicReferenceConstantPoolInfo{
    private final String interfaceInfo;
    private final String methodName;
    private final String methodDesc;

    public ConstantPoolInterfaceMethodrefInfo(String interfaceInfo, String methodName, String methodDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InterfaceMethodref_info);
        this.interfaceInfo = interfaceInfo;
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }

    public ConstantPoolInterfaceMethodrefInfo(String interfaceInfo, String methodName, String methodDesc) {
        this(interfaceInfo, methodName, methodDesc, null);
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getInterfaceInfo() {
        return interfaceInfo;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putInterfaceMethodrefInfo(interfaceInfo, methodName, methodDesc);
    }
}
