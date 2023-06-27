package org.attribute.cls;

import org.attribute.Attribute;
import org.constantpool.ConstantPool;
import org.other.Pool;
import org.tools.ConvertTool;

import java.util.HashMap;
import java.util.Map;

public class BootstrapMethods extends Attribute implements Pool {

    public final static short defaultSize = 8;
    public static short expansionFactor = 2;
    public short currentSize = defaultSize;
    private short bootStrapMethodCount = 0;
    BootstrapMethod[] pool = new BootstrapMethod[defaultSize];
    Map<Integer,Short> hash2Index = new HashMap<>();

    public BootstrapMethods(){
        attributeLength = 2;
    }

    public short putBootStrapMethod(BootstrapMethod bootStrapMethod){
        considerExpansion();
        Short value = hash2Index.putIfAbsent(bootStrapMethod.hashCode(), bootStrapMethodCount);
        if (value != null){
            return value;
        }
        pool[bootStrapMethodCount] = bootStrapMethod;
        attributeLength += 4 + bootStrapMethod.getArgsCount()*2;
        return bootStrapMethodCount++;
    }

    public boolean isEmpty(){
        return bootStrapMethodCount == 0;
    }

    private void considerExpansion() {
        if(bootStrapMethodCount <= currentSize)
            return;
        if (currentSize*expansionFactor >= (1<<16))
            throw new RuntimeException("pool too large");
        BootstrapMethod[] newPool = new BootstrapMethod[currentSize * expansionFactor];
        System.arraycopy(pool ,0,newPool,0,currentSize);
        currentSize *= expansionFactor;
        pool = newPool;
    }


    @Override
    public short load(ConstantPool constantPool) {
        if (loaded){
            return cpIndex;
        }
        cpIndex = constantPool.putUtf8Info("BootstrapMethods");
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod temp = pool[i];
            temp.load(constantPool);
        }
        loaded = true;
        return cpIndex;
    }

    public byte[] toByteArray(){
        if (!loaded){
            throw new RuntimeException("bootstrapMethods attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(bootStrapMethodCount),0,result,6,2);

        int index = 0;
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool[i];
            int length = bootstrapMethod.getLength();
            System.arraycopy(bootstrapMethod.getValue(),0,result,8 + index,length);
            index += length;
        }
        return result;
    }

    public BootstrapMethod[] getPool(){
        BootstrapMethod[] result = new BootstrapMethod[bootStrapMethodCount];
        System.arraycopy(pool,0,result,0,bootStrapMethodCount);
        return result;
    }
}
