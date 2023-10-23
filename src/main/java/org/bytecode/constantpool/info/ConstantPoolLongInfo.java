package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolLongInfo extends LiteralConstantPoolInfo<Long> implements Parameterizable {

    public ConstantPoolLongInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Long_info, ConvertTool.B2L(literalBytes),literalBytes);
        if (literalBytes.length != 8)
            throw new RuntimeException("literal need a 8 byte array");
    }

    public ConstantPoolLongInfo(Long literal) {
        super(ConstantPoolTag.CONSTANT_Long_info, literal, ConvertTool.L2B(literal));
    }

    @Override
    public String literalToString() {
        return super.literalToString() + 'L';
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putLongInfo(literal);
    }
}
