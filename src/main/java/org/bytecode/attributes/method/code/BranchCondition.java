package org.bytecode.attributes.method.code;

import org.bytecode.attributes.method.code.instruction.InstructionSet;

import static org.bytecode.attributes.method.code.instruction.InstructionSet.*;

public enum BranchCondition {
    EQ(IFNE),
    NE(IFEQ),
    LT(IFGT),
    LE(IFGE),
    GT(IFLT),
    GE(IFLE),
    NOTNULL(IFNULL),
    NULL(IFNONNULL),
    ICMPEQ(IF_ICMPNE),
    ICMPNE(IF_ICMPEQ),
    ICMPLT(IF_ICMPGT),
    ICMPGE(IF_ICMPGE),
    ICMPGT(IF_ICMPLT),
    ICMPLE(IF_ICMPLE),
    ACOMPEQ(IF_ACMPNE),
    ACOMPNE(IF_ACMPEQ);

    public InstructionSet parallel;
    BranchCondition(InstructionSet instructionSet){
        this.parallel = instructionSet;
    }


}
