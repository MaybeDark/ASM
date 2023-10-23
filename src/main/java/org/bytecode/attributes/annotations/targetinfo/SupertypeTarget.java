package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ConvertTool;

/**
 * 目标为父类或接口
 * class A extends @TypeAnnotation B implements @TypeAnnotation C{}
 */
public class SupertypeTarget extends TargetInfo {

    /**
     * 当目标为父类时superTypeIndex的值应为[-1,-1]
     */
    public static final short SUPER_CLASS_TYPE_INDEX = (short) 0xffff;
    /**
     * 表示接口索引值或父类
     */
    private short superTypeIndex;

    public SupertypeTarget(short index) {
        this.superTypeIndex = index;
    }

    @Override
    public byte[] toByteArray() {
        return ConvertTool.S2B(superTypeIndex);
    }

    @Override
    public int getLength() {
        return 2;
    }
}
