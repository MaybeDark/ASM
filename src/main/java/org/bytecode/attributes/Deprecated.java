package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

/**
 * Deprecated用于表示某个类或者方法字段已经被认定为不推荐使用，
 * 它可以通过在代码中使用 @deprecated 注释进行设置
 */
public class Deprecated extends FixedLengthAttribute {
    public Deprecated() {
        super((byte) (Target.class_info | Target.method_info | Target.field_info));
        attributeLength = 0;
        attributeName = "Deprecated";
    }


    @Override
    public short load(ConstantPool cp) {
        loadAttributeName(cp);
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6);
        result.putShort(cpIndex)
                .putInt(attributeLength);
        return result.end();
    }
}
