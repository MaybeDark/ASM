package org.bytecode.method;

import org.InstructionSet;
import org.Type;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

public class Constructor extends Method{
    public static final MethodWrapper objectInit = new MethodWrapper(Type.getType(Object.class),"init",null);
    private boolean calledParentConstructor = false;

    public Constructor(short access, String fullClassName,MethodWrapper parentConstructor,LocalVariableWrapper... parameters) {
        super(access, fullClassName, "<init>", Type.VOID, parameters);
        loadLocal("this");
        if (parentConstructor.getPut() == 0){
            invokeMethod(InstructionSet.INVOKESPECIAL.toString(),parentConstructor);
            calledParentConstructor = true;
        }
   }



    @Override
    public byte[] toByteArray() {
        if (!calledParentConstructor){
            throw new RuntimeException("Parent not init");
        }
        return super.toByteArray();
    }

    public boolean isCalledParentConstructor(){
        return calledParentConstructor;
    }


}