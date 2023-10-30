package org.modify;

import org.Access;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.ArrayList;

public class FieldModifier {
    short accessFlag;
    short nameIndex;
    short descIndex;
    short attributeCount = 0;
    ArrayList<AttributeModifier> attributes = new ArrayList<>();
    private ClassModifier owner;

    FieldModifier(ClassModifier owner) {
        this.owner = owner;
    }

    public static FieldModifier visit(ClassModifier owner, ByteVector resource) {
        FieldModifier newField = new FieldModifier(owner);
        newField.accessFlag = resource.getShort();
        ;
        newField.nameIndex = resource.getShort();
        ;
        newField.descIndex = resource.getShort();
        ;
        newField.attributeCount = resource.getShort();
        ;
        for (int i = 0; i < newField.attributeCount; i++) {
            newField.attributes.add(AttributeModifier.visit(resource));
        }
        return newField;
    }

    public String getFieldName() {
        InfoWrapper nameInfo = owner.getConstantPoolInfo(nameIndex);
        return new String(nameInfo.value);
    }

    public String getFieldDesc() {
        InfoWrapper descInfo = owner.getConstantPoolInfo(descIndex);
        return new String(descInfo.value);
    }

    /**
     * 修改属性的权限修饰符
     *
     * @param access 权限修饰符 {@link Access}
     */
    public FieldModifier modifyAccessFlag(int access) {
        accessFlag = (short) access;
        return this;
    }

    public FieldModifier modifyFieldName(String name, boolean safe) {
        if (safe) {
            nameIndex = owner.addUtf8Info(name);
            return this;
        }
        owner.modifyConstantPoolInfo(nameIndex, owner.buildUtfInfo(name));
        return this;
    }

    public FieldModifier modifyFieldDesc(String desc, boolean safe) {
        if (safe) {
            descIndex = owner.addUtf8Info(desc);
            return this;
        }
        owner.modifyConstantPoolInfo(nameIndex, owner.buildUtfInfo(desc));
        return this;
    }

    public FieldModifier addAttribute(byte[] attributeValue) {
        attributes.add(AttributeModifier.visit(new ByteVector(attributeValue)));
        attributeCount++;
        return this;
    }

    public ClassModifier getOwner() {
        return owner;
    }

    public short getAccessFlag() {
        return accessFlag;
    }

    public short getNameIndex() {
        return nameIndex;
    }

    public short getDescIndex() {
        return descIndex;
    }

    public short getAttributeCount() {
        return attributeCount;
    }

    public ArrayList<AttributeModifier> getAttributes() {
        return attributes;
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(accessFlag)
                .putShort(nameIndex)
                .putShort(descIndex)
                .putShort(attributeCount);
        for (AttributeModifier attribute : attributes) {
            byteVectors.putArray(attribute.toByteArray());
        }
        return byteVectors.toByteArray();
    }
}
