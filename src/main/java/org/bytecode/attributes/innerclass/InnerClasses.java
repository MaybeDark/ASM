package org.bytecode.attributes.innerclass;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.HashMap;
import java.util.Map;

public class InnerClasses extends VariableLengthAttribute {
    private short innerClassCount;
    private Map<String, InnerClass> pool;

    public InnerClasses() {
        super(Target.class_info);
        attributeLength = 2;
        attributeName = "InnerClasses";
        pool = new HashMap<>();
    }

    public short addInnerClass(InnerClass innerClass) {
        pool.put(innerClass.getInnerClassName(), innerClass);
        attributeLength += 8;
        cpIndex = 0;
        return innerClassCount++;
    }

    public boolean isEmpty() {
        return innerClassCount == 0;
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            System.err.println("empty attribute:" + getAttributeName());
        }
        loadAttributeName(cp);
        pool.values().forEach((innerClass -> innerClass.load(cp)));
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short count = byteVector.getShort(), outerClassInfoIndex, innerNameIndex, access;
        InnerClass newInnerClass;
        String outerClassName = null, innerName = null, innerClassName;
        for (int i = 0; i < count; i++) {
            innerClassName = constantPool.getUtf8OfClassInfo(byteVector.getShort());
            if ((outerClassInfoIndex = byteVector.getShort()) != 0) {
                outerClassName = constantPool.getUtf8OfClassInfo(outerClassInfoIndex);
            }
            if ((innerNameIndex = byteVector.getShort()) != 0) {
                innerName = constantPool.getUtf8(innerNameIndex);
            }
            access = byteVector.getShort();
            newInnerClass = new InnerClass(access, innerClassName, outerClassName, innerName);
            addInnerClass(newInnerClass);
        }
        return this;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6 + attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(innerClassCount);
        pool.values().forEach(innerClass -> result.putArray(innerClass.toByteArray()));
        return result.end();
    }
}
