package org.tools;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DescTool {

//    private static final Map<Class<?> ,String> transforms = new HashMap<>();
//    private static final Map<String,Class<?>> backTransforms = new HashMap<>();
//    static{
//        transforms.put(Integer.class,"I");
//        transforms.put(Double.class,"D");
//        transforms.put(Byte.class,"B");
//        transforms.put(Character.class,"C");
//        transforms.put(Float.class,"F");
//        transforms.put(Long.class,"J");
//        transforms.put(Short.class,"S");
//        transforms.put(Boolean.class,"Z");
//        backTransforms.put("int",Integer.class);
//        backTransforms.put("double",Double.class);
//        backTransforms.put("byte",Byte.class);
//        backTransforms.put("char",Character.class);
//        backTransforms.put("float",Float.class);
//        backTransforms.put("long",Long.class);
//        backTransforms.put("short",Short.class);
//        backTransforms.put("boolean",Boolean.class);
//    }
//
//    public static Class<?> getComponentType(Class<?> arrayClass){
//        if (arrayClass == null){
//            throw new RuntimeException("parameter 'arrayClass' must be not null");
//        }else if (!arrayClass.isArray()){
//            throw new RuntimeException("parameter 'arrayClass' must be a array");
//        }
//        Class<?> componentClass = arrayClass.getComponentType();
//        Class<?> result = backTransforms.get(componentClass.getName());
//        if (result!=null){
//            return result;
//        }else return componentClass;
//    }
//
//    public static String typeTransforms(@NotNull Class<?> clazz){
//        if (clazz == null){
//            throw new RuntimeException("parameter 'clazz' must be not null");
//        }
//        StringBuilder result = new StringBuilder();
//        if (transforms.get(clazz) != null) {
//            result.append(transforms.get(clazz));
//        }else{
//            String className = clazz.getName();
//            className = className.replace(".","/");
//            if (clazz.isArray()){
//                result.append(className);
//            }else {
//                result.append('L');
//                result.append(className);
//                result.append(';');
//            }
//       }
//        return result.toString();
//    }
//
//    public static String getMethodDesc(@Nullable Class<?> returnType, @Nullable Class<?>... parameterType){
//        StringBuilder result = new StringBuilder();
//        result.append('(');
//        if(parameterType != null && parameterType.length != 0) {
//            for (Class<?> clazz : parameterType) {
//                if (clazz == null)
//                    break;
//                result.append(typeTransforms(clazz));
//            }
//        }
//        result.append(')');
//        if (returnType == null){
//            result.append('V');
//        }else {
//            result.append(typeTransforms(returnType));
//        }
//        return result.toString();
//    }


}
