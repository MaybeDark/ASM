package TestPackage;

import com.sun.istack.internal.Nullable;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@RunnerTime("这是运行时注解")
@NotRunnerTime("这是编译时注解")
public class Test3 extends Test4 implements Say{
    public int a = 0;
//    public float c = 4.5F;
//    public static final String b = "1";
//    public Test t;

    public Test3(int a,Test t){
//        super("张三");
        a = 1;
        t = new Test("张三");
//        this.a = a;
//        this.t = t;
    }
    static{
        System.out.println("张三");
    }

    public void setA(int a){
        a=1;
    }

    @Deprecated
    public int getA(){
        return this.a;
    }

    @Override
    public String say() {
        System.out.println("TestPackage.Say");
        return "";
    }

    private void execute(Say say){
        say.say();
        new innerClass().eat();
    }

    protected Test3 getT(){
        class methodInnerClass extends Test3{

            public methodInnerClass(int a, Test t) {
                super(a, t);
            }
        }

        return new methodInnerClass(1, null);
    }

    static class innerClass{
        void eat(){

        }
    }
}


