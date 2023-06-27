package org.bytecode.cls.attribute;

import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;

/**
 * SourceFile属性用于记录生成这个Class文件的源码文件名称。
 * 这个属性也是可选的，如果不生成这项属性，
 * 当抛出异常时，堆栈中将不会显示出错代码所属的文件名。
 */
public class SourceFile extends Attribute{
    private short sourceCpIndex;
    private String sourceFile;

    public SourceFile(String sourceFile){
        this.sourceFile = sourceFile;
        this.attributeLength = 2;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("SourceFile");
        sourceCpIndex = cp.putUtf8Info(sourceFile);
        loaded = true;
        return sourceCpIndex;
    }

    public byte[] toByteArray(){
        if (!loaded){
            throw new RuntimeException("SourceFile attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(sourceCpIndex),0,result,6,2);
        return result;
    }
}
