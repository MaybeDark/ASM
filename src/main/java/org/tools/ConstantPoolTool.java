package org.tools;

//import org.bytecode.constantpool.ConstantPool;
//import org.bytecode.constantpool.ReferenceKind;
//import org.bytecode.constantpool.info.*;
//
//import java.nio.charset.StandardCharsets;
//
//public class ConstantPoolTool {
//    public static ConstantPool load(ByteArrayIterator bai) {
//        ConstantPool cp = new ConstantPool();
//        short index = 1;
//        short count = ConvertTool.B2S(bai.next(2));
//        while (index < count) {
//            switch (bai.next()) {
//                case 1:
//                    short length = ConvertTool.B2S(bai.next(2));
//                    String str = new String(bai.next(length), StandardCharsets.UTF_8);
//                    cp.putUtf8Info(str);
//                    index++;
//                    break;
//                case 3:
//                    int iValue = ConvertTool.B2I(bai.next(4));
//                    cp.putIntegerInfo(iValue);
//                    index++;
//                    break;
//                case 4:
//                    float fValue = ConvertTool.B2F(bai.next(4));
//                    cp.putFloatInfo(fValue);
//                    index++;
//                    break;
//                case 5:
//                    long lValue = ConvertTool.B2L(bai.next(8));
//                    cp.putLongInfo(lValue);
//                    index++;
//                    break;
//                case 6:
//                    double dValue = ConvertTool.B2D(bai.next(8));
//                    cp.putDoubleInfo(dValue);
//                    index++;
//                    break;
//                case 7:
//                    short nameCpIndex = ConvertTool.B2S(bai.next(2));
//                    ConstantPoolUtf8Info constantPoolUtf8Info = (ConstantPoolUtf8Info) cp.get(nameCpIndex);
//                    String clazzName = constantPoolUtf8Info.getLiteral();
//                    cp.putClassInfo(clazzName);
//                    index++;
//                    break;
//                case 8:
//                    short strCpIndex = ConvertTool.B2S(bai.next(2));
//                    String sValue = ((ConstantPoolUtf8Info) cp.get(strCpIndex)).getLiteral();
//                    cp.putStringInfo(sValue);
//                    index++;
//                    break;
//                case 9:
//                    short classCpIndex = ConvertTool.B2S(bai.next(2));
//                    short nameAndTypeCpIndex = ConvertTool.B2S(bai.next(2));
//                    String className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getClassInfo();
//                    ConstantPoolNameAndTypeInfo cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
//                    cp.putFieldrefInfo(className, cpnati.getName(), cpnati.getDesc());
//                    index++;
//                    break;
//                case 10:
//                    classCpIndex = ConvertTool.B2S(bai.next(2));
//                    nameAndTypeCpIndex = ConvertTool.B2S(bai.next(2));
//                    className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getClassInfo();
//                    cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
//                    cp.putMethodrefInfo(className, cpnati.getName(), cpnati.getDesc());
//                    index++;
//                    break;
//                case 11:
//                    classCpIndex = ConvertTool.B2S(bai.next(2));
//                    nameAndTypeCpIndex = ConvertTool.B2S(bai.next(2));
//                    className = ((ConstantPoolClassInfo) cp.get(classCpIndex)).getClassInfo();
//                    cpnati = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
//                    cp.putInterfaceMethodrefInfo(className, cpnati.getName(), cpnati.getDesc());
//                    index++;
//                    break;
//                case 12:
//                    short nameIndex = ConvertTool.B2S(bai.next(2));
//                    short descIndex = ConvertTool.B2S(bai.next(2));
//                    String name = ((ConstantPoolUtf8Info) cp.get(nameIndex)).getLiteral();
//                    String desc = ((ConstantPoolUtf8Info) cp.get(descIndex)).getLiteral();
//                    cp.putNameAndTypeInfo(name, desc);
//                    index++;
//                    break;
//                case 15:
//                    ReferenceKind tag = ReferenceKind.get(bai.next());
//                    short ref = ConvertTool.B2S(bai.next(2));
//                    switch (tag) {
//                        case REF_getField:
//                        case REF_getStatic:
//                        case REF_putField:
//                        case REF_putStatic:
//                            ConstantPoolFieldrefInfo cpfi = (ConstantPoolFieldrefInfo) cp.get(ref);
//                            className = cpfi.getClassInfo();
//                            name = cpfi.getFieldName();
//                            desc = cpfi.getFileDesc();
//                            break;
//                        case REF_invokeInterface:
//                            ConstantPoolInterfaceMethodrefInfo cpimi = (ConstantPoolInterfaceMethodrefInfo) cp.get(ref);
//                            className = cpimi.getInterfaceInfo();
//                            name = cpimi.getMethodName();
//                            desc = cpimi.getMethodDesc();
//                            break;
//                        case REF_newInvokeSpecial:
//                        case REF_invokeVirtual:
//                        case REF_invokeSpecial:
//                        case REF_invokeStatic:
//                            ConstantPoolMethodrefInfo cpmi = (ConstantPoolMethodrefInfo) cp.get(ref);
//                            className = cpmi.getClassInfo();
//                            name = cpmi.getMethodName();
//                            desc = cpmi.getMethodDesc();
//                            break;
//                        default:
//                            throw new RuntimeException("No support this ReferenceKind");
//                    }
//                    cp.putMethodHandleInfo(tag, className, name, desc);
//                    index++;
//                    break;
//                case 16:
//                    descIndex = ConvertTool.B2S(bai.next(2));
//                    ConstantPoolUtf8Info cpui = (ConstantPoolUtf8Info) cp.get(descIndex);
//                    cp.putMethodTypeInfo(cpui.getLiteral());
//                    index++;
//                    break;
//                case 18:
//                    nameAndTypeCpIndex = ConvertTool.B2S(bai.next(2));
//                    ConstantPoolNameAndTypeInfo cpnti = (ConstantPoolNameAndTypeInfo) cp.get(nameAndTypeCpIndex);
//                    short bootstrapMethodIndex = ConvertTool.B2S(bai.next(2));
//                    cp.putInvokeDynamicInfo(cpnti.getName(), cpnti.getDesc(), bootstrapMethodIndex);
//                    index++;
//                    break;
//                default:
//                    throw new RuntimeException("error byteArray of constantPool");
//            }
//
//        }
//        return cp;
//    }
//}
