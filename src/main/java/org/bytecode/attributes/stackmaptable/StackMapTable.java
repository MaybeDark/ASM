package org.bytecode.attributes.stackmaptable;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.VariableLengthAttribute;
import org.bytecode.attributes.stackmaptable.stackmapframe.StackMapFrame;
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
        return null;
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
                .putByte(attributeLength)
                .putShort(entriesCount);
        for (StackMapFrame stackMapFrame : stackMapFrames) {
            byteVectors.putArray(stackMapFrame.toByteArray());
        }
        return byteVectors.toByteArray();
    }
}