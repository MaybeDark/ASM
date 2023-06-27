package org.bytecode.cls.attribute.bootstrapmethod;

import org.Listable;
import org.bytecode.BytecodeFileSpecification;
import org.bytecode.com.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.other.Pool;
import org.tools.ConvertTool;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BootstrapMethods extends Attribute implements Listable {
    public final static int BOOTSTRAP_METHOD_COUNT_LENGTH = 2;
    private short bootStrapMethodCount = 0;
    private List<BootstrapMethod> pool;
    private Map<Integer,Short> hash2Index = new HashMap<>();

    public BootstrapMethods(){
        attributeLength = BOOTSTRAP_METHOD_COUNT_LENGTH;
        pool = new ArrayList<>();
    }

    public short putBootStrapMethod(BootstrapMethod bootStrapMethod){
        int key = bootStrapMethod.hashCode();
        Short value = hash2Index.putIfAbsent(key, bootStrapMethodCount);
        if (value != null) {
            return value;
        }
        pool.add(bootStrapMethod);
        attributeLength += bootStrapMethod.getLength();
        return bootStrapMethodCount++;
    }

    public boolean isEmpty(){
        return bootStrapMethodCount == 0;
    }

    @Override
    public short load(ConstantPool constantPool) {
        if (loaded){
            return cpIndex;
        }
        cpIndex = constantPool.putUtf8Info("BootstrapMethods");
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            bootstrapMethod.load(constantPool);
        }
        loaded = true;
        return cpIndex;
    }

    public byte[] toByteArray(){
        if (!loaded){
            throw new RuntimeException("bootstrapMethods attribute need load before use");
        }
        byte[] result = new byte[2 + 4 + attributeLength];
        System.arraycopy(ConvertTool.S2B(cpIndex),0,result,0,2);
        System.arraycopy(ConvertTool.I2B(attributeLength),0,result,2,4);
        System.arraycopy(ConvertTool.S2B(bootStrapMethodCount),0,result,6,2);

        int index = 0;
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            int length = bootstrapMethod.getLength();
            System.arraycopy(bootstrapMethod.getValue(),0,result,8 + index,length);
            index += length;
        }
        return result;
    }

    @Override
    public String list() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bootStrapMethodCount; i++) {
            BootstrapMethod bootstrapMethod = pool.get(i);
            sb.append(String.format("[%02d]", i));
            sb.append(String.format("%-15s", bootstrapMethod.getReferenceKind()));
            sb.append(String.format("%-15s", bootstrapMethod.getFullClassName()));
            sb.append(String.format("%-15s", bootstrapMethod.getMethodName()+":"+bootstrapMethod.getMethodDesc()));
            sb.append(bootstrapMethod.getArgs());
        }
        return sb.toString();
    }
}
