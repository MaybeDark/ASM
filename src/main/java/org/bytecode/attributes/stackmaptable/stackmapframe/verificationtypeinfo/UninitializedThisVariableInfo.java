package org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo;

public class UninitializedThisVariableInfo implements VariableInfo {
    public static final byte tag = 6;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
