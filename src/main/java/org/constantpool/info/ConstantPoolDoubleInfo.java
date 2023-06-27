package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolDoubleInfo extends LiteralConstantPoolInfo<Double> implements Parameterizable {

    public ConstantPoolDoubleInfo(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Double_info,ConvertTool.B2D(literalBytes),literalBytes);
        if (literalBytes.length != 8)
            throw new RuntimeException("literalBytes need a 8 byte array");
    }

    public ConstantPoolDoubleInfo(double literal){
        super(ConstantPoolTag.CONSTANT_Double_info,literal,ConvertTool.D2B(literal));
    }

}
