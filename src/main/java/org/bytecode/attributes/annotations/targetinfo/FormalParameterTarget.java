package org.bytecode.attributes.annotations.targetinfo;

/**
 * 目标为非泛型参数
 * void method(@TypeAnnotation Type arg0){}
 */
public class FormalParameterTarget extends TargetInfo {
    /**
     * 目标参数在参数列表中下标
     */
    byte formalParameterIndex;

    public FormalParameterTarget(byte formalParameterIndex) {
        this.formalParameterIndex = formalParameterIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{formalParameterIndex};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
