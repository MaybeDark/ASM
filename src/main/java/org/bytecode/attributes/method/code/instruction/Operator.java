package org.bytecode.attributes.method.code.instruction;

import static org.bytecode.attributes.method.code.instruction.InstructionType.CALCULATE;
import static org.bytecode.attributes.method.code.instruction.InstructionType.LOGICAL;

public enum Operator {
    ADD(CALCULATE),
    SUB(CALCULATE),
    MUL(CALCULATE),
    DIV(CALCULATE),
    REM(CALCULATE),
    NEG(LOGICAL),
    SHL(LOGICAL),
    SHR(LOGICAL),
    AND(LOGICAL),
    OR(LOGICAL),
    XOR(LOGICAL);

    public final InstructionType type;
    Operator(InstructionType instructionType){
        this.type = instructionType;
    }

    public boolean isCalculateOperator(){
        return type.equals(CALCULATE);
    }

    public boolean isLogicalOperator(){
        return type.equals(LOGICAL);
    }
}
