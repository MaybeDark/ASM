package TestPackage;

import com.sun.istack.internal.Nullable;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@RunnerTime("这是运行时注解")
@NotRunnerTime("这是编译时注解")
public class Test3 implements Say{

    @Nullable
    public int a;
    public float c = 4.5F;
    public static final String b = "1";

    @Resource
    public Test t;

    public Test3(int a,Test t){
        this.a = a;
        this.t = t;
    }
    static{
        System.out.println("张三");
    }

    public void setA(int a)throws Exception{
        this.a = a;
    }

    @Deprecated
    public int getA(){
        return this.a;
    }

    @Override
    public void say() {
        System.out.println("TestPackage.Say");
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


