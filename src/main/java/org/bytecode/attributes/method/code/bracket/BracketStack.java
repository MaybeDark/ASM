package org.bytecode.attributes.method.code.bracket;

public class BracketStack {
    private int top = -1;
    private final Bracket[] brackets = new Bracket[8];

    public Bracket pop(int endPc){
        if (isEmpty()){
            throw new RuntimeException("Invalid bracket ending");
        }
        Bracket bracket = brackets[top];
        bracket.setLength(endPc - bracket.getStartPc());
        top --;
        return bracket;
    }

    public Bracket seeTop(){
        return brackets[top];
    }


    public void put(Bracket bracket){
        brackets[++top] = bracket;
    }

    public boolean isEmpty(){
        return top == -1;
    }
}

