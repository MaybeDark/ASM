package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

public class ConstantPoolNameAndTypeInfo extends SymbolicReferenceConstantPoolInfo {
    private String name;
    private String desc;

    public ConstantPoolNameAndTypeInfo(String name, String desc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_NameAndType_info);
        this.name = name;
        this.desc = desc;
        setValue(ref);
    }

    public ConstantPoolNameAndTypeInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_NameAndType_info);
        setValue(ref);
    }

    public ConstantPoolNameAndTypeInfo(String name, String desc) {
        this(name, desc, null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putNameAndTypeInfo(name, desc);
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        if (name != null) {
            return;
        }
        name = constantPool.getUtf8(ConvertTool.B2S(value[0], value[1]));
        desc = constantPool.getUtf8(ConvertTool.B2S(value[2], value[3]));
    }
}
