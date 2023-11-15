<h1 align="center">JerryBytecode</h1>
<h2>前言</h2>
<h4>什么是Java字节码框架?<a href="https://www.jianshu.com/p/6ec7846edf07"><sub>more</sub></a></h4>
<div>
    &emsp;&emsp;它可以用来修改现有的类或直接以二进制形式动态生成类。框架提供了一些常见的字节码转换和分析算法，从中可以构建定制的复杂转换和代码分析工具。
</div>
<h4>Java字节码框架有什么用?</h4>
<ol>
    <li>OpenJDK，生成lambda调用站点，以及Nashorn编译器</li>
    <li>Groovy编译器和Kotlin编译器</li>
    <li>Cobertura和Jacoco，以工具化类来度量代码覆盖率</li>
    <li>CGLIB，用于动态生成代理类</li>
    <li>Gradle，在运行时生成一些类</li>
</ol>
<h4>为什么生成字节码文件而不是生成java源文件</h4>
<div>
&emsp;&emsp;主要是因为Java程序在运行时会被编译成字节码，然后由Java虚拟机（JVM）解释执行。因此，直接生成字节码文件可以跳过Java编译过程，提高代码的执行效率。
此外，Java字节码框架提供了对Java字节码的直接访问和修改功能。通过使用Java字节码框架，开发人员可以创建、修改和分析Java类的字节码表示。由于Java字节码是平台无关的，因此使用Java字节码框架可以在不同的操作系统和JVM上运行生成的字节码文件。
</div>
<div>
&emsp;&emsp;如果使用Java编译器生成Java源文件，然后由JVM编译成字节码并执行，则会受到Java编译器和JVM版本的限制，并且需要进行多次编译和转换。而使用Java字节码框架直接生成字节码文件可以避免这些限制，并且可以更灵活地进行代码生成和分析。
</div>

## 项目介绍
<div>
    &emsp;&emsp;本项目是一个通用的字节码操作和分析框架,不但重新实现了JDK中ASM库的功能，在已用的基础上新添字节码相关功能。沿用ASM库中相关类名和方法名减低上手难度,方法的调用也多为链式调用提高使用性。
</div>
<div>
    &emsp;&emsp;项目中沿用ASM的ClassWriter和ClassVisitor类名,但是操作对象从晦涩难懂的字节数组变为对应的实体类,在使用时只需要新建和编辑对应的实体类,对于方法指令的编辑只需要调用对应的方法和输入必要的参数就会为你生成对应的指令;不需要你参照JVMS对操作数一个字节一个字节进行编辑只需告诉它做什么对什么做。
</div>
<div>
    &emsp;&emsp;ClassModify设计理念与原来JDK下的ASM包相似,尽可能的小和快操作对象都是字节数组,在ClassModify中字节码文件内容都已字节数组保存但会对不同区域(constantPool、classinfo、methodinfos、fieldinfos、attributes)划分方便使用者修改,MethodProxy中就使用了ClassModify动态修改模板类生成代理类。相较于ClassWriter牺牲了使用性但是提高效率,适合精通字节码文件结构和规范的使用者快速的生成一份字节码文件。
</div>
<div>&emsp;&emsp;ClassWriter能从0到1的生成一份字节码文件，而ClassVisitor继承于ClassWriter但是提供独特的visit方法可以对已有的字节码文件进行读入并使其进入可编辑的状态;ClassModify既可以从0到1生成一份字节码文件也可以读入已有字节码进行编辑,不过相较前面两者使用难度会更大。
</div>
<br>
<div>
    <b>额外内容</b>
    <div>
        ~ MethodProxy
        <br>
        &emsp;&emsp;MethodProxy结合了JDK动态代理和CGLIB动态代理的优势,采用以ClassWriter生成模板类、ClassModify进行填充的方式生成代理类,设计的应用场景就是像spring ioc容器在启动过程中短时间内生成大量的代理类。使用方式与JDK动态代理和CGLIB动态代理无异,但是使用的是项目中的工具无需导入其他的包。支持抽象类代理。
    </div>
</div>

## 模块功能
#### 已成历史：
 - 1.ClassWriter 一份字节码从0到1的构建，比ASM包下的ClassWriter交互性更高，使用性更高。
 - 2.ClassVisitor 继承于ClassWriter读入一份字节码并进行编辑
 - 3.ClassModify 与JDK中ASM库设计理念相同,相关操作都是基于字节数组,使用性较差、门槛较高
 - 4.MethodProxy 动态代理，通过ClassWriter生成模版使用ClassModify填充实现生成代理类
#### 未来展望：
 - 1.可视化界面 像ByteCodeView,但是会提供修改功能。
 - 2.idea插件 像jclasslib,功能参考可视化界面但是会内嵌到idea中。
 - 3.编译器 让不同的语言编译成字节码文件运行在JVM中。
## 包结构
``` lua
├── bytecode 包含字节码文件内容与ClassWriter
  └── attributes JVMS定义了30个attribute用于让JVM正确的解读字节码文件,包内是对应的实体类
  └── constantpool 字节码常量池,在准备阶段会加载的运行时常量池,有17种info对应不同的功能
├── exception 自定义异常
├── modify 模块功能介绍中提到的ClassModify相关类
├── proxy 模块功能介绍中提到的MethodProxy相关类
├── tools 包内有ArrayTool、ByteVector、ByteVectors、ConvertTool
├── visitor 模块功能介绍中提到的ClassVisitor相关类
└── wrapper 方法需要的参数包装成类,包装类提供快捷的生成方式
```
## 项目文档
// TODO

## 作者寄语
<div>
    <b>~ 仍需改进的地方</b> 
    <br>
    &emsp;&emsp;在写ClassWriter的时候重构了几次但是整体的结构还是不满意,像Code里的添加指令一开始内嵌在Code里后来用工厂模式但是跟Code耦合严重又被迫用回内嵌;
    我想项目不仅使用起来简单明了而且源码读起来也是简单明了的。后续也会根据DDD在再进行一次重构;因为是一个人在做,项目内也存在很多Bug没被发现,MethodProxy也没做完,只实现了接口类代理,后续也会继续完成。
</div>
<div>
    <b>~ 为什么做这项目?</b>
    <br>
    &emsp;&emsp;SpringAOP的底层是JDK动态代理和CGLIB,但是它们的底层又是什么？我是一个喜欢刨根问底的人,后来知道是ASM库;然后就尝试使用了一下这个JDK自带的库发现这个库的使用性不是一般的差,也是那时候开始想自己做一个通用的Java字节码框架。
</div>
<div>
     <b>~ 收获了什么?</b>
    <br>
    &emsp;&emsp;学习到了一份字节码文件的组成到底是怎样,理解了类加载每一步到底要处理字节码文件的那一部分,理解了什么是符号引用转直接引用,理解了什么是操作数栈什么是局部变量表,理解了Klass里面会保存什么,理解了Java指令集里面的每个指令有什么作用,理解为什么Java是跨平台语言,理解了什么是泛型擦除,理解了什么是运行时常量池。
</div>
