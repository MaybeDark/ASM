package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.LiteralConstantPoolInfo;
import org.tools.ByteVector;

public class ConstElementValue extends ElementValue {
    private LiteralConstantPoolInfo value;
    private short valueIndex;

    public ConstElementValue(byte tag, LiteralConstantPoolInfo value) {
        super(tag);
        this.value = value;
    }

    public short load(ConstantPool constantPool) {
        valueIndex = constantPool.resolveConstantPoolInfo(value);
        return valueIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVector(3).putByte(tag).putShort(valueIndex).end();
    }

    @Override
    public int getLength() {
        return 3;
    }

}
