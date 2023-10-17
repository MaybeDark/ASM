package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;
import org.tools.ByteVector;

public class SameLocals1StackItemFrameExtended implements StackMapFrame {
    public static final byte frameType = (byte) 247;
    private final VariableInfo stackTypeInfo;
    private final short offsetDelta;

    public SameLocals1StackItemFrameExtended(short offsetDelta, VariableInfo stackTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.stackTypeInfo = stackTypeInfo;
    }

    @Override
    public byte[] toByteArray() {
        ByteVector byteVector = new ByteVector(getLength());
        byteVector.putByte(frameType)
                .putShort(offsetDelta)
                .putArray(stackTypeInfo.toByteArray());
        return byteVector.end();
    }

    @Override
    public int getLength() {
        return 3 + stackTypeInfo.getLength();
    }
}
