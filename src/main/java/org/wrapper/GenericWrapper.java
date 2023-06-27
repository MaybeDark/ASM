package org.wrapper;

import org.Type;

import java.util.Objects;

public class GenericWrapper {

    private final String genericName;
    private final Type extendsBy;

    public GenericWrapper(String genericName, Type extendsBy) {
        this.genericName = genericName;
        this.extendsBy = extendsBy;
    }
    public GenericWrapper(String genericName){
        this.genericName = genericName;
        this.extendsBy = Type.getType(Object.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericWrapper that = (GenericWrapper) o;
        return Objects.equals(hashCode(), that.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(genericName, extendsBy);
    }

    public String getGenericName() {
        return genericName;
    }

    public Type getExtendsBy() {
        return extendsBy;
    }
}
