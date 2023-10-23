package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;
import org.tools.ArrayTool;

public class SameLocals1StackItemFrame implements StackMapFrame {

    private final byte offsetDelta;
    private final VariableInfo stackTypeInfo;

    public SameLocals1StackItemFrame(byte offsetDelta, VariableInfo stackTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.stackTypeInfo = stackTypeInfo;
    }

    public byte getOffsetDelta() {
        return offsetDelta;
    }

    public VariableInfo getStackTypeInfo() {
        return stackTypeInfo;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayTool.join((byte) (offsetDelta + 64), stackTypeInfo.toByteArray());
    }

    @Override
    public int getLength() {
        return 1 + stackTypeInfo.getLength();
    }
}
