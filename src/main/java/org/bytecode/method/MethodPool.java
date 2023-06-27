package org.bytecode.method;

import org.bytecode.constantpool.ConstantPool;
import org.tools.ArrayTool;
import org.tools.ConvertTool;

import java.util.*;

public class MethodPool {
    private ArrayList<Method> pool = new ArrayList<>();
    private Map<String,Map<String ,Integer>> name2methods = new HashMap<>();
    private short methodCount = 0;

    public void putMethod(Method method) {
        HashMap<String, Integer> desc2method = new HashMap<>();
        desc2method.put(method.getMethodDesc(),pool.size());
        Map<String, Integer> result = name2methods.putIfAbsent(method.getMethodName(), desc2method);
        if (result != null){
            result.putIfAbsent(method.getMethodDesc(),pool.size());
        }
        pool.add(method);
        methodCount ++;
    }

    public List<Method> getByName(String name){
        Map<String, Integer> desc2Method = name2methods.get(name);
        if (desc2Method == null){
            return null;
        }
        List<Method> methods = new ArrayList<>();
        desc2Method.forEach((k,v)->{
            Method method = getByIndex(v);
            methods.add(method);
        });
        return methods;
    }

    public byte[] toByteArray(){
        byte[] result = ConvertTool.S2B(methodCount);
        for (Method method : pool) {
            if (method!=null){
                result = ArrayTool.join(result,method.toByteArray());
            }
        }
        return result;
    }

    public void load(ConstantPool constantPool){
        pool.forEach((method)-> method.load(constantPool));
    }

    private Method getByIndex(int index){
        return pool.get(index);
    }
}

