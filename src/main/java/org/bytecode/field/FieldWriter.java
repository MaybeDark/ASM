package org.bytecode.field;

import org.Type;
import org.bytecode.ByteCodeWriter;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.exception.NotLoadException;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.wrapper.FieldWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FieldWriter implements ByteCodeWriter {
    public final ClassWriter classWriter;
    private int access;
    private String fullClassName;
    private String name;
    private Type type;
    private Map<String,Attribute> attributes = new HashMap<>();

    public FieldWriter(ClassWriter classWriter,int access, String name, Type type){
        this.classWriter = classWriter;
        this.fullClassName = classWriter.thisClass.getClassName();
        this.access =access;
        this.type = type;
        this.name = name;
    }

    public FieldWriter addAttribute(Attribute attribute){
        attributes.put(attribute.getAttributeName(),attribute);
        return this;
    }

    @Override
    public Attribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
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

    public byte[] toByteArray() {
        ConstantPool constantPool = classWriter.getConstantPool();
        short typeCpIndex = constantPool.putUtf8Info(type.getDescriptor());
        short nameCpIndex = constantPool.putUtf8Info(name);
        byte[] attributeByteArray = new byte[0];
        short attributeCount = 0;
        for (Attribute attribute : attributes.values()) {
            if (attribute !=null){
                attribute.load(constantPool);
                attributeByteArray = ArrayTool.join(attributeByteArray,attribute.toByteArray());
                attributeCount++;
            }
        }
        ByteVector result = new ByteVector(8 + attributeByteArray.length);
        result.putShort(access)
                .putShort(typeCpIndex)
                .putShort(nameCpIndex)
                .putShort(attributeCount)
                .putArray(attributeByteArray);
        return result.end();
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public FieldWrapper wrapper(){
        return new FieldWrapper(fullClassName,name,type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldWriter fieldWriter = (FieldWriter) o;
        return hashCode() == fieldWriter.hashCode();
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

