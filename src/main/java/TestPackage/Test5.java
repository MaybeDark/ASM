package TestPackage;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.clazz.SourceFile;
import org.bytecode.field.FieldWriter;
import org.bytecode.method.MethodWriter;
import org.bytecode.attributes.method.code.attrubute.LineNumberTable;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.MethodWrapper;

import java.util.Arrays;

import static org.Access.*;

@Deprecated
public  class Test5 {

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
//        FieldWrapper a = new FieldWrapper(Test6.class.getDeclaredField("A"));
        MethodWrapper say = new MethodWrapper(Test6.class.getConstructor());
        ClassWriter classWriter = new ClassWriter(ACC_PUBLIC+ACC_SUPER,"TestPackage/Test6");
        FieldWriter A = classWriter.addField(0, "A", Type.STRING);
        LineNumberTable lineNumberTable = new LineNumberTable().put(0,6).put(6,7).put(16,8);
        MethodWriter methodWriter = classWriter.addInterfaces(Say.class)
                .addMethod(ACC_PUBLIC, "say", Type.STRING)
                .loadLocal("this")
                .loadString("张三")
                .storeField(A.wrapper(), false)
                .loadField(new FieldWrapper("java/lang/System", "out", Type.getType("Ljava/io/PrintStream;")), true)
                .loadLocal("this")
                .loadField(A.wrapper(), false)
                .invokeVirtual(new MethodWrapper("java/io/PrintStream", "println", Type.VOID, Type.STRING))
                .loadLocal("this")
                .loadField(A.wrapper(), false);
        methodWriter.getCode().addAttribute(lineNumberTable);
        methodWriter.endMethod();
        System.out.println(Arrays.toString(classWriter.toByteArray()));
        
//        MethodWriter methodWriter3 = classWriter.addAttribute(new SourceFile("Test6.java"))
//                .addConstructor(ACC_PUBLIC, null);
////
//        methodWriter3.getCode()
//                .addAttribute(new LineNumberTable().put(0,6));
//        methodWriter3.endMethod().toByteArray();
//        MethodPool methods = classWriter.getMethods();
//        Method method1 = methods.getByName("say").get(0);
//        Method method2 = methods.getByName("<init>").get(0);
//        method1.load(classWriter.getConstantPool());
//        method1.load(classWriter.getConstantPool());
        System.out.println(classWriter.getConstantPool().print());

//        String print = method.print();
//        System.out.println(print);
//        String print1 = method3.print();
//        System.out.println(print1);
//        System.out.println(Arrays.toString(methodWriter.toByteArray()));
//        System.out.println(Arrays.toString(methodWriter3.toByteArray()));

//        System.out.println(Arrays.toString(method1.toByteArray()));

//        System.out.println(method.print());

//        byte[] bytes = classWriter.toByteArray();
//        System.out.println(Arrays.toString(bytes));
//        System.out.println(classWriter.getConstantPool().print());

//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream("Test6.class");
//            fos.write(bytes);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        byte[] bytes = classWriter.toByteArray();
//        System.out.println(Arrays.toString(bytes));
//        byte[] bytes = ConvertTool.F2B(3.2F);
//        System.out.println(ConvertTool.B2F(bytes));
//        int[][][] ints = new int[10][][];
//        System.out.println(Arrays.toString(ints[0]));

//        Code code = new Code(Type.VOID,Type.INT);
//        code.multiANewArray(Type.INT,2,10,-1);
//        if(ints instanceof int[][][]){
//
//        }
//        Type type = Type.getType(String.class);
//        System.out.println(type.getDescriptor());

//        BytecodeFactory.newClass(ACC_PUBLIC,"TestPackage/Test3");
//        Thread.currentThread().stop();
//        Object o = new Object();
//        Test t = (Test) o;
//        ConstantPool constantPool = new ConstantPool();
//        MethodPool methodPool = new MethodPool();
//
//        Method method1 = new Method(Access.get(Access.ACC_PUBLIC), "Test0", "say", null, new LocalVariableWrapper("a", Type.getType(String.class)));
//        Method method2 = new Method(Access.get(Access.ACC_PUBLIC), "Test0", "say", null, new LocalVariableWrapper("a", Type.INT));

//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        forkJoinPool.execute(()->{
//            System.out.println(Thread.currentThread().isDaemon());
//        });

//        Code code1 = method1.getCode();
//        code1.loadFromConstant(InstructionSet.ICONST_1);
//        code1.storeToLocals("a");
//        code1.endCode();
//
//        Code code2 = method2.getCode();
//        code2.loadFromConstant(InstructionSet.ICONST_1);
//        code2.storeToLocals("a");
//        code2.endCode();
//
//        methodPool.putMethod(method1);
//        methodPool.putMethod(method2);
//        methodPool.load(constantPool);
//        byte[] bytes = methodPool.toByteArray();
//        System.out.println(Arrays.toString(bytes));
//        ConstantPool load = ConstantPoolTool.load(new ByteArrayIterator(constantPool.toByteArray()));
//        System.out.println(load.list());
//        String s = InstructionSet.LRETURN.toString();
//        System.out.println(s);
//        InstructionSet if_icmpne = InstructionSet.valueOf("IF_ICMPNE");
//        System.out.println(if_icmpne);

//        byte[] bytes = method1.toByteArray();
//        System.out.println(Arrays.toString(bytes));
      //        Method method3 = new Method(Access.get(Access.ACC_PUBLIC), "Test0", "say", null, new LocalVariableWrapper("a", Type.DOUBLE));
//        Method method4 = new Method(Access.get(Access.ACC_PUBLIC), "Test0", "say", null, new LocalVariableWrapper("a", Type.FLOAT));

//        MethodPool methodPool = new MethodPool();
//            methodPool.putMethod(method1);
//            methodPool.putMethod(method2);
//            methodPool.putMethod(method3);
//            methodPool.putMethod(method4);
//            List<Method> test0 = methodPool.getByName("say");
//            test0.forEach(System.out::println);
    }
}