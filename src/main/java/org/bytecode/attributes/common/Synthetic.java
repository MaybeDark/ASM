package org.bytecode.attributes.common;

import org.bytecode.ByteCodeWriter;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

public class Synthetic extends Attribute {

    public Synthetic(ByteCodeWriter writer){
        this.writer = writer;
        attributeLength = 0;
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
