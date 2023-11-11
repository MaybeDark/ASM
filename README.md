<p align="center">
    <img width="200" src="src/main/resources/02182936_XNdd.png">
</p>
<h1 align="center">ASM</h1>
<h2>前言</h2>
<h3>什么是ASM?<a href="https://www.jianshu.com/p/6ec7846edf07"><sub>more</sub></a></h3>
<div>
    &emsp;&emsp;ASM是一个通用的Java字节码操作和分析框架，它可以用来修改现有的类或直接以二进制形式动态生成类。ASM提供了一些常见的字节码转换和分析算法，从中可以构建定制的复杂转换和代码分析工具。ASM提供了与其他Java字节码框架类似的功能，但侧重于性能。因为它的设计和实现都尽可能小和快，所以它非常适合在动态系统中使用（当然也可以以静态方式使用，例如在编译器中）。
</div>
<h3>ASM有什么用?</h3>
<ol>
    <li>OpenJDK，生成lambda调用站点，以及Nashorn编译器</li>
    <li>Groovy编译器和Kotlin编译器</li>
    <li>Cobertura和Jacoco，以工具化类来度量代码覆盖率</li>
    <li>CGLIB，用于动态生成代理类</li>
    <li>Gradle，在运行时生成一些类</li>
</ol>

## 项目介绍
本项目重新实现了ASM，为ClassWriter和ClassVisitor提高了使用性和交互性;ClassModify设计理念与原来JDK下的ASM包相同,尽可能的小和快Class文件的内容都已字节类型保存但仍会做不用区域的划分方便使用者修改,MethodProxy就使用了ClassModify对模板类进行动态修改生成代理类。

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
