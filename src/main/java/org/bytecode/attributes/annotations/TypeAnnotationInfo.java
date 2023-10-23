package org.bytecode.attributes.annotations;

import org.bytecode.attributes.annotations.elementvalue.ElementValue;
import org.bytecode.attributes.annotations.targetinfo.*;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

public class TypeAnnotationInfo extends AnnotationInfo {
    private byte targetType;
    private TargetInfo targetInfo;
    private TypePath typePath = new TypePath();

    public TypeAnnotationInfo(Class<?> annotationClass) {
        super(annotationClass);
    }

    TypeAnnotationInfo() {
        this.annotationType = annotationType;
    }

    static TypeAnnotationInfo visitTypeAnnotation(ConstantPool constantPool, ByteVector byteVector) {
        TypeAnnotationInfo typeAnnotationInfo = new TypeAnnotationInfo();
        typeAnnotationInfo.targetInfo = typeAnnotationInfo.visitTargetInfo(byteVector);
        byte pathLength = byteVector.getByte();
        for (byte i = 0; i < pathLength; i++) {
            typeAnnotationInfo.addTypePath(byteVector.getByte(), byteVector.getByte());
        }
        typeAnnotationInfo.annotationType = constantPool.getUtf8(byteVector.getShort());
        short elementCount = byteVector.getShort();
        String elementName;
        ElementValue elementValue;
        for (int i = 0; i < elementCount; i++) {
            elementName = constantPool.getUtf8(byteVector.getShort());
            elementValue = visitElementValue(constantPool, byteVector);
            typeAnnotationInfo.addPair(elementName, elementValue);
        }
        return typeAnnotationInfo;
    }

    public TypeAnnotationInfo setTargetInfo(byte targetType, TargetInfo targetInfo) {
        this.targetType = targetType;
        this.targetInfo = targetInfo;
        return this;
    }

    public TypeAnnotationInfo addTypePath(byte kind, byte index) {
        typePath.addPath(kind, index);
        return this;
    }

    TargetInfo visitTargetInfo(ByteVector byteVector) {
        targetType = byteVector.getByte();
        switch (targetType) {
            case 0x00:
            case 0x01:
                return new TypeParameterTarget(byteVector.getByte());
            case 0x10:
                return new SupertypeTarget(byteVector.getShort());
            case 0x11:
            case 0x12:
                return new TypeParameterBoundTarget(byteVector.getByte(), byteVector.getByte());
            case 0x13:
            case 0x14:
            case 0x15:
                return new EmptyTarget();
            case 0x16:
                return new FormalParameterTarget(byteVector.getByte());
            case 0x17:
                return new ThrowsTarget(byteVector.getShort());
            case 0x40:
            case 0x41:
                return new LocalVarTarget(byteVector.getShort()).visit(byteVector);
            case 0x42:
                return new CatchTarget(byteVector.getShort());
            case 0x43:
            case 0x44:
            case 0x45:
            case 0x46:
                return new OffsetTarget(byteVector.getShort());
            case 0x47:
            case 0x48:
            case 0x49:
            case 0x4A:
            case 0x4B:
                return new TypeArgumentTarget(byteVector.getShort(), byteVector.getByte());
            default:
                throw new RuntimeException("unknown tag: " + targetType);
        }
    }

    public short load(ConstantPool constantPool) {
        return super.load(constantPool);
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putByte(targetType)
                .putArray(targetInfo.toByteArray())
                .putArray(typePath.toByteArray())
                .putArray(super.toByteArray());
        return byteVectors.toByteArray();
    }

    @Override
    public int getLength() {
        return 1 + targetInfo.getLength() + typePath.getLength() + super.getLength();
    }
}
