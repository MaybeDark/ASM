package org.bytecode.attributes;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolUtf8Info;
import org.tools.ByteVector;

/**
 * SourceFile属性用于记录生成这个Class文件的源码文件名称。
 * 这个属性也是可选的，如果不生成这项属性，
 * 当抛出异常时，堆栈中将不会显示出错代码所属的文件名。
 */
public class SourceFile extends FixedLengthAttribute {
    private short sourceCpIndex;
    private String sourceFile;

    public SourceFile() {
        super(Target.class_info);
        attributeLength = 2;
        attributeName = "SourceFile";
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            throw new RuntimeException("SourceFile is null,should setSourceFile");
        }
        loadAttributeName(cp);
        sourceCpIndex = cp.putUtf8Info(sourceFile);
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        sourceFile = ((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral();
        return this;
    }

    @Override
    public boolean isEmpty() {
        return sourceFile == null || sourceFile.isEmpty();
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        cpIndex = 0;
        this.sourceFile = sourceFile;
    }

    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(8);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(sourceCpIndex);
        return result.end();
    }
}
