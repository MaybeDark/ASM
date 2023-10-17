package org.bytecode.attributes.annotations;

import com.sun.istack.internal.NotNull;
import org.Type;
import org.bytecode.attributes.annotations.elementvalue.*;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.*;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.HashMap;
import java.util.Map;

public class AnnotationInfo {
    private final String annotationType;
    private short annotationTypeIndex = 0;
    private short elementValuePairsCount = 0;
    private Map<String, ElementValuePairs> pairs;

    public AnnotationInfo(Class<?> annotation) {
        if (annotation == null || ! annotation.isAnnotation()) {
            throw new RuntimeException("args0 must be a annotation");
        }
        annotationType = Type.getType(annotation).getDescriptor();
        pairs = new HashMap<>();
    }

    AnnotationInfo(String annotationType) {
        this.annotationType = annotationType;
        pairs = new HashMap<>();
    }

    static AnnotationInfo visitAnnotation(ConstantPool constantPool, ByteVector byteVector) {
        AnnotationInfo newAnnotationInfo = new AnnotationInfo(((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral());
        for (int i = 0; i < byteVector.getShort(); i++) {
            String elementName = ((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral();
            ElementValue elementValue = visitElementValue(constantPool, byteVector);
            newAnnotationInfo.addPair(elementName, elementValue);
        }
        return newAnnotationInfo;
    }

    static ElementValue visitElementValue(ConstantPool constantPool, ByteVector byteVector) {
        byte tag = byteVector.getByte();
        switch (tag) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
            case 'D':
            case 'F':
            case 'J':
            case 's':
                return new ConstElementValue(tag, (LiteralConstantPoolInfo) constantPool.get(byteVector.getShort()));
            case 'e':
                return new EnumElementValue(((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral(),
                        ((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral()
                );
            case 'c':
                return new ClassElementValue(((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral());
            case '@':
                return new AnnotationElementValue(visitAnnotation(constantPool, byteVector));
            case '[':
                short valueCount = byteVector.getShort();
                ElementValue[] elementValues = new ElementValue[valueCount];
                for (int i = 0; i < valueCount; i++) {
                    elementValues[i] = visitElementValue(constantPool, byteVector);
                }
                return new ArrayElementValue(elementValues);
            default:
                throw new RuntimeException("error tag");
        }
    }

    public AnnotationInfo setElement(@NotNull String elementName, byte constValue) {
        return setIntElement((byte) 'B', elementName, constValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, char constValue) {
        return setIntElement((byte) 'C', elementName, constValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, short constValue) {
        return setIntElement((byte) 'S', elementName, constValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, boolean constValue) {
        return setIntElement((byte) 'S', elementName, constValue ? 1 : 0);
    }

    public AnnotationInfo setElement(@NotNull String elementName, int constValue) {
        return setIntElement((byte) 'I', elementName, constValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, double constValue) {
        ConstElementValue constElementValue = new ConstElementValue((byte) 'D', new ConstantPoolDoubleInfo(constValue));
        return addPair(elementName, constElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, long constValue) {
        ConstElementValue constElementValue = new ConstElementValue((byte) 'J', new ConstantPoolLongInfo(constValue));
        return addPair(elementName, constElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, float constValue) {
        ConstElementValue constElementValue = new ConstElementValue((byte) 'F', new ConstantPoolFloatInfo(constValue));
        return addPair(elementName, constElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull String constValue) {
        ConstElementValue constElementValue = new ConstElementValue((byte) 's', new ConstantPoolUtf8Info(constValue));
        return addPair(elementName, constElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull Enum enumValue) {
        EnumElementValue enumElementValue = new EnumElementValue(Type.getType(enumValue.getClass()).getDescriptor(), enumValue.name());
        return addPair(elementName, enumElementValue);
    }

    AnnotationInfo setElement(@NotNull String elementName, String enumName, String constName) {
        EnumElementValue enumElementValue = new EnumElementValue(elementName, constName);
        return addPair(elementName, enumElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull Class classValue) {
        ClassElementValue classElementValue = new ClassElementValue(Type.getType(classValue).getDescriptor());
        return addPair(elementName, classElementValue);
    }

    AnnotationInfo setElement(@NotNull String elementName, String classInfo, boolean flag) {
        ClassElementValue classElementValue = new ClassElementValue(classInfo);
        return addPair(elementName, classElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, AnnotationInfo annotationValue) {
        AnnotationElementValue annotationElementValue = new AnnotationElementValue(annotationValue);
        return addPair(elementName, annotationElementValue);
    }

    public AnnotationInfo setElement(@NotNull String elementName, byte[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'B', new ConstantPoolIntegerInfo((int) constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, char[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'C', new ConstantPoolIntegerInfo((int) constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, short[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'S', new ConstantPoolIntegerInfo((int) constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, boolean[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'Z', new ConstantPoolIntegerInfo(constValues[i] ? 1 : 0));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, int[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'I', new ConstantPoolIntegerInfo(constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, double[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'D', new ConstantPoolDoubleInfo(constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, long[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'L', new ConstantPoolLongInfo(constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, float[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 'F', new ConstantPoolFloatInfo(constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull String[] constValues) {
        ElementValue[] elementValues = new ElementValue[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            elementValues[i] = new ConstElementValue((byte) 's', new ConstantPoolUtf8Info(constValues[i]));
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull Enum[] enumValues) {
        ElementValue[] elementValues = new EnumElementValue[enumValues.length];
        String enumName = Type.getType(elementValues.getClass().getComponentType()).getDescriptor();
        for (int i = 0; i < enumValues.length; i++) {
            elementValues[i] = new EnumElementValue(enumName, enumValues[i].name());
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, @NotNull Class[] classValues) {
        ElementValue[] elementValues = new ClassElementValue[classValues.length];
        for (int i = 0; i < classValues.length; i++) {
            elementValues[i] = new ClassElementValue(Type.getType(classValues[i]).getDescriptor());
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    public AnnotationInfo setElement(@NotNull String elementName, AnnotationInfo[] annotationValues) {
        ElementValue[] elementValues = new AnnotationElementValue[annotationValues.length];
        for (int i = 0; i < annotationValues.length; i++) {
            elementValues[i] = new AnnotationElementValue(annotationValues[i]);
        }
        return addPair(elementName, new ArrayElementValue(elementValues));
    }

    private AnnotationInfo setIntElement(byte tag, String elementName, int constValue) {
        ConstElementValue constElementValue = new ConstElementValue(tag, new ConstantPoolIntegerInfo(constValue));
        return addPair(elementName, constElementValue);
    }

    AnnotationInfo addPair(@NotNull String elementName, ElementValue elementValue) {
        elementValuePairsCount++;
        pairs.put(elementName, new ElementValuePairs(elementName, elementValue));
        return this;
    }

    public ElementValue getElementValue(String elementName) {
        ElementValuePairs elementValuePairs = pairs.get(elementName);
        return elementValuePairs.elementValue;
    }

    public short load(ConstantPool constantPool) {
        annotationTypeIndex = constantPool.putUtf8Info(annotationType);
        pairs.values().forEach(elementValuePairs -> elementValuePairs.load(constantPool));
        return annotationTypeIndex;
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(annotationTypeIndex);
        pairs.values().forEach(elementValuePairs -> byteVectors.putArray(elementValuePairs.toByteArray()));
        return byteVectors.toByteArray();
    }

    public int getLength() {
        return pairs.values()
                .stream()
                .mapToInt(ElementValuePairs::getLength)
                .sum() + 2;
    }

    class ElementValuePairs {
        private final String elementName;
        private final ElementValue elementValue;
        private short elementNameIndex;

        public ElementValuePairs(String elementName, ElementValue elementValue) {
            this.elementName = elementName;
            this.elementValue = elementValue;
        }

        public short load(ConstantPool constantPool) {
            elementNameIndex = constantPool.putUtf8Info(elementName);
            elementValue.load(constantPool);
            return elementNameIndex;
        }

        public byte[] toByteArray() {
            return new ByteVectors()
                    .putShort(elementNameIndex)
                    .putArray(elementValue.toByteArray())
                    .toByteArray();
        }

        public ElementValue getElementValue() {
            return elementValue;
        }

        public short getElementNameIndex() {
            return elementNameIndex;
        }

        public int getLength() {
            return 2 + elementValue.getLength();
        }
    }

}