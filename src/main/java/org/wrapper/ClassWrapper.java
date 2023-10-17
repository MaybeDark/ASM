package org.wrapper;


import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.exception.TypeErrorException;

import java.util.Objects;

public class ClassWrapper {
    private final String classInfo;
    private Type type;
    private short cpIndex;
    private boolean loaded = false;

    public ClassWrapper(Class<?> clazz){
        this.type = Type.getType(clazz);
        this.classInfo = type.getClassInfo();
    }

    public ClassWrapper(Type type){
        if (!(type.isObjectType()|| type.isArrayType()))
            throw new TypeErrorException(type.getDescriptor() + " not an object or array type ");
        this.type = type;
        this.classInfo = type.getClassInfo();
    }

    public short load(ConstantPool constantPool){
        if (loaded)
            return cpIndex;
        cpIndex = constantPool.putClassInfo(classInfo);
        loaded = true;
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
        return Objects.hash(classInfo);
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

    public String getClassInfo() {
        return classInfo;
    }
}
