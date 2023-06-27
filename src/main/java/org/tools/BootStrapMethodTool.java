package org.tools;

import org.attribute.cls.BootstrapMethod;
import org.attribute.cls.BootstrapMethods;
import org.constantpool.ConstantPool;
import org.constantpool.info.ConstantPoolMethodHandleInfo;
import org.constantpool.info.ConstantPoolMethodTypeInfo;
import org.constantpool.ReferenceKind;

public class BootStrapMethodTool {
    private static final String lambdaMetafactoryClassName = "java/lang/invoke/LambdaMetafactory";
    private static final String lambdaMetafactoryMethodName = "metafactory";
    private static final String lambdaMetafactoryMethodDesc = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";

    public static short creatLambdaMethodInMethod(BootstrapMethods bsmp, ConstantPool constantPool, String lambdaMethodFullInterfaceName, String lambdaMethodName,String lambdaMethodDesc,String callerMethodName, String callerFullClassName,int beforeLambdaMethodCount){
        BootstrapMethod bootStrapMethod = new BootstrapMethod(
                ReferenceKind.REF_invokeStatic
                ,lambdaMetafactoryClassName
                ,lambdaMetafactoryMethodName
                ,lambdaMetafactoryMethodDesc
                ,new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
                ,new ConstantPoolMethodHandleInfo(ReferenceKind.REF_invokeStatic,callerFullClassName,"lambda$"+callerMethodName+"$"+beforeLambdaMethodCount,lambdaMethodDesc)
                ,new ConstantPoolMethodTypeInfo(lambdaMethodDesc)
        );
        String newLambdaMethodDesc = "()"+lambdaMethodFullInterfaceName;
        return constantPool.putInvokeDynamicInfo(bsmp, lambdaMethodName, newLambdaMethodDesc, bootStrapMethod);
    }

    public static short creatLambdaMethodInMethod(BootstrapMethods bsmp, ConstantPool constantPool, String lambdaMethodInterfaceName, String lambdaMethodName,String lambdaMethodDesc,String callerMethodName, String callerFullClassName){
        return creatLambdaMethodInMethod(bsmp, constantPool, lambdaMethodInterfaceName, lambdaMethodName, lambdaMethodDesc, callerMethodName, callerFullClassName, 0);
    }

    public static short creatLambdaMethodInField(BootstrapMethods bsmp, ConstantPool constantPool, String lambdaMethodInterfaceName, String lambdaMethodName,String lambdaMethodDesc, String callerFullClassName){
        return creatLambdaMethodInMethod(bsmp,constantPool,lambdaMethodInterfaceName,lambdaMethodName,lambdaMethodDesc,"new",callerFullClassName,0);
    }

    public static short creatLambdaMethodInField(BootstrapMethods bsmp, ConstantPool constantPool, String lambdaMethodInterfaceName, String lambdaMethodName,String lambdaMethodDesc, String callerFullClassName,int beforeLambdaMethodCount){
        return creatLambdaMethodInMethod(bsmp,constantPool,lambdaMethodInterfaceName,lambdaMethodName,lambdaMethodDesc,"new",callerFullClassName,beforeLambdaMethodCount);
    }
}