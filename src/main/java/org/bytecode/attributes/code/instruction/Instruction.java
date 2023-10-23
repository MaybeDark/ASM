package org.bytecode.attributes.code.instruction;

import org.tools.ArrayTool;

public class Instruction {
    public final byte opcode;
    private byte[] operand;
    public int length;

    public Instruction(InstructionSet instructionSet, byte[] operand) {
        this.opcode = instructionSet.getOpcode();
        this.length = 1 + instructionSet.operandLength;
        setOperand(operand);
    }

    public Instruction(InstructionSet instruction) {
        this(instruction, null);
    }

    public Instruction(byte opcode, byte[] operand) {
        this.opcode = opcode;
        this.length = 1 + InstructionSet.get(opcode).operandLength;
        setOperand(operand);
    }

    public Instruction(byte opcode) {
        this(opcode, null);
    }

    public void setOperand(byte[] operand) {
        if (operand != null) {
            if (length == 0) {
                this.length += 1 + operand.length;
            } else if (operand.length != (length - 1))
                throw new RuntimeException("Wrong operand length");
        }
        this.operand = operand;
    }

    public byte[] toByteArray() {
        return ArrayTool.join(opcode, operand);
    }

    public byte[] getOperand() {
        return operand;
    }
}
