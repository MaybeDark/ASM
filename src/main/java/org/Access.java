package org;

public enum Access {
    ACC_PUBLIC(0x0001,0x07),
    ACC_PRIVATE(0x0002,0x07),
    ACC_PROTECTED(0x0004,0x07),
    ACC_STATIC(0x0008,0x07),
    ACC_FINAL(0x0010,0x0F),
    ACC_SUPER(0x0020,0x07),
    ACC_SYNCHRONIZED(0x0020,0x02),
    ACC_VOLATILE(0x0040,0x01),
    ACC_BRIDGE(0x0040,0x02),
    ACC_VARARGS(0x0080,0x02),
    ACC_TRANSIENT(0x0080,0x01),
    ACC_NATIVE(0x0100,0x02),
    ACC_INTERFACE(0x0200,0x04),
    ACC_ABSTRACT(0x0400,0x06),
    ACC_STRICT(0x0800,0x02),
    ACC_SYNTHETIC(0x1000,0x0F),
    ACC_ANNOTATION(0x2000,0x04),
    ACC_ENUM(0x4000,0x04),
    ACC_MANDATED(0x8000,0x08),
    ACC_DEPRECATED(0x20000,0x07);


    private int opcode;
    private byte target;
    Access(int opcode,int target){
        this.opcode = opcode;
        this.target = (byte) target;
    }

    public static short get(Access... access){
        short result = 0;
        if (access != null && access.length != 0){
            for (Access acc : access) {
                result |= acc.opcode;
            }
        }
       return result;
    }

    public boolean isClassAccess(){
        return (target & 1) == 1;
    }
    public boolean isMethodAccess(){
        return (target & 1<<1) == 2;
    }

    public boolean isFieldAccess(){
        return (target & 1<<2) == 1<<2;
    }
    public boolean isParameter(){
        return (target>>3) == 1;
    }

}
