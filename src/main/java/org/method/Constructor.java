package org.method;

import org.Type;

public class Constructor extends Method{


    public Constructor(short access, String fullClassName, String methodName, Type returnType, Type... parameterType) {
        super(access, fullClassName, "<init>", returnType, parameterType);
    }
}
