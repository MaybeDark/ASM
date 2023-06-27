package org.attribute.method.code.stackmaptable.verificationtypeinfo;

public class FloatVariableInfo implements VariableInfo{
    public static final byte tag = 2;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }
}
