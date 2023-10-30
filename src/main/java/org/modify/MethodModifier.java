package org.modify;

import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.HashMap;
import java.util.Map;

public class MethodModifier {
    short accessFlag;
    short nameIndex;
    short descIndex;
    short attributesCount;
    Map<String, AttributeModifier> attributes = new HashMap<>();
    private ClassModifier owner;

    MethodModifier(ClassModifier owner) {
        this.owner = owner;
    }

    public static MethodModifier visit(ClassModifier owner, ByteVector resource) {
        MethodModifier newMethod = new MethodModifier(owner);
        newMethod.accessFlag = resource.getShort();
        newMethod.nameIndex = resource.getShort();
        newMethod.descIndex = resource.getShort();
        newMethod.attributesCount = resource.getShort();
        AttributeModifier newAttribute;
        for (int i = 0; i < newMethod.attributesCount; i++) {
            newAttribute = AttributeModifier.visit(resource);
            newMethod.attributes.put(newAttribute.getAttributeName(owner), newAttribute);
        }
        AttributeModifier code = newMethod.attributes.get("Code");
        if (code != null) {
            newMethod.attributes.put("Code", CodeModifier.visit(owner, code));
        }
        return newMethod;
    }

    public MethodModifier modifyAccessFlag(int access) {
        accessFlag = (short) access;
        return this;
    }

    public MethodModifier addAttribute(String name, AttributeModifier newAttribute) {
        attributes.put(name, newAttribute);
        attributesCount++;
        return this;
    }

    public String getMethodName() {
        return owner.getUtf8(nameIndex);
    }

    public String getMethodDesc() {
        return owner.getUtf8(descIndex);
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

    public short getAttributesCount() {
        return attributesCount;
    }

    public Map<String, AttributeModifier> getAttributes() {
        return attributes;
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(accessFlag)
                .putShort(nameIndex)
                .putShort(descIndex)
                .putShort(attributesCount);
        for (AttributeModifier attribute : attributes.values()) {
            byteVectors.putArray(attribute.toByteArray());
        }
        return byteVectors.toByteArray();
    }
}
