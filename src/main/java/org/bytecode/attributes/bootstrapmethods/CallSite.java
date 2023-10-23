package org.bytecode.attributes.bootstrapmethods;

import org.Type;
import org.bytecode.MethodWriter;
import org.bytecode.constantpool.info.ConstantPoolMethodHandleInfo;

public class CallSite {
    private final Type targetType;
    private final String targetMethodName;
    public final static Type callSiteType = Type.getType(java.lang.invoke.CallSite.class);
    private MethodWriter proxyMethodWriter;
    private final short index;
    private ConstantPoolMethodHandleInfo handleInfo;

    public CallSite(Class<?> functionInterface, ConstantPoolMethodHandleInfo handleInfo, short index) {
        targetType = Type.getType(functionInterface);
        this.handleInfo = handleInfo;
        this.index = index;
        targetMethodName = functionInterface.getDeclaredMethods()[0].getName();
    }

    public void setProxyMethod(MethodWriter proxyMethodWriter) {
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
