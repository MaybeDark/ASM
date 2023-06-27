package org.bytecode.cls.attribute.bootstrapmethod;

import org.Loadable;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.bytecode.constantpool.Parameterizable;
import org.bytecode.constantpool.ReferenceKind;

import java.util.*;

public class BootstrapMethod implements Loadable<ConstantPool> {
    public final static int BOOTSTRAP_METHOD_HANDLE_TYPE_LENGTH = 2;
    public final static int BOOTSTRAP_METHOD_ARGS_COUNT_LENGTH = 2;
    private final ReferenceKind referenceKind;
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;
    private int argsCount;
    private byte[] value;
    private int length;
    private List<Parameterizable> args;
    private boolean loaded = false;

    public BootstrapMethod(ReferenceKind referenceKind, String fullClassName, String methodName, String methodDesc, Parameterizable... args){
        this.referenceKind = referenceKind;
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.args = new ArrayList<>();
        if (args != null && args.length != 0){
            this.args.addAll(Arrays.asList(args));
            argsCount = (short) args.length;
            length = BOOTSTRAP_METHOD_HANDLE_TYPE_LENGTH + BOOTSTRAP_METHOD_ARGS_COUNT_LENGTH + argsCount * 2;
        }else {
            argsCount = 0;
            length = 4;
        }
    }

    public BootstrapMethod(ConstantPoolMethodHandleInfo cpmhif, Parameterizable... args){
        this(cpmhif.getKind(),cpmhif.getFullClassName(), cpmhif.getName(), cpmhif.getDesc(),args);
    }

    public BootstrapMethod addArgument(Parameterizable argument){
        args.add(argument);
        argsCount++;
        length += 2;
        return this;
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

    public List<Parameterizable> getArgs() {
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
        return Objects.hash(referenceKind,fullClassName,methodName,methodDesc);
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
