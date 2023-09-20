package org.bytecode.attributes.field;

import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolStringInfo;
import org.bytecode.constantpool.info.LiteralConstantPoolInfo;
import org.bytecode.field.FieldWriter;
import org.tools.ByteVector;

/**
 * 属性为基本类型或者String类型且同时被static final修饰
 * 编译时则会为此属性生成ConstantValue并通过常量池索引为属性赋值
 * 若属性只被static则会通过<clinit>方法为属性赋值
 */
public class ConstantValue extends Attribute {
    private AbsConstantPoolInfo value;
    private short valueCpIndex;

    public ConstantValue(FieldWriter fieldWriter,LiteralConstantPoolInfo value){
        init(fieldWriter,value);
    }

    public ConstantValue(FieldWriter fieldWriter,ConstantPoolStringInfo value){
        init(fieldWriter,value);
    }

    public void init(FieldWriter fieldWriter,AbsConstantPoolInfo value){
        attributeName = "ConstantValue";
        attributeLength = 2;
        this.value =value;
        setOrSwap();
    }

    @Override
    public short load(ConstantPool cp) {
        super.load(cp);
        valueCpIndex = cp.resolveConstantPoolInfo(value);
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(8);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(valueCpIndex);
//        byte[] result = new byte[2 + 4 + 2];
//        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
//        System.arraycopy(ConvertTool.I2B(2),0,result,2,4);
//        System.arraycopy(ConvertTool.S2B(valueCpIndex),0,result,6,2);
        return result.end();
    }

    public short getValueCpIndex() {
        return valueCpIndex;
    }
}
