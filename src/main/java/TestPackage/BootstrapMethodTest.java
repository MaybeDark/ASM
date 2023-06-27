package TestPackage;

import org.Type;
import org.bytecode.cls.attribute.bootstrapmethod.BootstrapMethod;
import org.bytecode.cls.attribute.bootstrapmethod.BootstrapMethods;
import org.bytecode.constantpool.ConstantPool;
import org.tools.BootStrapMethodTool;


public class BootstrapMethodTest {
    public void test(){
        FunctionInterface functionInterface = str -> str;
        functionInterface.add("z");
    }

    public static void main(String[] args) {
        BootstrapMethods bootstrapMethods = new BootstrapMethods();
        ConstantPool constantPool = new ConstantPool();
        BootStrapMethodTool.creatLambdaMethodInMethod(bootstrapMethods, constantPool, "FunctionInterface", "add", Type.getMethodDescriptor(String.class,String.class),"test", "BootstrapMethodTest");
//        System.out.println(constantPool.list());
        System.out.println(bootstrapMethods.list());
    }
}
