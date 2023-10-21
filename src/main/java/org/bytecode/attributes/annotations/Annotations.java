package org.bytecode.attributes.annotations;

import com.sun.istack.internal.NotNull;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.ArrayList;

public abstract class Annotations extends VariableLengthAttribute {
    protected int annotationCount = 0;
    protected ArrayList<AnnotationInfo> annotationInfos;

    public Annotations(String attributeName) {
        super((byte) (Target.class_info | Target.method_info | Target.field_info | Target.code_info));
        annotationInfos = new ArrayList<>();
        this.attributeName = attributeName;
    }

    public Annotations addAnnotationInfo(@NotNull AnnotationInfo annotationInfo) {
        annotationInfos.add(annotationInfo);
        annotationCount++;
        cpIndex = 0;
        return this;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short count = byteVector.getShort();
        for (int i = 0; i < count; i++) {
            addAnnotationInfo(AnnotationInfo.visitAnnotation(constantPool, byteVector));
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return annotationCount == 0;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(annotationCount);
        annotationInfos.forEach((annotationInfo) -> byteVectors.putArray(annotationInfo.toByteArray()));
        return byteVectors.toByteArray();
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            System.err.println("empty attribute:" + getAttributeName());
        }
        loadAttributeName(cp);
        attributeLength = 0;
        annotationInfos.forEach(annotationInfo -> {
            annotationInfo.load(cp);
            attributeLength += annotationInfo.getLength();
        });
        return cpIndex;
    }
}
