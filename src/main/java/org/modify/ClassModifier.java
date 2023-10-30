package org.modify;

import org.Access;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.tools.ByteVectors;
import org.tools.ConvertTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ClassModifier {
    public static final byte[] CLASSFILEHEADER = {- 54, - 2, - 70, - 66};
    ByteVector resource;
    byte[] minorVersion;
    byte[] majorVersion;
    short constantPoolCount = 1;
    List<InfoWrapper> constantPool = new LinkedList<>();
    Map<Integer, Short> hash2Index = new HashMap<>();
    short accessFlag;
    short thisClass;
    short supperClass;
    short interfacesCount;
    List<Short> interfaces = new LinkedList<>();
    short fieldsCount;
    Map<String, FieldModifier> fields = new HashMap<>();
    short methodsCount;
    ArrayList<MethodModifier> methods = new ArrayList<>();
    Map<String, Map<String, Short>> methodsMap = new HashMap<>();
    short classAttributesCount;
    Map<String, AttributeModifier> classAttributes = new HashMap<>();

    private ClassModifier() {
    }

    public ClassModifier(byte[] byteCode) {
        this.resource = new ByteVector(byteCode);
        visit();
    }

    public ClassModifier(File classFile) throws FileNotFoundException {
        if (! classFile.exists()) {
            throw new FileNotFoundException();
        }
        try {
            final byte[] bytes = Files.readAllBytes(classFile.toPath());
            resource = new ByteVector(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        visit();
    }

    public MethodModifier addMethod(short access, String name, String desc) {
        MethodModifier newMethod = new MethodModifier(this);
        newMethod.accessFlag = access;
        newMethod.nameIndex = addUtf8Info(name);
        newMethod.descIndex = addUtf8Info(desc);
        addMethod0(newMethod);
        return newMethod;
    }

    private void addMethod0(MethodModifier newMethod) {
        Map<String, Short> methodsByName = methodsMap.computeIfAbsent(newMethod.getMethodName(), k -> new HashMap<>());
        methodsByName.put(newMethod.getMethodDesc(), methodsCount);
        methods.add(newMethod);
        methodsCount++;
    }

    public FieldModifier addField(short access, String name, String desc) {
        FieldModifier newField = new FieldModifier(this);
        newField.accessFlag = access;
        newField.nameIndex = addUtf8Info(name);
        newField.descIndex = addUtf8Info(desc);
        fields.put(name, newField);
        fieldsCount++;
        return newField;
    }

    /**
     * @param majorVersion 修改主版本号
     * @return {@link ClassModifier}
     */
    public ClassModifier modifyMajorVersion(byte[] majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }


    /**
     * 修改类的权限修饰符
     *
     * @param access 权限修饰符 {@link Access}
     * @return {@link ClassModifier}
     */
    public ClassModifier modifyClassAccessFlag(int access) {
        accessFlag = (short) access;
        return this;
    }

    /**
     * 修改本类名
     * 当safe参数为true会选择新添常量池信息来修改本类名，但需注意其他地方仍会引用旧本类名
     * 不建议通过安全的方式来修改,因为本类名的改变应使其他引用内容改变
     *
     * @param classInfo 修改值
     * @param safe      是否通过新添常量池信息来修改
     * @return {@link ClassModifier}
     */
    public ClassModifier modifyThisClassName(String classInfo, boolean safe) {
        byte[] bytes;
        if (safe) {
            bytes = ConvertTool.S2B(addUtf8Info(classInfo));
            thisClass = addConstantPoolInfo(7, bytes);
            return this;
        }
        InfoWrapper info = getConstantPoolInfo(thisClass);
        bytes = buildUtfInfo(classInfo);
        modifyConstantPoolInfo(ConvertTool.B2S(info.value), bytes);
        return this;
    }

    /**
     * 修改父类名,如果通过安全的方式修改，则需修改初始化方法中的调用父类初始化方法指令内容
     * 当safe参数为true会选择新添常量池信息来修改父类名，但需注意其他地方仍会引用旧父类名
     *
     * @param classInfo 修改值
     * @param safe      是否通过新添常量池信息来修改
     * @return {@link ClassModifier}
     */
    public ClassModifier modifySupperClassName(String classInfo, boolean safe) {
        byte[] bytes;
        if (safe) {
            bytes = ConvertTool.S2B(addUtf8Info(classInfo));
            supperClass = addConstantPoolInfo(7, bytes);
            return this;
        }
        InfoWrapper info = constantPool.get(supperClass);
        bytes = buildUtfInfo(classInfo);
        modifyConstantPoolInfo(ConvertTool.B2S(info.value), bytes);
        return this;
    }

    public ClassModifier delInterface(int index) {
        interfaces.remove(index);
        interfacesCount--;
        return this;
    }

    public ClassModifier addInterface(String interfaceInfo) {
        interfaces.add(addClassInfo(interfaceInfo));
        interfacesCount++;
        return this;
    }

    /**
     * 不安全的,因为修改常量池会造成其他地方的引用内容改变
     * 当safe参数为true会选择新添常量池信息来修改接口名，但需注意其他地方仍会引用旧接口名
     * 修改接口名
     *
     * @param index    接口下标
     * @param modifyTo 修改值
     * @param safe     是否通过新添常量池信息来修改
     */
    public ClassModifier modifyInterface(int index, String modifyTo, boolean safe) {
        if (index < 0 || index > interfacesCount)
            throw new RuntimeException("index " + index + "invalid, index must greater than or equal 0 and less than interfacesCount,now interfacesCount is " + interfacesCount);
        byte[] bytes;
        if (safe) {
            bytes = ConvertTool.S2B(addUtf8Info(modifyTo));
            interfaces.set(index, addConstantPoolInfo(7, bytes));
            return this;
        }
        bytes = buildUtfInfo(modifyTo);
        InfoWrapper classInfo = constantPool.get(interfaces.get(index));
        modifyConstantPoolInfo(ConvertTool.B2S(classInfo.value), bytes);
        return this;
    }

    public short addConstantPoolInfo(int tag, byte[] value) {
        InfoWrapper newInfo = new InfoWrapper(tag, value);
        Short index = hash2Index.putIfAbsent(newInfo.hashCode(), constantPoolCount);
        if (index != null && index != constantPoolCount) {
            return index;
        }
        constantPool.add(newInfo);
        return constantPoolCount++;
    }

    public short addInterfaceMethodRefInfo(short classInfo, short nameAndType) {
        return addConstantPoolInfo(11, ArrayTool.join(ConvertTool.S2B(classInfo), ConvertTool.S2B(nameAndType)));
    }

    public short addMethodRefInfo(short classInfo, short nameAndType) {
        return addConstantPoolInfo(10, ArrayTool.join(ConvertTool.S2B(classInfo), ConvertTool.S2B(nameAndType)));
    }

    public short addFieldRefInfo(short classInfo, short nameAndType) {
        return addConstantPoolInfo(9, ArrayTool.join(ConvertTool.S2B(classInfo), ConvertTool.S2B(nameAndType)));
    }

    public short addClassInfo(String classInfo) {
        return addConstantPoolInfo(7, ConvertTool.S2B(addUtf8Info(classInfo)));
    }

    public short addNameAndTypeInfo(short name, short desc) {
        return addConstantPoolInfo(12, ArrayTool.join(ConvertTool.S2B(name), ConvertTool.S2B(desc)));
    }

    public short addUtf8Info(String str) {
        return addConstantPoolInfo(1, buildUtfInfo(str));
    }

    public short addStringInfo(String str) {
        return addConstantPoolInfo(8, ConvertTool.S2B(addUtf8Info(str)));
    }

    /**
     * @param index 常量下标
     * @param value 修改值
     */
    public void modifyConstantPoolInfo(int index, byte[] value) {
        if (index < 0 || index > constantPoolCount)
            throw new RuntimeException("index " + index + " invalid, index must greater than 0 and less than constantPoolCount,now constantPoolCount is " + constantPoolCount);
        InfoWrapper info = getConstantPoolInfo(index);
        info.value = value;
    }

    byte[] buildUtfInfo(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        bytes = ArrayTool.join(ConvertTool.S2B(bytes.length), bytes);
        return bytes;
    }

    public InfoWrapper getConstantPoolInfo(int index) {
        return constantPool.get(index - 1);
    }

    public String getUtf8(int index) {
        InfoWrapper strInfo = constantPool.get(index - 1);
        byte[] str = new byte[strInfo.value.length - 2];
        System.arraycopy(strInfo.value, 2, str, 0, str.length);
        return new String(str);
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putArray(CLASSFILEHEADER)
                .putArray(minorVersion)
                .putArray(majorVersion)
                .putShort(constantPoolCount);
        for (InfoWrapper info : constantPool) {
            byteVectors.putArray(info.toByteArray());
        }
        byteVectors.putShort(accessFlag)
                .putShort(thisClass)
                .putShort(supperClass)
                .putShort(interfacesCount);
        for (Short index : interfaces) {
            byteVectors.putShort(index);
        }
        byteVectors.putShort(fieldsCount);
        for (FieldModifier field : fields.values()) {
            byteVectors.putArray(field.toByteArray());
        }

        byteVectors.putShort(methodsCount);
        for (MethodModifier method : methods) {
            byteVectors.putArray(method.toByteArray());
        }

        byteVectors.putShort(classAttributesCount);
        for (AttributeModifier attribute : classAttributes.values()) {
            byteVectors.putArray(attribute.toByteArray());
        }
        return byteVectors.toByteArray();
    }

    private void visit() {
        visitHeader();
        visitConstantPool();
        visitClassInfo();
        visitFields();
        visitMethods();
        visitClassAttribute();
    }

    private void visitHeader() {
        if (! checkFileHeader()) {
            throw new RuntimeException("error fileHeader make sure this is a classFile");
        }
        minorVersion = resource.getArray(2);
        majorVersion = resource.getArray(2);
    }

    private boolean checkFileHeader() {
        byte[] header = resource.getArray(4);
        return Arrays.equals(header, CLASSFILEHEADER);
    }

    private void visitConstantPool() {
        short count = resource.getShort();
        for (int i = 0; i < count - 1; i++) {
            visitConstantPoolInfo(i);
        }
    }

    private void visitConstantPoolInfo(int count) {
        byte tag;
        byte[] value;
        switch ((tag = resource.getByte())) {
            case 1:
                resource.mark();
                short length = resource.getShort();
                resource.back();
                value = resource.getArray(2 + length);
                break;
            case 3:
            case 4:
            case 9:
            case 10:
            case 11:
            case 12:
            case 17:
            case 18:
                value = resource.getArray(4);
                break;
            case 5:
            case 6:
                value = resource.getArray(8);
                break;
            case 7:
            case 8:
            case 16:
            case 19:
            case 20:
                value = resource.getArray(2);
                break;
            case 15:
                value = resource.getArray(3);
                break;
            default:
                throw new RuntimeException("unknown tag " + tag);
        }
        addConstantPoolInfo(tag, value);
    }

    private void visitClassInfo() {
        accessFlag = resource.getShort();
        thisClass = resource.getShort();
        supperClass = resource.getShort();
        interfacesCount = resource.getShort();
        for (int i = 0; i < interfacesCount; i++) {
            interfaces.add(resource.getShort());
        }
    }

    private void visitFields() {
        fieldsCount = resource.getShort();
        FieldModifier newField;
        for (int i = 0; i < fieldsCount; i++) {
            newField = FieldModifier.visit(this, resource);
            fields.put(newField.getFieldName(), newField);
        }
    }

    private void visitMethods() {
        short count = resource.getShort();
        MethodModifier newMethod;
        for (int i = 0; i < count; i++) {
            newMethod = MethodModifier.visit(this, resource);
            addMethod0(newMethod);
        }
    }

    private void visitClassAttribute() {
        classAttributesCount = resource.getShort();
        AttributeModifier newAttribute;
        for (int i = 0; i < classAttributesCount; i++) {
            newAttribute = AttributeModifier.visit(resource);
            classAttributes.put(newAttribute.getAttributeName(this), newAttribute);
        }
    }

    public Map<Integer, Short> getHash2Index() {
        return hash2Index;
    }

    public byte[] getMinorVersion() {
        return minorVersion;
    }

    public byte[] getMajorVersion() {
        return majorVersion;
    }

    public short getConstantPoolCount() {
        return constantPoolCount;
    }

    public List<InfoWrapper> getConstantPool() {
        return constantPool;
    }

    public short getAccessFlag() {
        return accessFlag;
    }

    public short getThisClass() {
        return thisClass;
    }

    public short getSupperClass() {
        return supperClass;
    }

    public short getInterfacesCount() {
        return interfacesCount;
    }

    public List<Short> getInterfaces() {
        return interfaces;
    }

    public short getFieldsCount() {
        return fieldsCount;
    }

    public Map<String, FieldModifier> getFields() {
        return fields;
    }

    public short getMethodsCount() {
        return methodsCount;
    }

    public ArrayList<MethodModifier> getMethods() {
        return methods;
    }

    public short getClassAttributesCount() {
        return classAttributesCount;
    }

    public Map<String, AttributeModifier> getClassAttributes() {
        return classAttributes;
    }

}
