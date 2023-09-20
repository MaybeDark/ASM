//package org.bytecode.method;
//
//import org.bytecode.constantpool.ConstantPool;
//import org.tools.ArrayTool;
//import org.tools.ConvertTool;
//
//import java.util.*;
//
//public class MethodPool {
//    private ArrayList<MethodWriter> pool = new ArrayList<>();
//    private Map<String,Map<String ,Integer>> name2methods = new HashMap<>();
//    private short methodCount = 0;
//
//    public void putMethod(MethodWriter methodWriter) {
//        HashMap<String, Integer> desc2method = new HashMap<>();
//        desc2method.put(methodWriter.getMethodDesc(),pool.size());
//        Map<String, Integer> result = name2methods.putIfAbsent(methodWriter.getMethodName(), desc2method);
//        if (result != null){
//            result.putIfAbsent(methodWriter.getMethodDesc(),pool.size());
//        }
//        pool.add(methodWriter);
//        methodCount ++;
//    }
//
//    public List<MethodWriter> getByName(String name){
//        Map<String, Integer> desc2Method = name2methods.get(name);
//        if (desc2Method == null){
//            return null;
//        }
//        List<MethodWriter> methodWriters = new ArrayList<>();
//        desc2Method.forEach((k,v)->{
//            MethodWriter methodWriter = getByIndex(v);
//            methodWriters.add(methodWriter);
//        });
//        return methodWriters;
//    }
//
//    public byte[] toByteArray(){
//        byte[] result = ConvertTool.S2B(methodCount);
//        for (MethodWriter methodWriter : pool) {
//            if (methodWriter !=null){
//                result = ArrayTool.join(result, methodWriter.toByteArray());
//            }
//        }
//        return result;
//    }
//
//    public void load(ConstantPool constantPool){
//        pool.forEach((method)-> method.load(constantPool));
//    }
//
//    private MethodWriter getByIndex(int index){
//        return pool.get(index);
//    }
//}
//
