package org.bytecode.method;

import org.Access;
import org.Type;
import org.bytecode.ClassWriter;
import org.bytecode.attributes.linenumbertable.LineNumberTable;
import org.wrapper.LocalVariableWrapper;
import org.wrapper.MethodWrapper;

public class ConstructorWriter extends MethodWriter {
    public static final String CONSTRUCTOR_METHODNAME = "<init>";
    public static final MethodWrapper BASECLASS_CONSTRUCTOR = new MethodWrapper("java/lang/Object", CONSTRUCTOR_METHODNAME, null);
    private boolean calledParentConstructor = false;

    public ConstructorWriter(ClassWriter classWriter, int access, MethodWrapper parentConstructor, LocalVariableWrapper... parameters) {
        super(classWriter, access, CONSTRUCTOR_METHODNAME, Type.VOID, parameters);
        if (parentConstructor == null) {
            loadLocal("this");
            invokeSpecial(BASECLASS_CONSTRUCTOR);
            calledParentConstructor = true;
            return;
        }
        if (parentConstructor.getPut() == 0) {
            loadLocal("this");
            invokeSpecial(parentConstructor);
            calledParentConstructor = true;
        }
    }

    private ConstructorWriter(ClassWriter classWriter) {
        super(classWriter, Access.ACC_PUBLIC, CONSTRUCTOR_METHODNAME, Type.VOID);
        loadLocal("this");
        invokeSpecial(new MethodWrapper(
                classWriter.getSuperClass().getClassInfo(),
                CONSTRUCTOR_METHODNAME,
                Type.VOID));
        return_();
        calledParentConstructor = true;
        getCode().addAttribute(new LineNumberTable().put(0, 6));
    }

    public static ConstructorWriter getNoArgsConstructor(ClassWriter classWriter) {
        return new ConstructorWriter(classWriter);
    }

    @Override
    public byte[] toByteArray() {
        if (! calledParentConstructor) {
            throw new RuntimeException("Parent class not init");
        }
        return super.toByteArray();
    }

    public boolean isCalledParentConstructor() {
        return calledParentConstructor;
    }


}