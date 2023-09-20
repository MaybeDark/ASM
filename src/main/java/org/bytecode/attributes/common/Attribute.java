package org.bytecode.attributes.common;

import org.bytecode.ByteCodeWriter;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolLoadable;
import org.exception.NotLoadException;

public abstract class Attribute extends ConstantPoolLoadable {
    protected int attributeLength;
    protected String attributeName;
    protected Attribute continuation;
    protected ByteCodeWriter writer;

    public int getAttributeLength() {
        return attributeLength;
    }

    protected void setOrSwap(){
        if (writer.getAttribute(attributeName) == null){
            writer.addAttribute(this);
            continuation = this;
        }else {
            continuation = writer.getAttribute(attributeName);
        }
    }

    public short load(ConstantPool constantPool){
        cpIndex = constantPool.putUtf8Info(attributeName);
        loaded = true;
        return cpIndex;
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void checkLoaded(){
        if (loaded){
            return;
        }
        throw new NotLoadException(attributeName + " need to load before use");
    }

    public abstract byte[] toByteArray();
}
