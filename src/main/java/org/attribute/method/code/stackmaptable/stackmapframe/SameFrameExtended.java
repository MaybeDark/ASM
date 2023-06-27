package org.attribute.method.code.stackmaptable.stackmapframe;

import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class SameFrameExtended implements StackMapFrame{
    public static final byte frameType = (byte) 251;
    private final short offsetDelta;
    public SameFrameExtended(short offsetDelta){
        this.offsetDelta = offsetDelta;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayTool.join(frameType, ConvertTool.S2B(offsetDelta));
    }
}
