package org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo;

public class IntegerVariableInfo implements VariableInfo{
    public static final byte tag = 1;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }
}