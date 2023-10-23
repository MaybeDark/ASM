package org.bytecode.attributes.annotations;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

/**
 * <p>@Target(ElementType.TYPE_USE)</p>
 * public @interface TypeAnnotation{}
 */
public class RuntimeVisibleTypeAnnotations extends VariableLengthAttribute {

    protected Annotations<TypeAnnotationInfo> annotations = new Annotations<>();

    public RuntimeVisibleTypeAnnotations() {
        super((byte) (Target.method_info | Target.field_info | Target.class_info | Target.code_info));
        attributeName = "RuntimeVisibleTypeAnnotations";
    }

    protected RuntimeVisibleTypeAnnotations(String attributeName) {
        super((byte) (Target.method_info | Target.field_info | Target.class_info | Target.code_info));
        this.attributeName = attributeName;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short annotationCount = byteVector.getShort();
        for (int i = 0; i < annotationCount; i++) {
            annotations.addAnnotationInfo(TypeAnnotationInfo.visitTypeAnnotation(constantPool, byteVector));
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return annotations.isEmpty();
    }

    @Override
    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putArray(annotations.toByteArray());
        return byteVectors.toByteArray();
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
