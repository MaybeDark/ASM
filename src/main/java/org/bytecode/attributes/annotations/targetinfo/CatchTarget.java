package org.bytecode.attributes.annotations.targetinfo;

import org.tools.ConvertTool;

/**
 * 注解对象为捕获的异常
 * void method(){
 * try{}catch(@TypeAnnotation Exception e){}
 * }
 */
public class CatchTarget extends TargetInfo {
    /**
     * 目标异常在异常表中的下标
     */
    short exceptionTableIndex;

    public CatchTarget(short exceptionTableIndex) {
        this.exceptionTableIndex = exceptionTableIndex;
    }


    @Override
    public byte[] toByteArray() {
        return ConvertTool.S2B(exceptionTableIndex);
    }

    @Override
    public int getLength() {
        return 2;
    }
}
