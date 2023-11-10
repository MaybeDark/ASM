## ASM

## 前言

`mall`ASM是一个通用的Java字节码操作和分析框架。它可以用来修改现有的类或者直接以二进制形式动态生成类。

## 项目介绍
`mall`本项目重写了JDK中ASM包下所有功能,也新添了字节码相关功能
已实现：1.ClassWriter 一份字节码从0到1的构建，比ASM包下的ClassWriter交互性更高，使用性更高。
        2.ClassVisitor 继承于ClassWriter读入一份字节码并进行添加
        3.ClassModify 与JDK中源码相像相关操作都是基于字节，使用性较差、门槛较高
        4.MethodProxy 动态代理，通过ClassWriter生成模版使用ClassModify修改实现生成代理类
待实现：1. 可视化界面
        2. idea插件
        3. 面向对象语言解释器
