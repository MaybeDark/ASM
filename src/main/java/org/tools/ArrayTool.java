package org.tools;

import com.sun.istack.internal.NotNull;

public class ArrayTool {
    public static byte[] join(@NotNull byte b,@NotNull byte[] a2){
        byte[] a1 = {b};
        return join(a1, a2);
    }
    public static byte[] join(@NotNull byte[] a1,@NotNull byte b){
        byte[] a2 = {b};
        return join(a1, a2);
    }

    public static byte[] join(@NotNull byte[] a1,@NotNull byte[] a2){
        if (a1 == null || a1.length == 0){
            return a2;
        }else if(a2 == null || a2.length == 0){
            return a1;
        }else {
            byte[] newArray = new byte[a1.length+a2.length];
            System.arraycopy(a1,0,newArray,0,a1.length);
            System.arraycopy(a2,0,newArray,a1.length,a2.length);
            return newArray;
        }
    }

    public static<T> T[] join(@NotNull T b,@NotNull T[] a2){
        Object[] a1 = new Object[1];
        a1[0] = b;
        return (T[]) join(a1, a2);
    }
    public static<T> T[] join(@NotNull T[] a1,@NotNull T[] a2){
        if (!notNull(a1)){
            return a2;
        }else if (!notNull(a2)){
            return a1;
        }else{
            Object[] newArray = new Object[a1.length+a2.length];
            System.arraycopy(a1,0,newArray,0,a1.length);
            System.arraycopy(a2,0,newArray,a1.length,a2.length);
            return (T[]) newArray;
        }
    }

    public static boolean notNull(Object[] a){
        return a != null && a.length != 0;
    }

    public static boolean allNotNull(Object[] a){
        if (a == null || a.length == 0){
            return false;
        }
        for (Object o : a) {
            if (o == null)
                return false;
        }
        return true;
    }

}
