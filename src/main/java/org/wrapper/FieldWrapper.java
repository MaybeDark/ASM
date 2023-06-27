package org.wrapper;

import org.Loadable;
import org.Type;
import org.constantpool.ConstantPool;
import org.exception.TypeErrorException;

public class FieldWrapper implements Loadable<ConstantPool> {

    private final String fullClassName;
    private final String fieldName;
    private final String fieldDesc;
    private boolean loaded = false;
    private short fieldInfoIndex;
    private Type type;
    public FieldWrapper(String fullClassName,String fieldName,Type type){
        this.type = type;
        if (type.isMethodType()){
            throw new TypeErrorException("field type cannot be a method");
        }
        fieldDesc = type.getDescriptor();
        this.fieldName = fieldName;
        this.fullClassName = fullClassName;
    }

    public short load(ConstantPool constantPool){
        fieldInfoIndex =constantPool.putFieldrefInfo(fullClassName, fieldName, fieldDesc);
        loaded = true;
        return fieldInfoIndex;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public short getFieldInfoIndex() {
        return fieldInfoIndex;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Type getType() {
        return type;
    }
}
