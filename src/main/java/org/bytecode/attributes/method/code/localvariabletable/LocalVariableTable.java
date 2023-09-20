package org.bytecode.attributes.method.code.localvariabletable;

import org.bytecode.attributes.common.Attribute;
import org.bytecode.constantpool.ConstantPool;
import org.exception.DefinedException;
import org.bytecode.attributes.method.code.bracket.Bracket;
import org.tools.ByteVector;
import org.wrapper.LocalVariableWrapper;

import java.util.HashMap;
import java.util.Map;

public class LocalVariableTable extends Attribute {
    //  default localVariable table size
    public static final int DEFAULTLOCALVARIABLETABLESIZE = 32;
    private short count = 0;
    private short max = 0;
    private LocalVariableWrapper[] scopeEndLocals;
    private LocalVariableWrapper[] activeLocals;
    private Map<String,Short> name2locals = new HashMap<>();
    public LocalVariableTable(){
        this(DEFAULTLOCALVARIABLETABLESIZE);
    }

    public LocalVariableTable(int size){
        attributeLength = 2;
        scopeEndLocals = new LocalVariableWrapper[size];
        activeLocals = new LocalVariableWrapper[size / 2];
    }

    public void expand(){
        LocalVariableWrapper[] newActiveLocals = new LocalVariableWrapper[activeLocals.length * 2];
        LocalVariableWrapper[] newScopeEndLocals = new LocalVariableWrapper[scopeEndLocals.length * 2];
        System.arraycopy(activeLocals,0,newActiveLocals,0,activeLocals.length);
        System.arraycopy(scopeEndLocals,0,newScopeEndLocals,0,scopeEndLocals.length);
        scopeEndLocals = newScopeEndLocals;
        activeLocals = newActiveLocals;
    }

    public short put(LocalVariableWrapper lv) {
        if (lv == null){
            throw new RuntimeException("LocalVariable must be not null");
        }
        checkDefined(lv.getName());
        short index = put0(lv);
        attributeLength += 10;
        lv.setTableIndex(index);
        name2locals.put(lv.getName(),index);
        return index;
    }

    private short put0(LocalVariableWrapper lv){
        int size = lv.getSize();
        for (short i = 0; i < max; i++) {
            LocalVariableWrapper slot = activeLocals[i];
            if (slot != null) continue;
            if (size == 1){
                activeLocals[i] = lv;
                return i;
            }else if (size == 2 && activeLocals[i+1] == null){
                if (i+1 == max) max++;
                activeLocals[i] = lv;
                activeLocals[i+1] = lv;
                return i;
            }
        }
        short index = max;
        activeLocals[max] = lv;
        if (size == 2) activeLocals[max+1] = lv;
        max += size;
        return index;
    }

    public void endLocalVariableScope(Bracket bracket) {
        for (int i = 0; i < max; i++) {
            LocalVariableWrapper activeLocal = activeLocals[i];
            if (activeLocal == null) continue;
            if (activeLocal.getStartPc() < bracket.getStartPc()) continue;
            activeLocal.setLength((bracket.getStartPc() + bracket.getLength()) - activeLocal.getStartPc());
            if (activeLocal.getLength() != 0){ scopeEndLocals[count++] = activeLocal; }
            activeLocals[i] = null;
            if (activeLocal.getSize() == 2) activeLocals[i+1] = null;
            activeLocal.scopeEnd = true;
            name2locals.remove(activeLocal.getName());
        }
    }


    private void checkDefined(String name) {
        LocalVariableWrapper localVariableByName = getLocalVariableByName(name);
        if (localVariableByName != null)
            throw new DefinedException("variable '" + name +"' defined in scope");
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public LocalVariableWrapper getLocalVariableByIndex(short index){
        return activeLocals[index];
    }


    public LocalVariableWrapper getLocalVariableByName(String localVariableName){
        Short index = name2locals.get(localVariableName);
        if (index == null){
            return null;
        }
        return activeLocals[index];
    }

    public short searchByName(String localVariableName){
        Short result =  name2locals.get(localVariableName);
        return result == null? -1 : result;
    }

    private void sortByEnd() {
        LocalVariableWrapper temp;
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (scopeEndLocals[j].getEnd() > scopeEndLocals[j + 1].getEnd()) {
                    temp = scopeEndLocals[j];
                    scopeEndLocals[j] = scopeEndLocals[j + 1];
                    scopeEndLocals[j + 1] = temp;
                }
            }
        }
    }

    public short getLocalsCount() {
        return count;
    }

    public short getLocalsMax() {
        return max;
    }

    public String list() {
        sortByEnd();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            LocalVariableWrapper localVariable = scopeEndLocals[i];
            sb.append(String.format("%- 3d  %- 3d  %- 3d %- 3d %- 3d\n",
                    localVariable.getStartPc(), localVariable.getLength(), localVariable.getTableIndex(), localVariable.getNameCpIndex(), localVariable.getDescCpIndex()));
        }
        return sb.toString();
    }

    @Override
    public byte[] toByteArray() {
        if (!loaded){
            throw new RuntimeException("LocalVariableTable attribute need load before use");
        }
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

    public short getMax(){
        return max;
    }
    @Override
    public short load(ConstantPool cp) {
        sortByEnd();
        for (int i = 0; i < count; i++) {
            LocalVariableWrapper scopeEndLocal = scopeEndLocals[i];
            scopeEndLocal.load(cp);
        }
        loaded = true;
        cpIndex = cp.putUtf8Info("LocalVariableTable");
        return cpIndex;
    }
}
