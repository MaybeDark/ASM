package org.bytecode.field;

import org.bytecode.constantpool.ConstantPool;
import org.exception.DefinedException;
import org.tools.ArrayTool;
import org.tools.ConvertTool;
import org.wrapper.LocalVariableWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FieldPool {
    private ArrayList<Field> pool = new ArrayList<>();
    private short count = 0;
    private Map<String,Short> name2field = new HashMap<>();

    public short putField(Field field){
        checkDefined(field.getName());
        pool.add(field);
        name2field.put(field.getName(),count);
        return count++;
    }

    private void checkDefined(String name) {
        Short index = name2field.get(name);
        if (index!= null) throw new DefinedException("field "+name + " is defined");
    }

    public void load(ConstantPool constantPool){
        pool.forEach(field -> {
            if (!field.isLoaded())
                field.load(constantPool);
        });
    }

    public byte[] toByteArray() {
        short fieldCount = 0;
        byte[] fieldByteArray = new byte[0];
        for (Field field : pool) {
            if (field!=null){
                fieldByteArray = ArrayTool.join(fieldByteArray,field.toByteArray());
                fieldCount++;
            }
        }
        byte[] result = new byte[2+fieldByteArray.length];
        System.arraycopy(ConvertTool.S2B(fieldCount),0,result,0,2);
        System.arraycopy(fieldByteArray,0,result,2,fieldByteArray.length);
        return result;
    }
}
