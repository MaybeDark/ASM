package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.tools.ConvertTool;

public class ConstantPoolStringInfo extends SymbolicReferenceConstantPoolInfo  implements Parameterizable {

    private String literal;

    public ConstantPoolStringInfo(String str) {
        this(str, null);
    }

    public ConstantPoolStringInfo(String str, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_String_info);
        this.literal = str;
        setValue(ref);
    }

    public ConstantPoolStringInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_String_info);
        setValue(ref);
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putStringInfo(literal);
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        if (literal != null) {
            return;
        }
        this.literal = constantPool.getUtf8(ConvertTool.B2S(value[0], value[1]));
    }
}
