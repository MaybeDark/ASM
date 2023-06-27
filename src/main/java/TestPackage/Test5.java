package TestPackage;

import org.Access;
import org.Type;
import org.bytecode.BytecodeFactory;

import static org.Access.*;

@Deprecated
public  class Test5 {


    public static void main(String[] args) {

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