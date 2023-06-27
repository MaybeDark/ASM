package org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo;

import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class UninitializedVariableInfo implements VariableInfo {
    public static final byte tag = 8;
    private short offset;
    public UninitializedVariableInfo(short offset){
        this.offset = offset;
    }
    @Override
    public byte[] toByteArray() {
        return ArrayTool.join(tag, ConvertTool.S2B(offset));
    }

    public short getOffset() {
        return offset;
    }
}
