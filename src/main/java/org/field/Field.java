package org.field;

import java.util.Objects;

public class Field {
    private String fullClassName;
    private int access;
    private String name;
    private String desc;
    private Field defaultValue;
    private short index;

    public Field(int access,String fullClassName,String name,String desc){
        this(access,fullClassName,name,desc,null);
    }

    public Field(int access,String fullClassName,String name,String desc ,Field defaultValue){
        this.access =access;
        this.fullClassName = fullClassName;
        this.name = name;
        this.desc = desc;
        resolveDefaultValue(defaultValue);
    }


    private void resolveDefaultValue(Field defaultValue) {

    }

    public void setIndex(short index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return hashCode() == field.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullClassName, name, desc);
    }
}
