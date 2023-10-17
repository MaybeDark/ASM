package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;

public class ConstantPoolClassInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {

    private final String classInfo;

    public ConstantPoolClassInfo(String classInfo, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.classInfo = classInfo;
        setValue(ref);
    }

    public ConstantPoolClassInfo(String classInfo) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.classInfo = classInfo;
    }


    public String getClassInfo() {
        return classInfo;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putClassInfo(classInfo);
    }
}
