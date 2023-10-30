package org.bytecode.attributes.code.instruction;

import com.sun.istack.internal.Nullable;
import org.Type;
import org.bytecode.attributes.bootstrapmethods.CallSite;
import org.bytecode.constantpool.ConstantPool;
import org.exception.TypeErrorException;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ConvertTool;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.MethodWrapper;

import java.util.HashMap;
import java.util.Map;

import static org.bytecode.attributes.code.instruction.InstructionSet.*;

/**
 * @author 12923
 * @date 2023/10/17
 */
public class CodeHelper {
    private static CodeHelper instance;
    private final InstructionSet[] loadIntInstructionCache = {
            ICONST_M1,
            ICONST_0,
            ICONST_1,
            ICONST_2,
            ICONST_3,
            ICONST_4,
            ICONST_5,
    };
    private final Map<String, Map<Integer, InstructionSet>> loadLocalInstructionCache = new HashMap<>();
    private final Map<String, Map<Integer, InstructionSet>> storeLocalInstructionCache = new HashMap<>();
    private final Map<Type, Integer> arrayTypeMap = new HashMap<>();
    private ConstantPool constantPool;

    {
        Map<Integer, InstructionSet> ILoadCache = new HashMap<>();
        ILoadCache.put(0, ILOAD_0);
        ILoadCache.put(1, ILOAD_1);
        ILoadCache.put(2, ILOAD_2);
        ILoadCache.put(3, ILOAD_3);
        Map<Integer, InstructionSet> LLoadCache = new HashMap<>();
        LLoadCache.put(0, LLOAD_0);
        LLoadCache.put(1, LLOAD_1);
        LLoadCache.put(2, LLOAD_2);
        LLoadCache.put(3, LLOAD_3);
        Map<Integer, InstructionSet> FLoadCache = new HashMap<>();
        FLoadCache.put(0, FLOAD_0);
        FLoadCache.put(1, FLOAD_1);
        FLoadCache.put(2, FLOAD_2);
        FLoadCache.put(3, FLOAD_3);
        Map<Integer, InstructionSet> DLoadCache = new HashMap<>();
        DLoadCache.put(0, DLOAD_0);
        DLoadCache.put(1, DLOAD_1);
        DLoadCache.put(2, DLOAD_2);
        DLoadCache.put(3, DLOAD_3);
        Map<Integer, InstructionSet> ALoadCache = new HashMap<>();
        ALoadCache.put(0, ALOAD_0);
        ALoadCache.put(1, ALOAD_1);
        ALoadCache.put(2, ALOAD_2);
        ALoadCache.put(3, ALOAD_3);
        loadLocalInstructionCache.put("I", ILoadCache);
        loadLocalInstructionCache.put("L", LLoadCache);
        loadLocalInstructionCache.put("F", FLoadCache);
        loadLocalInstructionCache.put("D", DLoadCache);
        loadLocalInstructionCache.put("A", ALoadCache);

        Map<Integer, InstructionSet> IStoreCache = new HashMap<>();
        IStoreCache.put(0, ISTORE_0);
        IStoreCache.put(1, ISTORE_1);
        IStoreCache.put(2, ISTORE_2);
        IStoreCache.put(3, ISTORE_3);
        Map<Integer, InstructionSet> LStoreCache = new HashMap<>();
        LStoreCache.put(0, LSTORE_0);
        LStoreCache.put(1, LSTORE_1);
        LStoreCache.put(2, LSTORE_2);
        LStoreCache.put(3, LSTORE_3);
        Map<Integer, InstructionSet> FStoreCache = new HashMap<>();
        FStoreCache.put(0, FSTORE_0);
        FStoreCache.put(1, FSTORE_1);
        FStoreCache.put(2, FSTORE_2);
        FStoreCache.put(3, FSTORE_3);
        Map<Integer, InstructionSet> DStoreCache = new HashMap<>();
        DStoreCache.put(0, DSTORE_0);
        DStoreCache.put(1, DSTORE_1);
        DStoreCache.put(2, DSTORE_2);
        DStoreCache.put(3, DSTORE_3);
        Map<Integer, InstructionSet> AStoreCache = new HashMap<>();
        AStoreCache.put(0, ASTORE_0);
        AStoreCache.put(1, ASTORE_1);
        AStoreCache.put(2, ASTORE_2);
        AStoreCache.put(3, ASTORE_3);
        storeLocalInstructionCache.put("I", IStoreCache);
        storeLocalInstructionCache.put("L", LStoreCache);
        storeLocalInstructionCache.put("F", FStoreCache);
        storeLocalInstructionCache.put("D", DStoreCache);
        storeLocalInstructionCache.put("A", AStoreCache);

        arrayTypeMap.put(Type.BOOLEAN, 4);
        arrayTypeMap.put(Type.CHAR, 5);
        arrayTypeMap.put(Type.FLOAT, 6);
        arrayTypeMap.put(Type.DOUBLE, 7);
        arrayTypeMap.put(Type.BYTE, 8);
        arrayTypeMap.put(Type.SHORT, 9);
        arrayTypeMap.put(Type.INT, 10);
        arrayTypeMap.put(Type.LONG, 11);
    }

