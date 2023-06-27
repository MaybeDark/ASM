package org.attribute.cls;

import org.attribute.Attribute;
import org.constantpool.ConstantPool;
import org.tools.ConvertTool;

import java.util.HashMap;
import java.util.Map;

public class InnerClasses extends Attribute{
    private short innerClassCount;
    InnerClass[] pool = new InnerClass[8];
    Map<Integer,Short> hash2Index = new HashMap<>();

    public InnerClasses(){
        attributeLength = 2;
    }

    public short addInnerClass(InnerClass innerClass){
        if (innerClassCount >= 8){
            throw new RuntimeException("Cannot accommodate more innerClass");
        }
        Short value  = hash2Index.putIfAbsent(innerClass.hashCode(),innerClassCount);
        if (value != null){
            return value;
        }
        attributeLength += 8;
        pool[innerClassCount] = innerClass;
        return innerClassCount++;
    }

    public boolean isEmpty(){
        return innerClassCount == 0;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("InnerClasses");
        for (int i = 0; i < innerClassCount; i++) {
            InnerClass temp = pool[i];
            temp.load(cp);
        }
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("innerClasses attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(innerClassCount),0,result,6,2);
        for (int i = 0; i < innerClassCount; i++) {
            InnerClass temp = pool[i];
            System.arraycopy(temp.toByteArray(),0,result,8+(8*i),8);
        }
        return result;
    }
}
