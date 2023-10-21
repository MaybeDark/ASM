package org.bytecode.method;

import com.sun.istack.internal.Nullable;
import org.Access;
import org.Type;
import org.bytecode.ByteCodeWriter;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.Attribute;
import org.bytecode.attributes.Target;
import org.bytecode.attributes.bootstrapmethods.CallSite;
import org.bytecode.attributes.code.BranchCondition;
import org.bytecode.attributes.code.Code;
import org.bytecode.attributes.code.instruction.InstructionSet;
import org.bytecode.attributes.code.instruction.Operator;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.wrapper.ClassWrapper;
import org.wrapper.FieldWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodWriter implements ByteCodeWriter {
    public final ClassWriter classWriter;
    private final int access;
    private final String methodName;
    private final String methodDesc;
    private LocalVariableWrapper[] parameters;
    private Type[] parameterTypes;
    private Type returnType;
    private Code code;
    private int callSiteCount;
    private final Map<String, Attribute> attributes;

    public MethodWriter(ClassWriter classWriter,int access, String methodName, @Nullable Type returnType, @Nullable LocalVariableWrapper... parameters) {
        this.classWriter = classWriter;
        this.access = access;
        this.methodName = methodName;
        this.code = new Code();
        this.parameters = parameters;
        this.returnType = returnType;
        attributes = new HashMap<>();
        if (ArrayTool.notNull(parameters)) {
            parameterTypes = new Type[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getType();
            }
            this.methodDesc = Type.getMethodDescriptor(returnType, parameterTypes);
        } else {
            this.methodDesc = Type.getMethodDescriptor(returnType);
        }
        code.initByWriter(this);
        attributes.put("Code", code);
    }

    public MethodWriter(ClassWriter classWriter, int access, String methodName, String methodDesc) {
        this.classWriter = classWriter;
        this.access = access;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        parameterTypes = Type.getArgumentTypes(methodDesc);
        if (ArrayTool.notNull(parameterTypes)) {
            parameters = new LocalVariableWrapper[parameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new LocalVariableWrapper("arg" + i, parameterTypes[i]);
            }
        }
        attributes = new HashMap<>();
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void Override() {
        this.code = new Code();
        code.initByWriter(this);
    }


    public boolean isStatic() {
        return Access.isStatic(access);
    }

    public MethodWriter loadNull() {
        code.loadNull();
        return this;
    }

    public MethodWriter loadInt(int num) {
        code.loadInt(num);
        return this;
    }

    public MethodWriter loadDouble(double num) {
        code.loadDouble(num);
        return this;
    }

    public MethodWriter loadLong(long num) {
        code.loadLong(num);
        return this;
    }

    public MethodWriter loadFloat(float num) {
        code.loadFloat(num);
        return this;
    }

    public MethodWriter loadString(String str) {
        code.loadString(str);
        return this;
    }

    public MethodWriter loadLocal(String localName) {
        code.loadLocal(localName);
        return this;
    }

    public MethodWriter loadArrayElement() {
        code.loadArrayElement();
        return this;
    }

    public MethodWriter loadField(FieldWrapper fieldWrapper, boolean isStatic) {
        code.loadField(fieldWrapper, isStatic);
        return this;
    }

    public MethodWriter loadClass(ClassWrapper classWrapper) {
        code.loadClass(classWrapper);
        return this;
    }


    void loadCallSite(CallSite callSite) {
        code.invokeDynamic(callSite);
        String proxyMethodName = "lambda$" + (this instanceof ConstructorWriter ? methodName : "new") + "$" + callSiteCount;
        callSite.setProxyMethod(classWriter.addMethod(4106, proxyMethodName, callSite.getTargetType()));
        callSiteCount++;
    }

    public MethodWriter storeLocal(String localName) {
        code.storeLocal(localName);
        return this;
    }

    public MethodWriter storeArrayElement() {
        code.storeArrayElement();
        return this;
    }

    public MethodWriter storeField(FieldWrapper fieldWrapper, boolean isStatic) {
        code.storeField(fieldWrapper, isStatic);
        return this;
    }

    public MethodWriter checkCast(ClassWrapper classWrapper) {
        code.checkCast(classWrapper);
        return this;
    }

    public MethodWriter stackBehavior(InstructionSet inst) {
        code.stackBehavior(inst);
        return this;
    }

    public MethodWriter stackBracket() {
        code.startBracket();
        return this;
    }

    public MethodWriter endBracket() {
        code.endBracket();
        return this;
    }

    public MethodWriter startIf(BranchCondition branchCondition) {
        code.startIf(branchCondition);
        return this;
    }

    public MethodWriter endIf() {
        code.endIf();
        return this;
    }

    public MethodWriter newObject(ClassWrapper classWrapper) {
        code.newObject(classWrapper);
        return this;
    }

    public MethodWriter newArray(Type elementType, int length) {
        code.newArray(elementType, length);
        return this;
    }

    public MethodWriter arrayLength() {
        code.arrayLength();
        return this;
    }

    public MethodWriter throwException() {
        code.throwException();
        return this;
    }

    public MethodWriter monitorEnter() {
        code.monitorEnter();
        return this;
    }

    public MethodWriter monitorExit() {
        code.monitorExit();
        return this;
    }

    public MethodWriter multiANewArray(Type elementType, int cow, int rol) {
        return multiANewArray(elementType, 2, cow, rol);
    }

    public MethodWriter multiANewArray(Type elementType, int dimensions, int... size) {
        code.multiANewArray(elementType, dimensions, size);
        return this;
    }

    public MethodWriter jumpUp() {
        code.jumpUp();
        return this;
    }

    public MethodWriter jumpDown() {
        code.jumpDown();
        return this;
    }

    public MethodWriter increment(String localName) {
        code.increment(localName);
        return this;
    }

    public MethodWriter invokeSpecial(MethodWrapper target) {
        code.invokeSpecial(target);
        return this;
    }

    public MethodWriter invokeDynamic(CallSite callSite) {
        loadCallSite(callSite);
        return this;
    }

    public MethodWriter invokeInterface(MethodWrapper target) {
        code.invokeInterface(target);
        return this;
    }

    public MethodWriter invokeStatic(MethodWrapper target) {
        code.invokeStatic(target);
        return this;
    }

    public MethodWriter invokeVirtual(MethodWrapper target) {
        code.invokeVirtual(target);
        return this;
    }

    public MethodWriter convert(Type newType) {
        code.convert(newType);
        return this;
    }

    public MethodWriter compare() {
        code.compare();
        return this;
    }

    public MethodWriter operation(Operator operator) {
        if (operator.isCalculateOperator())
            code.mathOperation(operator);
        else
            code.logicalOperation(operator);
        return this;
    }

    public MethodWriter return_() {
        code.return0();
        return this;
    }


    public ClassWriter endMethod() {
        code.end();
        return classWriter;
    }

    public MethodWriter addAttribute(Attribute attribute) {
        if (! Target.check(attribute.target, Target.method_info))
            throw new RuntimeException(attribute.getAttributeName() + " not a method attribute");
        attributes.put(attribute.getAttributeName(), attribute);
        return this;
    }

    @Override
    public Attribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public int getAccess() {
        return access;
    }

    public String getClassName() {
        return classWriter.thisClass.getClassInfo();
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] toByteArray() {
        code.end();
        ConstantPool constantPool = classWriter.getConstantPool();
        short methodNameCpIndex = constantPool.putUtf8Info(methodName);
        short methodDescCpIndex = constantPool.putUtf8Info(methodDesc);
        byte[] attrBytes = new byte[0];
        short attrCount = 0;
        Collection<Attribute> attr = attributes.values();
        for (Attribute attribute : attr) {
            if (attribute != null) {
                attrCount++;
                attribute.load(constantPool);
                byte[] bytes = attribute.toByteArray();
                attrBytes = ArrayTool.join(attrBytes, bytes);
            }
        }

        ByteVector result = new ByteVector(8 + attrBytes.length);
        result.putShort(access)
                .putShort(methodNameCpIndex)
                .putShort(methodDescCpIndex)
                .putShort(attrCount)
                .putArray(attrBytes);
        return result.end();
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public MethodWrapper wrapper() {
        return new MethodWrapper(getClassName(), methodName, returnType, parameterTypes);
    }

    public LocalVariableWrapper[] getParameterTypes() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodWriter methodWriter = (MethodWriter) o;
        return hashCode() == methodWriter.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, methodDesc);
    }
}
