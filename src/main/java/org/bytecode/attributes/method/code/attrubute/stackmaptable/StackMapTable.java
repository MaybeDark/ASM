package org.bytecode.attributes.method.code.attrubute.stackmaptable;

import org.bytecode.attributes.common.Attribute;
import org.bytecode.attributes.method.code.attrubute.stackmaptable.stackmapframe.StackMapFrame;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

import java.util.ArrayList;
import java.util.List;


public class StackMapTable extends Attribute {
    short entriesCount;
    List<StackMapFrame> stackMapFrames = new ArrayList<>();
    public StackMapTable(){
        attributeLength = 2;
    }

    public StackMapTable addStackMapFrame(StackMapFrame stackMapFrame){
        stackMapFrames.add(stackMapFrame);
        attributeLength += stackMapFrame.toByteArray().length;
        entriesCount++;
        return this;
    }

    public boolean isEmpty(){
        return entriesCount == 0;
    }
    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("StackMapTable");
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded) {
            throw new RuntimeException("StackMapTable attribute need load before use");
        }

        byte[] result = new byte[8];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(entriesCount),0,result,6,2);
        for (StackMapFrame stackMapFrame : stackMapFrames) {
            result = ArrayTool.join(result,stackMapFrame.toByteArray());
        }
        return result;
    }



}