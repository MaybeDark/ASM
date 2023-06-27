package org.bytecode.method.attribute.code.attrubute.stackmaptable.stackmapframe;

public class SameFrame implements StackMapFrame {
    private final byte offsetDelta;

    public SameFrame(byte offsetDelta) {
        this.offsetDelta = offsetDelta;
    }

    public byte getOffsetDelta() {
        return offsetDelta;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{offsetDelta};
    }
}
