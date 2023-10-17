package org.bytecode.constantpool;

import org.bytecode.constantpool.info.AbsConstantPoolInfo;

@SuppressWarnings("all")
public abstract class AbsConstantPool {
    public short constantPoolCount = 1;

    public abstract short putUtf8Info(String value);

    public abstract short putIntegerInfo(Integer value);

    public abstract short putFloatInfo(Float value);

    public abstract short putLongInfo(Long value);

    public abstract short putDoubleInfo(Double value);

    public abstract short putClassInfo(String classInfo);

    public abstract short putStringInfo(String value);

    public abstract short putFieldrefInfo(String classInfo, String fieldName, String fieldDesc);

    public abstract short putMethodrefInfo(String classInfo, String methodName, String methodDesc);

    public abstract short putInterfaceMethodrefInfo(String fullInterfaceName, String methodName, String methodDesc);

    public abstract short putNameAndTypeInfo(String name, String desc);

    public abstract short putMethodTypeInfo(String methodDesc);

    public abstract short putMethodHandleInfo(ReferenceKind referenceKind, String classInfo, String name, String methodDesc);

    public abstract short putInvokeDynamicInfo(String methodName, String methodDesc, short index);

    public abstract AbsConstantPoolInfo get(int index);

    public abstract byte[] toByteArray();
}
