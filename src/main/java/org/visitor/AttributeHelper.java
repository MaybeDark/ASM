package org.visitor;

import org.bytecode.attributes.Deprecated;
import org.bytecode.attributes.*;
import org.bytecode.attributes.annotations.RuntimeInvisibleAnnotations;
import org.bytecode.attributes.annotations.RuntimeInvisibleParameterAnnotations;
import org.bytecode.attributes.annotations.RuntimeVisibleAnnotations;
import org.bytecode.attributes.annotations.RuntimeVisibleParameterAnnotations;
import org.bytecode.attributes.bootstrapmethods.BootstrapMethods;
import org.bytecode.attributes.code.Code;
import org.bytecode.attributes.innerclass.InnerClasses;
import org.bytecode.attributes.linenumbertable.LineNumberTable;
import org.bytecode.attributes.stackmaptable.StackMapTable;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolUtf8Info;
import org.tools.ByteVector;

import java.util.HashMap;
import java.util.Map;

public class AttributeHelper implements Visitor<Attribute> {
    private static AttributeHelper instance;

    private Map<String, Class<? extends Attribute>> attributeMap = new HashMap<>();
    private ConstantPool constantPool;

    {
        attributeMap.put("ConstantValue", ConstantValue.class);
        attributeMap.put("Code", Code.class);
        attributeMap.put("StackMapTable", StackMapTable.class);
        attributeMap.put("Exceptions", Exceptions.class);
        attributeMap.put("BootstrapMethods", BootstrapMethods.class);
        attributeMap.put("InnerClasses", InnerClasses.class);
        attributeMap.put("EnclosingMethod", EnclosingMethod.class);
        attributeMap.put("Synthetic", Synthetic.class);
        attributeMap.put("Signature", Signature.class);
        attributeMap.put("RuntimeVisibleAnnotations", RuntimeVisibleAnnotations.class);
        attributeMap.put("RuntimeInvisibleAnnotations", RuntimeInvisibleAnnotations.class);
        attributeMap.put("RuntimeVisibleParameterAnnotations", RuntimeVisibleParameterAnnotations.class);
        attributeMap.put("RuntimeInvisibleParameterAnnotations", RuntimeInvisibleParameterAnnotations.class);
//        attributeMap.put("RuntimeVisibleTypeAnnotations",RuntimeVisibleTypeAnnotations.class);
//        attributeMap.put("RuntimeVisibleTypeAnnotations",RuntimeVisibleTypeAnnotations.class);
//        attributeMap.put("AnnotationDefault",AnnotationDefault.class);
//        attributeMap.put("MethodParameters",MethodParameters.class);
        attributeMap.put("SourceFile", SourceFile.class);
        attributeMap.put("SourceDebugExtension", SourceDebugExtension.class);
        attributeMap.put("LineNumberTable", LineNumberTable.class);
        attributeMap.put("LocalVariableTable", LocalVariableTable.class);
        attributeMap.put("LocalVariableTypeTable", LocalVariableTypeTable.class);
        attributeMap.put("Deprecated", Deprecated.class);
    }

    public AttributeHelper() {

    }

    public static AttributeHelper getHelper() {
        if (instance == null) {
            instance = new AttributeHelper();
        }
        return instance;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        String attributeName = ((ConstantPoolUtf8Info) constantPool.get(byteVector.getShort())).getLiteral();
        Class<? extends Attribute> attributeClass = attributeMap.get(attributeName);
        if (attributeClass == null) {
            throw new RuntimeException("no support " + attributeName);
        }
        try {
            Attribute attribute = attributeClass.getConstructor().newInstance();
            return attribute.visit(constantPool, byteVector);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error occurred in attribute visit");
        }
    }
}
