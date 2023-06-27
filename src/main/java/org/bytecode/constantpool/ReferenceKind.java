package org.bytecode.constantpool;

public enum ReferenceKind {
    /**
    * 如果reference_kind的值为1 (REF_getField), 2 (REF_getStatic), 3 (REF_putField), or 4 (REF_putStatic)，则对应的常量池索引项必须是CONSTANT_Fieldref_info，表示这是一个字段操作的句柄
    * 如果reference_kind的值是5 (REF_invokeVirtual) or 8 (REF_newInvokeSpecial)则对应的常量池项必须是CONSTANT_Methodref_info，表示这是一个执行普通方法或者构造方法的句柄，如果是8 (REF_newInvokeSpecial)，则CONSTANT_Methodref_info的方法名称必须是<init>。
    * 如果reference_kind的值是 6 (REF_invokeStatic) or 7 (REF_invokeSpecial)，且Class文件版本号大于52.0，即基于JDK8编译的，对应的常量池项必须是CONSTANT_Methodref_info structure or  CONSTANT_InterfaceMethodref_info，表示这是一个执行类或者接口的某个方法的句柄
    * 如果reference_kind的值是 9 (REF_invokeInterface)，则对应的常量池项必须是CONSTANT_InterfaceMethodref_info ，表示这是一个执行接口方法调用的句柄
    * 如果reference_kind的值是 5 (REF_invokeVirtual), 6 (REF_invokeStatic), 7 (REF_invokeSpecial), or 9 (REF_invokeInterface)，则对应的CONSTANT_Methodref_info项中方法名不能是<init> or <clinit>.
    **/
    REF_unknown(0x00),
    REF_getField(0x01),
    REF_getStatic(0x02),
    REF_putField(0x03),
    REF_putStatic(0x04),
    REF_invokeVirtual(0x05),
    REF_invokeStatic(0x06),
    REF_invokeSpecial(0x07),
    REF_newInvokeSpecial(0x08),
    REF_invokeInterface(0x09);

    private final int key;

    ReferenceKind(int key) {
        this.key = key;
    }

    public static ReferenceKind get(int key) {
        for (ReferenceKind referenceKind : ReferenceKind.values()) {
            if (referenceKind.getKey() == key)
                return referenceKind;
        }
        return REF_unknown;
    }

    public int getKey() {
        return key;
    }
}
