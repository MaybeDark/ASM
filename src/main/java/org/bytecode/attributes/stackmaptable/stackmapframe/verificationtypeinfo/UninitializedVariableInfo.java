package org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo;

import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class UninitializedVariableInfo implements VariableInfo {
    public static final byte tag = 8;
    private short offset;

    public UninitializedVariableInfo(short offset) {
        this.offset = offset;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayTool.join(tag, ConvertTool.S2B(offset));
    }

    @Override
    public int getLength() {
        return 3;
    }

    public short getOffset() {
        return offset;
    }
}
