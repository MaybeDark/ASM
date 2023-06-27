package org.wrapper;


import org.Loadable;
import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.exception.TypeErrorException;

import java.util.Objects;

public class ClassWrapper implements Loadable<ConstantPool> {
    private final String fullClassName;
    private Type type;
    private short cpIndex;
    private boolean loaded = false;

    public ClassWrapper(Class<?> clazz){
        this.type = Type.getType(clazz);
        this.fullClassName = type.getFullClassName();
    }

    public ClassWrapper(Type type){
        if (!type.isObjectType())
            throw new TypeErrorException(type.getDescriptor() + " not an object type");
        this.type = type;
        this.fullClassName = type.getFullClassName();
    }

    public short load(ConstantPool constantPool){
        loaded = true;
        cpIndex = constantPool.putClassInfo(fullClassName);
        return cpIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassWrapper that = (ClassWrapper) o;
        return that.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullClassName);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Type getType() {
        return type;
    }

    public short getCpIndex() {
        return cpIndex;
    }

    public String getFullClassName() {
        return fullClassName;
    }
}
