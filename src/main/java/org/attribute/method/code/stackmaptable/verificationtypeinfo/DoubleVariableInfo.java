package org.attribute.method.code.stackmaptable.verificationtypeinfo;

public class DoubleVariableInfo implements VariableInfo{
    public static final byte tag = 3;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }
}
