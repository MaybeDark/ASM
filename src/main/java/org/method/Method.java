package org.method;

import org.Type;
import org.attribute.Attribute;
import org.attribute.method.Code;
import org.constantpool.ConstantPool;
import org.instruction.Instruction;
import com.sun.istack.internal.Nullable;

import java.util.LinkedList;
import java.util.Objects;

public class Method {
    short cpIndex;
    int codeLength;
    short currentOperandStackDepth;
    short currentLocalVariableTableLength;
    short operandStackMax;
    short localVariableTableMax;
    int parameterCount;
    boolean returned = false;
    private final short access;
    private final String fullClassName;
    private final String methodName;
    private final String methodDesc;
    private Attribute[] attributes;
    private Code code;
    private final LinkedList<Instruction> instructions = new LinkedList<>();
    private final Type returnType;
    private final Type[] parameterType;

    public Method(short access, String fullClassName, String methodName, @Nullable Type returnType, @Nullable Type... parameterType) {
        this.access = access;
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.methodDesc = Type.getMethodDescriptor(returnType, parameterType);
        this.returnType = returnType;
        this.parameterType = parameterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return hashCode() == method.hashCode();
    }

    public short load(ConstantPool constantPool) {
        cpIndex = constantPool.putMethodrefInfo(fullClassName, methodName, methodDesc);
        return cpIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullClassName, methodName, methodDesc);
    }
}
