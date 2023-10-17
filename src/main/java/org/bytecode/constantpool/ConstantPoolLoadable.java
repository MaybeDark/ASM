package org.bytecode.constantpool;

public abstract class ConstantPoolLoadable{
    protected short cpIndex = 0;
    public abstract short load(ConstantPool cp);
    public short getCpIndex(){
        return cpIndex;
    }
    public boolean isLoaded(){
        return cpIndex != 0;
    }
}
