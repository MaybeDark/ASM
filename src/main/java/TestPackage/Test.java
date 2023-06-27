package TestPackage;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

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
    public void setName(String name){
        this.name = name;
    }
    public void setAge(int age){
       int a = age + 1;
    }

    public void add(int a,int b,int[] c,long d,double e){
    }


    public static void main(String[] args) throws IOException {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visitSource("Test3.java",null);
        classWriter.visit(V1_8,ACC_PUBLIC,"TestPackage.Test3",null,"java/lang/Object",null);

        classWriter.visit(V1_8,ACC_ABSTRACT+ACC_INTERFACE+ACC_PUBLIC,"TestPackage.Test3",null,"java/lang/Object",null);

        classWriter.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "a", "I",null,1).visitEnd();
        classWriter.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "b", "I",null,0).visitEnd();
        classWriter.visitMethod(ACC_PUBLIC+ACC_ABSTRACT, "printInfo", "()V", null, null)
                .visitEnd();

        classWriter.visitEnd();
        byte[] b = classWriter.toByteArray();
//        Class<?> aClass = new TestPackage.MyClassLoader().findClass(null, b, 0, b.length);
//        System.out.println(aClass.getMethods()[0].getName());
        System.out.println(Arrays.toString(b));
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream("TestPackage.Test3.class");
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
