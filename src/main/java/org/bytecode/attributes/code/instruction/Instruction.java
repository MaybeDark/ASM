package org.bytecode.attributes.code.instruction;

import org.tools.ArrayTool;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return opcode == that.opcode && Arrays.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(opcode);
        result = 31 * result + Arrays.hashCode(operand);
        return result;
    }
}
