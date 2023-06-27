package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.constantpool.Parameterizable;

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
