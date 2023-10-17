package org.tools;


public class ConvertTool {

    public static long B2L(final byte... bytes){
        long result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << (length-i-1) * 8;
        }
        return result;
    }

    public static int B2I(final byte... bytes){
        int result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (bytes[i] & 0xff) << (length-i-1) * 8;
        }
        return result;
    }

    public static short B2S(final byte... bytes){
        short result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return result;
    }

    public static double B2D(final byte... bytes){
        long result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return Double.longBitsToDouble(result);
    }

    public static float B2F(final byte... bytes){
        int result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return Float.intBitsToFloat(result);
    }

    public static byte[] F2B(final float num){
        return I2B(Float.floatToIntBits(num));
    }

    public static byte[] D2B(final double num){
        long l = Double.doubleToLongBits(num);
        l = Long.parseUnsignedLong(Long.toBinaryString(l),2);
        return L2B(l);
    }

    public static byte[] S2B(final short num) {
        byte[] result = new byte[2];
        int index = 0;
        result[index++] = (byte) (num >>> 8);
        result[index] = (byte) num;
        return result;
    }

    public static byte[] S2B(int num) {
        return S2B((short) num);
    }

    public static byte[] I2B(final int num) {
        byte[] result = new byte[4];
        int index = 0;
        result[index++] = (byte) (num >>> 24);
        result[index++] = (byte) (num >>> 16);
        result[index++] = (byte) (num >>> 8);
        result[index] = (byte) num;
        return result;
    }

    public static byte[] L2B(final long num) {
        byte[] result = new byte[8];
        int index = 0;
        int i = (int) (num >>> 32);
        result[index++] = (byte) (i >>> 24);
        result[index++] = (byte) (i >>> 16);
        result[index++] = (byte) (i >>> 8);
        result[index++] = (byte) i;

        i = (int) num;
        result[index++] = (byte) (i >>> 24);
        result[index++] = (byte) (i >>> 16);
        result[index++] = (byte) (i >>> 8);
        result[index]   = (byte) i;
        return result;
    }
}
