package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.constantpool.Parameterizable;
import org.constantpool.ReferenceKind;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

public class ConstantPoolMethodHandleInfo extends SymbolicReferenceConstantPoolInfo implements Parameterizable {
    private final byte type;
    private final String fullClassName;
    private final String name;
    private final String desc;

    public ConstantPoolMethodHandleInfo(byte type, String fullClassName, String name, String desc,byte[] ref) {
        super(ConstantPoolTag.CONSTANT_MethodHandle_info);
        this.type = type;
        this.fullClassName = fullClassName;
        this.name = name;
        this.desc = desc;
        if (ref != null)
            setValue(ArrayTool.join(type,ref));
    }

    public ConstantPoolMethodHandleInfo(byte type, String fullClassName, String name, String desc) {
        this(type,fullClassName,name,desc,null);
    }

    public ConstantPoolMethodHandleInfo(ReferenceKind referenceKind, String fullClassName, String name, String desc) {
        this(referenceKind,fullClassName,name,desc,null);
    }

    ConstantPoolMethodHandleInfo(ReferenceKind referenceKind, String fullClassName, String name, String desc,byte[] ref) {
        this((byte) referenceKind.getKey(),fullClassName,name,desc,ref);
    }

    public ReferenceKind getKind() {
        return ReferenceKind.get(type);
    }

    @Override
    public String valueToString() {
        return String.format("%s #%d",getKind(),ConvertTool.B2S(new byte[]{value[1],value[2]}));
    }

    public String getDesc() {
        return desc;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getName() {
        return name;
    }
}
