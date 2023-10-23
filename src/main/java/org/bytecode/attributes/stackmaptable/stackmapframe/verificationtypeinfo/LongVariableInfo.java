package org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo;

public class LongVariableInfo implements VariableInfo {
    public static final byte tag = 4;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
