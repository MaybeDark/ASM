package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

@SuppressWarnings("all")
public class ConstantPoolFieldrefInfo extends SymbolicReferenceConstantPoolInfo {

    private String classInfo;
    private String fieldName;
    private String fieldDesc;

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

    public ConstantPoolFieldrefInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Fieldref_info);
        setValue(ref);
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
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

    @Override
    public void ldc(ConstantPool constantPool) {
        if (classInfo != null) {
            return;
        }
        // 获取常量池中的类名
        classInfo = constantPool.getUtf8OfClassInfo(ConvertTool.B2S(value[0], value[1]));
        // 获取常量池中的字段名
        ConstantPoolNameAndTypeInfo nameAndTypeInfo = (ConstantPoolNameAndTypeInfo) constantPool.get(ConvertTool.B2S(this.value[2], this.value[3]));
        nameAndTypeInfo.ldc(constantPool);
        fieldName = nameAndTypeInfo.getName();
        fieldDesc = nameAndTypeInfo.getDesc();
    }
}
