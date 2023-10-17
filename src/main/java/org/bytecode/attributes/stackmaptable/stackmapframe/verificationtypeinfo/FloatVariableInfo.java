package org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo;

public class FloatVariableInfo implements VariableInfo {
    public static final byte tag = 2;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
