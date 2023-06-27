package org.tools;


public class ConvertTool {

    public static long B2L(final byte[] bytes){
        long result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << (length-i-1) * 8;
        }
        return result;
    }

    public static int B2I(final byte[] bytes){
        int result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (bytes[i] & 0xff) << (length-i-1) * 8;
        }
        return result;
    }

    public static short B2S(final byte[] bytes){
        short result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return result;
    }

    public static double B2D(final byte[] bytes){
        long result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return Double.longBitsToDouble(result);
    }

    public static float B2F(final byte[] bytes){
        int result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            result |= (long) (bytes[i] & 0xff) << ((length-i-1) * 8);
        }
        return Float.intBitsToFloat(result);
    }

    public static byte[] S2B(final short num){
        byte[] result = new byte[2];
        int index = 0;
        result[index++] = (byte) (num >>> 8);
        result[index]   = (byte) num;
        return result;
    }

    public static byte[] I2B(final int num){
        byte[] result = new byte[4];
        int index = 0;
        result[index++] = (byte) (num >>> 24);
        result[index++] = (byte) (num >>> 16);
        result[index++] = (byte) (num >>> 8);
        result[index]   = (byte) num;
        return result;
    }

    public static byte[] F2B(final float num){
        byte[] result = new byte[4];
        int index = 0;
        int i = Float.floatToIntBits(num);
        String s = Integer.toBinaryString(i);
//        i = Integer.parseUnsignedInt(s,2);
        result[index++] = (byte) (i >>> 24);
        result[index++] = (byte) (i >>> 16);
        result[index++] = (byte) (i >>> 8);
        result[index]   = (byte)  i;
        return result;
    }

    public static byte[] L2B(Long num) {
        byte[] result = new byte[8];
        long l = num;
        int index = 0;
        result[index++] = (byte) (l >>> 56);
        result[index++] = (byte) (l >>> 48);
        result[index++] = (byte) (l >>> 40);
        result[index++] = (byte) (l >>> 32);
        result[index++] = (byte) (l >>> 24);
        result[index++] = (byte) (l >>> 16);
        result[index++] = (byte) (l >>> 8);
        result[index]   = (byte) l;
        return result;
    }

    public static byte[] D2B(final double num){
        byte[] result = new byte[8];
        int index = 0;
        long l = Double.doubleToLongBits(num);
        l = Long.parseUnsignedLong(Long.toBinaryString(l),2);

        result[index++] = (byte) (l >>> 56);
        result[index++] = (byte) (l >>> 48);
        result[index++] = (byte) (l >>> 40);
        result[index++] = (byte) (l >>> 32);
        result[index++] = (byte) (l >>> 24);
        result[index++] = (byte) (l >>> 16);
        result[index++] = (byte) (l >>> 8);
        result[index]   = (byte)  l;
        return result;
    }


}
