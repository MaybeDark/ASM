package org.bytecode.attributes.annotations;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

/**
 * 运行时可见注解
 * <p>@Retention(RetentionPolicy.RUNTIME)</p>
 * public @interface Annotation{}
 */
public class RuntimeVisibleAnnotations extends VariableLengthAttribute {

    Annotations<AnnotationInfo> annotations = new Annotations<>();

    public RuntimeVisibleAnnotations() {
        this("RuntimeVisibleAnnotations");
    }

    protected RuntimeVisibleAnnotations(String attributeName) {
        super((byte) (Target.class_info | Target.field_info | Target.method_info));
        this.attributeName = attributeName;
    }

    public RuntimeVisibleAnnotations addAnnotation(AnnotationInfo annotationInfo) {
        annotations.addAnnotationInfo(annotationInfo);
        return this;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short annotationCount = byteVector.getShort();
        for (int i = 0; i < annotationCount; i++) {
            annotations.addAnnotationInfo(AnnotationInfo.visitAnnotation(constantPool, byteVector));
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return annotations.isEmpty();
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector byteVectors = new ByteVector(6 + attributeLength);
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putArray(annotations.toByteArray());
        return byteVectors.end();
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            System.err.println("empty attribute:" + getAttributeName());
        }
        loadAttributeName(cp);
        attributeLength = 0;
        annotations.load(cp);
        attributeLength += annotations.getLength();
        return cpIndex;
    }
}
