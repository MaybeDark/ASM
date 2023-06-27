package org.bytecode.constantpool;

import org.bytecode.cls.attribute.bootstrapmethod.BootstrapMethod;
import org.bytecode.cls.attribute.bootstrapmethod.BootstrapMethods;
import org.bytecode.constantpool.info.*;
import org.other.Pool;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class ConstantPool extends AbsConstantPool implements Pool {
    public final static short defaultSize = 32;
    public static int expansionFactor = 2;
    public short currentSize = defaultSize;
    AbsConstantPoolInfo[] pool;
    Map<Integer, Short> hash2Index = new HashMap<>();

    public ConstantPool() {
        constantPoolCount = 1;
        pool = new AbsConstantPoolInfo[defaultSize];
    }

    private ConstantPool(short constantPoolCount) {
        this.constantPoolCount = constantPoolCount;
        while (constantPoolCount >= currentSize) {
            currentSize *= expansionFactor;
        }
        pool = new AbsConstantPoolInfo[currentSize];
    }


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
    public short putClassInfo(String fullClassName) {
        short index = putUtf8Info(fullClassName);
        byte[] ref = ConvertTool.S2B(index);
        ConstantPoolClassInfo info = new ConstantPoolClassInfo(fullClassName, ref);
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
    public short putFieldrefInfo(String fullClassName, String fieldName, String fieldDesc) {
        short classInfoIndex = putClassInfo(fullClassName);
        short nameAndTypeInfoIndex = putNameAndTypeInfo(fieldName, fieldDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(classInfoIndex), ConvertTool.S2B(nameAndTypeInfoIndex));
        ConstantPoolFieldrefInfo info = new ConstantPoolFieldrefInfo(fullClassName, fieldName, fieldDesc, ref);
        return putInfo(info);
    }

    @Override
    public short putMethodrefInfo(String fullClassName, String methodName, String methodDesc) {
        short classInfoIndex = putClassInfo(fullClassName);
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(classInfoIndex), ConvertTool.S2B(nameAndTypeInfoIndex));
        ConstantPoolMethodrefInfo info = new ConstantPoolMethodrefInfo(fullClassName, methodName, methodDesc, ref);
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
    public short putMethodHandleInfo(ReferenceKind referenceKind, String fullClassName, String name, String desc) {
        short infoIndex;
        switch (referenceKind) {
            case REF_getField:
            case REF_getStatic:
            case REF_putField:
            case REF_putStatic:
                infoIndex = putFieldrefInfo(fullClassName, name, desc);
                break;
            case REF_invokeInterface:
                infoIndex = putInterfaceMethodrefInfo(fullClassName, name, desc);
                break;
            case REF_newInvokeSpecial:
                if (!name.equals("<init>"))
                    throw new RuntimeException("if ReferenceKind is REF_newInvokeSpecial,the methodName must be <init>");
            case REF_invokeVirtual:
            case REF_invokeSpecial:
            case REF_invokeStatic:
                infoIndex = putMethodrefInfo(fullClassName, name, desc);
                break;
            default:
                throw new RuntimeException("No support this ReferenceKind");
        }
        byte type = (byte) referenceKind.getKey();
        byte[] ref = ConvertTool.S2B(infoIndex);
        ConstantPoolMethodHandleInfo info = new ConstantPoolMethodHandleInfo(type, fullClassName, name, desc, ref);
        return putInfo(info);
    }

    @Override
    public short putInvokeDynamicInfo(BootstrapMethods bmp, String methodName, String methodDesc, BootstrapMethod bootStrapMethod) {
        bootStrapMethod.setValue(resolveBootstrapMethod(bootStrapMethod));
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        short bootStrapMethodIndex = bmp.putBootStrapMethod(bootStrapMethod);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(nameAndTypeInfoIndex), ConvertTool.S2B(bootStrapMethodIndex));
        ConstantPoolInvokeDynamicInfo info = new ConstantPoolInvokeDynamicInfo(methodName, methodDesc, ref);
        return putInfo(info);
    }

    public short putInvokeDynamicInfo(String methodName, String methodDesc, short bootStrapMethodIndex) {
        short nameAndTypeInfoIndex = putNameAndTypeInfo(methodName, methodDesc);
        byte[] ref = ArrayTool.join(ConvertTool.S2B(nameAndTypeInfoIndex), ConvertTool.S2B(bootStrapMethodIndex));
        ConstantPoolInvokeDynamicInfo info = new ConstantPoolInvokeDynamicInfo(methodName, methodDesc, ref);
        return putInfo(info);
    }

    @Override
    public AbsConstantPoolInfo get(int index) {
        if (index == 0 || index >= constantPoolCount) {
            throw new RuntimeException("ConstanlPool index must be greater than 0 and less than constantPoolCount");
        }
        return pool[index - 1];
    }

    @Override
    public byte[] toByteArray() {
        int size = 0;
        size += 2; //length mark;
        for (int i = 0; i < constantPoolCount - 1; i++) {
            size += pool[i].getLength();
        }
        byte[] result = new byte[size];
        int length = 0;
        System.arraycopy(ConvertTool.S2B(constantPoolCount), 0, result, 0, 2);
        length += 2;
        for (int i = 0; i < constantPoolCount - 1; i++) {
            System.arraycopy(pool[i].toByteArray(), 0, result, length, pool[i].getLength());
            length += pool[i].getLength();
        }
        return result;
    }

    private short putInfo(AbsConstantPoolInfo info) {
        considerExpansion();
        Short value = hash2Index.putIfAbsent(info.hashCode(), constantPoolCount);
        if (value != null && value != constantPoolCount) {
            return value;
        }
        pool[constantPoolCount - 1] = info;
        return constantPoolCount++;
    }

    public short resolveConstantPoolInfo(AbsConstantPoolInfo scpi) {
        short infoIndex = 0;
        switch (scpi.getType()) {
            case CONSTANT_InterfaceMethodref_info:
                ConstantPoolInterfaceMethodrefInfo cpimr = (ConstantPoolInterfaceMethodrefInfo) scpi;
                infoIndex = putInterfaceMethodrefInfo(cpimr.getFullInterfaceName(), cpimr.getMethodName(), cpimr.getMethodDesc());
                break;
            case CONSTANT_InvokeDynamic_info:
                throw new RuntimeException("this method no support resolve this info");
            case CONSTANT_Methodref_info:
                ConstantPoolMethodrefInfo cpmri = (ConstantPoolMethodrefInfo) scpi;
                infoIndex = putMethodrefInfo(cpmri.getFullClassName(), cpmri.getMethodName(), cpmri.getMethodDesc());
                break;
            case CONSTANT_NameAndType_info:
                ConstantPoolNameAndTypeInfo cpnati = (ConstantPoolNameAndTypeInfo) scpi;
                infoIndex = putNameAndTypeInfo(cpnati.getName(), cpnati.getDesc());
                break;
            case CONSTANT_Utf8_info:
                ConstantPoolUtf8Info cpui = (ConstantPoolUtf8Info) scpi;
                infoIndex = putUtf8Info(cpui.literalToString());
                break;
            case CONSTANT_Fieldref_info:
                ConstantPoolFieldrefInfo cpfri = (ConstantPoolFieldrefInfo) scpi;
                infoIndex = putFieldrefInfo(cpfri.getFullClassName(), cpfri.getFieldName(), cpfri.getFileDesc());
                break;
            case CONSTANT_Class_info:
                ConstantPoolClassInfo cpci = (ConstantPoolClassInfo) scpi;
                infoIndex = putClassInfo(cpci.getFullClassName());
                break;
            case CONSTANT_Double_info:
                ConstantPoolDoubleInfo cpdi = (ConstantPoolDoubleInfo) scpi;
                infoIndex = putDoubleInfo(Double.parseDouble(cpdi.literalToString()));
                break;
            case CONSTANT_Float_info:
                ConstantPoolFloatInfo cpfi = (ConstantPoolFloatInfo) scpi;
                infoIndex = putFloatInfo(Float.parseFloat(cpfi.literalToString()));
                break;
            case CONSTANT_Integer_info:
                ConstantPoolIntegerInfo cpii = (ConstantPoolIntegerInfo) scpi;
                infoIndex = putIntegerInfo(Integer.parseInt(cpii.literalToString()));
                break;
            case CONSTANT_Long_info:
                ConstantPoolLongInfo cpli = (ConstantPoolLongInfo) scpi;
                infoIndex = putLongInfo(Long.parseLong(cpli.literalToString()));
                break;
            case CONSTANT_MethodHandle_info:
                ConstantPoolMethodHandleInfo cpmhi = (ConstantPoolMethodHandleInfo) scpi;
                infoIndex = putMethodHandleInfo(cpmhi.getKind(), cpmhi.getFullClassName(), cpmhi.getName(), cpmhi.getDesc());
                break;
            case CONSTANT_MethodType_info:
                ConstantPoolMethodTypeInfo cpmti = (ConstantPoolMethodTypeInfo) scpi;
                infoIndex = putMethodTypeInfo(cpmti.getMethodDesc());
                break;
            case CONSTANT_String_info:
                ConstantPoolStringInfo cpsi = (ConstantPoolStringInfo) scpi;
                infoIndex = putStringInfo(cpsi.getLiteral());
                break;
        }
        return infoIndex;
    }

    public byte[] resolveBootstrapMethod(BootstrapMethod bm) {
        int argsIndex = 0;
        int argsCount = bm.getArgsCount();
        byte[] result = new byte[2 + 2 + argsCount * 2];

        int methodHandleInfoIndex = putMethodHandleInfo(bm.getReferenceKind(), bm.getFullClassName(), bm.getMethodName(), bm.getMethodDesc());
        System.arraycopy(ConvertTool.S2B((short) methodHandleInfoIndex), 0, result, 0, 2);
        System.arraycopy(ConvertTool.S2B((short) argsCount), 0, result, 2, 2);
        List<Parameterizable> args = bm.getArgs();
        if (args == null) {
            return result;
        }
        for (int i = 0; i < argsCount; i++) {
            Parameterizable arg = args.get(i);
            int infoIndex = resolveConstantPoolInfo((AbsConstantPoolInfo) arg);
            args.set(i, (Parameterizable) get(infoIndex));
            System.arraycopy(ConvertTool.S2B((short) infoIndex), 0, result, 4 + 2 * (argsIndex++), 2);
        }
        return result;
    }

    private void considerExpansion() {
        if (constantPoolCount <= currentSize)
            return;
        if (currentSize * expansionFactor >= (1 << 15)) {
            throw new RuntimeException("pool too large");
        }
        AbsConstantPoolInfo[] newPool = new AbsConstantPoolInfo[currentSize * expansionFactor];
        System.arraycopy(pool, 0, newPool, 0, currentSize);
        currentSize *= expansionFactor;
        pool = newPool;
    }

    @Override
    public AbsConstantPoolInfo[] getPool() {
        AbsConstantPoolInfo[] result = new AbsConstantPoolInfo[constantPoolCount - 1];
        System.arraycopy(pool, 0, result, 0, constantPoolCount - 1);
        return result;
    }

    public String list() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < constantPoolCount - 1; i++) {
            AbsConstantPoolInfo absConstantPoolInfo = pool[i];
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
