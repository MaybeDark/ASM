package TestPackage;

public class MyClassLoader extends ClassLoader {

    protected Class<?> findClass(String name,byte[] b,int off,int len){
        return defineClass(name,b,off,len);
    }
}
