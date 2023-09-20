package org.bytecode;

import org.bytecode.attributes.common.Attribute;

public interface ByteCodeWriter {
    byte[] toByteArray();
    ByteCodeWriter addAttribute(Attribute attribute);
    Attribute getAttribute(String attributeName);
}
