package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

@SuppressWarnings("all")
public class ConstantPoolMethodrefInfo extends SymbolicReferenceConstantPoolInfo {
    private String classInfo;
    private String methodName;
    private String methodDesc;

    public ConstantPoolMethodrefInfo(String classInfo, String methodName, String methodDesc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Methodref_info);
        this.classInfo = classInfo;
        this.methodDesc = methodDesc;
        this.methodName = methodName;
        setValue(ref);
    }

    public ConstantPoolMethodrefInfo(byte[] ref) {
        super(ConstantPoolTag.CONSTANT_Methodref_info);
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

    @Override
    public void ldc(ConstantPool constantPool) {
        if (classInfo != null) {
            return;
        }
        // 获取常量池中的类名
        ConstantPoolClassInfo classInfo = (ConstantPoolClassInfo) constantPool.get(ConvertTool.B2S(this.value[0], this.value[1]));
        classInfo.ldc(constantPool);
        this.classInfo = classInfo.getClassInfo();
        // 获取常量池中的字段名
        ConstantPoolNameAndTypeInfo nameAndTypeInfo = (ConstantPoolNameAndTypeInfo) constantPool.get(ConvertTool.B2S(this.value[2], this.value[3]));
        nameAndTypeInfo.ldc(constantPool);
        methodName = nameAndTypeInfo.getName();
        methodDesc = nameAndTypeInfo.getDesc();
    }
}

