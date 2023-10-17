package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;

public class InitFrame extends FullFrame{
    public InitFrame(short numberOfLocals, VariableInfo[] localTypeInfo) {
        super((short) 0,numberOfLocals,localTypeInfo,(short) 0,null);
    }
}
