package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.constantpool.Parameterizable;

public class ConstantPoolClassInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {

    private final String fullClassName;

    public ConstantPoolClassInfo(String fullClassName,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.fullClassName = fullClassName;
        setValue(ref);
    }

    public ConstantPoolClassInfo(String fullClassName){
        super(ConstantPoolTag.CONSTANT_Class_info);
        this.fullClassName = fullClassName;
    }


    public String getFullClassName() {
        return fullClassName;
    }
}
