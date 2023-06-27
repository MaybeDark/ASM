package org.wrapper;

import org.Loadable;
import org.Type;
import org.constantpool.ConstantPool;
import org.exception.TypeErrorException;

public class LocalVariableWrapper implements Loadable<ConstantPool>{
    private int startPc;
    private int length;
    private int end;
    private final String name;

    private Type type;
    private String desc;
    private int size = 1;
    private short tableIndex = -1;

    /**
     * 初始化
     * 初始化后才显示在属性LocalVariableTable中
     * 但是不初始化仍会占用局部变量表
     */
    private boolean initialized = false;
    private boolean loaded = false;
    public boolean scopeEnd = false;
    private boolean generic = false;


    private short nameCpIndex;
    private short descCpIndex;

//    public LocalVariableWrapper(int startPc, int length, String name,Type type) {
//        this.startPc = startPc;
//        this.length = length;
//        this.end = startPc + length;
//        this.name = name;
//        this.type = type;
//        this.desc = type.getDescriptor();
//        if (type.isLongType() || type.isDoubleType())
//            this.size = 2;
//    }
//
//    public LocalVariableWrapper(int startPc, int length, String name, String desc){
//        this.generic = true;
//        this.startPc = startPc;
//        this.length = length;
//        this.end = startPc + length;
//        this.name = name;
//        this.type = null;
//        this.desc = desc;
//    }

//    public LocalVariableWrapper(String name){
//        this.name = name;
//    }

    public LocalVariableWrapper(String name,Type type){
        this.name = name;
        if (type.isMethodType()){
            throw new TypeErrorException("type cannot be a method type");
        }
        this.type = type;
        this.desc = type.getDescriptor();
        if (type.isDoubleType() || type.isLongType()){
            size = 2;
        }

    }

    public short getTableIndex(){
        return tableIndex;
    }

    public void setTableIndex(short tableIndex){
        this.tableIndex = tableIndex;
    }
    public int getStartPc() {
        return startPc;
    }

    public int getLength() {
        return length;
    }

    public short getNameCpIndex() {
        return nameCpIndex;
    }

    public short getDescCpIndex() {
        return descCpIndex;
    }

    @Override
    public short load(ConstantPool cp) {
       nameCpIndex = cp.putUtf8Info(name);
       descCpIndex = cp.putUtf8Info(desc);
       loaded = true;
       return 0;
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
    public boolean isLoaded() {
        return loaded;
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

    public void setStartPc(int startPc) {
        this.startPc = startPc;
    }

    public void setLength(int length) {
        this.length = length;
        this.end = startPc + length;
    }

    public void setEnd(int end) {
        this.end = end;
        this.length = end - startPc;
    }

    public void setNameCpIndex(short nameCpIndex) {
        this.nameCpIndex = nameCpIndex;
    }

    public void setDescCpIndex(short descCpIndex) {
        this.descCpIndex = descCpIndex;
    }
    public String getName() {
        return this.name;
    }
}
