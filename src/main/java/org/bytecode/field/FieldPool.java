//package org.bytecode.field;
//
//import org.bytecode.constantpool.ConstantPool;
//import org.exception.DefinedException;
//import org.tools.ArrayTool;
//import org.tools.ConvertTool;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class FieldPool {
//    private ArrayList<FieldWriter> pool = new ArrayList<>();
//    private short count = 0;
//    private Map<String,Short> name2field = new HashMap<>();
//
//    public short putField(FieldWriter fieldWriter){
//        checkDefined(fieldWriter.getName());
//        pool.add(fieldWriter);
//        name2field.put(fieldWriter.getName(),count);
//        return count++;
//    }
//
//    private void checkDefined(String name) {
//        Short index = name2field.get(name);
//        if (index!= null) throw new DefinedException("field "+name + " is defined");
//    }
//
//    public void load(ConstantPool constantPool){
//        pool.forEach(fieldWriter -> {
//            if (!fieldWriter.isLoaded())
//                fieldWriter.load(constantPool);
//        });
//    }
//
//    public byte[] toByteArray() {
//        short fieldCount = 0;
//        byte[] fieldsByteArray = ConvertTool.S2B(count);
//        for (FieldWriter fieldWriter : pool) {
//            if (fieldWriter !=null){
//                fieldsByteArray = ArrayTool.join(fieldsByteArray, fieldWriter.toByteArray());
//                fieldCount++;
//            }
//        }
//        //        System.arraycopy(ConvertTool.S2B(fieldCount),0,result,0,2);
////        System.arraycopy(fieldByteArray,0,result,2,fieldByteArray.length);
//        return fieldsByteArray;
//    }
//}
