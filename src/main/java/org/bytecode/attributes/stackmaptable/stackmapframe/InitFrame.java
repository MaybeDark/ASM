package org.bytecode.attributes.stackmaptable.stackmapframe;

import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.VariableInfo;

public class InitFrame extends FullFrame {
    public InitFrame(VariableInfo[] localTypeInfo) {
        super((short) 0, localTypeInfo, null);
    }
}
