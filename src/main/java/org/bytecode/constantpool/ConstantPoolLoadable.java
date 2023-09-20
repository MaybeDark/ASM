package org.bytecode.constantpool;

import org.Loadable;
import org.bytecode.constantpool.ConstantPool;

public abstract class ConstantPoolLoadable{
    protected boolean loaded = false;
    protected short cpIndex = -1;
    public abstract short load(ConstantPool cp);
    public short getCpIndex(){
        return cpIndex;
    }
    public boolean isLoaded(){
        return loaded;
    }
}
