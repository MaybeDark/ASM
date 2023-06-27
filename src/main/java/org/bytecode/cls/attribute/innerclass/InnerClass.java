package org.bytecode.cls.attribute.innerclass;

import org.Access;
import org.Loadable;
import org.Target;
import org.bytecode.constantpool.info.AbsConstantPoolInfo;
import org.bytecode.constantpool.ConstantPool;
import org.bytecode.constantpool.info.ConstantPoolClassInfo;
import org.tools.ConvertTool;

import java.util.Objects;

public class InnerClass implements Loadable<ConstantPool> {
    private short access;
    private Target target;
    private String innerClassName;
    private String externalName;
    private String externalClassName;
    private short  externalClassIndex = 0;

    private short innerClassNameIndex;
    private short innerClassIndex;
//    private short outerClassIndex;

    private boolean loaded = false;

    private InnerClass(short access,Target target,String innerClassName,short externalClassIndex){
        this.access  = access;
        this.innerClassName = innerClassName;
        this.externalClassIndex = externalClassIndex;
        this.target = target;
    }

    private InnerClass(short access,Target target,String innerClassName,String externalClassName){
        this.access = access;
        this.innerClassName = innerClassName;
        this.externalClassName = externalClassName;
        this.externalName = externalClassName + "$1" + innerClassName;
        this.target = target;
     }

    public static InnerClass innerClassOfClass(short access,String innerClassName,short externalClassIndex){
        return new InnerClass(access, Target.Class,innerClassName,externalClassIndex);
    }

    public static InnerClass innerClassOfMethod(short access,String innerClassName,String externalClassName){
        return new InnerClass(access,Target.Method,innerClassName,externalClassName);
    }

    public static InnerClass innerClassOfLambdaMethod(){
        return new InnerClass((short) (Access.ACC_PUBLIC+Access.ACC_STATIC+Access.ACC_FINAL),
                Target.LambdaMethod,"Lookup","java/lang/invoke/MethodHandles");
    }

    @Override
    public short load(ConstantPool cp) {
        innerClassNameIndex = cp.putUtf8Info(innerClassName);
        if (target.equals(Target.Method)){
            innerClassIndex = cp.putClassInfo(externalName);
            innerClassNameIndex = cp.putUtf8Info(innerClassName);
        }else if (target.equals(Target.LambdaMethod)){
            innerClassIndex = cp.putClassInfo(externalName);
            externalClassIndex = cp.putClassInfo(externalClassName);
            innerClassNameIndex = cp.putUtf8Info(innerClassName);
        }else {
            AbsConstantPoolInfo ccpi = cp.get(externalClassIndex);
            if (ccpi instanceof ConstantPoolClassInfo){
                externalClassName = ((ConstantPoolClassInfo) ccpi).getFullClassName();
                externalName = externalClassName + "$1" + innerClassName;
                innerClassIndex = cp.putClassInfo(externalName);
                innerClassNameIndex = cp.putUtf8Info(innerClassName);
            }else{
                throw new RuntimeException("error externalClassIndex");
            }
        }
        loaded = true;
        return innerClassIndex;
    }

    public byte[] toByteArray(){
        if (!loaded){
            throw new RuntimeException(innerClassName + " need load before user");
        }
        byte[] result = new byte[8];
        byte[] p1 = ConvertTool.S2B(innerClassIndex);
        byte[] p2 = ConvertTool.S2B(externalClassIndex);
        byte[] p3 = ConvertTool.S2B(innerClassNameIndex);
        byte[] p4 = ConvertTool.S2B(access);
        System.arraycopy(p1,0,result,0,2);
        System.arraycopy(p2,0,result,2,2);
        System.arraycopy(p3,0,result,4,2);
        System.arraycopy(p4,0,result,6,2);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerClass that = (InnerClass) o;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerClassName,externalClassName);
    }

}
