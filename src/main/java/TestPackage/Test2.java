package TestPackage;

import java.util.Map;

public class Test2<F extends Number&A,E> implements Say{

    Map<E,F> s;
    E e;
    FunctionInterface functionInterface = (s1) -> "hello";

    public<G> Test2(G value) throws Exception{
        {
            int a = 0;
            System.out.println(a);
        }
        {
            int b = 0;
            System.out.println(b);
        }
    }

//    public  T say(T data){
//        return null;
//    }
    public  F say1(Test a){
        return null;
    }

    @Override
    public void say() {
        FunctionInterface functionInterface1 = (s1) -> "yes";
        functionInterface1.add("a");
    }

//    public static void chop(int c){
//
//        if (c > 0) {
//            int i = 0;
//            chop(i);
//        }else if (c == 0){
//            chop(c);
//        }else {
//            Test2 i = new Test2();
//            chop(c);
//
//        }
//    }
}
