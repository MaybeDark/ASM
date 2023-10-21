package org.bytecode.attributes.linenumbertable;

import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.ArrayList;


/**
 * LineNumberTable属性用于描述java代码行号与字节码行号（字节码的偏移量）之间的对应关系。
 * 它不是运行时必须的属性。如果没有这项属性，不会对程序的运行产生任何影响，
 * 但是当程序抛出异常时，堆栈中没有出错的行号，而且在调试程序时，也无法按照源码行来调试断点。
 */
public class LineNumberTable extends Attribute {
    short tableLength = 0;
    ArrayList<LineNumberInfo> table = new ArrayList<>();
    public LineNumberTable(){
        super(Target.code_info);
        attributeName = "LineNumberTable";
        attributeLength = 2;
    }

    @Override
    public short load(ConstantPool cp) {
        return loadAttributeName(cp);
    }

    public LineNumberTable put(int startPc, int lineNumber) {
        tableLength++;
        table.add(new LineNumberInfo(startPc, lineNumber));
        attributeLength += 4;
        return this;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        short count = byteVector.getShort();
        for (int i = 0; i < count; i++) {
            put(byteVector.getShort(), byteVector.getShort());
        }
        return this;
    }

    public boolean isEmpty() {
        return tableLength == 0;
    }

    @Override
    public byte[] toByteArray() {
        ByteVector result = new ByteVector(8 + 4 * tableLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(tableLength);
        for (int i = 0; i < tableLength; i++) {
            LineNumberInfo temp = table.get(i);
            result.putShort(temp.getStartPc())
                    .putShort(temp.getLineNumber());
        }
        return result.end();
    }

}
