package org.other.Label;

public class Label {
    private int startPc;
    private int length;

    public Label(int startPc) {
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
