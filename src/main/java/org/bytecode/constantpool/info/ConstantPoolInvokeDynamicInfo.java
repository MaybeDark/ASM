package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

public class ConstantPoolInvokeDynamicInfo extends SymbolicReferenceConstantPoolInfo {

    private String methodName;
    private String methodDesc;

    public ConstantPoolInvokeDynamicInfo(String methodName, String methodDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InvokeDynamic_info);
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }

    public ConstantPoolInvokeDynamicInfo(String methodName, String methodDesc) {
        this(methodName, methodDesc, null);
    }

    public ConstantPoolInvokeDynamicInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_InvokeDynamic_info);
        setValue(ref);
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

    public String getMethodName() {
        return methodName;
    }

    @Override
    public short load(ConstantPool constantPool) {
        throw new RuntimeException("no support");
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        if (methodName != null) {
            return;
        }
        ConstantPoolNameAndTypeInfo nameAndTypeInfo = (ConstantPoolNameAndTypeInfo) constantPool.get(ConvertTool.B2S(value[3], value[4]));
        methodName = nameAndTypeInfo.getName();
        methodDesc = nameAndTypeInfo.getDesc();
    }
}
