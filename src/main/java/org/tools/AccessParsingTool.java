package org.tools;

public class AccessParsingTool {
    public static final int ACC_PUBLIC = 0x0001;            //public    class, com.field, com.method 访问权限修饰符
    public static final int ACC_PRIVATE = 0x0002;           //private   class, com.field, com.method 访问权限修饰符
    public static final int ACC_PROTECTED = 0x0004;         //protected class, com.field, com.method 访问权限修饰符
    public static final int ACC_STATIC = 0x0008;            //static    class, com.field, com.method 静态
    public static final int ACC_FINAL = 0x0010;             //final     class, com.field, com.method, parameter 不可变
    public static final int ACC_SUPER = 0x0020;             //super     class  继承
    public static final int ACC_SYNCHRONIZED = 0x0020;      //synchronized     com.method 锁
    public static final int ACC_VOLATILE = 0x0040;          //volatile  com.field   内存看见性
    public static final int ACC_BRIDGE = 0x0040;            //bridge    com.method
    public static final int ACC_VARARGS = 0x0080;           //varargs   com.method
    public static final int ACC_TRANSIENT = 0x0080;         //transient com.field   不参与序列化
    public static final int ACC_NATIVE = 0x0100;            //native    com.method  本地方法
    public static final int ACC_INTERFACE = 0x0200;         //interface class   接口
    public static final int ACC_ABSTRACT = 0x0400;          //abstract  class, com.method 抽象
    public static final int ACC_STRICT = 0x0800;            //strict com.method
    public static final int ACC_SYNTHETIC = 0x1000;         //synthetic class, com.field, com.method, parameter 编译器生成的
    public static final int ACC_ANNOTATION = 0x2000;        //annotation class  注解
    public static final int ACC_ENUM = 0x4000;              //enum class(?) com.field inner 枚举
    public static final int ACC_MANDATED = 0x8000;          //mandated parameter
    public static final int ACC_DEPRECATED = 0x20000;

    public static String parseFieldAccess(int access){
        StringBuilder result = new StringBuilder();
        if ((access & ACC_PUBLIC) == ACC_PUBLIC)
            result.append("public ");
        else if ((access & ACC_PRIVATE) == ACC_PRIVATE)
            result.append("private ");
        else if ((access & ACC_PROTECTED) == ACC_PROTECTED)
            result.append("protected ");

        if ((access & ACC_STATIC) == ACC_STATIC)
            result.append("static ");
        if ((access & ACC_FINAL) == ACC_FINAL)
            result.append("final ");
        if ((access & ACC_VOLATILE) == ACC_VOLATILE)
            result.append("volatile ");
        if ((access & ACC_TRANSIENT) == ACC_TRANSIENT)
            result.append("transient ");
        if ((access & ACC_SYNTHETIC) == ACC_SYNTHETIC)
            result.append("synthetic ");
        if ((access & ACC_DEPRECATED) == ACC_DEPRECATED)
            result.append("deprecated ");

        if (result.length() != 0)
            result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
    public static String parseMethodAccess(int access){
        StringBuilder result = new StringBuilder();
        if ((access & ACC_PUBLIC) == ACC_PUBLIC)
            result.append("public ");
        else if ((access & ACC_PRIVATE) == ACC_PRIVATE)
            result.append("private ");
        else if ((access & ACC_PROTECTED) == ACC_PROTECTED)
            result.append("protected ");

        if ((access & ACC_STATIC) == ACC_STATIC)
            result.append("static ");
        if ((access & ACC_FINAL) == ACC_FINAL)
            result.append("final ");
        if((access & ACC_SYNCHRONIZED) == ACC_SYNCHRONIZED)
            result.append("synchronized ");
        if((access & ACC_BRIDGE) == ACC_BRIDGE)
            result.append("bridge ");
        if ((access & ACC_VARARGS) == ACC_VARARGS)
            result.append("varargs ");
        if ((access & ACC_NATIVE) == ACC_NATIVE)
            result.append("native ");

        if ((access & ACC_ABSTRACT) == ACC_ABSTRACT)
            result.append("abstract ");
        if ((access & ACC_STRICT) == ACC_STRICT)
            result.append("strict ");
        if ((access & ACC_DEPRECATED) == ACC_DEPRECATED)
            result.append("deprecated ");

        if (result.length() != 0)
            result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String parseClassAccess(int access){
        StringBuilder result = new StringBuilder();
        if ((access & ACC_PUBLIC) == ACC_PUBLIC)
            result.append("public ");
        else if ((access & ACC_PRIVATE) == ACC_PRIVATE)
            result.append("private ");
        else if ((access & ACC_PROTECTED) == ACC_PROTECTED)
            result.append("protected ");

        if ((access & ACC_STATIC) == ACC_STATIC)
            result.append("static ");
        if ((access & ACC_FINAL) == ACC_FINAL)
            result.append("final ");
        if ((access & ACC_SUPER) == ACC_SUPER)
            result.append("super ");

        if ((access & ACC_DEPRECATED) == ACC_DEPRECATED)
            result.append("deprecated ");
        if ((access & ACC_SYNTHETIC) == ACC_SYNTHETIC)
            result.append("synthetic ");
        if ((access & ACC_ABSTRACT) == ACC_ABSTRACT)
            result.append("abstract ");

        if ((access & ACC_INTERFACE) == ACC_INTERFACE)
            result.append("interface ");
        if ((access & ACC_ANNOTATION) == ACC_ANNOTATION)
            result.append("annotation");
        if ((access & ACC_ENUM) == ACC_ENUM)
            result.append("enum ");

        if (result.length() != 0)
            result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    public static boolean isStatic(int access){
        return (access & ACC_STATIC) == ACC_STATIC;
    }
}
