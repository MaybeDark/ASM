package org.bytecode.attributes.method.code.instruction;

import org.tools.ArrayTool;

public class Instruction {
    public final InstructionSet instructionSet;
    public final byte opcode;
    private byte[] operand;
    public final int length;
    public Instruction(InstructionSet instructionSet, byte[] operand){
        this.instructionSet = instructionSet;
        this.opcode = instructionSet.getOpcode();
        this.length = 1 + instructionSet.operandLength;
        setOperand(operand);
    }

    public Instruction(InstructionSet instruction){
        this(instruction,null);
    }

    public void setOperand(byte[] operand){
        if (operand!= null && operand.length != (length-1)){
            throw new RuntimeException("Wrong operand length");
        }
        this.operand = operand;
    }

    public byte[] toByteArray(){
        return ArrayTool.join(opcode,operand);
    }

    public byte[] getOperand() {
        return operand;
    }
}
