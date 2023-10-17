package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolLoadable;
import org.exception.NotLoadException;
import org.tools.ByteVector;
import org.visitor.Visitor;

public abstract class Attribute extends ConstantPoolLoadable implements Visitor<Attribute> {
    protected int attributeLength;
    protected String attributeName;
    public final byte target;

    public Attribute(byte target) {
        this.target = target;
    }

    public int getAttributeLength() {
        return attributeLength;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public abstract Attribute visit(ConstantPool constantPool, ByteVector byteVector);

    protected short loadAttributeName(ConstantPool constantPool) {
        cpIndex = constantPool.putUtf8Info(attributeName);
        return cpIndex;
    }

    public abstract boolean isEmpty();

    public abstract byte[] toByteArray();

    public void checkLoaded() {
        if (isLoaded()) return;
        throw new NotLoadException(this.getClass().getSimpleName() + " need to load before toByteArray");
    }
}
