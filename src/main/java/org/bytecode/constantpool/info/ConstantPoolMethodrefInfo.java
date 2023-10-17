package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;

@SuppressWarnings("all")
public class ConstantPoolMethodrefInfo extends SymbolicReferenceConstantPoolInfo{
    private final String classInfo;
    private final String methodName;
    private final String methodDesc;

    public ConstantPoolMethodrefInfo(String classInfo, String methodName, String methodDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Methodref_info);
        this.classInfo = classInfo;
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }

    public ConstantPoolMethodrefInfo(String classInfo, String methodName, String methodDesc) {
        this(classInfo, methodName, methodDesc, null);
    }

    public String getClassInfo() {
        return classInfo;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putMethodrefInfo(classInfo, methodName, methodDesc);
    }
}

