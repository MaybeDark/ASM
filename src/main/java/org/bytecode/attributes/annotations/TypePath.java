package org.bytecode.attributes.annotations;

import org.tools.ByteVector;

public class TypePath {
    private byte pathLength;
    private Path[] paths = new Path[8];

    public byte[] toByteArray() {
        ByteVector byteVectors = new ByteVector(getLength());
        byteVectors.putByte(pathLength);
        Path path;
        for (int i = 0; i < pathLength; i++) {
            path = paths[i];
            byteVectors.putByte(path.kind);
            byteVectors.putByte(path.index);
        }
        return byteVectors.end();
    }

    public int getLength() {
        return 1 + pathLength * 2;
    }

    public TypePath addPath(byte kind, byte index) {
        paths[pathLength++] = new Path(kind, index);
        return this;
    }

    class Path {
        byte kind;
        byte index;

        public Path(byte kind, byte index) {
            this.kind = kind;
            this.index = index;
        }
    }

}
