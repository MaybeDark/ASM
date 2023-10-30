package org.modify;

import org.tools.ByteVector;
import org.tools.ByteVectors;

public class AttributeModifier {
    short nameIndex;
    int length;
    byte[] info;

    AttributeModifier() {
    }

    public static AttributeModifier visit(ByteVector resource) {
        AttributeModifier newAttribute = new AttributeModifier();
        newAttribute.nameIndex = resource.getShort();
        ;
        newAttribute.length = resource.getInt();
        newAttribute.info = resource.getArray(newAttribute.length);
        return newAttribute;
    }

    public String getAttributeName(ClassModifier classModifier) {
        InfoWrapper nameInfo = classModifier.getConstantPoolInfo(nameIndex);
        byte[] str = new byte[nameInfo.getValue().length - 2];
        System.arraycopy(nameInfo.value, 2, str, 0, str.length);
        return new String(str);
    }

    public int getLength() {
        return length;
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(nameIndex)
                .putInt(length)
                .putArray(info);
        return byteVectors.toByteArray();
    }
}
