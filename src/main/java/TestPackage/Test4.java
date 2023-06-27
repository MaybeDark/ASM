package TestPackage;


import org.InstructionSet;

import java.lang.reflect.Method;

public class Test4 {
    int a = 0;
    int b = a;
    public static void main(String[] args){
        System.out.println(InstructionSet.LXOR.getOpcode());
//        {
//            long d = 8L;
//            long e = 9L;
//            System.out.println(d);
//            if (d > 0) {
//                long b = 0L;
//                long a = 0L;
//                System.out.println(a + b);
//            }
//        }
//        long c = 0L;
//        {
//            long ab = 1L;
//            System.out.println(ab);
//        }
//        System.out.println(c);

//        ClassWrapper classWrapper = new ClassWrapper(Test4.class);
//        String fullClassName = classWrapper.getFullClassName();
//        {
//            int a = 10;
//            System.out.println(a);
//        }
//        Test4 test4 = new Test4();
//        byte a = (byte) 0xB2;
//        System.out.println(a);
//        InstructionSet i2b = InstructionSet.I2B;
//        float a = 1F;
//        float b = 2F;
//        boolean b1 = a == b;
//        Class<Integer[]> testClass = Integer[].class;
//        System.out.println(testClass.getName());
//        System.out.println(Type.getDescriptor(Integer[].class));
//        System.out.println(Type.getDescriptor(Integer.class));
//        System.out.println(Type.getDescriptor(Integer.TYPE));
//        Type l = Type.getType("L");
//        Type d = Type.getType("D");
//        Type type = Type.getType(Type.getDescriptor(Integer.class));
//        Type type1 = Type.getType(Type.getDescriptor(Integer[].class));
//        Type v = Type.getType("V");
//        Type componentType = Type.getComponentType(int[].class);
//        Type componentType1 = Type.getComponentType(Test[].class);
//        Type componentType2 = Type.getComponentType(Type.getType(Test4[].class));
//        Type componentType3 = Type.getComponentType(Type.getType(int[].class));
//        String methodDesc = Type.getMethodDescriptor(Integer.class, Test.class,Integer.class);
//        System.out.println(methodDesc);
//        Type returnType = Type.getReturnType(methodDesc);
//        System.out.println(returnType);
//        Type[] argumentsType = Type.getArgumentTypes(methodDesc);
////        System.out.println(Arrays.toString(argumentsType));
//        System.out.println(Type.getMethodDescriptor(returnType, argumentsType));
//        Type type = Type.getType(Test.class);
//        System.out.println(type);
//        Class<Integer> type = Integer.TYPE;
//        System.out.println(Integer.class.isPrimitive());
//        System.out.println(type.isPrimitive());
//        Class<Test4> test4Class = Test4.class;
//        ConstantPool cp = new ConstantPool();
//        LocalVariableWrapper localVariableWrapper = new LocalVariableWrapper("a", Type.INT);
//        LocalVariableWrapper localVariableWrapper1 = new LocalVariableWrapper("t", Type.getType(Test.class));
//        MethodWrapper methodWrapper = new MethodWrapper(Type.getClassDescriptor(Test.class),"say",Type.INT,null);
//        MethodWrapper methodWrapper1 = new MethodWrapper(Type.getClassDescriptor(Test.class),"say",Type.DOUBLE,null);
//        methodWrapper.load(cp);
//        methodWrapper1.load(cp);
//        ConstantPool constantPool = new ConstantPool();
//        FieldWrapper out = new FieldWrapper("java/lang/System", "out", Type.getType(PrintStream.class));
//        out.load(constantPool);
//        MethodWrapper println = new MethodWrapper(Type.getType(PrintStream.class), "println", Type.getType(Type.getMethodDescriptor(Type.VOID, Type.BOOLEAN)));
//        println.load(constantPool);
//        LocalVariableWrapper localVariableWrapper = new LocalVariableWrapper("args", Type.getType(String[].class));
//        Code code = new Code(null,localVariableWrapper);
//        code.loadFromConstant(InstructionSet.ICONST_1);
//        code.storeToLocals("a");
//        code.loadFromConstant(InstructionSet.ICONST_2);
//        code.storeToLocals("b");
//        code.loadOrSetField(InstructionSet.GETSTATIC,out);
//        code.loadFromLocals((short) 1);
//        code.loadFromLocals((short) 2);
//        code.startIf(InstructionSet.IF_ICMPNE);
//        code.loadFromConstant(InstructionSet.ICONST_1);
//        code.jumpTo((short) 4);
//        code.endIf();
//        code.loadFromConstant(InstructionSet.ICONST_0);
//        code.invokeMethod(InstructionSet.INVOKEVIRTUAL,println);
//        code.endCode();
//        System.out.println(code.list());
//        System.out.println(code.getLocals().list());
//        System.out.println(code.getAttributeLength());
//        Class<Test2> test2Class = Test2.class;
//        Method[] methods = test2Class.getMethods();
//        Method method = methods[0];
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,methodWrapper1);
//        code.startLabel();
//        code.startLabel();
//        code.addStoreInstruction("d1");
//        code.addStoreInstruction("i1");
//        code.endLabel();
//        code.startLabel();
//        code.addStoreInstruction("i2");
//        code.addStoreInstruction("i3");
//        code.addStoreInstruction("d2");
//        code.endLabel();
//        code.addStoreInstruction("d3");
//        code.endCode();
//        LocalVariableTable locals = code.getLocals();
//        locals.load(cp);
//        System.out.println(locals.list());
//        System.out.println(Arrays.toString(locals.toByteArray()));
//          char a = 'a';
//        int count = 0;
//        for (int i = 0; i < 1000; i++) {
//            count += 100;
//        }
//
//        long start = System.nanoTime();
//        for (int j = 0; j < Integer.MAX_VALUE; j++) {
//            int b = j%32;
//            getString(b);
//        }
//        long end = System.nanoTime();
//        System.out.println(end - start);
//        ConstantPool constantPool = new ConstantPool();
//        String s1 = DescTool.typeTransforms(Test5.class);
//        FieldWrapper fieldWrapper = new FieldWrapper(s1,"b",Integer.class);
//        fieldWrapper.load(constantPool);
//        MethodWrapper say = new MethodWrapper(s1, "say", Integer.class, null);
//        say.load(constantPool);
//
//        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,say);
//        code.addStoreInstruction("a");
////        code.addInvokeInstruction(InstructionSet.INVOKESTATIC,say);
////        code.addFieldInstruction(InstructionSet.GETFIELD,fieldWrapper);
//        code.endCode();
//            double a  = 124.1;
//            long d = 1234L;
//            int c = 9999999;
//            float b = 12.4F;

//        {
//            char a = 1;
//            byte b = 2;
//        }
//        short c = 3;
//        double d = 4.5;
//        int[] ir = new int[10];
//        boolean[] br = new boolean[10];
//        long[] lr = new long[10];
//        double[] dr = new double[10];
//        byte[] byr = new byte[10];
//        char[] cr = new char[10];
//        float[] fr = new float[10];
//        Test[] tests = new Test[10];
//        System.out.println(DescTool.getComponentType(ir.getClass()));
//        System.out.println(DescTool.getComponentType(br.getClass()));
//        System.out.println(DescTool.getComponentType(lr.getClass()));
//        System.out.println(DescTool.getComponentType(dr.getClass()));
//        System.out.println(DescTool.getComponentType(byr.getClass()));
//        System.out.println(DescTool.getComponentType(cr.getClass()));
//        System.out.println(DescTool.getComponentType(fr.getClass()));
//        System.out.println(DescTool.getComponentType(tests.getClass()));
//        System.out.println();
//        Class<?> aClass = ar.getClass();
//        Class<?> componentType = aClass.getComponentType();
//        System.out.println(componentType.getName());
//        Class<?> integerClass = Integer.class;
//        System.out.println(integerClass.getName());
//        System.out.println(componentType.equals(Integer.class));

//        Class<?> aClass = a.getClass();
//        System.out.println(aClass.getTypeName());

//        LocalVariableWrapper aThis = new LocalVariableWrapper((short) 4, (short) 3, "this", Test4.class);
//        LocalVariableWrapper aThis1 = new LocalVariableWrapper((short) 9, (short) 12, "a", Integer.class);
//        LocalVariableWrapper aThis4 = new LocalVariableWrapper((short) 2, (short) 20, "a", Double.class);
//        LocalVariableWrapper aThis2 = new LocalVariableWrapper((short) 0, (short) 22, "a", Test3.class);
//        LocalVariableWrapper aThis5 = new LocalVariableWrapper((short) 5, (short) 1, "a", Integer.class);
//        LocalVariableTable localVariableTable = new LocalVariableTable();
//        localVariableTable.put(aThis);
//        localVariableTable.put(aThis1);
//        localVariableTable.put(aThis2);
//        localVariableTable.put(aThis4);
//        localVariableTable.put(aThis5);
//        System.out.println(localVariableTable.list());

//        VariableInfo[] variableInfos = {
//                new ObjectVariableInfo((short) 21),
//                new DoubleVariableInfo(),
//                new TopVariableInfo(),
//                new IntegerVariableInfo(),
//                new IntegerVariableInfo()
//        };
//        StackMapTable stackMapTable = new StackMapTable();
//        FullFrame fullFrame = new FullFrame((short) 20, (short)5, variableInfos, (short)0, null);
//        ChopFrame chopFrame = new ChopFrame((byte) 1, (short) 2);
//        stackMapTable.addStackMapFrame(fullFrame);
//        stackMapTable.addStackMapFrame(chopFrame);
//        stackMapTable.load(constantPool);
//        System.out.println(Arrays.toString(stackMapTable.toByteArray()));
//        System.out.println((byte) 247);

//        BootstrapMethods bootstrapMethods = new BootstrapMethods();
//        constantPool.putClassInfo(DescTool.typeTransforms(Test4.class));
//        constantPool.putClassInfo(DescTool.typeTransforms(Test4.class));
//        constantPool.putClassInfo(DescTool.typeTransforms(Test4.class));
//        constantPool.putDoubleInfo(3.5);
//        constantPool.putFieldrefInfo(DescTool.typeTransforms(Test4.class),"a","I");
//        constantPool.putFloatInfo(4.5F);
//        constantPool.putIntegerInfo(4);
//        constantPool.putInterfaceMethodrefInfo(DescTool.typeTransforms(A.class),"see",DescTool.getMethodDesc(null, (Class<?>) null));
//       constantPool.putLongInfo(1L);
//        constantPool.putStringInfo("张三");
//        constantPool.putUtf8Info("李四");
//        BootStrapMethodTool.creatLambdaMethodInMethod(bootstrapMethods,constantPool,DescTool.typeTransforms(FunctionInterface.class),"add",DescTool.getMethodDesc(String.class,String.class),"say",DescTool.typeTransforms(Test2.class),0);
//        BootStrapMethodTool.creatLambdaMethodInField(bootstrapMethods,constantPool,DescTool.typeTransforms(FunctionInterface.class),"add",DescTool.getMethodDesc(String.class,String.class),DescTool.typeTransforms(Test2.class),1);
//        bootstrapMethods.load(constantPool);
//        System.out.println(constantPool.list());
//        System.out.println(Arrays.toString(constantPool.toByteArray()));
//        System.out.println(Arrays.toString(bootstrapMethods.toByteArray()));
//        ByteArrayIterator byteArrayIterator = new ByteArrayIterator(constantPool.toByteArray());
//        ConstantPool load = ConstantPoolTool.load(byteArrayIterator);
//        System.out.println(load.list());
//        while(byteArrayIterator.hasNext()){
//            byte[] next = byteArrayIterator.next(2);
//            System.out.println(Arrays.toString(next));
//        }

//        ConstantPoolMethodHandleInfo info = new ConstantPoolMethodHandleInfo(ReferenceKind.REF_getField, "s","as", "()V");
//        ConstantPoolMethodTypeInfo info1 = new ConstantPoolMethodTypeInfo("1");
//        BootstrapMethod bootStrapMethod = new BootstrapMethod(ReferenceKind.REF_getField, "java", "say", "()V",info,info1);
//        bootstrapMethods.putBootStrapMethod(bootStrapMethod);
//        constantPool.putInvokeDynamicInfo(bootstrapMethods,"load",bootStrapMethod.getMethodDesc(),bootStrapMethod);
//        System.out.println(Arrays.toString(bootstrapMethods.getPool()));
//        bootstrapMethods.load(constantPool);
//        System.out.println(Arrays.toString(bootstrapMethods.toByteArray()));
//        InnerClasses innerClasses = new InnerClasses();
//        innerClasses.addInnerClass(InnerClass.innerClassOfLambdaMethod());
//        innerClasses.addInnerClass(InnerClass.innerClassOfLambdaMethod());
//        short a = 4;
//        innerClasses.addInnerClass(InnerClass.innerClassOfMethod(a,"innerClass","Test4"));
//
//        innerClasses.load(constantPool);
//        System.out.println(Arrays.toString(innerClasses.toByteArray()));
//        System.out.println(constantPool);
//        for (AbsConstantPoolInfo info : constantPool.getPool()) {
//            System.out.println(info.getType());
//
//        }
//        System.out.println(Arrays.toString(constantPool.toByteArray()));
//        SourceFile sourceFile = new SourceFile("Test3.java");
//        sourceFile.load(constantPool);
//        System.out.println(Arrays.toString(sourceFile.toByteArray()));
//        System.out.println(constantPool);
//        Exceptions exceptions = new Exceptions();
//        ConstantPoolDoubleInfo constantPoolDoubleInfo = new ConstantPoolDoubleInfo(ConvertTool.D2B(3.4));
//        ConstantValue constantValue = new ConstantValue(constantPoolDoubleInfo);
//        String s = constantPoolDoubleInfo.literalToString();
//        System.out.println(s);
//
//        constantValue.load(constantPool);
//        System.out.println(Arrays.toString(constantValue.toByteArray()));
//        exceptions.addException(RuntimeException.class);
//        exceptions.addException(NoSuchMethodException.class);
//        exceptions.load(constantPool);
//        byte[] bytes = exceptions.toByteArray();
//        System.out.println(Arrays.toString(bytes));
//        System.out.println(constantPool);

