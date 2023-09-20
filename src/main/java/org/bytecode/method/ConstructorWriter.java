package org.bytecode.method;

import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.Specification;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

public class ConstructorWriter extends MethodWriter {
    public static final MethodWrapper BASECLASS_CONSTRUCTOR = new MethodWrapper("java/lang/Object",Specification.CONSTRUCTOR_METHODNAME,null);
    private boolean calledParentConstructor = false;

    public ConstructorWriter(ClassWriter classWriter, int access, MethodWrapper parentConstructor, LocalVariableWrapper... parameters) {
        super(classWriter,access, Specification.CONSTRUCTOR_METHODNAME, Type.VOID, parameters);
        if (parentConstructor == null){
            loadLocal("this");
            invokeSpecial(BASECLASS_CONSTRUCTOR);
            calledParentConstructor = true;
            return;
        }
        if (parentConstructor.getPut() == 0){
            loadLocal("this");
            invokeSpecial(parentConstructor);
            calledParentConstructor = true;
        }
   }


    @Override
    public byte[] toByteArray() {
        if (!calledParentConstructor){
            throw new RuntimeException("Parent class not init");
        }
        return super.toByteArray();
    }

    public boolean isCalledParentConstructor(){
        return calledParentConstructor;
    }


}