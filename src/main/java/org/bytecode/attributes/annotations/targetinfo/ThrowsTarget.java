package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ConvertTool;

/**
 * 目标为抛出的异常
 * void method throws @TypeAnnotation Exception{}
 */
public class ThrowsTarget extends TargetInfo {

    /**
     * 目标异常的下标
     */
    short throwsTypeIndex;

    public ThrowsTarget(short throwsTypeIndex) {
        this.throwsTypeIndex = throwsTypeIndex;
    }

    @Override
    public byte[] toByteArray() {
        return ConvertTool.S2B(throwsTypeIndex);
    }

    @Override
    public int getLength() {
        return 2;
    }
}
