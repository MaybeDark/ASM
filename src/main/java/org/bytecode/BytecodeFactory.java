package org.bytecode;

import org.bytecode.cls.ClassWriter;
import org.bytecode.constantpool.ConstantPool;

public class BytecodeFactory {
    public ConstantPool constantPool;

    public static ClassWriter newClass(int access,String fullClassName){
        return new ClassWriter(access,format(fullClassName));
    }

    public static ClassWriter newClass(int access,String packagePath,String className){
        return new ClassWriter(access,format(packagePath.concat("/"+className)));
    }

    private static String format(String fullClassName){
        fullClassName = fullClassName.replace(".","/").replace(" ","");
        return fullClassName;
    }
}
