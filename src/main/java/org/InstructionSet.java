package org;

@SuppressWarnings("all")
public enum InstructionSet {
    NOP(0x00,0,0,0),
    ACONST_NULL(0x01,0,1,0),
    ICONST_M1(0x02,0,1,0),
    ICONST_0(0x03,0,1,0),
    ICONST_1(0x04,0,1,0),
    ICONST_2(0x05,0,1,0),
    ICONST_3(0x06,0,1,0),
    ICONST_4(0x07,0,1,0),
    ICONST_5(0x08,0,1,0),
    LCONST_0(0x09,0,2,0),
    LCONST_1(0x0A,0,2,0),
    FCONST_0(0x0B,0,1,0),
    FCONST_1(0x0C,0,1,0),
    FCONST_2(0x0D,0,1,0),
    DCONST_0(0x0E,0,2,0),
    DCONST_1(0x0F,0,2,0),
    BIPUSH(0x10,0,1,1),
    SIPUSH(0x11,0,1,2),
    LDC(0x12,0,1,1),
    LDC_W(0x13,0,1,2),
    LDC2_W(0x14,0,2,2),
    ILOAD(0x15,0,1,1),
    LLOAD(0x16,0,2,1),
    FLOAD(0x17,0,1,1),
    DLOAD(0x18,0,2,1),
    ALOAD(0x19,0,1,1),
    ILOAD_0(0x1A,0,1,0),
    ILOAD_1(0x1B,0,1,0),
    ILOAD_2(0x1C,0,1,0),
    ILOAD_3(0x1D,0,1,0),
    LLOAD_0(0x1E,0,2,0),
    LLOAD_1(0x1F,0,2,0),
    LLOAD_2(0x20,0,2,0),
    LLOAD_3(0x21,0,2,0),
    FLOAD_0(0x22,0,1,0),
    FLOAD_1(0x23,0,1,0),
    FLOAD_2(0x24,0,1,0),
    FLOAD_3(0x25,0,1,0),
    DLOAD_0(0x26,0,2,0),
    DLOAD_1(0x27,0,2,0),
    DLOAD_2(0x28,0,2,0),
    DLOAD_3(0x29,0,2,0),
    ALOAD_0(0x2A,0,1,0),
    ALOAD_1(0x2B,0,1,0),
    ALOAD_2(0x2C,0,1,0),
    ALOAD_3(0x2D,0,1,0),
    IALOAD(0x2E,2,1,0),
    LALOAD(0x2F,2,2,0),
    FALOAD(0x30,2,1,0),
    DALOAD(0x31,2,2,0),
    AALOAD(0x32,2,1,0),
    BALOAD(0x33,2,1,0),
    CALOAD(0x34,2,1,0),
    SALOAD(0x35,2,1,0),
    ISTORE(0x36,1,0,1),
    LSTORE(0x37,2,0,1),
    FSTORE(0x38,1,0,1),
    DSTORE(0x39,2,0,1),
    ASTORE(0x3A,1,0,1),
    ISTORE_0(0x3B,1,0,0),
    ISTORE_1(0x3C,1,0,0),
    ISTORE_2(0x3D,1,0,0),
    ISTORE_3(0x3E,1,0,0),
    LSTORE_0(0x3F,2,0,0),
    LSTORE_1(0x40,2,0,0),
    LSTORE_2(0x41,2,0,0),
    LSTORE_3(0x42,2,0,0),
    FSTORE_0(0x43,1,0,0),
    FSTORE_1(0x44,1,0,0),
    FSTORE_2(0x45,1,0,0),
    FSTORE_3(0x46,1,0,0),
    DSTORE_0(0x47,2,0,0),
    DSTORE_1(0x48,2,0,0),
    DSTORE_2(0x49,2,0,0),
    DSTORE_3(0x4A,2,0,0),
    ASTORE_0(0x4B,1,0,0),
    ASTORE_1(0x4C,1,0,0),
    ASTORE_2(0x4D,1,0,0),
    ASTORE_3(0x4E,1,0,0),
    IASTORE(0x4F,3,0,0),
    LASTORE(0x50,4,0,0),
    FASTORE(0x51,3,0,0),
    DASTORE(0x52,4,0,0),
    AASTORE(0x53,3,0,0),
    BASTORE(0x54,3,0,0),
    CASTORE(0x55,3,0,0),
    SASTORE(0x56,3,0,0),
    POP(0x57,1,0,0),
    POP2(0x58,2,0,0),
    DUP(0x59,1,2,0),
    DUP_X1(0x5A,1,2,0),
    DUP_X2(0x5B,1,2,0),
    DUP2(0x5C,2,4,0),
    DUP2_X1(0x5D,2,4,0),
    DUP2_X2(0x5E,2,4,0),
    SWAP(0x5F,0,0,0),
    IADD(0x60,2,1,0),
    LADD(0x61,4,2,0),
    FADD(0x62,2,1,0),
    DADD(0x63,4,2,0),
    ISUB(0x64,2,1,0),
    LSUB(0x65,4,2,0),
    FSUB(0x66,2,1,0),
    DSUB(0x67,4,2,0),
    IMUL(0x68,2,1,0),
    LMUL(0x69,4,2,0),
    FMUL(0x6A,2,1,0),
    DMUL(0x6B,4,2,0),
    IDIV(0x6C,2,1,0),
    LDIV(0x6D,4,2,0),
    FDIV(0x6E,2,1,0),
    DDIV(0x6F,4,2,0),
    IREM(0x70,2,1,0),
    LREM(0x71,4,2,0),
    FREM(0x72,2,1,0),
    DREM(0x73,4,2,0),
    INEG(0x74,1,1,0),
    LNEG(0x75,2,2,0),
    FNEG(0x76,1,1,0),
    DNEG(0x77,2,2,0),
    ISHL(0x78,2,1,0),
    LSHL(0x79,3,2,0),
    ISHR(0x7A,2,1,0),
    LSHR(0x7B,3,2,0),
    IUSHR(0x7C,2,1,0),
    LUSHR(0x7D,3,2,0),
    IAND(0x7E,2,1,0),
    LAND(0x7F,4,2,0),
    IOR(0x80,2,1,0),
    LOR(0x81,4,2,0),
    IXOR(0x82,2,1,0),
    LXOR(0x83,4,2,0),
    IINC(0x84,0,0,2),
    I2L(0x85,1,2,0),
    I2F(0x86,1,1,0),
    I2D(0x87,1,2,0),
    L2I(0x88,2,1,0),
    L2F(0x89,2,1,0),
    L2D(0x8A,2,2,0),
    F2I(0x8B,1,1,0),
    F2L(0x8C,1,2,0),
    F2D(0x8D,1,2,0),
    D2I(0x8E,2,1,0),
    D2L(0x8F,2,2,0),
    D2F(0x90,2,1,0),
    I2B(0x91,1,1,0),
    I2C(0x92,1,1,0),
    I2S(0x93,1,1,0),
    LCMP(0x94,4,1,0),
    FCMPL(0x95,2,1,0),
    FCMPG(0x96,2,1,0),
    DCMPL(0x97,4,1,0),
    DCMPG(0x98,4,1,0),
    IFEQ(0x99,1,0,2),
    IFNE(0x9A,1,0,2),
    IFLT(0x9B,1,0,2),
    IFGE(0x9C,1,0,2),
    IFGT(0x9D,1,0,2),
    IFLE(0x9E,1,0,2),
    IF_ICMPEQ(0x9F,2,0,2),
    IF_ICMPNE(0xA0,2,0,2),
    IF_ICMPLT(0xA1,2,0,2),
    IF_ICMPGE(0xA2,2,0,2),
    IF_ICMPGT(0xA3,2,0,2),
    IF_ICMPLE(0xA4,2,0,2),
    IF_ACMPEQ(0xA5,2,0,2),
    IF_ACMPNE(0xA6,2,0,2),
    GOTO(0xA7,0,0,2),
    JSR(0xA8,0,0,2),
    RET(0xA9,0,0,2),
    TABLESWITCH(0xAA,1,0,-1),
    LOOKUPSWITCH(0xAB,1,0,-1),
    IRETURN(0xAC,1,0,0),
    LRETURN(0xAD,2,0,0),
    FRETURN(0xAE,1,0,0),
    DRETURN(0xAF,2,0,0),
    ARETURN(0xB0,1,0,0),
    RETURN(0xB1,0,0,0),
    GETSTATIC(0xB2,0,-1,2),
    PUTSTATIC(0xB3,-1,0,2),
    GETFIELD(0xB4,0,-1,2),
    PUTFIELD(0xB5,-1,0,2),
    INVOKEVIRTUAL(0xB6,-1,-1,2),
    INVOKESPECIAL(0xB7,-1,-1,2),
    INVOKESTATIC(0xB8,-1,-1,2),
    INVOKEINTERFACE(0xB9,-1,-1,2),
    INVOKEDYNAMIC(0xBA,-1,-1,2),
    NEW(0xBB,0,1,2),
    NEWARRAY(0xBC,1,1,1),
    ANEWARRAY(0xBD,1,1,2),
    ARRAYLENGTH(0xBE,1,1,0),
    ATHROW(0xBF,1,0,0),
    CHECKCAST(0xC0,1,1,2),
    INSTANCEOF(0xC1,1,1,2),
    MONITORENTER(0xC2,1,0,0),
    MONITOREXIT(0xC3,1,0,0),
    MULTIANEWARRAY(0xC4,2,1,3),
    IFNULL(0xC5,1,0,2),
    IFNONNULL(0xC6,1,0,2),
    GOTO_W(0xC7,0,0,4),
    JSR_W(0xC8,0,0,4);

