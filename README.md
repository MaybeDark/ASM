<p align="center">
    <img width="200" src="src/main/resources/02182936_XNdd.png">
</p>
<h1 align="center">ASM</h1>
<h2>前言</h2>
<h4>什么是ASM?<a href="https://www.jianshu.com/p/6ec7846edf07"><sub>more</sub></a></h4>
<div>
    &emsp;&emsp;ASM是一个通用的Java字节码操作和分析框架，它可以用来修改现有的类或直接以二进制形式动态生成类。ASM提供了一些常见的字节码转换和分析算法，从中可以构建定制的复杂转换和代码分析工具。ASM提供了与其他Java字节码框架类似的功能，但侧重于性能。因为它的设计和实现都尽可能小和快，所以它非常适合在动态系统中使用（当然也可以以静态方式使用，例如在编译器中）。
</div>
<h4>ASM有什么用?</h4>
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
此外，ASM库是一个Java字节码操作和分析框架，它提供了对Java字节码的直接访问和修改功能。通过使用ASM库，开发人员可以创建、修改和分析Java类的字节码表示。由于Java字节码是平台无关的，因此使用ASM库可以在不同的操作系统和JVM上运行生成的字节码文件。
</div>
<div>
&emsp;&emsp;如果使用Java编译器生成Java源文件，然后由JVM编译成字节码并执行，则会受到Java编译器和JVM版本的限制，并且需要进行多次编译和转换。而使用ASM库直接生成字节码文件可以避免这些限制，并且可以更灵活地进行代码生成和分析。
</div>

## 项目介绍
<div>&emsp;&emsp;本项目重新实现了JDK中ASM库的功能，在已用的基础上新添字节码相关功能。沿用相关类名和方法名减低新API上手难度,方法调用多为链式调用提高使用性。</div>
<div>&emsp;&emsp;项目中沿用ClassWriter和ClassVisitor命名,但是操作对象从晦涩难懂的字节数组变为对应的实体类,在使用时只需要新建和编辑对应的实体类,对于方法指令的编辑只需要调用对应的方法和输入必要的参数就会为你生成对应的指令不需要你参照JVMS对操作数一个字节一个字节进行编辑只需告诉它做什么对什么做。</div>
<div>&emsp;&emsp;ClassModify设计理念与原来JDK下的ASM包相似,尽可能的小和快操作对象都是字节数组,在ClassModify中字节码文件内容都已字节数组保存但仍会对不同区域(constantPool、classinfo、methodinfos、fieldinfos、attributes)划分方便使用者修改,MethodProxy中使用ClassModify动态修改模板类生成代理类。相较于ClassWriter牺牲了使用性但是提高效率,适合精通字节码文件结构和规范的使用者快速的生成一份字节码文件。</div>
<div>&emsp;&emsp;ClassWriter能从0到1的生成一份字节码文件，而ClassVisitor继承于ClassWriter但是提供独特的visit方法可以对已有的字节码文件进行读入并使其进入可编辑的状态;ClassModify既可以从0到1生成一份字节码文件也可以读入已有字节码进行编辑,不过相较前面两者使用难度会更大。</div>
<br>
<div>
    <b>额外内容</b>
    <div>
        ~ MethodProxy
        <br>
        &emsp;&emsp;MethodProxy学习了JDK动态代理和CGLIB动态代理的优势,采用以ClassWriter生成模板类、ClassModify进行修改的方式生成代理类,设计的应用场景就是像spring ioc容器在启动过程中短时间内生成大量的代理类。使用方式与JDK动态代理和CGLIB动态代理无异,但是使用的是项目中的工具无需导入其他的包。支持抽象类代理。
    </div>
</div>

## 模块功能
### 已成历史：
 - 1.ClassWriter 一份字节码从0到1的构建，比ASM包下的ClassWriter交互性更高，使用性更高。
 - 2.ClassVisitor 继承于ClassWriter读入一份字节码并进行编辑
 - 3.ClassModify 与JDK中ASM库设计理念相同,相关操作都是基于字节数组,使用性较差、门槛较高
 - 4.MethodProxy 动态代理，通过ClassWriter生成模版使用ClassModify修改实现生成代理类
### 未来展望：
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
    &emsp;&emsp;我一开始想要做这个项目其实就是学AOP底层的时候了解到了ASM,但是自己用的时候觉得这API太难用了。CGLIB和JDK动态代理也是对ASM进行了定制化去针对他们的使用场景。所以我就想做一个项目去自己实现,这也是为什么项目会一个MethodProxy模块的原因。
</div>
<div>
     <b>~ 收获了什么?</b>
    <br>
    &emsp;&emsp;项目看起来好像很容易就是对一个字节数组进行增删改查,但其实不是,JDK中的ASM库他不会对你字节码文件格式进行细致校验当你尝试使用ClassLoader加载的时候是返回的异常往往是无法处理的,所以在项目我定义了一些异常去告诉使用者在哪一步有什么问题,这就使的我要对字节码文件规范要精通;在字节码文件中methodinfo会用Code(attribute)去保存方法的编译后的指令也会保存包括操作数栈、局部变量表的相关信息,如果你要使用一下特殊的指令比如goto等跳转指令还有生成一个stackMapTable(attribute)而且在JVMS中是必需的,所以我对JVM在运行指令过程以及指令集也是要达到一个精通的程度。
</div>
