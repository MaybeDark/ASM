package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolFieldrefInfo  extends SymbolicReferenceConstantPoolInfo{

    private final String classInfo;
    private final String fieldName;
    private final String fieldDesc;

    public ConstantPoolFieldrefInfo(String classInfo, String fieldName, String fieldDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Fieldref_info);
        this.classInfo = classInfo;
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
        setValue(ref);
    }

    public ConstantPoolFieldrefInfo(String classInfo, String fieldName, String fieldType) {
        this(classInfo, fieldName, fieldType, null);
    }

    public String getClassInfo() {
        return classInfo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileDesc() {
        return fieldDesc;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putFieldrefInfo(classInfo, fieldName, fieldDesc);
    }
}
