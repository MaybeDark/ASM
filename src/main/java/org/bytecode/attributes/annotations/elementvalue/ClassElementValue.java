package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

public class ClassElementValue extends ElementValue {
    private String classInfo;
    private short classIndex = 0;

    public ClassElementValue(String classInfo) {
        super((byte) 'c');
        this.classInfo = classInfo;
    }

    public String getKlass() {
        return classInfo;
    }

    @Override
    public short load(ConstantPool constantPool) {
        classIndex = constantPool.putClassInfo(classInfo);
        return classIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVector(3).putByte(tag).putShort(classIndex).end();
    }

    @Override
    public int getLength() {
        return 3;
    }

}
