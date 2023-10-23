package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.info.ConstantPoolStringInfo;
import org.bytecode.constantpool.info.LiteralConstantPoolInfo;
import org.tools.ByteVector;

import java.util.Objects;

/**
 * 属性为基本类型或者String类型且同时被static final修饰
 * 编译时则会为此属性生成ConstantValue并通过常量池索引为属性赋值
 * 若属性只被static则会通过<clinit>方法为属性赋值
 */
public class ConstantValue extends FixedLengthAttribute {

    private AbsConstantPoolInfo value;
    private short valueCpIndex;

    public ConstantValue() {
        super(Target.class_info);
        attributeLength = 2;
        attributeName = "ConstantValue";
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            throw new RuntimeException("setValue is required before loading");
        }
        loadAttributeName(cp);
        valueCpIndex = cp.resolveConstantPoolInfo(value);
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        valueCpIndex = byteVector.getShort();
        value = constantPool.get(valueCpIndex);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return Objects.isNull(value);
    }

    public AbsConstantPoolInfo getValue() {
        return value;
    }

    public void setValue(ConstantPoolStringInfo value) {
        cpIndex = 0;
        this.value = value;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(8);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(valueCpIndex);
        return result.end();
    }

    public void setValue(LiteralConstantPoolInfo value) {
        cpIndex = 0;
        this.value = value;
    }

    public short getValueCpIndex() {
        return valueCpIndex;
    }
}
