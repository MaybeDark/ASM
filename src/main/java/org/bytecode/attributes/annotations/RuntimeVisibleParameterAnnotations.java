package org.bytecode.attributes.annotations;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.ArrayList;

/**
 * 参数注解,编译后保存在方法的属性中
 * void method(@Annotation Type arg0){}
 */
public class RuntimeVisibleParameterAnnotations extends VariableLengthAttribute {

    /**
     * 含注解的参数
     * 如果中间不连续则仍会包含在parametersNum中,但是对应的annotationNum会等于0
     */
    protected byte parametersNum;

    protected ArrayList<Annotations<AnnotationInfo>> annotationTable = new ArrayList<>();

    public RuntimeVisibleParameterAnnotations() {
        this("RuntimeVisibleParameterAnnotations");
    }

    protected RuntimeVisibleParameterAnnotations(String attributeName) {
        super((byte) (Target.class_info | Target.field_info | Target.method_info));
        this.attributeName = attributeName;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        parametersNum = byteVector.getByte();
        short annotationCount;
        for (int i = 0; i < parametersNum; i++) {
            annotationTable.add(new Annotations<>());
            annotationCount = byteVector.getShort();
            for (int j = 0; j < annotationCount; j++) {
                annotationTable.get(i).addAnnotationInfo(AnnotationInfo.visitAnnotation(constantPool, byteVector));
            }
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return parametersNum == 0;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVectors byteVectors = new ByteVectors();
        Annotations<AnnotationInfo> annotations;
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putByte(parametersNum);
        for (int i = 0; i < parametersNum; i++) {
            annotations = annotationTable.get(i);
            byteVectors.putArray(annotations.toByteArray());
        }
        return byteVectors.toByteArray();
    }

    @Override
    public short load(ConstantPool cp) {
        loadAttributeName(cp);
        attributeLength = 1;
        attributeLength += annotationTable.stream().filter(annotations -> {
            annotations.load(cp);
            return true;
        }).mapToInt(Annotations::getLength).sum();
        return cpIndex;
    }
}
