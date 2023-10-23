package org.bytecode;

import org.Type;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Signature;
import org.bytecode.attributes.Target;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.wrapper.FieldWrapper;
import org.wrapper.GenericWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FieldWriter implements ByteCodeWriter {
    public final ClassWriter classWriter;
    private int access;
    private String classInfo;
    private String name;
    private Type type;
    private Map<String, Attribute> attributes = new HashMap<>();

    /**
     * @param classWriter 所属类
     * @param access      权限修饰符
     * @param name        变量名
     * @param type        变量类型
     */
    public FieldWriter(ClassWriter classWriter, int access, String name, Type type) {
        this.classWriter = classWriter;
        this.classInfo = classWriter.thisClass.getClassInfo();
        this.access = access;
        this.type = type;
        this.name = name;
    }

    /**
     * @param classWriter 所属类
     * @param access      权限修饰符
     * @param name        变量名
     * @param generic     泛型类型
     */
    public FieldWriter(ClassWriter classWriter, int access, String name, GenericWrapper generic) {
        this.classWriter = classWriter;
        this.classInfo = classWriter.thisClass.getClassInfo();
        this.access = access;
        this.name = name;
        this.type = generic.getExtendsBy();
        Signature signature = new Signature();
        signature.setSignature(signature.getSignatureOfField(generic));
        addAttribute(signature);
    }


    public FieldWriter addAttribute(Attribute attribute) {
        if (! Target.check(attribute.target, Target.field_info))
            throw new RuntimeException(attribute.getAttributeName() + "not a field attribute");
        attributes.put(attribute.getAttributeName(), attribute);
        return this;
    }

    @Override
    public Attribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }


    public int getAccess() {
        return access;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }


    public byte[] toByteArray() {
        ConstantPool constantPool = classWriter.getConstantPool();
        short nameCpIndex = constantPool.putUtf8Info(name);
        short typeCpIndex = constantPool.putUtf8Info(type.getDescriptor());
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
                .putShort(nameCpIndex)
                .putShort(typeCpIndex)
                .putShort(attributeCount)
                .putArray(attributeByteArray);
        return result.end();
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public FieldWrapper wrapper() {
        return new FieldWrapper(classInfo, name, type);
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

