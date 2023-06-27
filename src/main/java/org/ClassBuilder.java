package org;

import org.constantpool.ConstantPool;
import org.field.Field;
import org.field.FieldPool;
import org.method.MethodPool;
import org.wrapper.ClassWrapper;

import java.util.ArrayList;

public class ClassBuilder {

    ClassWrapper thisClass;
    ClassWrapper superClass;

    ArrayList<ClassWrapper> interfaces = new ArrayList<>();

    public ConstantPool constantPool = new ConstantPool();
    public FieldPool fieldPool = new FieldPool();
    public MethodPool methodPool = new MethodPool();

    public ClassBuilder(){

    }

    public ClassBuilder(Class<?> klass){
        System.out.println(klass.getModifiers());
        String packageName = klass.getPackage().getName();
        String className = klass.getName();
        String fullClassName = packageName + "/" + className;


//        this.thisClassInfoIndex = constantPool.putClassInfo(fullClassName);
//        System.out.println(klass.getSuperclass().getName());
    }

//    1. new
//    2. number,string
//    3. filed
//    4.



    public <T extends Number> ClassBuilder addNumberField(String name,Class<T > typeClass,T defaultValue){
        String typeName = typeClass.getTypeName();
        System.out.println(typeName);

        return this;
    }

    public ClassBuilder addField(String name, String type, Field defaultValue){
//        short index = constantPool.putFieldrefInfo(fullClassName, "a", "I");
//        Field field = new Field(ACC_PUBLIC,fullClassName,name, "I",null);

        return this;
    }

}
