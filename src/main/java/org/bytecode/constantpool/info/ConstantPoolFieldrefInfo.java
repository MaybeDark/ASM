package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolFieldrefInfo  extends SymbolicReferenceConstantPoolInfo{

    private final String fullClassName;
    private final String fieldName;
    private final String fieldDesc;

    public ConstantPoolFieldrefInfo(String fullClassName, String fieldName, String fieldDesc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Fieldref_info);
        this.fullClassName = fullClassName;
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
        setValue(ref);
    }

    public ConstantPoolFieldrefInfo(String fullClassName, String fieldName, String fieldType){
        this(fullClassName,fieldName,fieldType,null);
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileDesc() {
        return fieldDesc;
    }

}
