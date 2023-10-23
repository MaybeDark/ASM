package org.bytecode.attributes.annotations;

import com.sun.istack.internal.NotNull;
import org.bytecode.constantpool.ConstantPool;
import org.tools.ByteVector;

import java.util.ArrayList;

public class Annotations<T extends AnnotationInfo> {
    private int annotationCount = 0;
    private ArrayList<T> annotationInfos = new ArrayList<>();

    public Annotations<T> addAnnotationInfo(@NotNull T annotationInfo) {
        annotationInfos.add(annotationInfo);
        annotationCount++;
        return this;
    }

    public int getAnnotationCount() {
        return annotationCount;
    }

    public ArrayList<T> getAnnotationInfos() {
        return annotationInfos;
    }

    void load(ConstantPool constantPool) {
        annotationInfos.forEach(annotationInfo -> {
            annotationInfo.load(constantPool);
        });
    }

    byte[] toByteArray() {
        ByteVector byteVector = new ByteVector(getLength());
        byteVector.putShort(annotationCount);
        annotationInfos.forEach((annotationInfo) -> byteVector.putArray(annotationInfo.toByteArray()));
        return byteVector.end();
    }

    public boolean isEmpty() {
        return annotationCount == 0;
    }

    public int getLength() {
        return 2 + annotationInfos.stream()
                .mapToInt(AnnotationInfo::getLength)
                .sum();
    }
}
