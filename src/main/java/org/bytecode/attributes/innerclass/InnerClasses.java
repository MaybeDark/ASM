package org.bytecode.attributes.innerclass;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolClassInfo;
import org.bytecode.constantpool.info.ConstantPoolUtf8Info;
import org.tools.ByteVector;

import java.util.HashMap;
import java.util.Map;

public class InnerClasses extends VariableLengthAttribute {
    private short innerClassCount;
    private Map<String, InnerClass> pool;

    public InnerClasses() {
        super(Target.class_info);
        attributeLength = 2;
        attributeName = "InnerClass";
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
        for (int i = 0; i < byteVector.getShort(); i++) {
            ConstantPoolClassInfo innerClassInfo = (ConstantPoolClassInfo) constantPool.get(byteVector.getShort());
            short outerClassInfoIndex = byteVector.getShort();
            String outerClassName = null;
            if (outerClassInfoIndex != 0) {
                outerClassName = ((ConstantPoolClassInfo) constantPool.get(outerClassInfoIndex)).getClassInfo();
            }
            ConstantPoolUtf8Info innerNameInfo = (ConstantPoolUtf8Info) constantPool.get(byteVector.getShort());
            short access = byteVector.getShort();
            InnerClass newInnerClass = new InnerClass(access, innerClassInfo.getClassInfo(), outerClassName, innerNameInfo.getLiteral());
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
