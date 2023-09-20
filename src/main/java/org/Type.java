package org;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.exception.InvalidDescriptorException;
import org.exception.NoSupportException;
import org.exception.NotNullException;
import org.exception.TypeErrorException;
import org.tools.ArrayTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Type {
    public static final int VOID_CODE = 0;
    public static final int BOOLEAN_CODE = 1;
    public static final int CHAR_CODE = 2;
    public static final int BYTE_CODE = 3;
    public static final int SHORT_CODE = 4;
    public static final int INT_CODE = 5;
    public static final int FLOAT_CODE = 6;
    public static final int LONG_CODE = 7;
    public static final int DOUBLE_CODE = 8;
    public static final int ARRAY_CODE = 9;
    public static final int OBJECT_CODE = 10;
    public static final int METHOD_CODE = 11;
    public static final int NULL_CODE = 12;
    public static final Type VOID = new Type(0, "V");
    public static final Type BOOLEAN = new Type(1, "Z");
    public static final Type CHAR = new Type(2, "C");
    public static final Type BYTE = new Type(3, "B");
    public static final Type SHORT = new Type(4, "S");
    public static final Type INT = new Type(5, "I");
    public static final Type FLOAT = new Type(6, "F");
    public static final Type LONG = new Type(7, "L");
    public static final Type DOUBLE = new Type(8, "D");
    public static final Type NULL = new Type(12,"null");
    public static final Type STRING = new Type(12,"Ljava/lang/String;");

    private static final Map<Class<?>, String> transforms = new HashMap<>();
    static {
        transforms.put(Void.TYPE, "V");
        transforms.put(Boolean.TYPE, "Z");
        transforms.put(Character.TYPE, "C");
        transforms.put(Byte.TYPE, "B");
        transforms.put(Short.TYPE, "S");
        transforms.put(Integer.TYPE, "I");
        transforms.put(Float.TYPE, "F");
        transforms.put(Long.TYPE, "J");
        transforms.put(Double.TYPE, "D");
    }

    private final int code;
    private String desc;

    private Type(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getClassDescriptor(@NotNull Class<?> clazz) {
        if (clazz == null) {
            throw new NotNullException("argument0 must be not null");
        }
        if (clazz.isPrimitive()) {
            return transforms.get(clazz);
        }
        if (clazz.isArray()) {
            return clazz.getName().replace(".", "/");
        }
        StringBuilder sb = new StringBuilder();
        String className = clazz.getName();
        className = className.replace(".", "/");
        sb.append('L');
        sb.append(className);
        sb.append(';');
        return sb.toString();
    }

    public static String getClassDescriptor(@NotNull String fullClassName){
        StringBuilder sb = new StringBuilder();
        sb.append('L');
        sb.append(fullClassName);
        sb.append(';');
        return sb.toString();
    }


    public static String getMethodDescriptor(@Nullable Class<?> returnType, @Nullable Class<?>... argumentTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        if (argumentTypes != null && argumentTypes.length != 0) {
            for (Class<?> clazz : argumentTypes) {
                if (clazz == null)
                    break;
                sb.append(getClassDescriptor(clazz));
            }
        }
        sb.append(')');
        if (returnType == null) {
            sb.append('V');
        } else {
            sb.append(getClassDescriptor(returnType));
        }
        return sb.toString();
    }

    public static String getMethodDescriptor(Method targetMethod) {
       return getMethodDescriptor(targetMethod.getReturnType(),targetMethod.getParameterTypes());
    }

    public static String getMethodDescriptor(@Nullable Type returnType, @Nullable Type... argumentTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        if (ArrayTool.notNull(argumentTypes)) {
            for (Type argumentType : argumentTypes) {
                if (argumentType == null || argumentType.equals(Type.VOID))
                    break;
                sb.append(argumentType.desc);
            }
        }
        sb.append(')');
        if (returnType == null || returnType.equals(Type.VOID)) {
            sb.append('V');
        } else {
            sb.append(returnType.desc);
        }
        return sb.toString();
    }

    public static Type getType(@NotNull final char descriptor){
        switch (descriptor) {
            case 'B':
                return BYTE;
            case 'C':
                return CHAR;
            case 'D':
                return DOUBLE;
            case 'F':
                return FLOAT;
            case 'I':
                return INT;
            case 'J':
                return LONG;
            case 'S':
                return SHORT;
            case 'V':
                return VOID;
            case 'Z':
                return BOOLEAN;
            default:
                throw new InvalidDescriptorException("Invalid descriptor: \"" + descriptor + "\"");
        }
    }

    public static Type getType(@NotNull final String descriptor) {
        if (descriptor == null || descriptor.isEmpty()) {
            throw new NotNullException("argument0 must be not null");
        }
        if (descriptor.length() == 1) {
            return getType(descriptor.charAt(0));
        } else if (descriptor.startsWith("L"))
            return new Type(OBJECT_CODE, descriptor);
        else if (descriptor.startsWith("("))
            return new Type(METHOD_CODE, descriptor);
        else if (descriptor.startsWith("["))
            return new Type(ARRAY_CODE, descriptor);
        else {
            throw new InvalidDescriptorException("Invalid descriptor: \"" + descriptor + "\"");
        }
    }

    public static Type getType(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return VOID;
        }
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE)
                return INT;
            else if (clazz == Boolean.TYPE)
                return BOOLEAN;
            else if (clazz == Byte.TYPE)
                return BYTE;
            else if (clazz == Character.TYPE)
                return CHAR;
            else if (clazz == Short.TYPE)
                return SHORT;
            else if (clazz == Double.TYPE)
                return DOUBLE;
            else if (clazz == Float.TYPE)
                return FLOAT;
            else if (clazz == Long.TYPE)
                return LONG;
            else if (clazz == Void.TYPE)
                return VOID;
            else {
                throw new NoSupportException();
            }
        } else {
            return getType(getClassDescriptor(clazz));
        }
    }

    public static Type[] getType(@NotNull Class<?>[] classes){
        if (!ArrayTool.notNull(classes)){
            return null;
        }
        Type[] types = new Type[classes.length];
        for (int i = 0; i < classes.length; i++) {
            types[i] = Type.getType(classes[i]);
        }
        return types;
    }

    public static Type[] getArgumentTypes(@NotNull final String methodDescriptor) {
        if (methodDescriptor == null || methodDescriptor.isEmpty()) {
            throw new NotNullException("argument0 must be not null");
        }
        if (!methodDescriptor.startsWith("(")) {
            throw new InvalidDescriptorException("Invalid methodDescriptor: \"" + methodDescriptor + "\"");
        }
        char[] argsDesc;
        {
            int start = 1;
            int end = methodDescriptor.indexOf(')');
            if (end <= 0) {
                throw new InvalidDescriptorException("Invalid methodDescriptor: \"" + methodDescriptor + "\"");
            }
            if (start == end) {
                return new Type[]{Type.VOID};
            }
            argsDesc = new char[end - start];
            methodDescriptor.getChars(start, end, argsDesc, 0);
        }
        int count = 0;
        Type[] container = new Type[16];
        {
            StringBuilder sb = new StringBuilder();
            for (char c : argsDesc) {
                sb.append(c);
                if ((sb.length() == 1 && isPrimitiveType(c)) || c == ';') {
                    container[count++] = getType(sb.toString());
                    sb.setLength(0);
                }
            }
            if (sb.length() != 0) {
                throw new InvalidDescriptorException("Invalid methodDescriptor: \"" + methodDescriptor + "\"");
            }
        }
        Type[] result = new Type[count];
        System.arraycopy(container, 0, result, 0, count);
        return result;
    }

    public static Type getReturnType(@Nullable final Type methodType) {
        if (methodType == null){
            return VOID;
        }
        if (!methodType.isMethodType()) {
            throw new TypeErrorException("type must be is a method type");
        }
        return getReturnType(methodType.getDescriptor());
    }

    public static Type getReturnType(@NotNull final String methodDescriptor) {
        if (methodDescriptor == null || methodDescriptor.isEmpty()) {
            throw new NotNullException("argument0 must be not null");
        }
        if (!methodDescriptor.startsWith("(")) {
            throw new InvalidDescriptorException("Invalid methodDescriptor: \"" + methodDescriptor + "\"");
        }
        int start = methodDescriptor.indexOf(')') + 1;
        if (start == 0) {
            throw new InvalidDescriptorException("Invalid methodDescriptor: \"" + methodDescriptor + "\"");
        }
        int end = methodDescriptor.length();
        String returnDescriptor = methodDescriptor.substring(start, end);
        return getType(returnDescriptor);
    }

    public static Type getElementType(Class<?> arrayClass) {
        if (arrayClass == null) {
            throw new RuntimeException("parameter 'arrayClass' must be not null");
        } else if (!arrayClass.isArray()) {
            throw new RuntimeException("parameter 'arrayClass' must be a array");
        }
        Class<?> componentClass = arrayClass.getComponentType();
        return getType(componentClass);
    }

    public static Type getElementType(Type type) {
        if (!type.isArrayType()) {
            throw new TypeErrorException(type.getDescriptor() + " not a array type");
        }
        String comDesc = type.getDescriptor().substring(1);
        return getType(comDesc);
    }

    public static Type getArrayType(Type componentType){
        return getType("[" + componentType.getDescriptor());
    }



    public boolean isPrimitiveType() {
        return code <= 8 && code >= 1;
    }

    public static boolean isPrimitiveType(String descriptor) {
        if (descriptor.length() != 1) {
            return false;
        }
        return isPrimitiveType(descriptor.charAt(0));
    }

    public static boolean isPrimitiveType(char descriptor) {
        switch (descriptor) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return true;
            default:
                return false;
        }
    }

    public static Type[] getArgumentTypes(@NotNull Type methodType) {
        if (methodType == null){
            throw new RuntimeException("methodType must be not null");
        }
        if (!methodType.isMethodType()) {
            throw new TypeErrorException("type must be is a method type");
        }
        return getArgumentTypes(methodType.getDescriptor());
    }

    public String getDescriptor() {
        return desc;
    }

    public String getFullClassName(){
        if (!(isObjectType() || isArrayType()))
            throw new TypeErrorException(getDescriptor() + " not an object type");
        String descriptor = getDescriptor();
        if (descriptor.startsWith("L")){
            descriptor = descriptor.substring(1, descriptor.length() - 1);
        }
        return descriptor;
    }

    public boolean isLongOrDoubleType(){
        return code == 7 || code == 8;
    }

    public boolean isObjectType() {
        return code == 10;
    }

    public boolean isVoidType() {
        return code == 0;
    }

    public boolean isReferenceType() {
        return code == 10 || code == 9;
    }

    public boolean isLongType() {
        return code == 7;
    }

    public boolean isDoubleType() {
        return code == 8;
    }

    public boolean isMethodType() {
        return code == 11;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return code == type.code && Objects.equals(desc, type.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, desc);
    }

    public boolean isArrayType() {
        return code == 9;
    }

    public boolean isIntType() {
        return code == 5;
    }

    public boolean isFloatType() {
        return code == 6;
    }

    public boolean isByteType() {
        return code == 3;
    }

    public boolean isCharType() {
        return code == 2;
    }

    public boolean isShortType() {
        return code == 4;
    }

}
