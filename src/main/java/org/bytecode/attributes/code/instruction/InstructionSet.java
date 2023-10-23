package org.bytecode.attributes.code.instruction;

import static org.bytecode.attributes.code.instruction.InstructionType.*;

/**
 * 指令集
 *
 * @author 12923
 * @date 2023/07/10
 */
@SuppressWarnings("all")
public enum InstructionSet {
    NOP(0x00,0,0,0,null),
    ACONST_NULL(0x01,0,1,0,LOAD),
    ICONST_M1(0x02,0,1,0, LOAD),
    ICONST_0(0x03,0,1,0, LOAD),
    ICONST_1(0x04,0,1,0, LOAD),
    ICONST_2(0x05,0,1,0, LOAD),
    ICONST_3(0x06,0,1,0, LOAD),
    ICONST_4(0x07,0,1,0, LOAD),
    ICONST_5(0x08,0,1,0, LOAD),
    LCONST_0(0x09,0,1,0, LOAD),
    LCONST_1(0x0A,0,1,0, LOAD),
    FCONST_0(0x0B,0,1,0, LOAD),
    FCONST_1(0x0C,0,1,0, LOAD),
    FCONST_2(0x0D,0,1,0, LOAD),
    DCONST_0(0x0E,0,1,0, LOAD),
    DCONST_1(0x0F,0,1,0, LOAD),
    BIPUSH(0x10,0,1,1, LOAD),
    SIPUSH(0x11,0,1,2, LOAD),
    LDC(0x12,0,1,1, LOAD),
    LDC_W(0x13,0,1,2, LOAD),
    LDC2_W(0x14,0,1,2, LOAD),
    ILOAD(0x15,0,1,1, LOAD),
    LLOAD(0x16,0,1,1, LOAD),
    FLOAD(0x17,0,1,1, LOAD),
    DLOAD(0x18,0,1,1, LOAD),
    ALOAD(0x19,0,1,1, LOAD),
    ILOAD_0(0x1A,0,1,0, LOAD),
    ILOAD_1(0x1B,0,1,0, LOAD),
    ILOAD_2(0x1C,0,1,0, LOAD),
    ILOAD_3(0x1D,0,1,0, LOAD),
    LLOAD_0(0x1E,0,1,0, LOAD),
    LLOAD_1(0x1F,0,1,0, LOAD),
    LLOAD_2(0x20,0,1,0, LOAD),
    LLOAD_3(0x21,0,1,0, LOAD),
    FLOAD_0(0x22,0,1,0, LOAD),
    FLOAD_1(0x23,0,1,0, LOAD),
    FLOAD_2(0x24,0,1,0, LOAD),
    FLOAD_3(0x25,0,1,0, LOAD),
    DLOAD_0(0x26,0,1,0, LOAD),
    DLOAD_1(0x27,0,1,0, LOAD),
    DLOAD_2(0x28,0,1,0, LOAD),
    DLOAD_3(0x29,0,1,0, LOAD),
    ALOAD_0(0x2A,0,1,0, LOAD),
    ALOAD_1(0x2B,0,1,0, LOAD),
    ALOAD_2(0x2C,0,1,0, LOAD),
    ALOAD_3(0x2D,0,1,0, LOAD),
    IALOAD(0x2E,2,1,0, LOAD),
    LALOAD(0x2F,2,1,0, LOAD),
    FALOAD(0x30,2,1,0, LOAD),
    DALOAD(0x31,2,1,0, LOAD),
    AALOAD(0x32,2,1,0, LOAD),
    BALOAD(0x33,2,1,0, LOAD),
    CALOAD(0x34,2,1,0, LOAD),
    SALOAD(0x35,2,1,0, LOAD),
    ISTORE(0x36,1,0,1,STORE),
    LSTORE(0x37,1,0,1, STORE),
    FSTORE(0x38,1,0,1, STORE),
    DSTORE(0x39,1,0,1, STORE),
    ASTORE(0x3A,1,0,1, STORE),
    ISTORE_0(0x3B,1,0,0, STORE),
    ISTORE_1(0x3C,1,0,0, STORE),
    ISTORE_2(0x3D,1,0,0, STORE),
    ISTORE_3(0x3E,1,0,0, STORE),
    LSTORE_0(0x3F,1,0,0, STORE),
    LSTORE_1(0x40,1,0,0, STORE),
    LSTORE_2(0x41,1,0,0, STORE),
    LSTORE_3(0x42,1,0,0, STORE),
    FSTORE_0(0x43,1,0,0, STORE),
    FSTORE_1(0x44,1,0,0, STORE),
    FSTORE_2(0x45,1,0,0, STORE),
    FSTORE_3(0x46,1,0,0, STORE),
    DSTORE_0(0x47,1,0,0, STORE),
    DSTORE_1(0x48,1,0,0, STORE),
    DSTORE_2(0x49,1,0,0, STORE),
    DSTORE_3(0x4A,1,0,0, STORE),
    ASTORE_0(0x4B,1,0,0, STORE),
    ASTORE_1(0x4C,1,0,0, STORE),
    ASTORE_2(0x4D,1,0,0, STORE),
    ASTORE_3(0x4E,1,0,0, STORE),
    IASTORE(0x4F,3,0,0, STORE),
    LASTORE(0x50,3,0,0, STORE),
    FASTORE(0x51,3,0,0, STORE),
    DASTORE(0x52,3,0,0, STORE),
    AASTORE(0x53,3,0,0, STORE),
    BASTORE(0x54,3,0,0, STORE),
    CASTORE(0x55,3,0,0, STORE),
    SASTORE(0x56,3,0,0, STORE),
    POP(0x57,1,0,0,STACKBEHAVIOR),
    POP2(0x58,2,0,0,STACKBEHAVIOR),
    DUP(0x59,1,2,0,STACKBEHAVIOR),
    DUP_X1(0x5A,1,2,0,STACKBEHAVIOR),
    DUP_X2(0x5B,1,2,0,STACKBEHAVIOR),
    DUP2(0x5C,1,2,0,STACKBEHAVIOR),
    DUP2_X1(0x5D,1,2,0,STACKBEHAVIOR),
    DUP2_X2(0x5E,1,2,0,STACKBEHAVIOR),
    SWAP(0x5F,0,0,0,STACKBEHAVIOR),
    IADD(0x60,2,1,0,CALCULATE),
    LADD(0x61,2,1,0,CALCULATE),
    FADD(0x62,2,1,0,CALCULATE),
    DADD(0x63,2,1,0,CALCULATE),
    ISUB(0x64,2,1,0,CALCULATE),
    LSUB(0x65,2,1,0,CALCULATE),
    FSUB(0x66,2,1,0,CALCULATE),
    DSUB(0x67,2,1,0,CALCULATE),
    IMUL(0x68,2,1,0,CALCULATE),
    LMUL(0x69,2,1,0,CALCULATE),
    FMUL(0x6A,2,1,0,CALCULATE),
    DMUL(0x6B,2,1,0,CALCULATE),
    IDIV(0x6C,2,1,0,CALCULATE),
    LDIV(0x6D,2,1,0,CALCULATE),
    FDIV(0x6E,2,1,0,CALCULATE),
    DDIV(0x6F,2,1,0,CALCULATE),
    IREM(0x70,2,1,0,CALCULATE),
    LREM(0x71,2,1,0,CALCULATE),
    FREM(0x72,2,1,0,CALCULATE),
    DREM(0x73,2,1,0,CALCULATE),
    INEG(0x74,1,1,0,LOGICAL),
    LNEG(0x75,1,1,0,LOGICAL),
    FNEG(0x76,1,1,0,LOGICAL),
    DNEG(0x77,1,1,0,LOGICAL),
    ISHL(0x78,2,1,0,LOGICAL),
    LSHL(0x79,2,1,0,LOGICAL),
    ISHR(0x7A,2,1,0,LOGICAL),
    LSHR(0x7B,2,1,0,LOGICAL),
    IUSHR(0x7C,2,1,0,LOGICAL),
    LUSHR(0x7D,2,1,0,LOGICAL),
    IAND(0x7E,2,1,0,LOGICAL),
    LAND(0x7F,2,1,0,LOGICAL),
    IOR(0x80,2,1,0,LOGICAL),
    LOR(0x81,2,1,0,LOGICAL),
    IXOR(0x82,2,1,0,LOGICAL),
    LXOR(0x83,2,1,0,LOGICAL),
    IINC(0x84,0,0,2,LOCALBEHAVIOR),
    I2L(0x85,1,1,0,CONVERT),
    I2F(0x86,1,1,0,CONVERT),
    I2D(0x87,1,1,0,CONVERT),
    L2I(0x88,1,1,0,CONVERT),
    L2F(0x89,1,1,0,CONVERT),
    L2D(0x8A,1,1,0,CONVERT),
    F2I(0x8B,1,1,0,CONVERT),
    F2L(0x8C,1,1,0,CONVERT),
    F2D(0x8D,1,1,0,CONVERT),
    D2I(0x8E,1,1,0,CONVERT),
    D2L(0x8F,1,1,0,CONVERT),
    D2F(0x90,1,1,0,CONVERT),
    I2B(0x91,1,1,0,CONVERT),
    I2C(0x92,1,1,0,CONVERT),
    I2S(0x93,1,1,0,CONVERT),
    LCMP(0x94,2,1,0,COMPARE),
    FCMPL(0x95,2,1,0,COMPARE),
    FCMPG(0x96,2,1,0,COMPARE),
    DCMPL(0x97,2,1,0,COMPARE),
    DCMPG(0x98,2,1,0,COMPARE),
    IFEQ(0x99,1,0,2,IF),
    IFNE(0x9A,1,0,2,IF),
    IFLT(0x9B,1,0,2,IF),
    IFGE(0x9C,1,0,2,IF),
    IFGT(0x9D,1,0,2,IF),
    IFLE(0x9E,1,0,2,IF),
    IF_ICMPEQ(0x9F,2,0,2,IF),
    IF_ICMPNE(0xA0,2,0,2,IF),
    IF_ICMPLT(0xA1,2,0,2,IF),
    IF_ICMPGE(0xA2,2,0,2,IF),
    IF_ICMPGT(0xA3,2,0,2,IF),
    IF_ICMPLE(0xA4,2,0,2,IF),
    IF_ACMPEQ(0xA5,2,0,2,IF),
    IF_ACMPNE(0xA6,2,0,2,IF),
    GOTO(0xA7,0,0,2,JUMP),
    JSR(0xA8,0,0,2,JUMP),
    RET(0xA9,0,0,2,JUMP),
    TABLESWITCH(0xAA,1,0,-1,SWITCH),
    LOOKUPSWITCH(0xAB,1,0,-1,SWITCH),
    IRETURN(0xAC,1,0,0,RETURN_INSC),
    LRETURN(0xAD,1,0,0,RETURN_INSC),
    FRETURN(0xAE,1,0,0,RETURN_INSC),
    DRETURN(0xAF,1,0,0,RETURN_INSC),
    ARETURN(0xB0,1,0,0,RETURN_INSC),
    RETURN(0xB1,0,0,0,RETURN_INSC),
    GETSTATIC(0xB2,0,-1,2,FIELDBEHAVIOR),
    PUTSTATIC(0xB3,-1,0,2,FIELDBEHAVIOR),
    GETFIELD(0xB4,0,-1,2,FIELDBEHAVIOR),
    PUTFIELD(0xB5,-1,0,2,FIELDBEHAVIOR),
    INVOKEVIRTUAL(0xB6,-1,-1,2,INVOKE),
    INVOKESPECIAL(0xB7,-1,-1,2,INVOKE),
    INVOKESTATIC(0xB8,-1,-1,2,INVOKE),
    INVOKEINTERFACE(0xB9,-1,-1,2,INVOKE),
    INVOKEDYNAMIC(0xBA,-1,-1,4,INVOKE),
    NEW(0xBB,0,1,2,NEW_INSC),
    NEWARRAY(0xBC,1,1,1,NEW_INSC),
    ANEWARRAY(0xBD,1,1,2,NEW_INSC),
    ARRAYLENGTH(0xBE,1,1,0,null),
    ATHROW(0xBF,1,0,0,null),
    CHECKCAST(0xC0,1,1,2,CONVERT),
    INSTANCEOF(0xC1,1,1,2,COMPARE),
    MONITORENTER(0xC2,1,0,0,LOCK),
    MONITOREXIT(0xC3,1,0,0,LOCK),
    MULTIANEWARRAY(0xC4,2,1,3,NEW_INSC),
    IFNULL(0xC5,1,0,2,IF),
    IFNONNULL(0xC6,1,0,2,IF),
    GOTO_W(0xC7,0,0,4,JUMP),
    JSR_W(0xC8,0,0,4,JUMP);

