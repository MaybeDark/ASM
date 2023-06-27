package org.bytecode.method.attribute.code.attrubute.stackmaptable.stackmapframe;

import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class ChopFrame implements StackMapFrame {
    private final byte operand;
    private final short offsetDelta;

    public ChopFrame(byte operand, short offsetDelta) {
        this.offsetDelta = offsetDelta;
        this.operand = operand;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayTool.join((byte) (251 - operand), ConvertTool.S2B(offsetDelta));
    }
}
