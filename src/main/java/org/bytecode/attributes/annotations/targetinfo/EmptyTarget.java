package org.bytecode.attributes.annotations.targetinfo;

/**
 * 注解目标为返回类型；record结构JDK1.8并未提及
 * <p>@TypeAnnotation</p>
 * ReturnType method(){}
 */
public class EmptyTarget extends TargetInfo {
    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public int getLength() {
        return 0;
    }
}
