package org.bytecode.cls;

import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ConvertTool;
import org.wrapper.ClassWrapper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClassWriter {
    private static final ClassWrapper baseClass = new ClassWrapper(Object.class);
    private Set<ClassWrapper> interfaces = new HashSet<>();
    private short interfaceCount = 0;
    private ClassWrapper superClass = baseClass;
    private int access;
    private String fullClassName;
    private boolean loaded = false;
    private short thisClassCpIndex;

    public ClassWriter(int access,String fullClassName){
        this.access = access;
        this.fullClassName = fullClassName;
    }

    public ClassWriter addInterfaces(ClassWrapper interfaceWrapper){
        interfaces.add(interfaceWrapper);
        interfaceCount ++;
        return this;
    }



    public ClassWrapper getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassWrapper superClass) {
        this.superClass = superClass;
    }

    public short load(ConstantPool constantPool){
        this.thisClassCpIndex = constantPool.putClassInfo(fullClassName);
        superClass.load(constantPool);
        interfaces.forEach((interfaceWrapper)->interfaceWrapper.load(constantPool));
        return this.thisClassCpIndex;
    }

    public byte[] toByteArray(){
        byte[] result = new byte[8 + interfaces.size()*2];
        System.arraycopy(ConvertTool.S2B((short) access),0,result,0,2);
        System.arraycopy(ConvertTool.S2B(thisClassCpIndex),0,result,2,2);
        System.arraycopy(ConvertTool.S2B(superClass.getCpIndex()),0,result,4,2);
        System.arraycopy(ConvertTool.S2B(interfaceCount),0,result,6,2);
        if (interfaceCount != 0){
            Iterator<ClassWrapper> iterator = interfaces.iterator();
            for (int i = 0; i < interfaceCount; i++) {
                System.arraycopy(ConvertTool.S2B(iterator.next().getCpIndex()),0,result,8+2*i,2);
            }
        }
        return result;
    }

    public boolean isLoaded() {
        return loaded;
    }

}
