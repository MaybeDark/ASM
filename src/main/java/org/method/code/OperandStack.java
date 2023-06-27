package org.method.code;

import org.Type;

public class OperandStack {
    public short currentDepth = 0;
    public int currentSize =  8;
    public Type[] stackFrame = new Type[currentSize];


    public short put(Type type){
        if (type == null){
            return currentDepth;
        }
        int depth = type.isDoubleType() || type.isLongType()?2:1;
        considerExpansion(depth);
        for (int i = 0; i < depth ; i++) {
            stackFrame[currentDepth++] = type;
        }
        return currentDepth;
    }


    public Type pop(){
        if (currentDepth == 0)
            throw new RuntimeException("no more element");
        Type type = stackFrame[currentDepth - 1];
        int depth = type.isDoubleType() || type.isLongType()?2:1;
        currentDepth -= depth;
        return type;
    }

    public short pop(int num){
        for (int i = 0; i < num; i++) {
            pop();
        }
        return currentDepth;
    }

    public short getDepth(){
        return currentDepth;
    }

    public Type[] look(){
        Type[] result = new Type[currentDepth];
        System.arraycopy(stackFrame,0,result,0,currentDepth);
        return result;
    }

    private void considerExpansion(int depth) {
        if(currentDepth+depth <= currentSize)
            return;

        if (currentSize * 2 >= (1<<16))
            throw new RuntimeException("operand stack too large");
        Type[] newFrame = new Type[currentSize * 2];
        System.arraycopy(stackFrame ,0,newFrame,0,currentSize);
        currentSize *= 2;
        stackFrame = newFrame;
    }
}
