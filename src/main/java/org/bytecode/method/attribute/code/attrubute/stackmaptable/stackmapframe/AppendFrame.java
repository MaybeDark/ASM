package org.bytecode.method.attribute.code.attrubute.stackmaptable.stackmapframe;

import org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo.VariableInfo;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class AppendFrame implements StackMapFrame {
    private final byte operand;
    private final short offsetDelta;
    private final VariableInfo[] localTypeInfo;

    public AppendFrame(byte operand, short offsetDelta, VariableInfo[] localTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.operand = operand;
        this.localTypeInfo = localTypeInfo;
    }

    @Override
    public byte[] toByteArray() {
        byte[] temp = ArrayTool.join((byte) (operand + 251), ConvertTool.S2B(offsetDelta));
        if (offsetDelta > 0){
            for (VariableInfo variableInfo : localTypeInfo) {
                temp = ArrayTool.join(temp,variableInfo.toByteArray());
            }
        }
        return temp;
    }
}
