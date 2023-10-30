package org.proxy.loader;

public class EasyByteCodeLoader extends ByteCodeLoader {
    @Override
    public Class<?> loadByteCode(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}
