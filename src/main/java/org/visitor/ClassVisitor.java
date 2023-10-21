package org.visitor;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.code.Code;
import org.bytecode.constantpool.info.ConstantPoolClassInfo;
import org.bytecode.constantpool.info.ConstantPoolUtf8Info;
import org.bytecode.field.FieldWriter;
import org.bytecode.method.MethodWriter;
import org.exception.NoSupportException;
import org.tools.ByteVector;
import org.wrapper.ClassWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class ClassVisitor extends ClassWriter {
    ByteVector byteCode;
    AttributeHelper attributeHelper = AttributeHelper.getHelper();
    {
        attributeHelper.setConstantPool(constantPool);
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

    public ClassVisitor visit() {
        checkHeader(byteCode.getArray(4));
        checkVersion(byteCode.getInt());
        constantPool.visit(constantPool, byteCode, true);
        access = byteCode.getShort();
        ConstantPoolClassInfo classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
        this.thisClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(classInfo.getClassInfo())));
        classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
        this.superClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(classInfo.getClassInfo())));
        short interfaceCount = byteCode.getShort();
        for (int i = 0; i < interfaceCount; i++) {
            classInfo = (ConstantPoolClassInfo) constantPool.get(byteCode.getShort());
            addInterfaces(classInfo.getClassInfo());
        }

        //fieldCount
        short fieldCount = byteCode.getShort();
        for (int i = 0; i < fieldCount; i++) {
            int access = byteCode.getShort();
            FieldWriter fieldWriter = addField(access, constantPool.getUtf8(byteCode.getShort()), Type.getType(constantPool.getUtf8(byteCode.getShort())));
            //attributeCount
            short count = byteCode.getShort();
            for (int j = 0; j < count; j++) {
                fieldWriter.addAttribute(attributeHelper.visit(constantPool, byteCode));
            }
        }

        //methodCount
        short methodCount = byteCode.getShort();
        for (int i = 0; i < methodCount; i++) {
            int access = byteCode.getShort();
            ConstantPoolUtf8Info methodName = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
            ConstantPoolUtf8Info methodDesc = (ConstantPoolUtf8Info) constantPool.get(byteCode.getShort());
            MethodWriter methodWriter = addMethod(access, methodName.getLiteral(), methodDesc.getLiteral());
            short attrCount = byteCode.getShort();
            for (int j = 0; j < attrCount; j++) {
                methodWriter.addAttribute(attributeHelper.visit(constantPool, byteCode));
            }
            methodWriter.setCode((Code) methodWriter.getAttribute("Code"));
        }
        short attrCount = byteCode.getShort();
        for (int i = 0; i < attrCount; i++) {
            addAttribute(attributeHelper.visit(constantPool, byteCode));
        }
        return this;
    }

//    public BytecodeWrapper easyVisit() {
//        BytecodeWrapper wrapper = new BytecodeWrapper();
//        checkHeader(byteCode.getArray(4));
//        wrapper.version = byteCode.getInt();
//        wrapper.constantPoolCount = constantPoolVisitor.getConstantPoolCount();
//        constantPool = constantPoolVisitor.visit(byteCode);
//        wrapper.constantPoolDescribe = constantPool.print();
//        wrapper.classAccess = Access.parseClassAccess(byteCode.getShort());
//
//        return wrapper;
//    }


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
