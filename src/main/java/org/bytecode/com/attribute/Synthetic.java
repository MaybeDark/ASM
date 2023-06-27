package org.bytecode.com.attribute;

import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;

public class Synthetic extends Attribute {

    public Synthetic(){
        attributeLength = 0;
    }
    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("Synthetic");
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {

        byte[] result = new byte[6];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        return result;
    }
}
