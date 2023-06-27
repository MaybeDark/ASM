package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;

public class ConstantPoolStringInfo extends SymbolicReferenceConstantPoolInfo  implements Parameterizable {

    private final String literal;

    public ConstantPoolStringInfo(String str){
        this(str,null);
    }

    public ConstantPoolStringInfo(String str,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_String_info);
        this.literal = str;
        setValue(ref);
    }

    public String getLiteral() {
        return literal;
    }
}
