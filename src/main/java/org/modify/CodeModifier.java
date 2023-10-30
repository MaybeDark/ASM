package org.modify;

import org.tools.ByteVector;

import java.util.HashMap;
import java.util.Map;

public class CodeModifier extends AttributeModifier {
    short stackMax;
    short localsMax;
    int codeLength;
    byte[] code;
    short exceptionTableLength;
    byte[] exceptionTable;
    short attributeCount;
    Map<String, AttributeModifier> attributes = new HashMap<>();


    public CodeModifier() {

    }

    public static CodeModifier visit(ClassModifier owner, AttributeModifier base) {
        CodeModifier newCode = new CodeModifier();
        newCode.nameIndex = base.nameIndex;
        newCode.length = base.length;
        newCode.info = base.info;
        ByteVector infoReader = new ByteVector(newCode.info);
        newCode.stackMax = infoReader.getShort();
        newCode.localsMax = infoReader.getShort();
        newCode.codeLength = infoReader.getInt();
        newCode.code = infoReader.getArray(newCode.codeLength);
        newCode.exceptionTableLength = infoReader.getShort();
        newCode.exceptionTable = infoReader.getArray(newCode.exceptionTableLength * 8);
        newCode.attributeCount = infoReader.getShort();
        AttributeModifier attribute;
        for (int i = 0; i < newCode.attributeCount; i++) {
            attribute = AttributeModifier.visit(infoReader);
            newCode.attributes.put(attribute.getAttributeName(owner), attribute);
        }
        return newCode;
    }

    public short getStackMax() {
        return stackMax;
    }

    public short getLocalsMax() {
        return localsMax;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public byte[] getCode() {
        return code;
    }

    public short getExceptionTableLength() {
        return exceptionTableLength;
    }

    public byte[] getExceptionTable() {
        return exceptionTable;
    }

    public short getAttributeCount() {
        return attributeCount;
    }

    public Map<String, AttributeModifier> getAttributes() {
        return attributes;
    }
}
