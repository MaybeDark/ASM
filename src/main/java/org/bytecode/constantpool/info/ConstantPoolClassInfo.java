package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolClassInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {

    private String classInfo;

    public ConstantPoolClassInfo(String classInfo, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.classInfo = classInfo;
        setValue(ref);
    }

    public ConstantPoolClassInfo(String classInfo) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.classInfo = classInfo;
    }

    public ConstantPoolClassInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        setValue(ref);
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getClassInfo() {
        return classInfo;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putClassInfo(classInfo);
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        classInfo = ((ConstantPoolUtf8Info) constantPool.get(ConvertTool.B2S(value))).getLiteral();
    }
}
