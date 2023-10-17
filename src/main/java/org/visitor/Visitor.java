package org.visitor;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

public interface Visitor<T> {
    T visit(ConstantPool constantPool, ByteVector byteVector);
}
