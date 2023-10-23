package org.bytecode.attributes.stackmaptable.stackmapframe;

public interface StackMapFrame {
    byte[] toByteArray();

    int getLength();
}
