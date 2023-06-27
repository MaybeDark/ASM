package org.bytecode.method.attribute.code.attrubute.stackmaptable.verificationtypeinfo;

import org.tools.ArrayTool;
import org.tools.ConvertTool;

/**
 * 引用对象类型
 * typeCpIndex对应常量池中的classInfo
 *
 * @author 12923
 * @date 2023/06/15
 */
public class ObjectVariableInfo implements VariableInfo {
    public static final byte tag = 7;
    private short cpIndex;

    public ObjectVariableInfo(short typeCpIndex) {
        this.cpIndex = typeCpIndex;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayTool.join(tag, ConvertTool.S2B(cpIndex));
    }

    public short getCpIndex() {
        return cpIndex;
    }


}
