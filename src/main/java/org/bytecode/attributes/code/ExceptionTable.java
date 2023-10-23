package org.bytecode.attributes.code;

import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.ArrayList;
import java.util.List;

public class ExceptionTable {
    private short handlerCount;
    private List<ExceptionHandler> handlers = new ArrayList<>();
    private boolean loaded = false;

    public void putHandler(short startPc, short endPc, short handlerPc, Type catchType) {
        handlers.add(new ExceptionHandler(startPc, endPc, handlerPc, catchType));
        handlerCount++;
    }

    public boolean isEmpty() {
        return handlerCount == 0;
    }

    public void load(ConstantPool constantPool) {
        if (! isEmpty()) {
            handlers.forEach(handler -> handler.setCatchTypeIndex(constantPool.putClassInfo(handler.getCatchType().getClassInfo())));
        }
        loaded = true;
    }

    public byte[] toByteArray() {
        if (loaded) {
            throw new RuntimeException("not loaded");
        }
        ByteVector byteVector = new ByteVector(2 + 4 * handlerCount);
        byteVector.putShort(handlerCount);
        if (! isEmpty()) {
            handlers.forEach(handler -> {
                byteVector.putShort(handler.startPc);
                byteVector.putShort(handler.endPc);
                byteVector.putShort(handler.handlerPc);
                byteVector.putShort(handler.catchTypeIndex);
            });
        }
        return byteVector.end();
    }

    class ExceptionHandler {
        private short startPc;
        private short endPc;
        private short handlerPc;
        private Type catchType;
        private short catchTypeIndex;

        public ExceptionHandler(short startPc, short endPc, short handlerPc, Type catchType) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.catchType = catchType;
        }

        public short getCatchTypeIndex() {
            return catchTypeIndex;
        }

        public void setCatchTypeIndex(short catchTypeIndex) {
            this.catchTypeIndex = catchTypeIndex;
        }

        public Type getCatchType() {
            return catchType;
        }

        public void setCatchType(Type catchType) {
            this.catchType = catchType;
        }
    }


}
