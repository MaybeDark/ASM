package org.constantpool.info;

import org.constantpool.ConstantPoolTag;
import org.tools.ConvertTool;

public abstract class SymbolicReferenceConstantPoolInfo extends AbsConstantPoolInfo{

    public SymbolicReferenceConstantPoolInfo(ConstantPoolTag tag) {
        this.tag = tag;
        if (!tag.isSymbolicReferenceConstantPoolInfo()){
            throw new RuntimeException("This tag does not belong to SymbolicReferenceConstantPoolInfo");
        }
    }

    public String valueToString() {
        if (value == null || value.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        switch (value.length){
            case 4:
                sb.append("#")
                        .append(ConvertTool.B2S(new byte[]{value[0], value[1]}))
                        .append(' ')
                        .append('#')
                        .append(ConvertTool.B2S(new byte[]{value[2],value[3]}));
                break;
            case 2:
                sb.append("#").append(ConvertTool.B2S(new byte[]{value[0], value[1]}));
                break;
        }
        return sb.toString();
    }
}