    /**
     * 单例,但constantPool可以替换,每次替换意味着新的classfile
     */
    public static CodeHelper getHelper() {
        if (instance == null) {
            instance = new CodeHelper();
        }
        return instance;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    public Instruction loadNull() {
        return new Instruction(ACONST_NULL);
    }

    public Instruction loadInt(int num) {
        Instruction newInstruction = null;
        if (num >= - 1 && num <= 5) {
            newInstruction = new Instruction(loadIntInstructionCache[num + 1]);
        } else if (num >= Byte.MIN_VALUE && num <= Byte.MAX_VALUE) {
            newInstruction = new Instruction(BIPUSH, new byte[]{(byte) num});
        } else if (num >= Short.MIN_VALUE && num <= Short.MAX_VALUE) {
            newInstruction = new Instruction(SIPUSH, ConvertTool.S2B(num));
        } else {
            short index = constantPool.putIntegerInfo(num);
            newInstruction = index > Byte.MAX_VALUE ? new Instruction(LDC_W, ConvertTool.S2B(index)) : new Instruction(LDC, new byte[]{(byte) index});
        }
        return newInstruction;
    }

    public Instruction loadDouble(double num) {
        Instruction newInstruction = null;
        if (num == 0D) {
            newInstruction = new Instruction(DCONST_0);
        } else if (num == 1D) {
            newInstruction = new Instruction(DCONST_1);
        } else {
            short index = constantPool.putDoubleInfo(num);
            newInstruction = new Instruction(LDC2_W, ConvertTool.S2B(index));
        }
        return newInstruction;
    }

    public Instruction loadLong(long num) {
        Instruction newInstruction = null;
        if (num == 0L) {
            newInstruction = new Instruction(LCONST_0);
        } else if (num == 1L) {
            newInstruction = new Instruction(LCONST_1);
        } else {
            short index = constantPool.putLongInfo(num);
            newInstruction = new Instruction(LDC2_W, ConvertTool.S2B(index));
        }
        return newInstruction;
    }

    public Instruction loadFloat(float num) {
        Instruction newInstruction = null;
        if (num == 0F) {
            newInstruction = new Instruction(FCONST_0);
        } else if (num == 1F) {
            newInstruction = new Instruction(FCONST_1);
        } else if (num == 2F) {
            newInstruction = new Instruction(FCONST_2);
        } else {
            short index = constantPool.putFloatInfo(num);
            newInstruction = new Instruction(LDC2_W, ConvertTool.S2B(index));
        }
        return newInstruction;
    }

    public Instruction loadString(String str) {
        short index = constantPool.putStringInfo(str);
        Instruction newInstruction = index > Byte.MAX_VALUE ? new Instruction(LDC_W, ConvertTool.S2B(index)) : new Instruction(LDC, new byte[]{(byte) index});
        return newInstruction;
    }

    public Instruction loadLocal(Type type, short localIndex) {
        Map<Integer, InstructionSet> tSet = loadLocalInstructionCache.get(type.getDescriptor());
        if (tSet == null) {

            return loadALocal(localIndex);
        }
        InstructionSet iSet = tSet.get((int) localIndex);
        if (iSet == null) {
            iSet = InstructionSet.valueOf(type.getDescriptor() + "LOAD");
            return new Instruction(iSet, ConvertTool.S2B(localIndex));
        }
        return new Instruction(iSet);
    }

    public Instruction loadClass(ClassWrapper classWrapper) {
        short index = constantPool.putClassInfo(classWrapper.getClassInfo());
        return new Instruction(LDC, ConvertTool.S2B(index));
    }

    private Instruction loadALocal(short localIndex) {
        InstructionSet iSet = loadLocalInstructionCache.get("A").get((int) localIndex);
        if (iSet == null) {
            iSet = ALOAD;
            return new Instruction(iSet, ConvertTool.S2B(localIndex));
        }
        return new Instruction(iSet);
    }

    public Instruction loadArrayElement(Type componentType) {
        InstructionSet instructionSet;
        if (componentType.isPrimitiveType()) {
            instructionSet = valueOf(componentType.getDescriptor() + "ALOAD");
        } else {
            instructionSet = AALOAD;
        }
        return new Instruction(instructionSet);
    }

    public Instruction loadField(FieldWrapper fieldWrapper, boolean isStatic) {
        InstructionSet instructionSet = GETFIELD;
        if (isStatic) {
            instructionSet = GETSTATIC;
        }
        return new Instruction(instructionSet, ConvertTool.S2B(fieldWrapper.load(constantPool)));
    }

    public Instruction storeLocal(Type type, short localIndex) {
        Map<Integer, InstructionSet> tSet = storeLocalInstructionCache.get(type.getDescriptor());
        if (tSet == null) {
            return storeALocal(localIndex);
        }
        InstructionSet iSet = tSet.get((int) localIndex);
        if (iSet == null) {
            iSet = InstructionSet.valueOf(type.getDescriptor() + "STORE");
            return new Instruction(iSet, ConvertTool.S2B(localIndex));
        }
        return new Instruction(iSet);
    }

    private Instruction storeALocal(short localIndex) {
        InstructionSet iSet = storeLocalInstructionCache.get("A").get((int) localIndex);
        if (iSet == null) {
            iSet = ASTORE;
            return new Instruction(iSet, ConvertTool.S2B(localIndex));
        }
        return new Instruction(iSet);
    }

    public Instruction storeArrayElement(Type componentType) {
        InstructionSet instructionSet;
        if (componentType.isPrimitiveType()) {
            instructionSet = valueOf(componentType.getDescriptor() + "ASTORE");
        } else {
            instructionSet = AASTORE;
        }
        return new Instruction(instructionSet);
    }

    public Instruction storeField(FieldWrapper fieldWrapper, boolean isStatic) {
        InstructionSet instructionSet = PUTFIELD;
        if (isStatic) {
            instructionSet = PUTSTATIC;
        }
        return new Instruction(instructionSet, ConvertTool.S2B(fieldWrapper.load(constantPool)));
    }

    public Instruction stackBehavior(InstructionSet instructionSet) {
        return new Instruction(instructionSet);
    }

    public Instruction checkCast(ClassWrapper classWrapper) {
        return new Instruction(CHECKCAST, ConvertTool.S2B(classWrapper.load(constantPool)));
    }

    public Instruction newObject(ClassWrapper classWrapper) {
        return new Instruction(NEW, ConvertTool.S2B(classWrapper.load(constantPool)));
    }

    public Instruction newArray(Type elementType) {
        if (! elementType.isPrimitiveType()) {
            return newAArray(new ClassWrapper(elementType));
        }
        Integer typeCode = arrayTypeMap.get(elementType);
        return new Instruction(NEWARRAY, ConvertTool.S2B(typeCode.shortValue()));
    }

    private Instruction newAArray(ClassWrapper object) {
        return new Instruction(ANEWARRAY, ConvertTool.S2B(object.load(constantPool)));
    }

    public Instruction arrayLength() {
        return new Instruction(ARRAYLENGTH);
    }

    public Instruction throwException() {
        return new Instruction(ATHROW);
    }

    public Instruction instanceOf(ClassWrapper classWrapper) {
        return new Instruction(INSTANCEOF, ConvertTool.S2B(classWrapper.load(constantPool)));
    }

    public Instruction monitorEnter() {
        return new Instruction(MONITORENTER);
    }

    public Instruction monitorExit() {
        return new Instruction(MONITOREXIT);
    }

    public Instruction multiANewArray(ClassWrapper classWrapper, byte dimensions) {
        return new Instruction(MULTIANEWARRAY, ArrayTool.join(ConvertTool.S2B(classWrapper.load(constantPool)), dimensions));
    }

    public Instruction jump(short offset) {
        return new Instruction(GOTO, ConvertTool.S2B(offset));
    }

    public Instruction branch(InstructionSet branchInst) {
        return new Instruction(branchInst);
    }

    public Instruction iinc(short index, int value) {
        return new Instruction(IINC, new byte[]{(byte) index, (byte) value});
    }

    public Instruction return0(@Nullable Type type) {
        if (type == null || type.isVoidType()) {
            return new Instruction(RETURN);
        } else if (type.isRuntimeIntType()) {
            return new Instruction(IRETURN);
        } else if (type.isPrimitiveType()) {
            return new Instruction(valueOf(type.getDescriptor() + "RETURN"));
        } else {
            return new Instruction(ARETURN);
        }
    }

    public Instruction compare(Type type) {
        if (type.isFloatType()) {
            return new Instruction(FCMPL);
        } else if (type.isDoubleType()) {
            return new Instruction(DCMPL);
        } else if (type.isLongType()) {
            return new Instruction(LCMP);
        } else {
            throw new TypeErrorException("compare method is used for long,double,float compare");
        }
    }

    public Instruction convert(Type oType, Type nType) {
        String s = oType.getDescriptor() + "2" + nType.getDescriptor();
        InstructionSet instructionSet = valueOf(s);
        return new Instruction(instructionSet);
    }

    public Instruction invokeVirtual(MethodWrapper target) {
        return new Instruction(INVOKEVIRTUAL, ConvertTool.S2B(target.load(constantPool)));
    }

    public Instruction invokeSpecial(MethodWrapper target) {
        return new Instruction(INVOKESPECIAL, ConvertTool.S2B(target.load(constantPool)));
    }

    public Instruction invokeStatic(MethodWrapper target) {
        return new Instruction(INVOKESTATIC, ConvertTool.S2B(target.load(constantPool)));
    }

    public Instruction invokeDynamic(CallSite callSite) {
        short index = constantPool.putInvokeDynamicInfo(callSite.getTargetMethodName(), "()" + callSite.getTargetType().getDescriptor(), callSite.getIndex());
        return new Instruction(INVOKEDYNAMIC, ArrayTool.join(ConvertTool.S2B(index), ConvertTool.S2B((short) 0)));
    }

    //  count记录了参数的个数，一个long或者double类型的参数记2，其余的参数记1
    public Instruction invokeInterface(MethodWrapper target) {
        short index = constantPool.putInterfaceMethodrefInfo(target.getClassName(), target.getMethodName(), target.getMethodDesc());
        int count = 1; // objectRef
        Type[] argumentTypes = Type.getArgumentTypes(target.getMethodDesc());
        for (Type argumentType : argumentTypes) {
            if (argumentType != null && ! argumentType.isVoidType()) {
                count++;
                if (argumentType.isLongOrDoubleType()) {
                    count++;
                }
            }
        }
        return new Instruction(INVOKEINTERFACE, new ByteVector(4).putShort(index).putByte(count).putByte(0).end());
    }

    public Instruction mathOperation(Type right, Operator operator) {
        InstructionSet instructionSet = valueOf(right.getDescriptor() + operator);
        return new Instruction(instructionSet);
    }

    public Instruction logicalOperation(Type left, Operator operator) {
        InstructionSet instructionSet = valueOf(left.getDescriptor() + operator);
        return new Instruction(instructionSet);
    }

    public Instruction neg(Type type) {
        InstructionSet instructionSet = valueOf(type.getDescriptor() + "NEG");
        return new Instruction(instructionSet);
    }

//    public byte[] toByteArray(){
//        ByteVectors byteVectors = new ByteVectors();
//        for (Instruction instruction : instructions) {
//            byteVectors.putArray(instruction.toByteArray());
//        }
//        return byteVectors.toByteArray();
//    }
//
//    public String print(){
//        StringBuilder sb = new StringBuilder();
//        int index = 1;
//        int pcIndex = 0;
//        for (Instruction instruction : instructions) {
//            String instructionStr = instruction.instructionSet.toString().toLowerCase();
//            sb.append(String.format("%- 3d  %- 3d  %s",
//                    index++,pcIndex,instructionStr));
//            if (instruction.length != 1)
//                sb.append(String.format("% -2d",ConvertTool.B2S(instruction.getOperand())));
//            sb.append('\n');
//            pcIndex += instruction.length;
//        }
//        return sb.toString();
//    }
}
