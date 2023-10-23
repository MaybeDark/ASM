package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

/**
 * 参数类型为枚举类型
 * Annotation{
 * Enum value();
 * }
 */
public class EnumElementValue extends ElementValue {
    /**
     * enumName 枚举类名
     * constName 枚举名
     */
    private String enumName, constName;
    private short enumNameIndex = 0, constNameIndex = 0;

    public EnumElementValue(String enumName, String constName) {
        super((byte) 'e');
        this.enumName = enumName;
        this.constName = constName;
    }

    public String getConstName() {
        return constName;
    }

    public String getEnumName() {
        return enumName;
    }

    public short getConstNameIndex() {
        return constNameIndex;
    }

    public short getEnumNameIndex() {
        return enumNameIndex;
    }

    @Override
    public short load(ConstantPool constantPool) {
        enumNameIndex = constantPool.putUtf8Info(enumName);
        constNameIndex = constantPool.putUtf8Info(constName);
        return enumNameIndex;
    }

    @Override
    public byte[] toByteArray() {
        return new ByteVector(5).putByte(tag).putShort(enumNameIndex).putShort(constNameIndex).end();
    }

    @Override
    public int getLength() {
        return 5;
    }
}
