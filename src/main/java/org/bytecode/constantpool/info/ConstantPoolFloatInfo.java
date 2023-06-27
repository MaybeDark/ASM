package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolFloatInfo extends LiteralConstantPoolInfo<Float> implements Parameterizable {

    public ConstantPoolFloatInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Float_info,ConvertTool.B2F(literalBytes), literalBytes);
        if (literalBytes.length != 4)
            throw new RuntimeException("literal need a 4 byte array");
    }

    public ConstantPoolFloatInfo(float literal) {
        super(ConstantPoolTag.CONSTANT_Float_info,literal,ConvertTool.F2B(literal));
    }

    @Override
    public String literalToString() {
        return super.literalToString()+'F';
    }
}
