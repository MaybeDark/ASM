package org.bytecode.method.attribute;

import org.Type;
import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;

import java.util.List;
import java.util.ArrayList;

/**
 * 这里的Exception属性是在方法表中与Code属性平级的一项属性，表示方法描述时，
 * 在throws关键字后面列举的异常
 */
public class Exceptions extends Attribute{
    private short exceptionCount;
    List<Class<? extends Exception>> exceptions = new ArrayList<>();
    private short[] exceptionsCpIndex;

    public Exceptions(){
        attributeLength = 2;
    }

    public short addException(Class<? extends Exception> exception){
        exceptions.add(exception);
        attributeLength += 2;
        return exceptionCount++;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Exceptions");
        exceptionsCpIndex = new short[exceptionCount];
        for (int i = 0; i < exceptionCount; i++) {
           exceptionsCpIndex[i] = cp.putClassInfo(Type.getType(exceptions.get(i)).getDescriptor());
        }
        loaded = true;
        return cpIndex;
    }

    public boolean isEmpty(){
        return exceptionCount == 0;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("Exceptions attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(exceptionCount),0,result,6,2);
        for (int i = 0; i < exceptionCount; i++) {
            System.arraycopy(ConvertTool.S2B(exceptionsCpIndex[i]),0,result,8+i*2,2);
        }
        return result;
    }
}
