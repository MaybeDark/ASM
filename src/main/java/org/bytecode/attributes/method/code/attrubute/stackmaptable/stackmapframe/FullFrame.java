package org.bytecode.attributes.method.code.attrubute.stackmaptable.stackmapframe;

import org.bytecode.attributes.method.code.attrubute.stackmaptable.verificationtypeinfo.VariableInfo;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class FullFrame implements StackMapFrame {
    public static final byte frameType = (byte) 255;
    private final short offsetDelta;
    private final short numberOfLocals;
    private final VariableInfo[] localTypeInfo;
    private final short numberOfStack;
    private final VariableInfo[] stackTypeInfo;
    public FullFrame(short offsetDelta, short numberOfLocals, VariableInfo[] localTypeInfo, short numberOfStack, VariableInfo[] stackTypeInfo) {
        this.offsetDelta = offsetDelta;
        this.numberOfLocals = numberOfLocals;
        this.localTypeInfo = localTypeInfo;
        this.numberOfStack = numberOfStack;
        this.stackTypeInfo = stackTypeInfo;
    }

    @Override
    public byte[] toByteArray() {
        byte[] temp = ArrayTool.join(frameType, ConvertTool.S2B(offsetDelta));
        temp = ArrayTool.join(temp,ConvertTool.S2B(numberOfLocals));
        if (numberOfLocals > 0) {
            for (VariableInfo lti : localTypeInfo) {
                temp = ArrayTool.join(temp, lti.toByteArray());
            }
        }
        temp = ArrayTool.join(temp,ConvertTool.S2B(numberOfStack));
        if (numberOfStack > 0){
            for (VariableInfo sti : stackTypeInfo) {
                temp = ArrayTool.join(temp,sti.toByteArray());
            }
        }
        return temp;
    }

    public short getOffsetDelta() {
        return offsetDelta;
    }

    public short getNumberOfLocals() {
        return numberOfLocals;
    }

    public short getNumberOfStack() {
        return numberOfStack;
    }

    public VariableInfo[] getLocalTypeInfo() {
        return localTypeInfo;
    }

    public VariableInfo[] getStackTypeInfo() {
        return stackTypeInfo;
    }
}
