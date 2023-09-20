package org.bytecode.attributes.method.code.bracket;

import org.bytecode.attributes.method.code.instruction.Instruction;

public class IfBracket extends Bracket {
    private Instruction instruction;
    public IfBracket(int startPc, Instruction instruction) {
        super(startPc);
        this.instruction = instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
