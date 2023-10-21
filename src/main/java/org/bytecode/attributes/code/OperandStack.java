package org.bytecode.attributes.code;

import org.Type;
import org.exception.TypeErrorException;

public class OperandStack {
    private short currentDepth = 0;
    private short max = 0;
    private int currentSize =  8;
    private Type[] stack = new Type[currentSize];
    private short tempMax = -1;

    public short put(Type type){
        if (type == null){
            return currentDepth;
        }
        int depth = type.isDoubleType() || type.isLongType()?2:1;
        considerExpansion(depth);
        for (int i = 0; i < depth ; i++) {
            stack[currentDepth++] = type;
        }
        if (currentDepth > max){
            max = currentDepth;
        }
        return currentDepth;
    }


    /**
     * 解决分支语句中应选择性压入值,而不是被视为顺序执行压入值
     * 例如表达式'a == b'编译会成为:
     * if_icmpne
     * iconst_1
     * goto
     * iconst_0
     * 这时候需要弹出顺序执行下压入的值
     */
    public void startIfScope(){
        tempMax = currentDepth;
    }

    public void endIfScope() {
        if (tempMax < currentDepth) {
            while (currentDepth > tempMax)
                pop();
        }
        tempMax = - 1;
    }

    public short getMax() {
        return max;
    }

    public void setMax(short max) {
        this.max = max;
    }

    public boolean isEmpty() {
        return currentDepth == 0;
    }

    public Type pop() {
        if (currentDepth == 0)
            throw new RuntimeException("no more element");
        Type type = stack[currentDepth - 1];
        int depth = type.isDoubleType() || type.isLongType() ? 2 : 1;
        currentDepth -= depth;
        return type;
    }

    public Type popAndCheck(Type type){
        Type pop = pop();
        if (!pop.equals(type)) {
            throw new TypeErrorException("Expected an "+type.getDescriptor()+" type but unexpectedly popped a "+pop.getDescriptor()+" type");
        }
        return pop;
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
        System.arraycopy(stack,0,result,0,currentDepth);
        return result;
    }

    private void considerExpansion(int depth) {
        if(currentDepth+depth <= currentSize)
            return;

        if (currentSize * 2 >= (1<<16))
            throw new RuntimeException("operand stack too large");
        Type[] newFrame = new Type[currentSize * 2];
        System.arraycopy(stack ,0,newFrame,0,currentSize);
        currentSize *= 2;
        stack = newFrame;
    }
}
