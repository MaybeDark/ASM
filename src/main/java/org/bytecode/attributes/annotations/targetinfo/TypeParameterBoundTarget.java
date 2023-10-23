package org.bytecode.attributes.annotations.targetinfo;

/**
 * 目标为连续多个泛型类型的参数
 */
public class TypeParameterBoundTarget extends TypeParameterTarget {
    /**
     * 从父类中继承了开始下标
     * 范围
     */
    protected byte boundIndex;

    public TypeParameterBoundTarget(byte index, byte boundIndex) {
        super(index);
        this.boundIndex = boundIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{index, boundIndex};
    }

    @Override
    public int getLength() {
        return 1 + super.getLength();
    }
}
