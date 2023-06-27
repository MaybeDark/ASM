package org.wrapper;

import com.sun.istack.internal.NotNull;
import org.Loadable;
import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.exception.NotNullException;
import org.exception.TypeErrorException;

public class FieldWrapper implements Loadable<ConstantPool> {

    private final String fullClassName;
    private final String fieldName;
    private final String fieldDesc;
    private boolean loaded = false;
    private short fieldInfoIndex;
    private Type type;

    public FieldWrapper(@NotNull Type classType,@NotNull  String filedName,@NotNull Type filedType){
        this(classType.getFullClassName(),filedName,filedType);
    }

    public FieldWrapper(@NotNull String fullClassName,@NotNull  String fieldName, @NotNull Type type){
        if (type == null){
            throw new NotNullException("type cannot be null");
        }
        if (type.isMethodType() || type.isVoidType()){
            throw new TypeErrorException("wrong field type");
        }
        this.type = type;
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
