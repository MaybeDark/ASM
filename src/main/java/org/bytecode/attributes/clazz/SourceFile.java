package org.bytecode.attributes.clazz;

import org.bytecode.ClassWriter;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

/**
 * SourceFile属性用于记录生成这个Class文件的源码文件名称。
 * 这个属性也是可选的，如果不生成这项属性，
 * 当抛出异常时，堆栈中将不会显示出错代码所属的文件名。
 */
public class SourceFile extends Attribute{
    private short sourceCpIndex;
    private String sourceFile;

    public SourceFile(ClassWriter classWriter,String sourceFile){
        ((SourceFile)continuation).sourceFile = sourceFile;
        attributeName = "SourceFile";
        this.attributeLength = 2;
        setOrSwap();
    }

    @Override
    public short load(ConstantPool cp) {
        super.load(cp);
        sourceCpIndex = cp.putUtf8Info(sourceFile);
        return cpIndex;
    }

    public byte[] toByteArray(){
        checkLoaded();
        ByteVector result = new ByteVector(8);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(sourceCpIndex);
        return result.end();
    }
}
