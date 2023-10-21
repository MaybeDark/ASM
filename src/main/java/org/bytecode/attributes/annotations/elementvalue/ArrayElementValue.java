package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVectors;

/**
 * 参数类型为数组类型
 * 元素类型 {@link ElementValue}
 * Annotation{
 * ElementValue[] value();
 * }
 */
public class ArrayElementValue extends ElementValue {
    private short valueCount;
    private ElementValue[] values;

    public ArrayElementValue(ElementValue[] values) {
        super((byte) '[');
        this.values = values;
        this.valueCount = (short) values.length;
    }

    @Override
    public short load(ConstantPool constantPool) {
        for (ElementValue value : values) {
            value.load(constantPool);
        }
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors()
                .putByte(tag)
                .putShort(valueCount);
        for (int i = 0; i < valueCount; i++) {
            byteVectors.putArray(values[i].toByteArray());
        }
        return byteVectors.toByteArray();
    }

    @Override
    public int getLength() {
        int length = 0;
        for (ElementValue value : values) {
            length += value.getLength();
        }
        return length;
    }
}
