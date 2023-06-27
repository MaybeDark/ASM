package org.bytecode.method.attribute.code.bracket;

public class Bracket {
    private int startPc;
    private int length;

    public Bracket(int startPc) {
        this.startPc = startPc;
    }

    public int getStartPc() {
        return startPc;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
