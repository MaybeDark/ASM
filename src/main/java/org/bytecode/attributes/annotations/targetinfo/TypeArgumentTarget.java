package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ByteVector;

/**
 * 目标为cast类型等
 * void method(){
 * (@TypeAnnotation Type) local;
 * }
 */
public class TypeArgumentTarget extends TargetInfo {
    /**
     * 偏移量
     */
    short offset;
    /**
     * 转换类型的下标（可能有多次转换）
     */
    byte typeArgumentIndex;

    public TypeArgumentTarget(short offset, byte typeArgumentIndex) {
        this.offset = offset;
        this.typeArgumentIndex = typeArgumentIndex;
    }

    @Override
    public byte[] toByteArray() {
        ByteVector byteVector = new ByteVector(getLength());
        byteVector.putShort(offset);
        byteVector.putByte(typeArgumentIndex);
        return byteVector.end();
    }

    @Override
    public int getLength() {
        return 3;
    }
}
