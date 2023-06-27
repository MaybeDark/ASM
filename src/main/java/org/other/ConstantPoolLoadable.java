package org.other;

import org.Loadable;
import org.constantpool.ConstantPool;

public abstract class ConstantPoolLoadable implements Loadable<ConstantPool> {
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
