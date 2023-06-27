package org.other.Label;

public class LabelStack {
    private int top = -1;
    private final Label[] labels = new Label[8];

    public Label pop(int endPc){
        if (top == -1){
            throw new RuntimeException("label end error");
        }
        Label label = labels[top];
        label.setLength(endPc - label.getStartPc());
        top --;
        return label;
    }

    public void put(Label label){
        labels[++top] = label;
    }

    public boolean isEmpty(){
        return top == -1;
    }
}

