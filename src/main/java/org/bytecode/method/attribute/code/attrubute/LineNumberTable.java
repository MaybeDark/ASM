package org.bytecode.method.attribute.code.attrubute;

import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;

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
        attributeLength = 2;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("LineNumberTable");
        loaded = true;
        return cpIndex;
    }

    public short add(LineNumberInfo lineNumberInfo){
        tableLength++;
        table.add(lineNumberInfo);
        attributeLength += 4;
        return tableLength;
    }

    public boolean isEmpty(){
        return tableLength == 0;
    }

    @Override
    public byte[] toByteArray() {
        byte[] result = new byte[2 + 4 + 2 + 4 * tableLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,2);
        System.arraycopy(ConvertTool.S2B(tableLength),0,result,6,2);
        for (int i = 0; i < tableLength; i++) {
            LineNumberInfo temp = table.get(i);
            System.arraycopy(ConvertTool.S2B(temp.getStartPc()),0,result,8+i*4,2);
            System.arraycopy(ConvertTool.S2B(temp.getLineNumber()),0,result,10+i*4,2);
        }
        return result;
    }

}
class LineNumberInfo {
    private short startPc;
    private short lineNumber;

    public LineNumberInfo(short startPc, short lineNumber) {
        this.startPc = startPc;
        this.lineNumber = lineNumber;
    }

    public short getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(short lineNumber) {
        this.lineNumber = lineNumber;
    }

    public short getStartPc() {
        return startPc;
    }

    public void setStartPc(short startPc) {
        this.startPc = startPc;
    }
}