package org.bytecode.attributes.method.code.attrubute.stackmaptable.stackmapframe;

import org.bytecode.attributes.method.code.attrubute.stackmaptable.verificationtypeinfo.VariableInfo;
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
        return ArrayTool.join((byte) (offsetDelta+64),stackTypeInfo.toByteArray());
    }
}
