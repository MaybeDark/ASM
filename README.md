<p align="center">
    <img width="200" src="https://static.oschina.net/uploads/img/202103/02182936_XNdd.png">
</p>
<h1 align="center">ASM</h1>

## 前言
### <a href="https://www.jianshu.com/p/6ec7846edf07">什么是ASM</a>
`mall`ASM是一个通用的Java字节码操作和分析框架。它可以用来修改现有的类或者直接以二进制形式动态生成类。

### 为什么要操纵分析字节码
#### 程序分析，发现 bug，检测无用代码
 - JaCoCo(Java Code Coverage Library 用于检查单元测试覆盖率)
#### 产生代码
 - openJDK lambda、Groovy 编译器、Kotlin 编译器
#### 优化、混淆代码，注入调试及监控代码等
 - Aspectj

## 项目介绍
`mall`本项目重写了JDK中ASM包下所有功能,也新添了字节码相关功能

## 功能介绍
### 已实现：
 - 1.ClassWriter 一份字节码从0到1的构建，比ASM包下的ClassWriter交互性更高，使用性更高。
 - 2.ClassVisitor 继承于ClassWriter读入一份字节码并进行添加
 - 3.ClassModify 与JDK中源码相像相关操作都是基于字节，使用性较差、门槛较高
 - 4.MethodProxy 动态代理，通过ClassWriter生成模版使用ClassModify修改实现生成代理类
### 待实现：
 - 1.可视化界面
 - 2.idea插件
 - 3.面向对象语言解释器
 - 
### 项目结构
``` lua
mall
├── mall-bytecode
 ├── mall-attributes JVMS定义了30个attribute用于让JVM正确的解读字节码文件
  ├── mall-必要的;对于 Java 虚拟机正确解释文件至关重要
   ├── mall-ConstantValue
   ├── mall-Code
   ├── mall-StackMapTable
   ├── mall-BootstrapMethods
   ├── mall-NestHost
   ├── mall-NestMembers
   ├── mall-PermittedSubclasses
  ├── mall-可选的;使Java 虚拟机正确解读Java SE的库或者有用的工具
   ├── mall-Exceptions
   ├── mall-InnerClasses
   ├── mall-EnclosingMethod
   ├── mall-Synthetic
   ├── mall-Signature
   ├── mall-Record
   ├── mall-SourceFile
   ├── mall-LineNumberTable
   ├── mall-LocalVariableTable
   ├── mall-LocalVariableTypeTable
  ├── mall-含元数据的;对于Java 虚拟机对文件的正确解释并不重要，但包含有关文件的元数据，这些元数据由 Java SE 平台或由工具提供
   ├── mall-SourceDebugExtension
   ├── mall-Deprecated
   ├── mall-RuntimeVisibleAnnotations
   ├── mall-RuntimeInvisibleAnnotations
   ├── mall-RuntimeVisibleParameterAnnotations
   ├── mall-RuntimeInvisibleParameterAnnotations
   ├── mall-RuntimeVisibleTypeAnnotations
   ├── mall-RuntimeInvisibleTypeAnnotations
   ├── mall-AnnotationDefault
   ├── mall-MethodParameters
   ├── mall-Module
   ├── mall-ModulePackages
   ├── mall-ModuleMainClass
├── mall-constantpool 字节码常量池，在准备
