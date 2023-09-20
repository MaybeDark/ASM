package org.bytecode.attributes.clazz;

import org.bytecode.ClassWriter;
import org.bytecode.attributes.common.Attribute;

public abstract class ClassAttribute extends Attribute {
    public ClassAttribute(ClassWriter classWriter){
        this.writer = classWriter;
    }

}
