package org.bytecode.attributes.annotations;

/**
 * 参数注解,编译后保存在方法的属性中
 * void method(@Annotation Type arg0){}
 */
public class RuntimeInvisibleParameterAnnotations extends RuntimeVisibleParameterAnnotations {

    public RuntimeInvisibleParameterAnnotations() {
        super("RuntimeInvisibleParameterAnnotations");
    }

}
