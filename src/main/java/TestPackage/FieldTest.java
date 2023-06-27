package TestPackage;

import org.Access;
import org.Type;
import org.bytecode.method.Method;
import org.bytecode.method.attribute.code.Code;
import org.wrapper.LocalVariableWrapper;

import java.util.Arrays;

public class FieldTest {
    int a = 1;
    Type b = Type.getType("I");
    Type c = Type.INT;
    Type d = null;
    Long e = 1L;
    FieldTest f = new FieldTest();

    public FieldTest(){

    }

    public FieldTest(String name){

    }

    public static void main(String[] args) {
//        LocalVariableWrapper localVariableWrapper = new LocalVariableWrapper("a", Type.INT);
//        LocalVariableWrapper[] parameters = new LocalVariableWrapper[0];
//        Object[] types = Arrays.stream(parameters).map(LocalVariableWrapper::getType).toArray();
//        System.out.println(((Type)types[0]).getDescriptor());

//        String classDescriptor = Type.getClassDescriptor(Test.class);
//        System.out.println(Type.getClassDescriptor("java/lang/Object"));
//
//        Type type = Type.getType("Ljava/lang/Object;");
//        System.out.println(type.getFullClassName());

        Method say = new Method(Access.ACC_PUBLIC+Access.ACC_SUPER, "java/lang/Object", "say", Type.VOID);
        System.out.println(say);
    }
}
