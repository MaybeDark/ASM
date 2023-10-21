package org.bytecode.attributes.annotations;

/**
 * 参数注解,编译后保存在方法的属性中
 * void method(@Annotation Type arg0){}
 * 和普通注解一样也分为运行时可见注解{@link RuntimeVisibleAnnotations}和运行时不可见注解{@link RuntimeInvisibleAnnotations}
 */
public class RuntimeInvisibleParameterAnnotations extends RuntimeVisibleParameterAnnotations {
    public RuntimeInvisibleParameterAnnotations() {
        attributeName = "RuntimeInvisibleParameterAnnotations";
    }
}
