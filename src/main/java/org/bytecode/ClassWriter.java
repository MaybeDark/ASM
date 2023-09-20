package org.bytecode;

import org.Type;
import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.field.FieldWriter;
import org.bytecode.method.ConstructorWriter;
import org.bytecode.method.MethodWriter;
import org.tools.ArrayTool;
import org.tools.ByteVectors;
import org.wrapper.ClassWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.*;

public class ClassWriter implements ByteCodeWriter {
    public static final byte[] CLASSFILEHEADER = {-54,-2,-70,-66};
    public final static int V1_8 = 52;
    public ClassWrapper superClass;
    public ClassWrapper thisClass;
    public final int access;
    private ConstantPool constantPool;
    private Map<String , FieldWriter> fields;
    private int fieldCount = 0;
    private ArrayList<MethodWriter> methods;
    private Map<String , Map<String , Integer>> name2Methods;
    private int methodCount = 0;
    private Map<String , Attribute> attributes;
    private int attributeCount = 0;
    private Map<Short , ClassWrapper> interfaces;
    private int interfaceCount = 0;

    public ClassWriter(int access,String fullClassName){
        this(access,fullClassName,Object.class);
    }
    public ClassWriter(int access,String fullClassName,Class<?> superClass){
        this(access,fullClassName,superClass, (Class<?>) null);
    }

    public ClassWriter(int access,String fullClassName,Class<?> superClass,Class<?>... interfaces){
        this.access = access;
        this.thisClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(fullClassName)));
        this.superClass = new ClassWrapper(superClass);
        this.constantPool = new ConstantPool();
        this.fields = new HashMap<>();
        this.methods = new ArrayList<>();
        this.name2Methods = new HashMap<>();
        this.attributes = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.thisClass.load(constantPool);
        this.superClass.load(constantPool);
        if (ArrayTool.notNull(interfaces))
            for (Class<?> anInterface : interfaces) {
                if (anInterface != null)
                    addInterfaces(anInterface);
            }

    }

    public ClassWriter addInterfaces(Class<?> anInterface){
        if (!anInterface.isInterface())
            throw new RuntimeException(anInterface.getName() + "not is an interface");
        ClassWrapper wrapper = new ClassWrapper(anInterface);
        interfaces.put(wrapper.load(constantPool),wrapper);
        interfaceCount++;
        return this;
    }

    public MethodWriter addMethod(int access, String methodName, Type returnType, LocalVariableWrapper... parameters){
        MethodWriter methodWriter = new MethodWriter(this,access, methodName, returnType, parameters);
        addMethod0(methodWriter);
        return methodWriter;
    }

    private void addMethod0(MethodWriter newMethod){
        Map<String, Integer> methodsByName = name2Methods.computeIfAbsent(newMethod.getMethodName(), k -> new HashMap<>());
        methodsByName.put(newMethod.getMethodDesc(),methodCount);
        methods.add(newMethod);
        methodCount++;
    }

    public MethodWriter addConstructor(int access, MethodWrapper parentConstructor, LocalVariableWrapper... parameters){
        ConstructorWriter constructorWriter = new ConstructorWriter(this,access, parentConstructor, parameters);
        addMethod0(constructorWriter);
        return constructorWriter;
    }

    public FieldWriter addField(int access, String fieldName, Type fieldType){
        FieldWriter newField = new FieldWriter(this,access,fieldName,fieldType);
        fields.put(fieldName,newField);
        fieldCount++;
        return newField;
    }

    public FieldWriter getField(String fieldName){
        return fields.get(fieldName);
    }

    public ClassWriter addAttribute(Attribute attribute){
        attributes.put(attribute.getAttributeName(), attribute);
        attributeCount ++;
        return this;
    }
    public Attribute getAttribute(String attributeName){
        return attributes.get(attributeName);
    }

    public ClassWrapper getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassWrapper superClass) {
        this.superClass = superClass;
    }

    public void setClassName(String name){
        this.thisClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(name)));
    }

    private void load(){
        for (Attribute attribute : attributes.values()) {
            if (attribute != null) {
                attribute.load(constantPool);
            }
        }
    }

    public List<MethodWriter> getMethodsByName(String name){
        Map<String, Integer> desc2Method = name2Methods.get(name);
        if (desc2Method == null){
            return null;
        }
        List<MethodWriter> methodWriters = new ArrayList<>();
        desc2Method.forEach((k,v)->{
            MethodWriter methodWriter = methods.get(v);
            methodWriters.add(methodWriter);
        });
        return methodWriters;
    }

    private void creatDefaultConstructor(){
//        addConstructor(ACC_PUBLIC,new MethodWrapper());
    }

    public byte[] toByteArray(){
        load();
        if (getMethodsByName("<init>") == null){
            creatDefaultConstructor();
        }
        ByteVectors classByteArray = new ByteVectors();
        classByteArray.putArray(CLASSFILEHEADER)
                .putInt(V1_8)
                .putArray(constantPool.toByteArray())
                .putShort(access)
                .putShort(thisClass.getCpIndex())
                .putShort(superClass.getCpIndex())
                .putShort(interfaceCount);
        interfaces.forEach((index,classWrapper)->{
            classByteArray.putShort(index);
        });
        classByteArray.putShort(fieldCount);
        fields.forEach((index,field)->{
            classByteArray.putArray(field.toByteArray());
        });
        classByteArray.putShort(methodCount);
        methods.forEach((method)->{
            classByteArray.putArray(method.toByteArray());
        });
        classByteArray.putShort(attributeCount);
        attributes.forEach((index,attribute)->{
            classByteArray.putArray(attribute.toByteArray());
        });
        return classByteArray.toByteArray();
//        ByteVector part1 = new ByteVector(8)
//                .putArray(CLASSFILEHEADER)
//                .putInt(V1_8);
//        byte[] part2 = constantPool.toByteArray();
//        ByteVector part3 = new ByteVector(8 + interfaceCount * 2)
//                .putShort(access)
//                .putShort(thisClass.getCpIndex())
//                .putShort(superClass.getCpIndex())
//                .putShort(interfaceCount);
//        interfaces.forEach(((index, classWrapper) -> {
//            part3.putShort(index);
//        }));
//
//        fields.forEach((name,field)->{
//
//        });
//        byte[] part4 = fields.toByteArray();
//        byte[] part5 = methods.toByteArray();
//        byte[] part6 = ConvertTool.S2B(attributeCount);
//        for (Attribute attribute : attributes.values()) {
//            if (attribute != null){
//                part6 = ArrayTool.join(part6,attribute.toByteArray());
//            }
//        }
//        int fileLength = part1.length + part2.length + part3.getLength() + part4.length + part5.length + part6.length;
//        ByteVector result = new ByteVector(fileLength);
//        result.putArray(part1)
//                .putArray(part2)
//                .putArray(part3.end())
//                .putArray(part4)
//                .putArray(part5)
//                .putArray(part6);
//        return result.end();
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public String getClassName() {
        return thisClass.getClassName();
    }
}
