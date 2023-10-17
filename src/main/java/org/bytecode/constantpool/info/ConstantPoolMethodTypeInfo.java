package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;

public class ConstantPoolMethodTypeInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {
    private final String methodDesc;

    public ConstantPoolMethodTypeInfo(String methodDesc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodType_info);
        this.methodDesc = methodDesc;
        setValue(ref);
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
}