    public final byte opcode;
    public final int pop;
    public final int put;
    public final short operandLength;
    public final InstructionType type;

    InstructionSet(byte opcode, int pop, int put, short length, InstructionType type) {
        this.opcode = opcode;
        this.pop = pop;
        this.put = put;
        this.operandLength = length;
        this.type = type;
    }

    InstructionSet(int opcode, int pop, int put, int length, InstructionType type) {
        this((byte) opcode, pop, put, (short) length, type);
    }

    public static InstructionSet get(byte opcode) {
        int uOpcode = opcode & 0xff;
        return InstructionSet.values()[uOpcode];
    }

    public byte getOpcode() {
        return opcode;
    }

    public int getPop() {
        return pop;
    }

    public int getPut() {
        return put;
    }

    public short getLength() {
        return operandLength;
    }

    public boolean checkOperandLength(int length){
        if(unknownOperandLength()){
            return true;
        }
        return this.operandLength == length;
    }

    public boolean isJumpInstruction(){
        if (isIfInstruction()){
            return true;
        }else if (isGotoInstruction()){
            return true;
        }else return isSwitchInstruction();
    }

    public boolean isInvokeInstruction(){
        return opcode >= -74 && opcode <= -70;
    }

    public boolean isFieldInstruction(){
        return opcode >= -78 && opcode <= -75;
    }

