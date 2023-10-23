package org.bytecode.attributes.annotations.targetinfo;

public abstract class TargetInfo {

    public abstract byte[] toByteArray();

    public abstract int getLength();

//    public abstract TargetInfo visit(ConstantPool constantPool, ByteVector byteVector);
}
