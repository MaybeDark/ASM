package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPoolTag;

public class ConstantPoolNameAndTypeInfo extends SymbolicReferenceConstantPoolInfo{
    private final String name;
    private final String desc;

    public ConstantPoolNameAndTypeInfo(String name, String desc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_NameAndType_info);
        this.name = name;
        this.desc = desc;
        setValue(ref);
    }

    public ConstantPoolNameAndTypeInfo(String name,String desc){
        this(name,desc,null);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
