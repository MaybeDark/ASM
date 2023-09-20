//package org.out;
//
//import org.Access;
//import org.bytecode.constantpool.ConstantPool;
//import org.tools.ByteVector;
//
//import java.util.Arrays;
//
//public class ClassVisitor extends ClassWriter {
//    public static final byte[] classFileHeader = {-54,-2,-70,-66};
//    private ConstantPool constantPool;
//    private int V1_8 = 0 << 16 | 52;;
//
//    private ClassVisitor(int access, String fullClassName) {
//        super(access, fullClassName);
//    }
//
//    public static ClassWriter visit(ByteVector byteVector){
//        checkHeader(byteVector.getArray(4));
//        int version = byteVector.getInt();
//        ConstantPool cp = new ConstantPoolVisitor().visit(byteVector);
//        constantPool = cp;
//        short access = byteVector.getShort();
//        String classAccess = Access.parseClassAccess(access);
//
//    }
//
//
//    private static void checkHeader(byte[] header){
//        if (Arrays.equals(header,classFileHeader)) {
//            return;
//        }
//        throw new RuntimeException("error file type");
//    }
//}
