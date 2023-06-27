package org.bytecode.method.attribute.code.attrubute.stackmaptable.stackmapframe;

import org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo.VariableInfo;

public class InitFrame extends FullFrame{
    public InitFrame(short numberOfLocals, VariableInfo[] localTypeInfo) {
        super((short) 0,numberOfLocals,localTypeInfo,(short) 0,null);
    }
}
