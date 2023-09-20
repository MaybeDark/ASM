package org.out;

import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.ReferenceKind;
import org.bytecode.constantpool.info.*;
import org.tools.ByteVector;
import org.tools.ConvertTool;

public class ConstantPoolVisitor {
    private short constantPoolCount;
    private ConstantPool cp;
    private ByteVector byteVector;

    public ConstantPool visit(ByteVector byteVector){
        this.byteVector = byteVector;
        constantPoolCount = byteVector.getShort();
        for (int i = 0; i < constantPoolCount - 1; i++) {
            byte tag = byteVector.getByte();
            resole(tag);
        }
        return cp;
    }

    public void resole(byte tag){
        switch (tag) {
            case 1:
                short length = byteVector.getShort();
                String str = new String(byteVector.getArray(length));
                cp.putUtf8Info(str);
                break;
            case 3:
                int iValue = byteVector.getInt();
                cp.putIntegerInfo(iValue);
                break;
            case 4:
                float fValue = ConvertTool.B2F(byteVector.getArray(4));
                cp.putFloatInfo(fValue);
                break;
            case 5:
                long lValue = byteVector.getLong();
                cp.putLongInfo(lValue);
                break;
            case 6:
                double dValue = ConvertTool.B2D(byteVector.getArray(8));
                cp.putDoubleInfo(dValue);
                break;
            case 7:
                short nameCpIndex = byteVector.getShort();
                ConstantPoolUtf8Info constantPoolUtf8Info = (ConstantPoolUtf8Info) cp.get(nameCpIndex);
                String clazzName = constantPoolUtf8Info.getLiteral();
                cp.putClassInfo(clazzName);
                break;
            case 8:
                short strCpIndex = byteVector.getShort();
                String sValue = ((ConstantPoolUtf8Info) cp.get(strCpIndex)).getLiteral();
                cp.putStringInfo(sValue);
                break;
            case 9:
                short classCpIndex = byteVector.getShort();
                short nameAndTypeCpIndex = byteVector.getShort();
                String className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getFullClassName();
                ConstantPoolNameAndTypeInfo cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
                cp.putFieldrefInfo(className, cpnati.getName(), cpnati.getDesc());
                break;
            case 10:
                classCpIndex = byteVector.getShort();
                nameAndTypeCpIndex = byteVector.getShort();
                className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getFullClassName();
                cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
                cp.putMethodrefInfo(className, cpnati.getName(), cpnati.getDesc());
                break;
            case 11:
                classCpIndex = byteVector.getShort();
                nameAndTypeCpIndex = byteVector.getShort();
                className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getFullClassName();
                cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
                cp.putInterfaceMethodrefInfo(className, cpnati.getName(), cpnati.getDesc());
                break;
            case 12:
                short nameIndex = byteVector.getShort();
                short descIndex = byteVector.getShort();
                String name = ((ConstantPoolUtf8Info) cp.get(nameIndex)).getLiteral();
                String desc = ((ConstantPoolUtf8Info) cp.get(descIndex)).getLiteral();
                cp.putNameAndTypeInfo(name, desc);
                break;
            case 15:
                ReferenceKind kind = ReferenceKind.get(byteVector.getByte());
                short ref = byteVector.getShort();
                switch (kind) {
                    case REF_getField:
                    case REF_getStatic:
                    case REF_putField:
                    case REF_putStatic:
                        ConstantPoolFieldrefInfo cpfi = (ConstantPoolFieldrefInfo) cp.get(ref);
                        className = cpfi.getFullClassName();
                        name = cpfi.getFieldName();
                        desc = cpfi.getFileDesc();
                        break;
                    case REF_invokeInterface:
                        ConstantPoolInterfaceMethodrefInfo cpimi = (ConstantPoolInterfaceMethodrefInfo) cp.get(ref);
                        className = cpimi.getFullInterfaceName();
                        name = cpimi.getMethodName();
                        desc = cpimi.getMethodDesc();
                        break;
                    case REF_newInvokeSpecial:
                    case REF_invokeVirtual:
                    case REF_invokeSpecial:
                    case REF_invokeStatic:
                        ConstantPoolMethodrefInfo cpmi = (ConstantPoolMethodrefInfo) cp.get(ref);
                        className = cpmi.getFullClassName();
                        name = cpmi.getMethodName();
                        desc = cpmi.getMethodDesc();
                        break;
                    default:
                        throw new RuntimeException("No support this ReferenceKind");
                }
                cp.putMethodHandleInfo(kind, className, name, desc);
                break;
            case 16:
                descIndex = byteVector.getShort();
                ConstantPoolUtf8Info cpui = (ConstantPoolUtf8Info) cp.get(descIndex);
                cp.putMethodTypeInfo(cpui.getLiteral());
                break;
            case 18:
                nameAndTypeCpIndex = byteVector.getShort();
                ConstantPoolNameAndTypeInfo cpnti = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
                short bootstrapMethodIndex = byteVector.getShort();
                cp.putInvokeDynamicInfo(cpnti.getName(), cpnti.getDesc(),bootstrapMethodIndex);
                break;
            default:
                throw new RuntimeException("error byteArray of constantPool");
        }
    }

}
