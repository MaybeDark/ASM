package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

public class SourceDebugExtension extends FixedLengthAttribute {
    public SourceDebugExtension() {
        super(Target.class_info);
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public short load(ConstantPool cp) {
        return 0;
    }
}
