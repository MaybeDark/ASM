package org.bytecode.attributes;

import org.bytecode.attributes.code.bracket.Bracket;
import org.bytecode.constantpool.ConstantPool;
import org.exception.DefinedException;
import org.tools.ByteVector;
import org.wrapper.LocalVariableWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LocalVariableTable extends VariableLengthAttribute {
    private final LocalVariableWrapper[] scopeEndLocals;
    private final LocalVariableWrapper[] activeLocals;
    private final Map<String, Short> name2locals = new HashMap<>();
    private short count = 0;
    private short max = 0;

    public LocalVariableTable() {
        super(Target.code_info);
        attributeLength = 2;
        attributeName = "LocalVariableTable";
        scopeEndLocals = new LocalVariableWrapper[16];
        activeLocals = new LocalVariableWrapper[8];
    }

    public short put(LocalVariableWrapper lv) {
        if (lv == null) {
            throw new RuntimeException("LocalVariable must be not null");
        }
        checkDefined(lv.getName());
        short index = put0(lv);
        attributeLength += 10;
        lv.setTableIndex(index);
        name2locals.put(lv.getName(), index);
        cpIndex = 0;
        return index;
    }

    private short put0(LocalVariableWrapper lv) {
        int size = lv.getSize();
        for (short i = 0; i < max; i++) {
            LocalVariableWrapper slot = activeLocals[i];
            if (slot != null) continue;
            if (size == 1) {
                activeLocals[i] = lv;
                return i;
            } else if (size == 2 && activeLocals[i + 1] == null) {
                if (i + 1 == max) max++;
                activeLocals[i] = lv;
                activeLocals[i + 1] = lv;
                return i;
            }
        }
        short index = max;
        activeLocals[max] = lv;
        if (size == 2) activeLocals[max + 1] = lv;
        max += size;
        return index;
    }

    public void endLocalVariableScope(Bracket bracket) {
        for (int i = 0; i < max; i++) {
            LocalVariableWrapper activeLocal = activeLocals[i];
            if (activeLocal == null) continue;
            if (activeLocal.getStartPc() < bracket.getStartPc()) continue;
            activeLocal.setLength((bracket.getStartPc() + bracket.getLength()) - activeLocal.getStartPc());
            if (activeLocal.getLength() != 0) {
                scopeEndLocals[count++] = activeLocal;
            }
            activeLocals[i] = null;
            if (activeLocal.getSize() == 2) activeLocals[i + 1] = null;
            activeLocal.scopeEnd = true;
            name2locals.remove(activeLocal.getName());
        }
    }


    private void checkDefined(String name) {
        LocalVariableWrapper localVariableByName = getLocalVariableByName(name);
        if (localVariableByName != null)
            throw new DefinedException("variable '" + name + "' defined in scope");
    }

    public LocalVariableWrapper getLocalVariableByName(String localVariableName) {
        Short index = name2locals.get(localVariableName);
        if (index == null) {
            return null;
        }
        return activeLocals[index];
    }

    public short searchByName(String localVariableName) {
        Short result = name2locals.get(localVariableName);
        return result == null ? - 1 : result;
    }

    private void sortByEnd() {
        Arrays.sort(scopeEndLocals, 0, count, (o1, o2) -> {
            if (o1.getEnd() == o2.getEnd()) return 0;
            return o1.getEnd() > o2.getEnd() ? 1 : - 1;
        });
    }

    public String print() {
        sortByEnd();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            LocalVariableWrapper localVariable = scopeEndLocals[i];
            sb.append(String.format("%- 3d  %- 3d  %- 3d %- 3d %- 3d\n",
                    localVariable.getStartPc(), localVariable.getLength(), localVariable.getTableIndex(), localVariable.getNameCpIndex(), localVariable.getDescCpIndex()));
        }
        return sb.toString();
    }

    /**
     * 如果要修改code无需读入localVariableTable
     */
    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        attributeLength = byteVector.getInt();
        short length = byteVector.getShort();
        for (int i = 0; i < length; i++) {
            LocalVariableWrapper lv = LocalVariableWrapper.getEmptyWrapper();
            lv.setStartPc(byteVector.getShort());
            lv.setLength(byteVector.getShort());
            lv.setNameCpIndex(byteVector.getShort());
            lv.setDescCpIndex(byteVector.getShort());
            lv.setTableIndex(byteVector.getShort());
            lv.scopeEnd = true;
            scopeEndLocals[count++] = lv;
        }
        return this;
    }

    @Override
    public short load(ConstantPool cp) {
        cpIndex = cp.putUtf8Info("LocalVariableTable");
        sortByEnd();
        for (int i = 0; i < count; i++) {
            LocalVariableWrapper scopeEndLocal = scopeEndLocals[i];
            scopeEndLocal.load(cp);
        }
        return cpIndex;
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(6 + attributeLength);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(count);
        for (int i = 0; i < count; i++) {
            LocalVariableWrapper scopeEndLocal = scopeEndLocals[i];
            result.putShort(scopeEndLocal.getStartPc())
                    .putShort(scopeEndLocal.getLength())
                    .putShort(scopeEndLocal.getNameCpIndex())
                    .putShort(scopeEndLocal.getDescCpIndex())
                    .putShort(scopeEndLocal.getTableIndex());
        }
        return result.end();
    }

    public short getMax() {
        return max;
    }

    public short getLocalsCount() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public LocalVariableWrapper getLocalVariableByIndex(short index) {
        return activeLocals[index];
    }
}
