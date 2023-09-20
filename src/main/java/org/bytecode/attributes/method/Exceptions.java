package org.bytecode.attributes.method;

import org.Type;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.method.MethodWriter;
import org.tools.ByteVector;

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

    public Exceptions(MethodWriter methodWriter){
        writer = methodWriter;
        attributeLength = 2;
        attributeName = "Exceptions";
        setOrSwap();
    }
    public short addException(Class<? extends Exception> exception){
        return ((Exceptions) continuation).addException0(exception);
    }

    private short addException0(Class<? extends Exception> exception){
        exceptions.add(exception);
        attributeLength += 2;
        return exceptionCount++;
    }

    @Override
    public short load(ConstantPool cp) {
        super.load(cp);
        exceptionsCpIndex = new short[exceptionCount];
        for (int i = 0; i < exceptionCount; i++) {
           exceptionsCpIndex[i] = cp.putClassInfo(Type.getType(exceptions.get(i)).getDescriptor());
        }
        return cpIndex;
    }

    public boolean isEmpty(){
        return exceptionCount == 0;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6 + attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(exceptionCount);
//        byte[] result = new byte[2 + 4 + attributeLength];
//        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
//        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
//        System.arraycopy(ConvertTool.S2B(exceptionCount),0,result,6,2);
        for (int i = 0; i < exceptionCount; i++) {
            result.putShort(exceptionsCpIndex[i]);
//            System.arraycopy(ConvertTool.S2B(exceptionsCpIndex[i]),0,result,8+i*2,2);
        }
        return result.end();
    }
}
