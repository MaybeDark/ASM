package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolIntegerInfo extends LiteralConstantPoolInfo<Integer> implements Parameterizable {

    public ConstantPoolIntegerInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Integer_info, ConvertTool.B2I(literalBytes), literalBytes);
        if (literalBytes.length != 4) {
            throw new RuntimeException("literal need a 4 byte array");
        }
    }

    public ConstantPoolIntegerInfo(Integer literal) {
        super(ConstantPoolTag.CONSTANT_Integer_info, literal, ConvertTool.I2B(literal));
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putIntegerInfo(literal);
    }
}
