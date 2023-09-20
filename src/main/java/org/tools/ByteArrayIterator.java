package org.tools;


public class ByteArrayIterator{

    public byte[] value;
    public int length;
    private int index;

    public ByteArrayIterator(byte[] value){
        this.value = value;
        this.length = value.length;
    }

    public boolean hasNext(){
        return index < this.length;
    }

    public byte next(){
        return next(1)[0];
    }
    public byte[] next(int length){
        if (!hasNext()){
            return null;
        }
        byte[] result;
        if (index + length > this.length)
            length = this.length - index;
        result = new byte[length];
        System.arraycopy(value,index,result,0,length);
        index += length;
        return result;
    }

}
