package org.bytecode.constantpool;

import com.sun.istack.internal.NotNull;
import org.bytecode.constantpool.info.*;
import org.exception.NotNullException;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ByteVectors;
import org.tools.ConvertTool;
import org.visitor.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConstantPool extends AbsConstantPool implements Visitor<ConstantPool> {
    private final ArrayList<AbsConstantPoolInfo> pool = new ArrayList<>();
    private final Map<Integer, Short> hash2Index = new HashMap<>();

    @Override
    public short putUtf8Info(String str) {
        ConstantPoolUtf8Info info = new ConstantPoolUtf8Info(str);
        return putInfo(info);
    }

    @Override
    public short putIntegerInfo(Integer value) {
        ConstantPoolIntegerInfo info = new ConstantPoolIntegerInfo(value);
        return putInfo(info);
    }

    @Override
    public short putFloatInfo(Float value) {
        ConstantPoolFloatInfo info = new ConstantPoolFloatInfo(ConvertTool.F2B(value));
        return putInfo(info);
    }

    @Override
    public short putLongInfo(Long value) {
        ConstantPoolLongInfo info = new ConstantPoolLongInfo(value);
        return putInfo(info);
    }

    @Override
    public short putDoubleInfo(Double value) {
        ConstantPoolDoubleInfo info = new ConstantPoolDoubleInfo(value);
        return putInfo(info);
    }

    @Override
    public short putClassInfo(String classInfo) {
        short index = putUtf8Info(classInfo);
        byte[] ref = ConvertTool.S2B(index);
        ConstantPoolClassInfo info = new ConstantPoolClassInfo(classInfo, ref);
        return putInfo(info);
    }

    @Override
    public short putStringInfo(String value) {
        short index = putUtf8Info(value);
        byte[] ref = ConvertTool.S2B(index);
        ConstantPoolStringInfo info = new ConstantPoolStringInfo(value, ref);
        return putInfo(info);
    }

    @Override
    public short putFieldrefInfo(String classInfo, String fieldName, String fieldDesc) {
        short classInfoIndex = putClassInfo(classInfo);
        short nameAndTypeInfoIndex = putNameAndTypeInfo(fieldName, fieldDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(classInfoIndex), ConvertTool.S2B(nameAndTypeInfoIndex));
        ConstantPoolFieldrefInfo info = new ConstantPoolFieldrefInfo(classInfo, fieldName, fieldDesc, ref);
        return putInfo(info);
    }

    @Override
    public short putMethodrefInfo(String classInfo, String methodName, String methodDesc) {
        short classInfoIndex = putClassInfo(classInfo);
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(classInfoIndex), ConvertTool.S2B(nameAndTypeInfoIndex));
        ConstantPoolMethodrefInfo info = new ConstantPoolMethodrefInfo(classInfo, methodName, methodDesc, ref);
        return putInfo(info);
    }

    @Override
    public short putInterfaceMethodrefInfo(String fullInterfaceName, String methodName, String methodDesc) {
        short classInfoIndex = putClassInfo(fullInterfaceName);
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(classInfoIndex), ConvertTool.S2B(nameAndTypeInfoIndex));
        ConstantPoolInterfaceMethodrefInfo info = new ConstantPoolInterfaceMethodrefInfo(fullInterfaceName, methodName, methodDesc, ref);
        return putInfo(info);
    }

    @Override
    public short putNameAndTypeInfo(String name, String desc) {
        short nameIndex = putUtf8Info(name);
        short descIndex = putUtf8Info(desc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(nameIndex), ConvertTool.S2B(descIndex));
        ConstantPoolNameAndTypeInfo info = new ConstantPoolNameAndTypeInfo(name, desc, ref);
        return putInfo(info);
    }

    @Override
    public short putMethodTypeInfo(String methodDesc) {
        short infoIndex = putUtf8Info(methodDesc);
        byte[] ref = ConvertTool.S2B(infoIndex);
        ConstantPoolMethodTypeInfo info = new ConstantPoolMethodTypeInfo(methodDesc, ref);
        return putInfo(info);
    }

    @Override
    public short putMethodHandleInfo(ReferenceKind referenceKind, String classInfo, String name, String desc) {
        short infoIndex;
        switch (referenceKind) {
            case REF_getField:
            case REF_getStatic:
            case REF_putField:
            case REF_putStatic:
                infoIndex = putFieldrefInfo(classInfo, name, desc);
                break;
            case REF_invokeInterface:
                infoIndex = putInterfaceMethodrefInfo(classInfo, name, desc);
                break;
            case REF_newInvokeSpecial:
                if (!name.equals("<init>"))
                    throw new RuntimeException("if ReferenceKind is REF_newInvokeSpecial,the methodName must be <init>");
            case REF_invokeVirtual:
            case REF_invokeSpecial:
            case REF_invokeStatic:
                infoIndex = putMethodrefInfo(classInfo, name, desc);
                break;
            default:
                throw new RuntimeException("No support this ReferenceKind");
        }
        byte type = (byte) referenceKind.getKey();
        byte[] ref = ConvertTool.S2B(infoIndex);
        ConstantPoolMethodHandleInfo info = new ConstantPoolMethodHandleInfo(type, classInfo, name, desc, ref);
        return putInfo(info);
    }

    public short putInvokeDynamicInfo(String methodName, String methodDesc,short index) {
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(nameAndTypeInfoIndex), ConvertTool.S2B(index));
        ConstantPoolInvokeDynamicInfo info = new ConstantPoolInvokeDynamicInfo(methodName, methodDesc, ref);
        return putInfo(info);
    }

    private short putInfo(AbsConstantPoolInfo info) {
        Short value = hash2Index.putIfAbsent(info.hashCode(), constantPoolCount);
        if (value != null && value != constantPoolCount) {
            return value;
        }
        pool.add(info);
        return constantPoolCount++;
    }

    private short putInfo(AbsConstantPoolInfo info, boolean unique) {
        if (unique) return putInfo(info);
        hash2Index.putIfAbsent(info.hashCode(), constantPoolCount);
        pool.add(info);
        return constantPoolCount++;
    }


    @Override
    public AbsConstantPoolInfo get(int index) {
        if (index == 0 || index >= constantPoolCount) {
            throw new RuntimeException("ConstantPool index must be greater than 0 and less than constantPoolCount," + "index: " + index);
        }
        return pool.get(index - 1);
    }

    public String getUtf8(int index) {
        AbsConstantPoolInfo absConstantPoolInfo = get(index);
        if (! (absConstantPoolInfo instanceof ConstantPoolUtf8Info))
            throw new RuntimeException("constantPool[" + index + "] not a utf8_info");
        return ((ConstantPoolUtf8Info) absConstantPoolInfo).getLiteral();
    }

    public String getUtf8OfClassInfo(int index) {
        AbsConstantPoolInfo absConstantPoolInfo = get(index);
        if (! (absConstantPoolInfo instanceof ConstantPoolClassInfo))
            throw new RuntimeException("constantPool[index] not a class_info");
        return getUtf8(ConvertTool.B2S(absConstantPoolInfo.getValue()));
    }


    @Override
    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putShort(constantPoolCount);
        for (int i = 0; i < constantPoolCount - 1; i++) {
            byteVectors.putArray(pool.get(i).toByteArray());
        }
        return byteVectors.toByteArray();
    }

    @Override
    public ConstantPool visit(ConstantPool constantPool, ByteVector byteVector) {
        int count = byteVector.getShort();
        for (int i = 0; i < count - 1; i++) {
            resole(byteVector);
        }
        return this;
    }

    public ConstantPool visit(ByteVector byteVector, boolean complete) {
        visit(this, byteVector);
        if (complete) {
            complete();
        }
        return this;
    }

    public void complete() {
        for (AbsConstantPoolInfo info : pool) {
            if (info instanceof SymbolicReferenceConstantPoolInfo) {
                ((SymbolicReferenceConstantPoolInfo) info).ldc(this);
            }
        }
    }

    private void resole(ByteVector byteVector) {
        byte tag = byteVector.getByte();
        switch (tag) {
            case 1:
                putInfo(new ConstantPoolUtf8Info(new String(byteVector.getArray(byteVector.getShort()))), false);
                break;
            case 3:
                putInfo(new ConstantPoolIntegerInfo(byteVector.getInt()), false);
                break;
            case 4:
                putInfo(new ConstantPoolFloatInfo(ConvertTool.B2F(byteVector.getArray(4))), false);
                break;
            case 5:
                putInfo(new ConstantPoolLongInfo(byteVector.getLong()), false);
                break;
            case 6:
                putInfo(new ConstantPoolDoubleInfo(ConvertTool.B2D(byteVector.getArray(8))), false);
                break;
            case 7:
                putInfo(new ConstantPoolClassInfo(byteVector.getArray(2)), false);
                break;
            case 8:
                putInfo(new ConstantPoolStringInfo(byteVector.getArray(2)), false);
                break;
            case 9:
                putInfo(new ConstantPoolFieldrefInfo(byteVector.getArray(4)), false);
                break;
            case 10:
                putInfo(new ConstantPoolMethodrefInfo(byteVector.getArray(4)), false);
                break;
            case 11:
                putInfo(new ConstantPoolInterfaceMethodrefInfo(byteVector.getArray(4)), false);
                break;
            case 12:
                putInfo(new ConstantPoolNameAndTypeInfo(byteVector.getArray(4)), false);
                break;
            case 15:
                putInfo(new ConstantPoolMethodHandleInfo(byteVector.getByte(), byteVector.getArray(2)), false);
                break;
            case 16:
                putInfo(new ConstantPoolMethodTypeInfo(byteVector.getArray(2)), false);
                break;
            case 18:
                putInfo(new ConstantPoolInvokeDynamicInfo(byteVector.getArray(4)), false);
                break;
            default:
                throw new RuntimeException("error byteArray of constantPool");
        }
    }


    public short resolveConstantPoolInfo(@NotNull AbsConstantPoolInfo scpi) {
        if (Objects.isNull(scpi)) {
            throw new NotNullException("args0 must be not null");
        }
        return scpi.load(this);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("count : ").append(constantPoolCount).append("\n");
        for (int i = 0; i < constantPoolCount - 1; i++) {
            AbsConstantPoolInfo absConstantPoolInfo = pool.get(i);
            sb.append(String.format("[%02d]", i + 1));
            sb.append(String.format("%-35s", absConstantPoolInfo.getTag()));
            if (absConstantPoolInfo instanceof LiteralConstantPoolInfo) {
                LiteralConstantPoolInfo literal = ((LiteralConstantPoolInfo) absConstantPoolInfo);
                sb.append(" ").append(literal.literalToString()).append("\n");
            } else {
                if (absConstantPoolInfo instanceof SymbolicReferenceConstantPoolInfo) {
                    sb.append(" ").append(((SymbolicReferenceConstantPoolInfo) absConstantPoolInfo).valueToString()).append("\n");
                }
            }
        }
        return sb.toString();
    }

}
