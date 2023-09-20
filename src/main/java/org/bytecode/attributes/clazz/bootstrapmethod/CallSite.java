package org.bytecode.attributes.clazz.bootstrapmethod;

import org.Type;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;
import org.bytecode.method.MethodWriter;

public class CallSite{
    private final Type targetType;
    private final String targetMethodName;
    public final static Type callSiteType = Type.getType(java.lang.invoke.CallSite.class);
    private MethodWriter proxyMethodWriter;
    private final short index;
    private ConstantPoolMethodHandleInfo handleInfo;

    CallSite(Class<?> target,ConstantPoolMethodHandleInfo handleInfo,short index){
        targetType = Type.getType(target);
        this.handleInfo = handleInfo;
        this.index = index;
        targetMethodName = target.getDeclaredMethods()[0].getName();
    }

    public void setProxyMethod(MethodWriter proxyMethodWriter){
        handleInfo.setName(proxyMethodWriter.getMethodName());
        this.proxyMethodWriter = proxyMethodWriter;
    }

    public MethodWriter getProxyMethod(){
        return proxyMethodWriter;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public Type getTargetType() {
        return targetType;
    }

    public short getIndex() {
        return index;
    }
}
