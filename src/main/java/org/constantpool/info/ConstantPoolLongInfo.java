package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolLongInfo extends LiteralConstantPoolInfo<Long> implements Parameterizable {

    public ConstantPoolLongInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Long_info, ConvertTool.B2L(literalBytes),literalBytes);
        if (literalBytes.length != 8)
            throw new RuntimeException("literal need a 8 byte array");
    }

    public ConstantPoolLongInfo(Long literal) {
        super(ConstantPoolTag.CONSTANT_Long_info, literal,ConvertTool.L2B(literal));
    }

    @Override
    public String literalToString() {
        return super.literalToString()+'L';
    }
}
