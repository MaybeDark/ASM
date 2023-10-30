package org.proxy;

public interface ProxyConstant {
    String SYNTHETIC_CLASS_NAME_PREFIX = "Proxy$";
    String SYNTHETIC_FIELD_TYPE = "Ljava/lang/reflect/Method;";
    String OBJECT_CLASS_INFO = "java/lang/Object";
    String HANDLE_METHOD_NAME = "invoke";
    String HANDLE_METHOD_DESC = "(Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;";
    String CODE_ATTRIBUTE_NAME = "Code";
    String KLASS_CLASS_INFO = "java/lang/Class";
    //    String FORNAME_METHOD_NAME = "forName";
//    String FORNAME_METHOD_DESC = "(Ljava/lang/String;)Ljava/lang/Class;";
    String GETMETHOD_METHOD_NAME = "getMethod";
    String GETMETHOD_METHOD_DESC = "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;";
    String CLINIT_METHOD_NAME = "<clinit>";
    String CLINIT_METHOD_DESC = "()V";
    String CATCH_CLASS_INFO = "java/lang/Throwable";
    String THROW_CLASS_INFO = "java/lang/reflect/UndeclaredThrowableException";
    String INIT_METHOD_NAME = "<init>";
    String THROW_INIT_DESC = "(Ljava/lang/Throwable;)V";
}