    private final byte opcode;
    private final int pop;
    private final int put;
    private final short operand;
    InstructionSet(byte opcode,int pop,int put,short operand){
        this.opcode = opcode;
        this.pop = pop;
        this.put = put;
        this.operand = operand;
    }

    InstructionSet(int opcode,int pop,int put,int operand){
        this((byte) opcode,pop,put,(short) operand);
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

    public short getOperand() {
        return operand;
    }

    public boolean checkOperandLength(int length){
        if(unknownOperandLength()){
            return true;
        }
        return this.operand == length;
    }
    public boolean isInvokeInstruction(){
        switch (opcode) {
            case -74:
            case -73:
            case -72:
            case -71:
            case -70:
                return true;
            default:
                return false;
        }
    }

    public boolean isLoadConstInstruction(){
        if (opcode >= 1 && opcode <= 15){
            return true;
        }
        return false;
    }

    public boolean isStoreInstruction(){
        if (opcode >= 54 && opcode <= 86){
            return true;
        }
        return false;
    }
    public boolean isFieldInstruction(){
        switch (opcode){
            case -78:
            case -77:
            case -76:
            case -75:
                return true;
            default:
                return false;
        }
    }
    public boolean isIfInstruction(){
        return opcode >= -103 && opcode <= -90;
    }
    public boolean isGotoInstruction(){
        if (opcode >= -83 && opcode <= -81){
            return true;
        }else return opcode == -56 || opcode == -55;
    }

    public boolean isJumpInstruction(){
        if (isIfInstruction()){
            return true;
        }else if (isGotoInstruction()){
            return true;
        }else return isSwitchInstruction();
    }

    public boolean isSwitchInstruction(){
        return opcode == -85 || opcode == -84;
    }

    public boolean unknownPopDepth(){
        return pop == -1;
    }

    public boolean unknownPutDepth(){
        return put == -1;
    }

    public boolean unknownOperandLength(){
        return operand == -1;
    }
}
