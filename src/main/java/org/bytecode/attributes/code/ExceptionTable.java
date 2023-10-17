package org.bytecode.attributes.code;

import org.Type;

public class ExceptionTable {
    private short handlerCount;

    public void putHandler(short startPc, Type catchType) {

    }

    class ExceptionHandler {
        private short startPc;
        private short endPc;
        private short handlerPc;
        private Type catchType;
        private short catchTypeIndex;


    }


}
