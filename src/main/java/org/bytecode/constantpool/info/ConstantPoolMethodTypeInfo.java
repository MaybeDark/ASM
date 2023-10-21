package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolMethodTypeInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {
    private String methodDesc;

    public ConstantPoolMethodTypeInfo(String methodDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodType_info);
        this.methodDesc = methodDesc;
        setValue(ref);
    }

    public ConstantPoolMethodTypeInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodType_info);
        setValue(ref);
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public ConstantPoolMethodTypeInfo(String methodDesc) {
        this(methodDesc, null);
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putMethodTypeInfo(methodDesc);
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        if (methodDesc != null) {
            return;
        }
        this.methodDesc = constantPool.getUtf8(ConvertTool.B2S(value[1], value[2]));
    }
}
