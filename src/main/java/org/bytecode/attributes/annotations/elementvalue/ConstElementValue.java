package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.LiteralConstantPoolInfo;
import org.tools.ByteVector;

/**
 * 注解类型为基本类型和{@link String}
 * Annotation{
 * int value();
 * String value0();
 * }
 */
public class ConstElementValue extends ElementValue {

    private LiteralConstantPoolInfo value;
    private short valueIndex;

    /**
     * @param tag 对应的tag <a href="https://docs.oracle.com/javase/specs/jvms/se19/html/jvms-4.html#jvms-4.7.16.1"></a>
     * @param value 对应的ConstantPoolInfo
     */
    public ConstElementValue(byte tag, LiteralConstantPoolInfo value) {
        super(tag);
        this.value = value;
    }

    public short load(ConstantPool constantPool) {
        valueIndex = constantPool.resolveConstantPoolInfo(value);
        return valueIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVector(3)
                .putByte(tag)
                .putShort(valueIndex)
                .end();
    }

    @Override
    public int getLength() {
        return 3;
    }

}
