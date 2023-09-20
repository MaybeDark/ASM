package org.bytecode.attributes.method.code.attrubute.stackmaptable.verificationtypeinfo;

public class LongVariableInfo implements VariableInfo {
    public static final byte tag = 4;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }
}
