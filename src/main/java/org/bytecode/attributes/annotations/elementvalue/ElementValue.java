package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;

public abstract class ElementValue {
    protected byte tag;

    protected ElementValue(byte tag) {
        this.tag = tag;
    }

    public byte getTag() {
        return tag;
    }

    public abstract short load(ConstantPool constantPool);

    public abstract byte[] toByteArray();

    public abstract int getLength();
}
