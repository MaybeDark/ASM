package org.bytecode.attributes.annotations;

import org.bytecode.attributes.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

/**
 * 参数注解,编译后保存在方法的属性中
 * void method(@Annotation Type arg0){}
 * 和普通注解一样也分为运行时可见注解{@link RuntimeVisibleAnnotations}和运行时不可见注解{@link RuntimeInvisibleAnnotations}
 */
public class RuntimeVisibleParameterAnnotations extends Annotations {

    /**
     *
     */
    protected byte parametersNum;

    public RuntimeVisibleParameterAnnotations() {
        super("RuntimeVisibleParameterAnnotations");
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        parametersNum = byteVector.getByte();
        short count;
        for (int i = 0; i < parametersNum; i++) {
            count = byteVector.getShort();
            for (int j = 0; j < count; j++) {
                addAnnotationInfo(AnnotationInfo.visitAnnotation(constantPool, byteVector));
            }
        }
        return this;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putByte(parametersNum);
        for (int i = 0; i < parametersNum; i++) {
            byteVectors.putShort(annotationCount);
            annotationInfos.forEach((annotationInfo) -> byteVectors.putArray(annotationInfo.toByteArray()));
        }
        return byteVectors.toByteArray();
    }
}
