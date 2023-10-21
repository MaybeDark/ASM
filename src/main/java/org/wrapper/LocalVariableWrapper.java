package org.wrapper;

import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.exception.TypeErrorException;

public class LocalVariableWrapper {
    private final boolean generic = false;
    public boolean scopeEnd = false;
    private int startPc;
    private int length;
    private int end;
    private Type type;
    private String desc;
    private int size = 1;
    private String name;

    /**
     * 初始化
     * 初始化后才显示在属性LocalVariableTable中
     * 但是不初始化仍会占用局部变量表
     */
    private boolean initialized = false;
    private short tableIndex = - 1;


    private short nameCpIndex;
    private short descCpIndex;

    public LocalVariableWrapper(String name, Type type) {
        this.name = name;
        if (type.isMethodType()) {
            throw new TypeErrorException("type cannot be a method type");
        }
        this.type = type;
        this.desc = type.getDescriptor();
        if (type.isDoubleType() || type.isLongType()) {
            size = 2;
        }
    }

    public LocalVariableWrapper(String name, String desc) {
        this(name, Type.getType(desc));
    }

    private LocalVariableWrapper() {
    }

    /**
     * 用于visit
     */
    public static LocalVariableWrapper getEmptyWrapper() {
        return new LocalVariableWrapper();
    }

    public short getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(short tableIndex) {
        this.tableIndex = tableIndex;
    }

    public int getStartPc() {
        return startPc;
    }

    public void setStartPc(int startPc) {
        this.startPc = startPc;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        this.end = startPc + length;
    }

    public short getNameCpIndex() {
        return nameCpIndex;
    }

    public void setNameCpIndex(short nameCpIndex) {
        this.nameCpIndex = nameCpIndex;
    }

    public short getDescCpIndex() {
        return descCpIndex;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        this.descCpIndex = 0;
    }

    public void setDescCpIndex(short descCpIndex) {
        this.descCpIndex = descCpIndex;
    }

    public boolean isLoaded() {
        return nameCpIndex != 0 && descCpIndex != 0;
    }

    public short load(ConstantPool cp) {
        if (isLoaded()) {
            return nameCpIndex;
        }
        nameCpIndex = cp.putUtf8Info(name);
        descCpIndex = cp.putUtf8Info(desc);
        return nameCpIndex;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        this.desc = type.getDescriptor();
        if (type.isLongType() || type.isDoubleType())
            this.size = 2;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isGeneric() {
        return generic;
    }

    public int getSize() {
        return size;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
        this.length = end - startPc;
    }

    public void setName(String name) {
        this.name = name;
        nameCpIndex = 0;
    }

    public String getName() {
        return this.name;
    }

    public void ldc(ConstantPool constantPool) {
        name = constantPool.getUtf8(nameCpIndex);
        desc = constantPool.getUtf8(descCpIndex);
    }
}
