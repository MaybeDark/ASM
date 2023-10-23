package org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo;

/**
 * 1.已定义但未初始化
 *
 * @author 12923
 * @date 2023/06/15
 */
public class TopVariableInfo implements VariableInfo {
    public static final byte tag = 0;

    @Override
    public byte[] toByteArray() {
        return new byte[]{tag};
    }

    @Override
    public int getLength() {
        return 1;
    }
}
