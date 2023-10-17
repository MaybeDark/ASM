package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

import java.nio.charset.StandardCharsets;

public class ConstantPoolUtf8Info extends LiteralConstantPoolInfo<String> {

    public ConstantPoolUtf8Info(String str){
        this(str.getBytes(StandardCharsets.UTF_8));
    }

    public ConstantPoolUtf8Info(byte[] literalBytes) {
        super(ConstantPoolTag.CONSTANT_Utf8_info, new String(literalBytes, StandardCharsets.UTF_8), literalBytes);
        setValue(ArrayTool.join(ConvertTool.S2B((short) literalBytes.length), literalBytes));
    }

    @Override
    public String literalToString() {
        return String.format("\"%s\"", literal);
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putUtf8Info(literal);
    }
}
