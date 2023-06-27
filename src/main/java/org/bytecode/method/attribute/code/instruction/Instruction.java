package org.bytecode.method.attribute.code.instruction;

import org.InstructionSet;
import org.tools.ArrayTool;

public class Instruction {
    private InstructionSet instructionSet;
    private final byte opcode;
    private byte[] operand;
    private byte[] value;

    public Instruction(InstructionSet instructionSet, byte[] operand){
        this.instructionSet = instructionSet;
        this.opcode = instructionSet.getOpcode();
        this.operand = operand;
        if (operand != null){
            this.value = ArrayTool.join(opcode,operand);
        }else {
            this.value = new byte[]{opcode};
        }
    }

    public Instruction(InstructionSet instruction){
        this(instruction,null);
    }

    public InstructionSet getInstructionSet(){
        return instructionSet;
    }

    public byte[] getOperand() {
        return operand;
    }

    public byte getOpcode() {
        return opcode;
    }

    public byte[] getValue() {
        return value;
    }

    public void setOperand(byte[] operand){
        this.operand = operand;
        this.value = ArrayTool.join(opcode,operand);
    }
    public byte[] toByteArray(){
        return value;
    }

    public int getLength(){
        return value.length;
    }
}
