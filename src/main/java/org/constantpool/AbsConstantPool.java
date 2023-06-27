package org.constantpool;

import org.attribute.cls.BootstrapMethod;
import org.attribute.cls.BootstrapMethods;
import org.constantpool.info.AbsConstantPoolInfo;
import org.other.Pool;

@SuppressWarnings("all")
public abstract class AbsConstantPool implements Pool {
    public short constantPoolCount = 1;
    public abstract short putUtf8Info(String value);
    public abstract short putIntegerInfo(Integer value);
    public abstract short putFloatInfo(Float value);
    public abstract short putLongInfo(Long value);
    public abstract short putDoubleInfo(Double value);
    public abstract short putClassInfo(String fullClassName);
    public abstract short putStringInfo(String value);
    public abstract short putFieldrefInfo(String fullClassName,String fieldName,String fieldDesc);
    public abstract short putMethodrefInfo(String fullClassName,String methodName,String methodDesc);
    public abstract short putInterfaceMethodrefInfo(String fullInterfaceName,String methodName,String methodDesc);
    public abstract short putNameAndTypeInfo(String name,String desc);
    public abstract short putMethodTypeInfo(String methodDesc);
    public abstract short putMethodHandleInfo(ReferenceKind referenceKind,String fullClassName,String name,String methodDesc);
    public abstract short putInvokeDynamicInfo(BootstrapMethods bmp, String methodName, String methodDesc, BootstrapMethod bootStrapMethod);
    public abstract AbsConstantPoolInfo get(int index);
    public abstract byte[] toByteArray();
}
