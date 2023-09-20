package org.bytecode.attributes.field;

import org.bytecode.attributes.common.Attribute;
import org.bytecode.field.FieldWriter;

public abstract class FieldAttribute extends Attribute {
    public FieldAttribute(FieldWriter fieldWriter){
        this.writer = fieldWriter;
    }
}
