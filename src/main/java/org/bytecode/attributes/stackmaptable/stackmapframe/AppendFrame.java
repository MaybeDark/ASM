package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;
import org.tools.ByteVectors;

public class AppendFrame implements StackMapFrame {
    private final byte operand;
    private final short offsetDelta;
    private final VariableInfo[] localTypeInfo;
    private int length;

    public AppendFrame(byte operand, short offsetDelta, VariableInfo[] localTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.operand = operand;
        this.localTypeInfo = localTypeInfo;
        length = 3;
        for (VariableInfo variableInfo : localTypeInfo) {
            length += variableInfo.getLength();
        }
    }

    @Override
    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putByte(251 + operand)
                .putShort(offsetDelta);
        for (VariableInfo variableInfo : localTypeInfo) {
            byteVectors.putArray(variableInfo.toByteArray());
        }
        return byteVectors.toByteArray();
    }

    @Override
    public int getLength() {
        return length;
    }
}
