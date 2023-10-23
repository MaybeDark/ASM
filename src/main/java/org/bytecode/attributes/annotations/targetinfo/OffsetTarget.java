package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ConvertTool;

/**
 * 目标为指令,记录了偏移量
 */
public class OffsetTarget extends TargetInfo {
    short offset;

    public OffsetTarget(short offset) {
        this.offset = offset;
    }

    @Override
    public byte[] toByteArray() {
        return ConvertTool.S2B(offset);
    }

    @Override
    public int getLength() {
        return 2;
    }
}
