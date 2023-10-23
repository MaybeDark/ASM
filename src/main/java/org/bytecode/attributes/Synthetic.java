package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

public class Synthetic extends FixedLengthAttribute {

    public Synthetic() {
        super((byte) (Target.class_info | Target.method_info | Target.field_info));
        attributeName = "Synthetic";
        attributeLength = 0;
    }

    @Override
    public short load(ConstantPool cp) {
        loadAttributeName(cp);
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6);
        result.putShort(cpIndex).putInt(attributeLength);
        return result.end();
    }
}
