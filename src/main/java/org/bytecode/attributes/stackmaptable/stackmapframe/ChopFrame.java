package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.tools.ByteVector;

public class ChopFrame implements StackMapFrame {
    private final byte operand;
    private final short offsetDelta;

    public ChopFrame(byte operand, short offsetDelta) {
        this.offsetDelta = offsetDelta;
        this.operand = operand;
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVector(3).putByte(251 - operand).putShort(offsetDelta).end();
    }

    @Override
    public int getLength() {
        return 3;
    }
}
