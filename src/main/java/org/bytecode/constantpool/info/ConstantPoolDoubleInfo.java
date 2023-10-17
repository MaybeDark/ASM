package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolDoubleInfo extends LiteralConstantPoolInfo<Double> implements Parameterizable {

    public ConstantPoolDoubleInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Double_info, ConvertTool.B2D(literalBytes), literalBytes);
        if (literalBytes.length != 8)
            throw new RuntimeException("literalBytes need a 8 byte array");
    }

    public ConstantPoolDoubleInfo(double literal) {
        super(ConstantPoolTag.CONSTANT_Double_info, literal, ConvertTool.D2B(literal));
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putDoubleInfo(literal);
    }
}
