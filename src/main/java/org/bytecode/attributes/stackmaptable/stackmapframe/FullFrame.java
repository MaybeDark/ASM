package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;
import org.tools.ByteVectors;

public class FullFrame implements StackMapFrame {
    public static final byte frameType = (byte) 255;
    private final short offsetDelta;
    private final short numberOfLocals;
    private final VariableInfo[] localTypeInfo;
    private final short numberOfStack;
    private final VariableInfo[] stackTypeInfo;
    private int length;
    public FullFrame(short offsetDelta, short numberOfLocals, VariableInfo[] localTypeInfo, short numberOfStack, VariableInfo[] stackTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.numberOfLocals = numberOfLocals;
        this.localTypeInfo = localTypeInfo;
        this.numberOfStack = numberOfStack;
        this.stackTypeInfo = stackTypeInfo;
        length = 5;
        for (VariableInfo variableInfo : localTypeInfo) {
            length += variableInfo.getLength();
        }

        for (VariableInfo variableInfo : stackTypeInfo) {
            length += variableInfo.getLength();
        }
    }

    @Override
    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putByte(frameType)
                .putShort(offsetDelta)
                .putShort(numberOfLocals);
        if (numberOfLocals > 0) {
            for (VariableInfo lti : localTypeInfo) {
                byteVectors.putArray(lti.toByteArray());
            }
        }
        byteVectors.putShort(numberOfStack);
        if (numberOfStack > 0) {
            for (VariableInfo sti : stackTypeInfo) {
                byteVectors.putArray(sti.toByteArray());
            }
        }
        return byteVectors.toByteArray();
    }

    @Override
    public int getLength() {
        return length;
    }

    public short getOffsetDelta() {
        return offsetDelta;
    }

    public short getNumberOfLocals() {
        return numberOfLocals;
    }

    public short getNumberOfStack() {
        return numberOfStack;
    }

    public VariableInfo[] getLocalTypeInfo() {
        return localTypeInfo;
    }

    public VariableInfo[] getStackTypeInfo() {
        return stackTypeInfo;
    }
}
