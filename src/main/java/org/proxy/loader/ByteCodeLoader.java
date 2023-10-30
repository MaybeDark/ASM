package org.proxy.loader;

public abstract class ByteCodeLoader extends ClassLoader {
    public abstract Class<?> loadByteCode(String name, byte[] bytes);
}
