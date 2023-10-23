package org.bytecode.attributes;

public interface Target {
    byte class_info = 1;
    byte field_info = 2;
    byte method_info = 4;
    byte code_info = 8;

    static boolean check(byte target, byte check) {
        return (target & check) != 0;
    }

}
