package org.attribute;

import org.other.ConstantPoolLoadable;

public abstract class Attribute extends ConstantPoolLoadable {
    protected int attributeLength;

    public int getAttributeLength() {
        return attributeLength;
    }

    public abstract  byte[] toByteArray();
}