        //        ConstantPool constantPool = new ConstantPool();
//        ConstantPoolMethodHandleInfo info = new ConstantPoolMethodHandleInfo(ReferenceKind.REF_getField, "s","as", "()V");
//        ConstantPoolMethodTypeInfo info1 = new ConstantPoolMethodTypeInfo("1");
//        BootStrapMethod bootStrapMethod = new BootStrapMethod(ReferenceKind.REF_getField, "java", "say", "()V",info,info1);

//        constantPool.resovleConstantPoolInfo(info);
//        constantPool.resovleConstantPoolInfo(info1);
//        byte[] bytes = constantPool.resovleBootstrapMethod(bootStrapMethod);
//        System.out.println(Arrays.toString(bytes));
//        constantPool.putUtf8Info("Test");
//        constantPool.putNameAndTypeInfo("a","I");
//        constantPool.putMethodrefInfo("张三","say","()V");
//        constantPool.putLongInfo(1000L);
//        constantPool.putIntegerInfo(9999);
//        constantPool.putMethodTypeInfo("()V");
//        constantPool.putFloatInfo(900.9f);
//        constantPool.putDoubleInfo(88.0008);
//        constantPool.putStringInfo("Test");
//        constantPool.putClassInfo("Test");
//        constantPool.putFieldrefInfo("Test","a","I");
//        constantPool.putInterfaceMethodrefInfo("张三","say","()V");

//        byte[] bytes1 = constantPool.toByteArray();
//        System.out.println(Arrays.toString(bytes1));
//        ClassBuilder classBuilder = new ClassBuilder(Test2.class);
//        classBuilder.addNumberField("a",Double.class,3.2);
//        String s = DescTool.getMethodDesc(null,null);
//        System.out.println(s);
//        System.out.println(DescTool.typeTransforms(Test.class));

//        System.out.println(Signature.getClassSignature(new SignatureWrapper[]{new SignatureWrapper("T",Object.class),
//                                                        new SignatureWrapper("F",Number.class)}, null).getSignatureStr());
//        System.out.println(Say.class.isInterface());
//        GenericWrapper[] genericWrappers = {
//                new GenericWrapper("T", Number.class),
//                new GenericWrapper("F", Object.class),
//        };

//        System.out.println(DescTool.typeTransforms(Map.class));
//        Class<Map> mapClass = Map.class;
//        System.out.println(mapClass.getGenericSuperclass());
//        GenericWrapper[] genericWrappers = {
//                new GenericWrapper("F", Object.class),
//                new GenericWrapper("T", Object.class)
//        };
//        Object[] objects = {
//            genericWrappers[0],
//                Test.class,
//                Object.class,
//                genericWrappers[1]
//        };
//        System.out.println(Signature.creatMethodSignature(null, genericWrappers, genericWrappers[0], objects).getSignatureStr());
//        OperandStack operandStack = new OperandStack();
//        operandStack.put(Integer.class);
//        operandStack.put(Long.class);
//        operandStack.put(Double.class);
//        operandStack.put(Test4.class);
//        operandStack.pop();
//        operandStack.pop();
//        operandStack.pop();
//        operandStack.pop();
//        System.out.println(Arrays.toString(operandStack.look()));
//            int a =  (1<<16);
//        System.out.println(a);
//        HashMap<String,Object> map = new HashMap<>();
//        System.out.println(map.getClass().getGenericSuperclass());
//        System.out.println(Arrays.toString(map.getClass().getGenericInterfaces()));

//        System.out.println(Signature.creatMethodSignature(genericWrappers, (GenericWrapper) null, null, null).getSignatureStr());
//        System.out.println(DescTool.getMethodDesc(null, Integer[].class,Double.class,Test4[].class));
//        System.out.println(AccessParsingTool.parseClassAccess(0x0021));
//            FunctionInterface functionInterface = ()->{
//                System.out.println("hhh");
//                int a = 1;
//                int b = 2;
//                int c = a+b;
//                if (a == b)
//                    c = a;
//                else
//                    c = b;
//            };

//        System.out.println(InstructionSet.GETSTATIC.getOpcode());
//        System.out.println(InstructionSet.PUTSTATIC.getOpcode());
//        System.out.println(InstructionSet.GETFIELD.getOpcode());
//        System.out.println(InstructionSet.PUTFIELD.getOpcode());

    }

//   static String getString(int num){
////        if (num == 1){
////            return "张三";
////        }else if (num == 2){
////            return "王五";
////        } else if (num == 3) {
////            return "刘醒";
////        }else {
////            return null;
////        }
//       switch (num){
//           case 1:
//               return "张三";
//           case 2:
//               return "王五";
//           case 31:
//               return "刘醒";
//           default:
//               return null;
//       }
//    }
}
