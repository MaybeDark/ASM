package org.proxy;

import org.proxy.loader.ByteCodeLoader;
import org.proxy.loader.EasyByteCodeLoader;

public class ClassLoaderFactory {
        private static ByteCodeLoader defaultLoader;

        public static ByteCodeLoader getDefaultByteCodeLoader() {
                if (defaultLoader == null) {
                        defaultLoader = new EasyByteCodeLoader();
                }
                return defaultLoader;
        }

}
