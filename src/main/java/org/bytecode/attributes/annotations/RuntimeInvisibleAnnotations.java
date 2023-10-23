package org.bytecode.attributes.annotations;

/**
 * 运行时不可见注解
 * <p>@Retention(RetentionPolicy.CLASS)</p>
 * public @interface Annotation{}
 */
public class RuntimeInvisibleAnnotations extends RuntimeVisibleAnnotations {

    public RuntimeInvisibleAnnotations() {
        super("RuntimeInvisibleAnnotations");
    }

}
