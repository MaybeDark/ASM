package org.proxy;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.ConstructorWriter;
import org.bytecode.FieldWriter;
import org.bytecode.MethodWriter;
import org.bytecode.attributes.code.Code;
import org.bytecode.attributes.code.instruction.InstructionSet;
import org.bytecode.attributes.stackmaptable.StackMapTable;
import org.bytecode.attributes.stackmaptable.stackmapframe.SameLocals1StackItemFrame;
import org.bytecode.attributes.stackmaptable.stackmapframe.verificationtypeinfo.ObjectVariableInfo;
import org.wrapper.ClassWrapper;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

import java.lang.reflect.*;

import static org.Access.*;
import static org.proxy.ProxyConstant.CATCH_CLASS_INFO;
import static org.proxy.ProxyConstant.INIT_METHOD_NAME;

public class ProxyFactory {
    protected static byte[] interfaceProxyModel;

    public static <T> T getInterfaceProxy(Class<T> target, InvocationHandler handler) {
        if (! target.isInterface()) {
            throw new RuntimeException("this method just handle interface");
        }
        Exchanger exchanger = new Exchanger(target, getInterfaceProxyModel());
        Class<?> targetClass = ClassLoaderFactory.getDefaultByteCodeLoader().loadByteCode(exchanger.getClassName(), exchanger.toByteArray());
        try {
            Constructor<?> constructor = targetClass.getConstructors()[0];
            return (T) constructor.newInstance(handler);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public static <T> T getClassProxy(Class<T> target) {

        return null;
    }

    public static byte[] getInterfaceProxyModel() {
        if (interfaceProxyModel == null) {
            interfaceProxyModel = getInterfaceProxyModel0();
        }
        return interfaceProxyModel;
    }

    protected static byte[] getInterfaceProxyModel0() {
        ClassWriter classWriter = new ClassWriter(ACC_PUBLIC | ACC_SUPER, "org/proxy/Proxy");
        FieldWriter handler = classWriter.addField(ACC_PROTECTED, "handler", Type.getType("Ljava/lang/reflect/InvocationHandler;"));
        MethodWriter handlerMethod = classWriter.addMethod(ACC_PROTECTED, "invoke", Type.getType(Object.class),
                new LocalVariableWrapper("method", Type.getType(Method.class)),
                new LocalVariableWrapper("args", Type.getType(Object[].class)));
        handlerMethod.loadLocal("this");
        handlerMethod.loadField(handler.wrapper(), false);
        handlerMethod.loadLocal("this");
        handlerMethod.loadLocal("method");
        handlerMethod.loadLocal("args");
        try {
            handlerMethod.invokeInterface(new MethodWrapper(InvocationHandler.class.getMethod("invoke", Object.class, Method.class, Object[].class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        handlerMethod.return_();
        handlerMethod.putExceptionToStack(Throwable.class);
        handlerMethod.storeLocal("throwable");
        handlerMethod.newObject(new ClassWrapper(UndeclaredThrowableException.class));
        handlerMethod.stackBehavior(InstructionSet.DUP);
        handlerMethod.loadLocal("throwable");
        handlerMethod.invokeSpecial(new MethodWrapper("java/lang/reflect/UndeclaredThrowableException", INIT_METHOD_NAME, Type.VOID, Type.getType(Throwable.class)));
        handlerMethod.throwException();
        Code code = handlerMethod.getCode();
        Type catchType = Type.getType(Type.getClassDescriptor(CATCH_CLASS_INFO));
        code.getExceptionTable().putHandler(0, 12, 13, catchType);
        StackMapTable stackMapTable = new StackMapTable();
        stackMapTable.addStackMapFrame(new SameLocals1StackItemFrame((byte) 13, new ObjectVariableInfo(classWriter.getConstantPool().putClassInfo(CATCH_CLASS_INFO))));
        code.addAttribute(stackMapTable);
        MethodWriter constructor = classWriter.addConstructor(ACC_PUBLIC, ConstructorWriter.BASECLASS_CONSTRUCTOR, new LocalVariableWrapper("handler", Type.getType(InvocationHandler.class)));
        constructor.loadLocal("this")
                .loadLocal("handler")
                .storeField(handler.wrapper(), false)
                .return_();
        return classWriter.toByteArray();
    }


}
