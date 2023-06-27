package org.bytecode.com;

import org.bytecode.constantpool.ConstantPoolLoadable;

public abstract class Attribute extends ConstantPoolLoadable {
    public static final int ATTRIBUTE_LENGTH_BYTECODE_LENGTH = 4;
    protected int attributeLength;

    public int getAttributeLength() {
        return attributeLength;
    }

    public abstract byte[] toByteArray();
}
