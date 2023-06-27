package org.attribute.com;

import org.attribute.Attribute;
import org.constantpool.ConstantPool;
import org.tools.ConvertTool;

/**
 * Deprecated用于表示某个类或者方法字段已经被认定为不推荐使用，
 * 它可以通过在代码中使用 @deprecated 注释进行设置
 */
public class Deprecated extends Attribute {
    public Deprecated(){
        attributeLength = 0;
    }


    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Deprecated");
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("Deprecated attribute need load before use");
        }
        byte[] result = new byte[6];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        return result;
    }
}
