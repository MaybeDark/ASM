package org.bytecode;

import org.Type;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.code.instruction.CodeHelper;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.field.FieldWriter;
import org.bytecode.method.ConstructorWriter;
import org.bytecode.method.MethodWriter;
import org.tools.ArrayTool;
import org.tools.ByteVectors;
import org.wrapper.ClassWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassWriter implements ByteCodeWriter {
    public static final byte[] CLASSFILEHEADER = {- 54, - 2, - 70, - 66};
    public final static int V1_8 = 52;
    public ClassWrapper superClass;
    public ClassWrapper thisClass;
    protected int access;
    protected ConstantPool constantPool = new ConstantPool();
    protected Map<String, FieldWriter> fields = new HashMap<>();
    protected int fieldCount = 0;
    protected ArrayList<MethodWriter> methods = new ArrayList<>();
    protected Map<String, Map<String, Integer>> name2Methods = new HashMap<>();
    protected int methodCount = 0;
    protected Map<String, Attribute> attributes = new HashMap<>();
    protected int attributeCount = 0;
    protected Map<Short, ClassWrapper> interfaces = new HashMap<>();
    protected int interfaceCount = 0;

    {
        CodeHelper.getHelper().setConstantPool(constantPool);
    }

    public ClassWriter(int access, String classInfo) {
        this(access, classInfo, Object.class);
    }

    public ClassWriter(int access, String classInfo, Class<?> superClass) {
        this(access, classInfo, superClass, (Class<?>) null);
    }


    /**
     * @param access     权限修饰符
     * @param classInfo  完整类名由包路径和类名组成
     * @param superClass 父类
     * @param interfaces 接口
     */
    public ClassWriter(int access, String classInfo, Class<?> superClass, Class<?>... interfaces) {
        this.access = access;
        this.thisClass = new ClassWrapper(Type.getType(Type.getClassDescriptor(classInfo)));
        this.superClass = new ClassWrapper(superClass);
        this.thisClass.load(constantPool);
        this.superClass.load(constantPool);
        if (ArrayTool.notNull(interfaces))
            for (Class<?> anInterface : interfaces) {
                if (anInterface != null)
                    addInterfaces(anInterface);
            }

    }

    protected ClassWriter() {

    }

    public ClassWriter addInterfaces(Class<?> anInterface) {
        if (! anInterface.isInterface())
            throw new RuntimeException(anInterface.getName() + "not is an interface");
        ClassWrapper wrapper = new ClassWrapper(anInterface);
        interfaces.put(wrapper.load(constantPool), wrapper);
        interfaceCount++;
        return this;
    }

    protected ClassWriter addInterfaces(String interfaceName) {
        Type type = Type.getType(Type.getClassDescriptor(interfaceName));
        ClassWrapper wrapper = new ClassWrapper(type);
        interfaces.put(wrapper.load(constantPool), wrapper);
        interfaceCount++;
        return this;
    }


    public MethodWriter addMethod(int access, String methodName, Type returnType, LocalVariableWrapper... parameters) {
        MethodWriter methodWriter = new MethodWriter(this, access, methodName, returnType, parameters);
        addMethod0(methodWriter);
        return methodWriter;
    }

    protected MethodWriter addMethod(int access, String methodName, String methodDesc) {
        MethodWriter methodWriter = new MethodWriter(this, access, methodName, methodDesc);
        addMethod0(methodWriter);
        return methodWriter;
    }


    private void addMethod0(MethodWriter newMethod) {
        Map<String, Integer> methodsByName = name2Methods.computeIfAbsent(newMethod.getMethodName(), k -> new HashMap<>());
        methodsByName.put(newMethod.getMethodDesc(), methodCount);
        methods.add(newMethod);
        methodCount++;
    }

    public MethodWriter addConstructor(int access, MethodWrapper parentConstructor, LocalVariableWrapper... parameters) {
        ConstructorWriter constructorWriter = new ConstructorWriter(this, access, parentConstructor, parameters);
        addMethod0(constructorWriter);
        return constructorWriter;
    }

    public FieldWriter addField(int access, String fieldName, Type fieldType){
        FieldWriter newField = new FieldWriter(this, access, fieldName, fieldType);
        fields.put(fieldName, newField);
        fieldCount++;
        return newField;
    }

    public FieldWriter getField(String fieldName) {
        return fields.get(fieldName);
    }

    public ClassWriter addAttribute(Attribute attribute) {
        if (! Target.check(attribute.target, Target.class_info))
            throw new RuntimeException(attribute.getAttributeName() + " not a class attribute");
        attributes.put(attribute.getAttributeName(), attribute);
        attributeCount++;
        return this;
    }

    public Attribute getAttribute(String attributeName) {
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

    private void load() {
        thisClass.load(constantPool);
        superClass.load(constantPool);
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
        addMethod0(ConstructorWriter.getNoArgsConstructor(this));
    }

    public byte[] toByteArray() {
        load();
        if (getMethodsByName("<init>") == null) {
            creatDefaultConstructor();
        }
        ByteVectors fieldByteArray = new ByteVectors();
        fields.forEach((index, field) -> fieldByteArray.putArray(field.toByteArray()));
        ByteVectors methodByteArray = new ByteVectors();
        methods.forEach((method) -> methodByteArray.putArray(method.toByteArray()));
        ByteVectors classByteArray = new ByteVectors();
        classByteArray.putArray(CLASSFILEHEADER)
                .putInt(V1_8)
                .putArray(constantPool.toByteArray())
                .putShort(access)
                .putShort(thisClass.getCpIndex())
                .putShort(superClass.getCpIndex())
                .putShort(interfaceCount);
        interfaces.forEach((index, classWrapper) -> classByteArray.putShort(index));
        classByteArray.putShort(fieldCount);
        classByteArray.putArray(fieldByteArray.toByteArray());
        classByteArray.putShort(methodCount);
        classByteArray.putArray(methodByteArray.toByteArray());
        classByteArray.putShort(attributeCount);
        attributes.forEach((index, attribute) -> classByteArray.putArray(attribute.toByteArray()));
        return classByteArray.toByteArray();
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public String getClassName() {
        return thisClass.getClassInfo();
    }
}
