package org.bytecode.method.attribute.code;

import org.Type;
import org.bytecode.method.attribute.code.operandstack.OperandStack;

public class StackLoader {
    private final OperandStack stack;
    public StackLoader(Code owner){
        this.stack = owner.getStack();
    }

    public StackLoader Null(){
        stack.put(Type.NULL);
        return this;
    }

    public StackLoader Int(int num){
        stack.put(Type.INT);
        return this;
    }

    public StackLoader Double(double num){
        stack.put(Type.DOUBLE);
        return this;
    }

    public StackLoader Long(long num){
        stack.put(Type.LONG);
        return this;
    }

    public StackLoader Float(float num){
        stack.put(Type.FLOAT);
        return this;
    }

    public StackLoader String(String str){

        stack.put(Type.STRING);
        return this;
    }

    public StackLoader Local(String localName){

        return this;
    }

    public StackLoader ArrayElements(String arrayLocalName,int index){

        return this;
    }
}
