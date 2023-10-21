package org.bytecode.attributes.annotations.elementvalue;

import org.bytecode.constantpool.ConstantPool;

/**
 * 对于注解参数的抽象
 */
public abstract class ElementValue {
    protected byte tag;

    protected ElementValue(byte tag) {
        this.tag = tag;
    }

    /**
     * 获取当前参数类型对应的标签
     */
    public byte getTag() {
        return tag;
    }

    /**
     * 将参数信息加载的到常量池中
     */
    public abstract short load(ConstantPool constantPool);

    public abstract byte[] toByteArray();

    public abstract int getLength();
}
