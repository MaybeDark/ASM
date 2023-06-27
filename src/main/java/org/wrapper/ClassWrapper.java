package org.wrapper;


import org.Loadable;
import org.Type;
import org.constantpool.ConstantPool;

public class ClassWrapper implements Loadable<ConstantPool> {
    private final String fullClassName;
    private boolean loaded = false;

    public ClassWrapper(String packageName,String className){
        this.fullClassName = packageName + "/" + className;
    }

    public ClassWrapper(Class<?> clazz){
        this.fullClassName = Type.getClassDescriptor(clazz);
    }

    public ClassWrapper(Type type){
        this.fullClassName = type.getDescriptor();
    }

    public short load(ConstantPool constantPool){
        loaded = true;
        return constantPool.putClassInfo(fullClassName);
    }

}
