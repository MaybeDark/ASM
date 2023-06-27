package org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo;

/**
 * 1. 栈中的空元素(ACONST_NULL)
 *
 * @author 12923
 * @date 2023/06/15
 */
public class NullVariableInfo implements VariableInfo{
    public static final byte tag = 5;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }
}
