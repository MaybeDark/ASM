package org.bytecode.attributes.clazz.innerclass;

import org.bytecode.ClassWriter;
import org.bytecode.attributes.clazz.ClassAttribute;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InnerClasses extends ClassAttribute {
    private short innerClassCount;
    ArrayList<InnerClass> pool;
    Map<Integer,Short> hash2Index = new HashMap<>();

    public InnerClasses(ClassWriter classWriter){
        super(classWriter);
        attributeLength = 2;
        attributeName = "InnerClass";
        pool = new ArrayList<>();
        setOrSwap();
    }
    public short addInnerClass(InnerClass innerClass){
        return ((InnerClasses) continuation).addInnerClass0(innerClass);
    }

    private short addInnerClass0(InnerClass innerClass){
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
        super.load(cp);
        for (int i = 0; i < innerClassCount; i++) {
            InnerClass temp = pool.get(i);
            temp.load(cp);
        }
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6+attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(innerClassCount);
//        byte[] result = new byte[2 + 4 + attributeLength];
//        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
//        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
//        System.arraycopy(ConvertTool.S2B(innerClassCount),0,result,6,2);
        for (int i = 0; i < innerClassCount; i++) {
            InnerClass temp = pool.get(i);
            result.putArray(temp.toByteArray());
//            System.arraycopy(temp.toByteArray(),0,result,8+(8*i),8);
        }
        return result.end();
    }
}
