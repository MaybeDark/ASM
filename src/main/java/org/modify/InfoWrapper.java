package org.modify;

import org.tools.ByteVectors;

import java.util.Arrays;
import java.util.Objects;

public class InfoWrapper {
    byte tag;
    byte[] value;

    public InfoWrapper(byte tag, byte[] value) {
        this.tag = tag;
        this.value = value;
    }

    public InfoWrapper(int tag, byte[] value) {
        this((byte) tag, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoWrapper that = (InfoWrapper) o;
        return tag == that.tag && Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tag);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    public byte getTag() {
        return tag;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] toByteArray() {
        ByteVectors byteVectors = new ByteVectors();
        byteVectors.putByte(tag)
                .putArray(value);
        return byteVectors.toByteArray();
    }
}