    public boolean isReturnInstruction(){
        return opcode >= -84 && opcode <= -79;
    }

    public boolean isSwitchInstruction(){
        return opcode == -86 || opcode == -85;
    }

    public boolean isGotoInstruction(){
        if (opcode >= -89 && opcode <= -87){
            return true;
        }else return opcode == -56 || opcode == -55;
    }

    public boolean isIfInstruction(){
        return (opcode >= -103 && opcode <= -90) || (opcode >= -58 && opcode <= -57);
    }

    public boolean isCompareInstruction(){
        return opcode >= -108 && opcode <= -104;
    }

    public boolean isConvertInstruction(){
        return opcode >= -123 && opcode <= -109;
    }

    public boolean ismathOperandOperationInstruction(){
        return opcode >= 96 && opcode <= 115;
    }

    public boolean islogicalOperandOperationInstrucion(){
        return (opcode >= 116 && opcode <= 127) || (opcode >= -128 && opcode <= -125);
    }

    public boolean isStackBehaviorInstruction(){
        return opcode >= 87 && opcode <= 95;
    }

    public boolean isStoreToArrayInstruction(){
        return opcode >= 79 && opcode <= 86;
    }

    public boolean isStoreToLocalsInstruction(){
        return opcode >= 54 && opcode <= 78;
    }

    public boolean isLoadFromArrayInstruction(){
        return opcode >= 46 && opcode <= 53;
    }

    public boolean isLoadFromLocalsInstruction(){
        return opcode >= 21 && opcode <= 45;
    }

    public boolean isLoadFromConstantPoolInstruction(){
        return opcode>=18 && opcode <= 20;
    }

    public boolean isLoadFromShortNumInstruction(){
        return opcode == 16 || opcode == 17;
    }

    public boolean isLoadFromConstantInstruction(){
       return opcode >= 1 && opcode <= 15;
    }

    public boolean unknownPopDepth(){
        return pop == -1;
    }

    public boolean unknownPutDepth(){
        return put == -1;
    }

    public boolean unknownOperandLength(){
        return operandLength == -1;
    }
}