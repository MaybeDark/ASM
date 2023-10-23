package org.bytecode.attributes.stackmaptable;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.attributes.stackmaptable.stackmapframe.*;
import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.*;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;
import org.tools.ByteVectors;

import java.util.ArrayList;
import java.util.List;


public class StackMapTable extends VariableLengthAttribute {
    short entriesCount;
    List<StackMapFrame> stackMapFrames = new ArrayList<>();

    public StackMapTable() {
        super(Target.code_info);
        attributeLength = 2;
        attributeName = "StackMapTable";
    }

    public StackMapTable addStackMapFrame(StackMapFrame stackMapFrame) {
        stackMapFrames.add(stackMapFrame);
        attributeLength += stackMapFrame.getLength();
        entriesCount++;
        return this;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short count = byteVector.getShort();
        for (int i = 0; i < count; i++) {
            int tag = byteVector.getByte() & 0xff;
            if (tag <= 63) {
                addStackMapFrame(new SameFrame((byte) tag));
            } else if (tag <= 127) {
                addStackMapFrame(new SameLocals1StackItemFrame((byte) (tag - 64), visitVariableInfo(byteVector)));
            } else if (tag == 247) {
                addStackMapFrame(new SameLocals1StackItemFrameExtended(byteVector.getShort(), visitVariableInfo(byteVector)));
            } else if (tag <= 250) {
                addStackMapFrame(new ChopFrame((byte) (251 - tag), byteVector.getShort()));
            } else if (tag == 251) {
                addStackMapFrame(new SameFrameExtended(byteVector.getShort()));
            } else if (tag <= 254) {
                addStackMapFrame(new AppendFrame((byte) (tag - 251), byteVector.getShort(), visitVariableInfos(tag - 251, byteVector)));
            } else {
                addStackMapFrame(new FullFrame(byteVector.getShort()
                        , visitVariableInfos(byteVector.getShort(), byteVector),
                        visitVariableInfos(byteVector.getShort(), byteVector)));
            }
        }
        return this;
    }

    private VariableInfo[] visitVariableInfos(int count, ByteVector byteVector) {
        VariableInfo[] variableInfos = new VariableInfo[count];
        for (int i = 0; i < count; i++) {
            variableInfos[i] = visitVariableInfo(byteVector);
        }
        return variableInfos;
    }

    private VariableInfo visitVariableInfo(ByteVector byteVector) {
        byte tag = byteVector.getByte();
        switch (tag) {
            case 0:
                return new TopVariableInfo();
            case 1:
                return new IntegerVariableInfo();
            case 2:
                return new FloatVariableInfo();
            case 3:
                return new LongVariableInfo();
            case 4:
                return new DoubleVariableInfo();
            case 5:
                return new NullVariableInfo();
            case 6:
                return new UninitializedThisVariableInfo();
            case 7:
                return new ObjectVariableInfo(byteVector.getShort());
            case 8:
                return new UninitializedVariableInfo(byteVector.getByte());
            default:
                throw new IllegalArgumentException("Unknown variable type tag: " + tag);
        }
    }

    public boolean isEmpty() {
        return entriesCount == 0;
    }

    @Override
    public short load(ConstantPool cp) {
        loadAttributeName(cp);
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(entriesCount);
        for (StackMapFrame stackMapFrame : stackMapFrames) {
            byteVectors.putArray(stackMapFrame.toByteArray());
        }
        return byteVectors.toByteArray();
    }
}