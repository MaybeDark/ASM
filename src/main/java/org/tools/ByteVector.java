package org.tools;

public class ByteVector {
    private byte[] data;
    private int writePoint,readPoint;

    public ByteVector(final int initialSize) {
        data = new byte[initialSize];
    }
    public ByteVector(final byte[] data){
        this(data.length);
        putArray(data);
    }

    public ByteVector putByte(final int b) {
        data[writePoint++] = (byte) b;
        return this;
    }

    public ByteVector putShort(final int s) {
        data[writePoint++] = (byte) (s >>> 8);
        data[writePoint++] = (byte) s;
        return this;
    }

    public ByteVector putInt(final int i) {
        data[writePoint++] = (byte) (i >>> 24);
        data[writePoint++] = (byte) (i >>> 16);
        data[writePoint++] = (byte) (i >>> 8);
        data[writePoint++] = (byte) i;
        return this;
    }

    public ByteVector putLong(final long l) {
        int i = (int) (l >>> 32);
        data[writePoint++] = (byte) (i >>> 24);
        data[writePoint++] = (byte) (i >>> 16);
        data[writePoint++] = (byte) (i >>> 8);
        data[writePoint++] = (byte) i;
        i = (int) l;
        data[writePoint++] = (byte) (i >>> 24);
        data[writePoint++] = (byte) (i >>> 16);
        data[writePoint++] = (byte) (i >>> 8);
        data[writePoint++] = (byte) i;
        return this;
    }

    public ByteVector putArray(final byte[] a){
        System.arraycopy(a,0,data,this.writePoint,a.length);
        writePoint += a.length;
        return this;
    }

    public byte getByte(){
        return data[readPoint++];
    }

    public short getShort(){
        return ConvertTool.B2S(data[readPoint++],data[readPoint++]);
    }

    public int getInt() {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = data[readPoint++];
        }
        return ConvertTool.B2I(bytes);
    }

    public long getLong(){
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = data[readPoint++];
        }
        return ConvertTool.B2L(bytes);
    }

    public byte[] getArray(int length){
        byte[] bytes = new byte[length];
        System.arraycopy(data,readPoint,bytes,0,length);
        readPoint += length;
        return bytes;
    }

    public boolean isEmpty(){
        return writePoint == 0;
    }

    public int getLength() {
        return writePoint;
    }

    public byte[] end(){
        if (isEmpty()){ return null; }
        byte[] result = new byte[writePoint];
        System.arraycopy(data,0,result,0,writePoint);
        data = null;
        return result;
    }
}
