package TestPackage;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    private String name;
    private int age;
    private static int num;
    Test(String name){
        this.name = name;
    }

    public  String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        int a = age + 1;
    }

    public void add(int a, int b, int[] c, long d, double e) {
    }

    private static byte[] readClass(final InputStream is, boolean close)
            throws IOException {
        if (is == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] b = new byte[is.available()];
            int len = 0;
            while (true) {
                int n = is.read(b, len, b.length - len);
                if (n == - 1) {
                    if (len < b.length) {
                        byte[] c = new byte[len];
                        System.arraycopy(b, 0, c, 0, len);
                        b = c;
                    }
                    return b;
                }
                len += n;
                if (len == b.length) {
                    int last = is.read();
                    if (last < 0) {
                        return b;
                    }
                    byte[] c = new byte[b.length + 1000];
                    System.arraycopy(b, 0, c, 0, len);
                    c[len++] = (byte) last;
                    b = c;
                }
            }
        } finally {
            if (close) {
                is.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        FileInputStream is = new FileInputStream("C:/Users/12923/Desktop/ClazzBuilder/target/classes/org/bytecode/constantpool/ConstantPool.class");
//        byte[] bytes = readClass(is, true);
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.printf("%3d, ",bytes[i]);
//            if (i != 0 && i % 64 == 0){
//                System.out.println();
//            }
//        }
        ClassReader classReader = new ClassReader(new FileInputStream("C:/Users/12923/Desktop/ClazzBuilder/target/classes/org/bytecode/constantpool/ConstantPool.class"));
        ClassWriter classWriter = new ClassWriter(Opcodes.ASM5);
        classReader.accept(classWriter, 0);
        byte[] bytes = classWriter.toByteArray();
        for (int i = 0; i < bytes.length; i++) {
            System.out.printf("%3d, ", bytes[i]);
            if (i != 0 && i % 64 == 0) {
                System.out.println();
            }
        }
//        ClassWriter classWriter = new ClassWriter(0);
//        classWriter.visitSource("Test3.java",null);
//        classWriter.visit(V1_8,ACC_PUBLIC,"TestPackage.Test3",null,"java/lang/Object",null);
//
//        classWriter.visit(V1_8,ACC_ABSTRACT+ACC_INTERFACE+ACC_PUBLIC,"TestPackage.Test3",null,"java/lang/Object",null);
//
//        classWriter.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "a", "I",null,1).visitEnd();
//        classWriter.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "b", "I",null,0).visitEnd();
//        classWriter.visitMethod(ACC_PUBLIC+ACC_ABSTRACT, "printInfo", "()V", null, null)
//                .visitEnd();
//
//        classWriter.visitEnd();
//        byte[] b = classWriter.toByteArray();
//        Class<?> aClass = new TestPackage.MyClassLoader().findClass(null, b, 0, b.length);
//        System.out.println(aClass.getMethods()[0].getName());
//        System.out.println(Arrays.toString(b));
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream("Test3.class");
//            fos.write(b);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ClassReader cr = new ClassReader(TestPackage.MyClassLoader.getSystemResourceAsStream("TestPackage.Test3.class"));
//        ClassWriter cw = new ClassWriter(0);
//        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//                return super.visitMethod(access, name, desc, signature, exceptions);
//            }
//        };
//        cr.accept(cv,0);
//        byte[] bytes = cw.toByteArray();
//
//        Class<?> aClass = new TestPackage.MyClassLoader().findClass(null, bytes, 0, bytes.length);
    }
}
