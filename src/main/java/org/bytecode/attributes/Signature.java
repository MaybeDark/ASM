package org.bytecode.attributes;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.Type;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolUtf8Info;
import org.tools.ArrayTool;
import org.tools.ByteVector;
import org.wrapper.GenericWrapper;

/**
 * 任何类、接口、初始化成员或者方法的泛型签名如果包含了类型变量或参数化类型，
 * 则Signature会为它记录泛型签名信息
 */
//TODO‘参数化类泛型’参数和属性定义(Class<T>);多范围泛型定义(Class<T extends A&B)
public class Signature extends VariableLengthAttribute {
    private String signature;
    private short signatureIndex = 0;

    public Signature() {
        super((byte) (Target.class_info | Target.method_info | Target.field_info));
        this.attributeName = "Signature";
    }

    public static Signature genericsOfClass(@NotNull GenericWrapper[] generics, @Nullable Type superClass, @Nullable Type... implementsClasses) {
        Signature newSignature = new Signature();
        newSignature.setSignature(getSignatureOfClass(generics, superClass, implementsClasses));
        return newSignature;
    }

    public static String getSignatureOfClass(@NotNull GenericWrapper[] generics, @Nullable Type superClass, @Nullable Type... implementsClasses) {
        if (ArrayTool.notNull(generics)) {
            throw new RuntimeException("generics must be not null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        for (GenericWrapper generic : generics) {
            if (generic != null) {
                sb.append(generic.getGenericName());
                sb.append(':');
                sb.append(generic.getExtendsBy().getDescriptor());
                generic.setDefined(true);
            }
        }
        sb.append('>');
        if (superClass != null) {
            sb.append(superClass.getDescriptor());
        } else {
            sb.append(Type.getClassDescriptor(Object.class));
        }
        if (implementsClasses != null && implementsClasses.length != 0) {
            for (Type clazz : implementsClasses) {
                if (clazz != null && ! clazz.isObjectType())
                    sb.append(clazz.getDescriptor());
            }
        }
        return sb.toString();
    }

    public static String getSignatureOfField(@NotNull GenericWrapper generic) {
        return "T" + generic.getGenericName() + ";";
    }

    @Override
    public short load(ConstantPool cp) {
        if (isEmpty()) {
            throw new RuntimeException("setSignature is required before loading");
        }
        loadAttributeName(cp);
        signatureIndex = cp.putUtf8Info(signature);
        return cpIndex;
    }

    @Override
    public Attribute visit(ConstantPool constantPool, ByteVector byteVector) {
        byteVector.skip(4);
        ConstantPoolUtf8Info signature = (ConstantPoolUtf8Info) constantPool.get(byteVector.getShort());
        setSignature(signature.getLiteral());
        return this;
    }

    @Override
    public boolean isEmpty() {
        return signature == null || signature.isEmpty();
    }

    @Override
    public byte[] toByteArray() {
        checkLoaded();
        ByteVector result = new ByteVector(8);
        result.putShort(cpIndex)
                .putInt(attributeLength)
                .putShort(signatureIndex);
        return result.end();
    }

    public String getSignature() {
        return signature;
    }

    public Signature setSignature(String signature) {
        cpIndex = 0;
        this.signature = signature;
        return this;
    }

    public short getSignatureIndex() {
        return signatureIndex;
    }

}
