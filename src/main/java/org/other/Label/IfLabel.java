package org.other.Label;

import org.instruction.Instruction;

public class IfLabel extends Label{
    private Instruction instruction;
    public IfLabel(int startPc, Instruction instruction) {
        super(startPc);
        this.instruction = instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
