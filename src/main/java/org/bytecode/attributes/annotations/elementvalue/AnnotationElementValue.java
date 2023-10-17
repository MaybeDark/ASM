package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.attributes.annotations.AnnotationInfo;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVectors;

public class AnnotationElementValue extends ElementValue {
    private AnnotationInfo annotationInfo;

    public AnnotationElementValue(AnnotationInfo annotationInfo) {
        super((byte) '@');
        this.annotationInfo = annotationInfo;
    }

    public AnnotationInfo getAnnotationInfo() {
        return annotationInfo;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return annotationInfo.load(constantPool);
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVectors().putByte(tag).putArray(annotationInfo.toByteArray()).toByteArray();
    }

    @Override
    public int getLength() {
        return 1 + annotationInfo.getLength();
    }
}
