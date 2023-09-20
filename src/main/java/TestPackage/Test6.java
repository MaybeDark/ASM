package TestPackage;

import org.Listable;
import org.Loadable;

public class Test6 implements Say {
    public String A;
    public String say(){
        A = "张三";
        System.out.println(A);
        return A;
    }
}
