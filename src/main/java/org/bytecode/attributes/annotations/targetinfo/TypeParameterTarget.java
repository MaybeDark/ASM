package org.bytecode.attributes.annotations.targetinfo;

/**
 * 目标为泛型参数
 * <T> void method(@TypeAnnotation T arg0){}
 */
public class TypeParameterTarget extends TargetInfo {


    /**
     * 参数索引
     */
    protected byte index;

    public TypeParameterTarget(byte index) {
        this.index = index;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{index};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
