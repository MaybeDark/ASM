package org.bytecode.attributes.common;

import org.bytecode.ByteCodeWriter;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

/**
 * Deprecated用于表示某个类或者方法字段已经被认定为不推荐使用，
 * 它可以通过在代码中使用 @deprecated 注释进行设置
 */
public class Deprecated extends Attribute {
    public Deprecated(ByteCodeWriter writer){
        this.writer = writer;
        attributeLength = 0;
        attributeName = "Deprecated";
        setOrSwap();
    }


    @Override
    public short load(ConstantPool cp) {
        super.load(cp);
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6);
        result.putShort(cpIndex).putInt(attributeLength);
        return result.end();
    }
}
