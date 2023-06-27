package org.attribute.field;

import org.attribute.Attribute;
import org.constantpool.info.AbsConstantPoolInfo;
import org.constantpool.ConstantPool;
import org.constantpool.info.ConstantPoolStringInfo;
import org.constantpool.info.LiteralConstantPoolInfo;
import org.tools.ConvertTool;

/**
 * 属性为基本类型或者String类型且同时被static final修饰
 * 编译时则会为此属性生成ConstantValue并通过常量池索引为属性赋值
 * 若属性只被static则会通过<clinit>方法为属性赋值
 */
public class ConstantValue extends Attribute {
    private AbsConstantPoolInfo value;
    private short valueCpIndex;

    public ConstantValue(LiteralConstantPoolInfo value){
        attributeLength = 2;
        this.value = value;
    }

    public ConstantValue(ConstantPoolStringInfo value){
        attributeLength = 2;
        this.value = value;
    }


    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("ConstantValue");
        valueCpIndex = cp.resolveConstantPoolInfo(value);
        loaded = true;
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("ConstantValue attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(valueCpIndex),0,result,6,2);
        return result;
    }

    public short getValueCpIndex() {
        return valueCpIndex;
    }
}
