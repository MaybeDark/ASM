package org.bytecode.attributes;

import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolClassInfo;
import org.tools.ByteVector;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里的Exception属性是在方法表中与Code属性平级的一项属性，表示方法描述时，
 * 在throws关键字后面列举的异常
 */
public class Exceptions extends VariableLengthAttribute {
    private short exceptionCount;
    List<String> exceptions = new ArrayList<>();
    private short[] exceptionsCpIndex;

    public Exceptions() {
        super(Target.method_info);
        attributeLength = 2;
        attributeName = "Exceptions";
    }


    public short addException(Class<? extends Exception> exception) {
        return addException(Type.getType(exception).getClassInfo());
    }

    private short addException(String exceptionName) {
        cpIndex = 0;
        exceptions.add(exceptionName);
        attributeLength += 2;
        return exceptionCount++;
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            System.err.println("empty attribute:" + getAttributeName());
        }
        loadAttributeName(cp);
        exceptionsCpIndex = new short[exceptionCount];
        for (int i = 0; i < exceptionCount; i++) {
            exceptionsCpIndex[i] = cp.putClassInfo(exceptions.get(i));
        }
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short count = byteVector.getShort();
        for (int i = 0; i < count; i++) {
            addException(((ConstantPoolClassInfo) constantPool.get(byteVector.getShort())).getClassInfo());
        }
        return this;
    }

    public boolean isEmpty() {
        return exceptionCount == 0;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6 + attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(exceptionCount);
        for (int i = 0; i < exceptionCount; i++) {
            result.putShort(exceptionsCpIndex[i]);
        }
        return result.end();
    }
}
