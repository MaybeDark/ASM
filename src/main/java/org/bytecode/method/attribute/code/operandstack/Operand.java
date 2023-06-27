package org.bytecode.method.attribute.code.operandstack;

import org.Type;
import org.bytecode.method.attribute.code.bracket.Bracket;

public class Operand {
    private final Type type;
    private Bracket bracket;

    public Operand(Type type){
        this.type = type;
    }

    public Operand(Type type ,Bracket bracket){
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
