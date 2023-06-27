package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPoolTag;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbsConstantPoolInfo{
    protected final int TAG_LENGTH = 1;
    protected ConstantPoolTag tag;
    protected byte[] value;
    protected int length;

    public byte[] toByteArray(){
        byte[] bytes = new byte[length];
        bytes[0] = tag.getTagNum();
        System.arraycopy(value,0,bytes,1,value.length);
        return bytes;
    }

    protected void setValue(byte[] value){
        if (value == null)
            return;
        this.value = value;
        this.length = value.length + TAG_LENGTH;
    }

    public ConstantPoolTag getType(){
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbsConstantPoolInfo that = (AbsConstantPoolInfo) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, Arrays.hashCode(value));
    }

    public ConstantPoolTag getTag() {
        return tag;
    }

    public byte[] getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }

}
