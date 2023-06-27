package org.attribute.cls;

import org.Loadable;
import org.constantpool.ConstantPool;
import org.constantpool.info.ConstantPoolMethodHandleInfo;
import org.constantpool.Parameterizable;
import org.constantpool.ReferenceKind;

import java.util.Arrays;

public class BootstrapMethod implements Loadable<ConstantPool> {

    ReferenceKind referenceKind;
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;
    private final short argsCount;
    private byte[] value;
    private int length;
    private Parameterizable[] args;
    private boolean loaded = false;

    public BootstrapMethod(ReferenceKind referenceKind, String fullClassName, String methodName, String methodDesc, Parameterizable... args){
        this.referenceKind = referenceKind;
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.methodDesc =methodDesc;
        if (args != null && args.length != 0){
            this.args =args;
            argsCount = (short) args.length;
            length = 4 + argsCount * 2;
        }else {
            this.args = null;
            argsCount = 0;
            length = 4;
        }
    }

    public BootstrapMethod(ConstantPoolMethodHandleInfo cpmhif, Parameterizable... args){
        this(cpmhif.getKind(),cpmhif.getFullClassName(), cpmhif.getName(), cpmhif.getDesc(),args);
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

    public short getArgsCount() {
        return argsCount;
    }

    public Parameterizable[] getArgs() {
        return args;
    }

    public void setValue(byte[] value){
        loaded = true;
        this.value = value;
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
        return Arrays.hashCode(value);
    }

    public byte[] toByteArray(){
        if (loaded)
            return value;
        else
            throw new RuntimeException("Custom bootstrapMethod need load before use");
    }
    @Override
    public short load(ConstantPool pool) {
        setValue(pool.resolveBootstrapMethod(this));
        loaded = true;
        return 0;
    }
}
