package org.bytecode.cls.attribute.innerclass;

import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InnerClasses extends Attribute{
    private short innerClassCount;
    ArrayList<InnerClass> pool;
    Map<Integer,Short> hash2Index = new HashMap<>();

    public InnerClasses(){
        attributeLength = 2;
        pool = new ArrayList<>();
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
        pool.add(innerClass);
        return innerClassCount++;
    }

    public boolean isEmpty(){
        return innerClassCount == 0;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("InnerClasses");
        for (int i = 0; i < innerClassCount; i++) {
            InnerClass temp = pool.get(i);
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
            InnerClass temp = pool.get(i);
            System.arraycopy(temp.toByteArray(),0,result,8+(8*i),8);
        }
        return result;
    }
}
