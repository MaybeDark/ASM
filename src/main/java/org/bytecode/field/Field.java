package org.bytecode.field;

import org.Access;
import org.Type;
import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.method.Constructor;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Field {
    private int access;
//    private boolean isStatic;
//    private boolean isPrimitive;
//    private boolean isFinal;
    private String fullClassName;
    private String name;
    private Type type;
    private Map<String,Attribute> attributes = new HashMap<>();
    private short cpIndex;
    private short nameCpIndex;
    private short typeCpIndex;
    private boolean loaded =false;

    public Field(int access, String fullClassName, String name, Type type){
        this.access =access;
        this.type = type;
        this.fullClassName = fullClassName;
        this.name = name;
//        this.isStatic = Access.isStatic(access);
//        this.isFinal = Access.isFinal(access);
//        this.isPrimitive = Type.isPrimitiveType(type);
    }

    public short load(ConstantPool constantPool) {
        cpIndex = constantPool.putFieldrefInfo(fullClassName,name,type.getDescriptor());
        nameCpIndex = constantPool.putUtf8Info(name);
        typeCpIndex = constantPool.putUtf8Info(type.getDescriptor());
        for (Attribute attr : attributes.values()) {
            attr.load(constantPool);
        }
        loaded = true;
        return cpIndex;
    }

    public int getAccess() {
        return access;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    byte[] toByteArray() {
        byte[] attributeByteArray = new byte[0];
        short attributeCount = 0;
        for (Attribute attribute : attributes.values()) {
            if (attribute !=null){
                attributeByteArray = ArrayTool.join(attributeByteArray,attribute.toByteArray());
                attributeCount++;
            }
        }
        byte[] result = new byte[8+attributeByteArray.length];
        System.arraycopy(ConvertTool.S2B((short) access),0,result,0,2);
        System.arraycopy(ConvertTool.S2B(typeCpIndex),0,result,2,2);
        System.arraycopy(ConvertTool.S2B(nameCpIndex),0,result,4,2);
        System.arraycopy(ConvertTool.S2B(attributeCount),0,result,6,2);
        System.arraycopy(attributeByteArray,0,result,8,attributeByteArray.length);
        return result;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public short getCpIndex() {
        return cpIndex;
    }

    public short getNameCpIndex() {
        return nameCpIndex;
    }

    public short getTypeCpIndex() {
        return typeCpIndex;
    }

    public boolean isLoaded() {
        return loaded;
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
        return Objects.hash(name);
    }
}

