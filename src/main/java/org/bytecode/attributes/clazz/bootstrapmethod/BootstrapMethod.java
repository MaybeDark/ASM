package org.bytecode.attributes.clazz.bootstrapmethod;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.bytecode.constantpool.Parameterizable;
import org.bytecode.constantpool.ReferenceKind;
import org.tools.ByteVector;

import java.util.*;

public class BootstrapMethod{
    private final ReferenceKind referenceKind;
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;
    private final int argsCount;
    private final Parameterizable[] args;
    private byte[] value;
    private final int length;
    private boolean loaded = false;
    BootstrapMethod(ReferenceKind referenceKind, String fullClassName, String methodName, String methodDesc, Parameterizable... args){
        this.referenceKind = referenceKind;
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.args = args;
        if (args != null && args.length != 0){
            argsCount = args.length;
            length = 4 + argsCount * 2;
        }else {
            argsCount = 0;
            length = 4;
        }
    }

    BootstrapMethod(ConstantPoolMethodHandleInfo cpmhif, Parameterizable... args){
        this(cpmhif.getKind(),cpmhif.getFullClassName(), cpmhif.getName(), cpmhif.getDesc(),args);
    }

    byte[] toByteArray(){
        return value;
    }

    public void load(ConstantPool constantPool) {
        if (loaded){ return; }
        ByteVector value = new ByteVector(length);
        value.putShort(constantPool.putMethodHandleInfo(referenceKind, fullClassName, methodName, methodDesc));
        value.putShort(argsCount);
        for (int i = 0; i < argsCount; i++) {
            short infoIndex = constantPool.resolveConstantPoolInfo((AbsConstantPoolInfo) args[i]);
            value.putShort(infoIndex);
        }
        loaded = true;
        this.value = value.end();
    }

    public ReferenceKind getReferenceKind() {
        return referenceKind;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public int getArgsCount() {
        return argsCount;
    }

    public Parameterizable[] getArgs() {
        return args;
    }

    public byte[] getValue() {
        return value;
    }

    public int getLength(){
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BootstrapMethod that = (BootstrapMethod) o;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceKind,fullClassName,methodName,methodDesc);
    }

}
