package org.bytecode.constantpool.info;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ConstantPoolTag;
import org.bytecode.constantpool.Parameterizable;
import org.bytecode.constantpool.ReferenceKind;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class ConstantPoolMethodHandleInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {
    private byte type;
    private String classInfo;
    private String name;
    private String desc;

    public ConstantPoolMethodHandleInfo(byte type, String classInfo, String name, String desc, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodHandle_info);
        this.type = type;
        this.classInfo = classInfo;
        this.name = name;
        this.desc = desc;
        if (ref != null)
            setValue(ArrayTool.join(type, ref));
    }

    public ConstantPoolMethodHandleInfo(byte type, byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodHandle_info);
        this.type = type;
        setValue(ArrayTool.join(type, ref));
    }

    public ConstantPoolMethodHandleInfo(byte type, String classInfo, String name, String desc) {
        this(type, classInfo, name, desc, null);
    }

    public ConstantPoolMethodHandleInfo(ReferenceKind referenceKind, String classInfo, String name, String desc) {
        this(referenceKind, classInfo, name, desc, null);
    }

    ConstantPoolMethodHandleInfo(ReferenceKind referenceKind, String classInfo, String name, String desc, byte[] ref) {
        this((byte) referenceKind.getKey(), classInfo, name, desc, ref);
    }

    public ReferenceKind getKind() {
        return ReferenceKind.get(type);
    }

    @Override
    public void ldc(ConstantPool constantPool) {
        AbsConstantPoolInfo info = constantPool.get(ConvertTool.B2S(value[1], value[2]));
        byte[] value = info.getValue();
        ConstantPoolClassInfo classInfo = (ConstantPoolClassInfo) constantPool.get(ConvertTool.B2S(value[1], value[2]));
        classInfo.ldc(constantPool);
        this.classInfo = classInfo.getClassInfo();
        ConstantPoolNameAndTypeInfo nameAndTypeInfo = (ConstantPoolNameAndTypeInfo) constantPool.get(ConvertTool.B2S(value[3], value[4]));
        nameAndTypeInfo.ldc(constantPool);
        this.name = nameAndTypeInfo.getName();
        this.desc = nameAndTypeInfo.getDesc();
    }

    @Override
    public String valueToString() {
        return String.format("%s #%d", getKind(), ConvertTool.B2S(value[1], value[2]));
    }

    public String getDesc() {
        return desc;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public short load(ConstantPool constantPool) {
        return constantPool.putMethodHandleInfo(getKind(), classInfo, name, desc);
    }
}
