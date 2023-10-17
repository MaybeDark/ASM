package org.visitor;

import org.Access;
import org.bytecode.ClassWriter;
import org.exception.NoSupportException;
import org.tools.ByteVector;
import org.wrapper.BytecodeWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class ClassVisitor extends ClassWriter {
    ByteVector byteCode;
    ConstantPoolVisitor constantPoolVisitor = new ConstantPoolVisitor();

    {
        AttributeHelper.getHelper().setConstantPool(constantPool);
    }

    public ClassVisitor(byte[] byteCode) {
        this.byteCode = new ByteVector(byteCode);
    }

    public ClassVisitor(File classFile) throws FileNotFoundException {
        if (! classFile.exists()) {
            throw new FileNotFoundException();
        }
        try {
            final byte[] bytes = Files.readAllBytes(classFile.toPath());
            byteCode = new ByteVector(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public ClassVisitor visit(){
//        checkHeader(byteCode.getArray(4));
//        checkVersion(byteCode.getInt());
//        constantPool = constantPoolVisitor.visit(byteCode);
//        access = byteCode.getShort();
//        ConstantPoolClassInfo classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
//        this.thisClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(classInfo.getClassInfo())));
//        classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
//        this.superClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(classInfo.getClassInfo())));
//        //interfaceCount
//        for (int i = 0; i < byteCode.getShort(); i++) {
//            classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
//            addInterfaces(classInfo.getClassInfo()) ;
//        }
//
//        //fieldCount
//        for (int i = 0; i < byteCode.getShort(); i++) {
//            int access = byteCode.getShort();
//            ConstantPoolUtf8Info fieldName = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
//            ConstantPoolUtf8Info fieldType = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
//            addField(access, fieldName.literalToString(), Type.getType(fieldType.literalToString()));
//            //attributeCount
//            for (int j = 0; j < byteCode.getShort(); j++) {
//                ConstantPoolUtf8Info attributeName = (ConstantPoolUtf8Info)constantPool.get(byteCode.getShort());
//
//            }
//        }
//
//        //methodCount
//        for (int i = 0; i < byteCode.getShort(); i++) {
//            int access = byteCode.getShort();
//            ConstantPoolUtf8Info methodName = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
//            ConstantPoolUtf8Info methodDesc = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
//            Type returnType = Type.getReturnType(methodDesc.literalToString());
//            Type[] argumentTypes = Type.getArgumentTypes(methodDesc.literalToString());
//            addMethod(access,methodName,returnType,)
//        }
//
//    }

    public BytecodeWrapper easyVisit() {
        BytecodeWrapper wrapper = new BytecodeWrapper();
        checkHeader(byteCode.getArray(4));
        wrapper.version = byteCode.getInt();
        wrapper.constantPoolCount = constantPoolVisitor.getConstantPoolCount();
        constantPool = constantPoolVisitor.visit(byteCode);
        wrapper.constantPoolDescribe = constantPool.print();
        wrapper.classAccess = Access.parseClassAccess(byteCode.getShort());

        return wrapper;
    }


    private void checkVersion(int version) {
        if (version == V1_8) {
            return;
        }
        throw new NoSupportException("Just support JDK1.8");

    }

    private void checkHeader(byte[] header) {
        if (Arrays.equals(header, CLASSFILEHEADER)) {
            return;
        }
        throw new RuntimeException("error file type");
    }
}
