package org.bytecode.attributes.method.code.attrubute.stackmaptable.stackmapframe;

import org.bytecode.attributes.method.code.attrubute.stackmaptable.verificationtypeinfo.VariableInfo;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

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
        byte[] temp = ArrayTool.join(frameType, ConvertTool.S2B(offsetDelta));
        return ArrayTool.join(temp, stackTypeInfo.toByteArray());
    }
}
