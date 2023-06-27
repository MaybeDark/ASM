package org.constantpool;

@SuppressWarnings("all")
public enum ConstantPoolTag {
    CONSTANT_Unknown(0x00),
    CONSTANT_Utf8_info(0x01),
    CONSTANT_Integer_info(0x03),
    CONSTANT_Float_info(0x04),
    CONSTANT_Long_info(0x05),
    CONSTANT_Double_info(0x06),
    CONSTANT_Class_info(0x07),
    CONSTANT_String_info(0x08),
    CONSTANT_Fieldref_info(0x09),
    CONSTANT_Methodref_info(0x0A),
    CONSTANT_InterfaceMethodref_info(0x0B),
    CONSTANT_NameAndType_info(0x0C),
    CONSTANT_MethodHandle_info(0x0F),
    CONSTANT_MethodType_info(0x10),
    CONSTANT_InvokeDynamic_info(0x12);

    byte tagNum;
    ConstantPoolTag(int tagNum){
        this.tagNum = (byte) tagNum;
    }

    public byte getTagNum(){
        return this.tagNum;
    }

    public static ConstantPoolTag get(byte key){
        for (ConstantPoolTag value : ConstantPoolTag.values()) {
            if(value.getTagNum() == key)
                return value;
        }
        return CONSTANT_Unknown;
    }

    public boolean isSymbolicReferenceConstantPoolInfo(){
        switch (this){
            case CONSTANT_Class_info:
            case CONSTANT_String_info:
            case CONSTANT_Fieldref_info:
            case CONSTANT_Methodref_info:
            case CONSTANT_InterfaceMethodref_info:
            case CONSTANT_NameAndType_info:
            case CONSTANT_MethodHandle_info:
            case CONSTANT_MethodType_info:
            case CONSTANT_InvokeDynamic_info:
                return true;
            default:
                return false;
        }
    }
    public boolean isTiteralConstantPoolInfo(){
        switch (this){
            case CONSTANT_Utf8_info:
            case CONSTANT_Integer_info:
            case CONSTANT_Float_info:
            case CONSTANT_Long_info:
            case CONSTANT_Double_info:
                return true;
            default:
                return false;
        }
    }
}
