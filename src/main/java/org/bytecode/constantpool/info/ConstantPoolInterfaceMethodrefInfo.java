package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

@SuppressWarnings("all")
public class ConstantPoolInterfaceMethodrefInfo extends SymbolicReferenceConstantPoolInfo {
    private String interfaceInfo;
    private String methodName;
    private String methodDesc;

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

    public ConstantPoolInterfaceMethodrefInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InterfaceMethodref_info);
        setValue(ref);
    }

    public void setInterfaceInfo(String interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
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

    @Override
    public void ldc(ConstantPool constantPool) {
        if (interfaceInfo != null) {
            return;
        }
        // 获取常量池中的类名
        interfaceInfo = constantPool.getUtf8OfClassInfo(ConvertTool.B2S(value[0], value[1]));
        // 获取常量池中的字段名
        ConstantPoolNameAndTypeInfo nameAndTypeInfo = (ConstantPoolNameAndTypeInfo) constantPool.get(ConvertTool.B2S(this.value[2], this.value[3]));
        nameAndTypeInfo.ldc(constantPool);
        methodName = nameAndTypeInfo.getName();
        methodDesc = nameAndTypeInfo.getDesc();
    }
}
