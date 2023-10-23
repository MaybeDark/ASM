package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ByteVector;

import java.util.ArrayList;

/**
 * 目标为方法中的局部变量
 * void method(){
 * <p>@TypeAnnotation</p>
 * Type local;
 * }
 */
public class LocalVarTarget extends TargetInfo {
    /**
     * 内置记录表的条数
     */
    private short tableLength;
    private ArrayList<Table> tables = new ArrayList<>();

    public LocalVarTarget(short tableLength) {
        this.tableLength = tableLength;
    }

    public LocalVarTarget putTable(short startPc, short length, short index) {
        tables.add(new Table(startPc, length, index));
        tableLength++;
        return this;
    }

    public LocalVarTarget visit(ByteVector byteVector) {
        for (int i = 0; i < tableLength; i++) {
            tables.add(new Table(byteVector.getShort(), byteVector.getShort(), byteVector.getShort()));
        }
        return this;
    }

    @Override
    public byte[] toByteArray() {
        ByteVector byteVector = new ByteVector(getLength());
        byteVector.putShort(tableLength);
        Table table;
        for (int i = 0; i < tableLength; i++) {
            table = tables.get(i);
            byteVector.putShort(table.startPc);
            byteVector.putShort(table.length);
            byteVector.putShort(table.index);
        }
        return byteVector.end();
    }

    @Override
    public int getLength() {
        return 2 + tableLength * 6;
    }

    class Table {
        short startPc;
        short length;
        short index;

        public Table(short startPc, short length, short index) {
            this.startPc = startPc;
            this.length = length;
            this.index = index;
        }
    }

}
