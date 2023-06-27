package org.attribute.method;

import org.attribute.Attribute;
import org.constantpool.ConstantPool;
import org.tools.ConvertTool;

/**
 * 这里的Exception属性是在方法表中与Code属性平级的一项属性，表示方法描述时，
 * 在throws关键字后面列举的异常
 */
public class Exceptions extends Attribute{
    private short exceptionCount;
    private Class<? extends Exception>[] exceptions = new Class[8];
    private short[] exceptionsCpIndex = new short[8];

    public Exceptions(){
        attributeLength = 2;
    }

    public short addException(Class<? extends Exception> exception ){
        if (exceptionCount >= 8){
            throw new RuntimeException("too much exception");
        }
        exceptions[exceptionCount] = exception;
        attributeLength += 2;
        return exceptionCount++;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Exceptions");
        for (int i = 0; i < exceptionCount; i++) {
            String className = exceptions[i].getName();
            className = className.replace(".","/");
            exceptionsCpIndex[i] = cp.putClassInfo(className);
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
