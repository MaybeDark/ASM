package org.bytecode.attributes.bootstrapmethods;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.Parameterizable;
import org.bytecode.constantpool.ReferenceKind;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.tools.ByteVector;

import java.util.Objects;

public class BootstrapMethod {
    private final ReferenceKind referenceKind;
    private final String classInfo;
    private final String methodName;
    private final String methodDesc;
    private final Parameterizable[] args;
    private int argsCount;
    private byte[] value;
    private int length;
    private boolean loaded = false;

    BootstrapMethod(ReferenceKind referenceKind, String classInfo, String methodName, String methodDesc, Parameterizable... args) {
        this.referenceKind = referenceKind;
        this.classInfo = classInfo;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.args = args;
        if (args != null && args.length != 0) {
            argsCount = args.length;
            length = 4 + argsCount * 2;
        } else {
            argsCount = 0;
            length = 4;
        }
    }

    BootstrapMethod(ConstantPoolMethodHandleInfo cpmhif, Parameterizable... args) {
        this(cpmhif.getKind(), cpmhif.getClassInfo(), cpmhif.getName(), cpmhif.getDesc(), args);
    }

    public void addArgs(Parameterizable arg) {
        args[argsCount] = arg;
        argsCount++;
        length += 2;
    }

    byte[] toByteArray() {
        return value;
    }

    public void load(ConstantPool constantPool) {
        if (loaded) {
            return;
        }
        ByteVector value = new ByteVector(length);
        value.putShort(constantPool.putMethodHandleInfo(referenceKind, classInfo, methodName, methodDesc));
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

    public String getClassInfo() {
        return classInfo;
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

    public int getLength() {
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
        return Objects.hash(referenceKind, classInfo, methodName, methodDesc);
    }
}