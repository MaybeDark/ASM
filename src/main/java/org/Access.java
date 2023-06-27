package org;

public interface Access{
    short ACC_PUBLIC = 0x0001;
    short ACC_PRIVATE = 0x0002;
    short ACC_PROTECTED = 0x0004;
    short ACC_STATIC = 0x0008;
    short ACC_FINAL = 0x0010;
    short ACC_SUPER = 0x0020;
    short ACC_SYNCHRONIZED = 0x0020;
    short ACC_VOLATILE = 0x0040;
    short ACC_BRIDGE = 0x0040;
    short ACC_VARARGS = 0x0080;
    short ACC_TRANSIENT = 0x0080;
    short ACC_NATIVE = 0x0100;
    short ACC_INTERFACE = 0x0200;
    short ACC_ABSTRACT = 0x0400;
    short ACC_STRICT = 0x0800;
    short ACC_SYNTHETIC = 0x1000;
    short ACC_ANNOTATION = 0x2000;
    short ACC_ENUM = 0x4000;
//    过时的访问标志
//    short ACC_MANDATED = 0x8000;
//    short ACC_DEPRECATED = 0x20000;


    static boolean isPublic(int access){
        return isAay(access,ACC_PUBLIC);
    }

    static boolean isPrivate(int access){
        return isAay(access,ACC_PRIVATE);
    }

    static boolean isProtected(int access){
        return isAay(access,ACC_PROTECTED);
    }

    static boolean isStatic(int access) {
        return isAay(access,ACC_STATIC);
    }

    static boolean isFinal(int access){
        return isAay(access,ACC_FINAL);
    }

    static boolean isSynchronized(int access){
        return isAay(access,ACC_SYNCHRONIZED);
    }

    static boolean isVolatile(int access){
        return isAay(access,ACC_VOLATILE);
    }

    static boolean isBridge(int access){
        return isAay(access,ACC_BRIDGE);
    }

    static boolean isVarargs(int access){
        return isAay(access,ACC_VARARGS);
    }

    static boolean isTransient(int access){
        return isAay(access,ACC_TRANSIENT);
    }

    static boolean isNative(int access){
        return isAay(access,ACC_NATIVE);
    }

    static boolean isInterface(int access){
        return isAay(access,ACC_INTERFACE);
    }

    static boolean isAbstract(int access){
        return isAay(access,ACC_ABSTRACT);
    }

    static boolean isStrict(int access){
        return isAay(access,ACC_STRICT);
    }

    static boolean isSynthetic(int access){
        return isAay(access,ACC_SYNTHETIC);
    }

    static boolean isAnnotation(int access){
        return isAay(access,ACC_ANNOTATION);
    }

    static boolean isEnum(int access){
        return isAay(access,ACC_ENUM);
    }

    static boolean isAay(int access,int any){
        return (access & any) == any;
    }

}

//public enum Access {
//    ACC_PUBLIC(0x0001,0x07),
//    ACC_PRIVATE(0x0002,0x07),
//    ACC_PROTECTED(0x0004,0x07),
//    ACC_STATIC(0x0008,0x07),
//    ACC_FINAL(0x0010,0x0F),
//    ACC_SUPER(0x0020,0x07),
//    ACC_SYNCHRONIZED(0x0020,0x02),
//    ACC_VOLATILE(0x0040,0x01),
//    ACC_BRIDGE(0x0040,0x02),
//    ACC_VARARGS(0x0080,0x02),
//    ACC_TRANSIENT(0x0080,0x01),
//    ACC_NATIVE(0x0100,0x02),
//    ACC_INTERFACE(0x0200,0x04),
//    ACC_ABSTRACT(0x0400,0x06),
//    ACC_STRICT(0x0800,0x02),
//    ACC_SYNTHETIC(0x1000,0x0F),
//    ACC_ANNOTATION(0x2000,0x04),
//    ACC_ENUM(0x4000,0x04),
//    ACC_MANDATED(0x8000,0x08),
//    ACC_DEPRECATED(0x20000,0x07);
//
//
//    private short opcode;
//    private byte target;
//    Access(short opcode,short target){
//        this.opcode = opcode;
//        this.target = (byte) target;
//    }
//
//    public static short get(Access... access){
//        short result = 0;
//        if (access != null && access.length != 0){
//            for (Access acc : access) {
//                result |= acc.opcode;
//            }
//        }
//       return result;
//    }
//
//    public static boolean isStatic(short opcode){
//        return (opcode & ACC_STATIC.opcode) == opcode;
//    }
//    public static boolean isFinal(short opcode){
//        return (opcode & ACC_FINAL.opcode) == opcode;
//    }
//
//    public boolean isClassAccess(){
//        return (target & 1) == 1;
//    }
//    public boolean isMethodAccess(){
//        return (target & 1<<1) == 2;
//    }
//
//    public boolean isFieldAccess(){
//        return (target & 1<<2) == 1<<2;
//    }
//    public boolean isParameter(){
//        return (target>>3) == 1;
//    }
//
//}
