package org.bytecode.attributes.annotations;

/**
 * 运行时可见注解
 * <p>@Retention(RetentionPolicy.RUNTIME)</p>
 * Annotation{}
 */
public class RuntimeVisibleAnnotations extends Annotations {

    public RuntimeVisibleAnnotations(String attributeName) {
        super("RuntimeVisibleAnnotations");
    }
}
