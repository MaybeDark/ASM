package org.bytecode.attributes.innerclass;

import org.Access;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.Objects;

public class InnerClass {
    private short access;
    private String innerClassName;
    private String outerClassName;
    private String innerName;
    private short innerClassIndex = 0;
    private short outerClassIndex = 0;
    private short innerNameIndex = 0;
    private boolean inClass = true;

    public InnerClass(short access, String innerClassName, String outerClassName, String innerName) {
        this.access = access;
        this.innerClassName = innerClassName;
        if (Objects.isNull(outerClassName) || outerClassName.isEmpty()) {
            inClass = false;
        }
        this.outerClassName = outerClassName;
        this.innerName = innerName;
    }

    public static InnerClass innerClassOfClass(short access, String outerClassName, String innerName) {
        return new InnerClass(access, outerClassName + "$" + innerName, outerClassName, innerName);
    }

    /**
     * 不支持不同方法声明同名内部类
     */
    public static InnerClass innerClassOfMethod(short access, String outerClassName, String innerName) {
        return new InnerClass(access, outerClassName + "$1" + innerName, null, innerName);
    }

    public static InnerClass innerClassOfLambda() {
        return innerClassOfClass((short) (Access.ACC_PUBLIC + Access.ACC_STATIC + Access.ACC_FINAL),
                "java/lang/invoke/MethodHandles",
                "Lookup"
        );
    }

    short load(ConstantPool cp) {
        innerNameIndex = cp.putUtf8Info(innerName);
        innerClassIndex = cp.putClassInfo(innerClassName);
        if (inClass) {
            outerClassIndex = cp.putClassInfo(outerClassName);
        }
        return innerClassIndex;
    }

    byte[] toByteArray() {
        ByteVector result = new ByteVector(8);
        result.putShort(innerClassIndex)
                .putShort(outerClassIndex)
                .putShort(innerNameIndex)
                .putShort(access);
        return result.end();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerClass that = (InnerClass) o;
        return innerClassName.equals(that.innerClassName);
    }

    @Override
    public int hashCode() {
        return innerClassName.hashCode();
    }

    public short getAccess() {
        return access;
    }

    public String getInnerClassName() {
        return innerClassName;
    }

    public String getOuterClassName() {
        return outerClassName;
    }

    public String getInnerName() {
        return innerName;
    }

    public short getInnerClassIndex() {
        return innerClassIndex;
    }

    public short getOuterClassIndex() {
        return outerClassIndex;
    }

    public short getInnerNameIndex() {
        return innerNameIndex;
    }

    public boolean isInClass() {
        return inClass;
    }
}
