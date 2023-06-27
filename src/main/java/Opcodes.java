@SuppressWarnings({"PointlessBitwiseExpression", "unused"})
public interface Opcodes {

    // ASM API 版本
    int ASM4 = 4 << 16 | 0 << 8 | 0;
    int ASM5 = 5 << 16 | 0 << 8 | 0;

    // JDK版本
    int V1_1 = 3 << 16 | 45;
    int V1_2 = 0 << 16 | 46;
    int V1_3 = 0 << 16 | 47;
    int V1_4 = 0 << 16 | 48;
    int V1_5 = 0 << 16 | 49;
    int V1_6 = 0 << 16 | 50;
    int V1_7 = 0 << 16 | 51;
    int V1_8 = 0 << 16 | 52;

    // 修饰符
    int ACC_PUBLIC = 0x0001;            //public    class, com.field, com.method 访问权限修饰符
    int ACC_PRIVATE = 0x0002;           //private   class, com.field, com.method 访问权限修饰符
    int ACC_PROTECTED = 0x0004;         //protected class, com.field, com.method 访问权限修饰符
    int ACC_STATIC = 0x0008;            //static    com.field, com.method 静态
    int ACC_FINAL = 0x0010;             //final     class, com.field, com.method, parameter 不可变
    int ACC_SUPER = 0x0020;             //super     class  继承
    int ACC_SYNCHRONIZED = 0x0020;      //synchronized     com.method 锁
    int ACC_VOLATILE = 0x0040;          //volatile  com.field   内存看见性
    int ACC_BRIDGE = 0x0040;            //bridge    com.method
    int ACC_VARARGS = 0x0080;           //varargs   com.method
    int ACC_TRANSIENT = 0x0080;         //transient com.field   不参与序列化
    int ACC_NATIVE = 0x0100;            //native    com.method  本地方法
    int ACC_INTERFACE = 0x0200;         //interface class   接口
    int ACC_ABSTRACT = 0x0400;          //abstract  class, com.method 抽象
    int ACC_STRICT = 0x0800;            //strict com.method
    int ACC_SYNTHETIC = 0x1000;         //synthetic class, com.field, com.method, parameter 编译器生成的
    int ACC_ANNOTATION = 0x2000;        //annotation class  注解
    int ACC_ENUM = 0x4000;              //enum class(?) com.field inner 枚举
    int ACC_MANDATED = 0x8000;          //mandated parameter
    int ACC_DEPRECATED = 0x20000;       //deprecated class, com.field, com.method //废弃的

    //数组类型
    int T_BOOLEAN = 4;
    int T_CHAR = 5;
    int T_FLOAT = 6;
    int T_DOUBLE = 7;
    int T_BYTE = 8;
    int T_SHORT = 9;
    int T_INT = 10;
    int T_LONG = 11;

    //指令集
    int H_GETFIELD = 1;         //getfield  获取实例变量
    int H_GETSTATIC = 2;        //getstatic 获取类变量
    int H_PUTFIELD = 3;         //putfield  赋值实例变量
    int H_PUTSTATIC = 4;        //putstatic 赋值类变量
    int H_INVOKEVIRTUAL = 5;    //invokevirtual 调用虚方法
    int H_INVOKESTATIC = 6;     //invokestatic  调用静态方法
    int H_INVOKESPECIAL = 7;    //invokespecial 调用一些需要特殊处理的实例方法，包括实例初始化方法、私有方法和父类方法。
    int H_NEWINVOKESPECIAL = 8; //invokedynamic 调用Lambda表达式和默认方法
    int H_INVOKEINTERFACE = 9;  //invokeinterface 调用接口方法

//    stackmaptable指令集 对操作数栈和局部变量表中数据的类型进行缓存
//    例：f_new (Locals[1]: java/lang/String) (Stack[3]: String, int, int) 新建一个stackmaptable
//       Locals 局部变量表 Stack 操作数栈
//       在代码中局部变量表和操作数栈是变化的
//       所以有了FULL、APPEND、CHOP、SAME、SAME1以及偏移量来表示stackmaptable随着代码的执行有哪些变动或者不变

    int F_NEW = -1;
    int F_FULL = 0;
    int F_APPEND = 1;
    int F_CHOP = 2;
    int F_SAME = 3;
    int F_SAME1 = 4;


    //操作数栈指令集   32bit(4byte)一个栈单位深度、槽单位长度
    int NOP = 0;            //Do nothing
    int ACONST_NULL = 1;    //将空对象放入操作数栈
    int ICONST_M1 = 2;      //将常量-1放入操作数栈(int 4byte)
    int ICONST_0 = 3;       //将常量0放入操作数栈(int 4byte)
    int ICONST_1 = 4;       //将常量1放入操作数栈(int 4byte)
    int ICONST_2 = 5;       //将常量2放入操作数栈(int 4byte)
    int ICONST_3 = 6;       //将常量3放入操作数栈(int 4byte)
    int ICONST_4 = 7;       //将常量4放入操作数栈(int 4byte)
    int ICONST_5 = 8;       //将常量5放入操作数栈(int 4byte)
    int LCONST_0 = 9;       //将常量0放入操作数栈(long 8byte)
    int LCONST_1 = 10;      //将常量1放入操作数栈(long 8byte)
    int FCONST_0 = 11;      //将常量0放入操作数栈(float 4byte)
    int FCONST_1 = 12;      //将常量1放入操作数栈(float 4byte)
    int FCONST_2 = 13;      //将常量2放入操作数栈(float 4byte)
    int DCONST_0 = 14;      //将常量0放入操作数栈(double 8byte)
    int DCONST_1 = 15;      //将常量1放入操作数栈(double 8byte)

    int BIPUSH = 16;        //将1byte的值压入操作数栈中 byte类型和-127~127的int类型都会使用此指令将值压入操作数栈
    int SIPUSH = 17;        //将2byte的值压入操作数栈中 -32768~32767的int类型会使用此指令将值压入操作数栈

    //小于-32768和大于32767的int类会存储在运行时处理池
    //'ldc index'
    int LDC = 18;           //通过索引(1byte)将运行时常量池中的值压入操作数栈中，主要针对int、char、float、String
    int LDC_W = 19;         //与LDC命令一样但是索引为2byte
    int LDC2_W = 20;        //通过索引(2byte)将运行时常量池中的值压入操作数栈中，针对long、double

    int ILOAD = 21;         //将局部变量表中的int类型值压入操作数栈中
    int LLOAD = 22;         //将局部变量表中的long类型值压入操作数栈中
    int FLOAD = 23;         //将局部变量表中的float类型值压入操作数栈中
    int DLOAD = 24;         //将局部变量表中的double类型值压入操作数栈中
    int ALOAD = 25;         //将局部变量表中的引用类型值压入操作数栈中

    //成员方法局部变量表[0]是当前对象引用，但是如果静态方法则没有。
    int ILOAD_0 = 26; //将局部变量表[0]int类型的值压入操作数栈中
    int ILOAD_1 = 27; //将局部变量表[1]int类型的值压入操作数栈中
    int ILOAD_2 = 28; //将局部变量表[2]int类型的值压入操作数栈中
    int ILOAD_3 = 29; //将局部变量表[3]int类型的值压入操作数栈中
    int LLOAD_0 = 30; //将局部变量表[0]long类型的值压入操作数栈中
    int LLOAD_1 = 31; //将局部变量表[1]long类型的值压入操作数栈中
    int LLOAD_2 = 32; //将局部变量表[2]long类型的值压入操作数栈中
    int LLOAD_3 = 33; //将局部变量表[3]long类型的值压入操作数栈中
    int FLOAD_0 = 34; //将局部变量表[0]float类型的值压入操作数栈中
    int FLOAD_1 = 35; //将局部变量表[1]float类型的值压入操作数栈中
    int FLOAD_2 = 36; //将局部变量表[2]float类型的值压入操作数栈中
    int FLOAD_3 = 37; //将局部变量表[3]float类型的值压入操作数栈中
    int DLOAD_0 = 38; //将局部变量表[0]double类型的值压入操作数栈中
    int DLOAD_1 = 39; //将局部变量表[1]double类型的值压入操作数栈中
    int DLOAD_2 = 40; //将局部变量表[2]double类型的值压入操作数栈中
    int DLOAD_3 = 41; //将局部变量表[3]double类型的值压入操作数栈中
    int ALOAD_0 = 42; //将局部变量表[0]引用类型的值压入操作数栈中
    int ALOAD_1 = 43; //将局部变量表[1]引用类型的值压入操作数栈中
    int ALOAD_2 = 44; //将局部变量表[2]引用类型的值压入操作数栈中
    int ALOAD_3 = 45; //将局部变量表[3]引用类型的值压入操作数栈中

    //通过解析引用将值压入操作数栈中,针对数组
    //要先将数组引用压入操作数栈，再将索引(int)压入操作数栈
    int IALOAD = 46; //从数组引用中装载int类型值
    int LALOAD = 47; //从数组引用中装载long类型值
    int FALOAD = 48; //从数组引用中装载float类型值
    int DALOAD = 49; //从数组引用中装载double类型值
    int AALOAD = 50; //从数组引用中装载引用类型值
    int BALOAD = 51; //从数组引用中装载boolean类型值
    int CALOAD = 52; //从数组引用中装载char类型值
    int SALOAD = 53; //从数组引用中装载short类型值
    int ISTORE = 54; //将int类型的值从操作数栈中弹出存放在局部变量表中
    int LSTORE = 55; //将long类型的值从操作数栈中弹出存放在局部变量表中
    int FSTORE = 56; //将float类型的值从操作数栈中弹出存放在局部变量表中
    int DSTORE = 57; //将double类型的值从操作数栈中弹出存放在局部变量表中
    int ASTORE = 58; //将引用类型的值从操作数栈中弹出存放在局部变量表中
    int ISTORE_0 = 59; //将int类型的值从操作数栈中弹出存放在局部变量表[0]中
    int ISTORE_1 = 60; //将int类型的值从操作数栈中弹出存放在局部变量表[1]中
    int ISTORE_2 = 61; //将int类型的值从操作数栈中弹出存放在局部变量表[2]中
    int ISTORE_3 = 62; //将int类型的值从操作数栈中弹出存放在局部变量表[3]中
    int LSTORE_0 = 63; //将long类型的值从操作数栈中弹出存放在局部变量表[0]中
    int LSTORE_1 = 64; //将long类型的值从操作数栈中弹出存放在局部变量表[1]中
    int LSTORE_2 = 65; //将long类型的值从操作数栈中弹出存放在局部变量表[2]中
    int LSTORE_3 = 66; //将long类型的值从操作数栈中弹出存放在局部变量表[3]中
    int FSTORE_0 = 67; //将float类型的值从操作数栈中弹出存放在局部变量表[0]中
    int FSTORE_1 = 68; //将float类型的值从操作数栈中弹出存放在局部变量表[1]中
    int FSTORE_2 = 69; //将float类型的值从操作数栈中弹出存放在局部变量表[2]中
    int FSTORE_3 = 70; //将float类型的值从操作数栈中弹出存放在局部变量表[3]中
    int DSTORE_0 = 71; //将double类型的值从操作数栈中弹出存放在局部变量表[0]中
    int DSTORE_1 = 72; //将double类型的值从操作数栈中弹出存放在局部变量表[1]中
    int DSTORE_2 = 73; //将double类型的值从操作数栈中弹出存放在局部变量表[2]中
    int DSTORE_3 = 74; //将double类型的值从操作数栈中弹出存放在局部变量表[3]中
    int ASTORE_0 = 75; //将引用类型的值从操作数栈中弹出存放在局部变量表[0]中
    int ASTORE_1 = 76; //将引用类型的值从操作数栈中弹出存放在局部变量表[1]中
    int ASTORE_2 = 77; //将引用类型的值从操作数栈中弹出存放在局部变量表[2]中
    int ASTORE_3 = 78; //将引用类型的值从操作数栈中弹出存放在局部变量表[3]中
    int IASTORE = 79; //将int类型值存入数组中
    int LASTORE = 80; //将long类型值存入数组中
    int FASTORE = 81; //将float类型值存入数组中
    int DASTORE = 82; //将double类型值存入数组中
    int AASTORE = 83; //将引用类型值存入数组中
    int BASTORE = 84; //将boolean类型值存入数组中
    int CASTORE = 85; //将char类型值存入数组中
    int SASTORE = 86; //将short类型值存入数组中

    //操作数栈管理指令
    int POP = 87; //弹出栈顶一个栈单位深度,弹出元素废弃
    int POP2 = 88; //弹出栈顶两个栈单位深度,弹出元素废弃
    int DUP = 89; //复制栈顶一个栈单位深度的值,正常压入栈中

    //return ++filed(int);
    int DUP_X1 = 90; //复制栈顶一个栈单位深度的值,并置于第三个栈单位深度
    int DUP_X2 = 91; //复制栈顶一个栈单位深度的值,并置于第四个栈单位深度

    //return ++filed(long);
    int DUP2 = 92; //复制栈顶两个栈单位深度的值,正常压入栈中
    int DUP2_X1 = 93; //复制栈顶两个栈单位深度的值,并置于第三、四个栈单位深度
    int DUP2_X2 = 94; //复制栈顶两个栈单位深度的值,并置于第四、五个栈单位深度

    int SWAP = 95; //交换栈顶第一个栈单位深度的值和第二个栈单位深度的值

    //左操作数先入栈，右操作数后入栈，从操作数栈中弹出两个值相加，结果压入栈中
    int IADD = 96;//int
    int LADD = 97;//long
    int FADD = 98;//float
    int DADD = 99;//double

    //左操作数先入栈，右操作数后入栈，从操作数栈中弹出两个值相减，结果压入栈中
    int ISUB = 100;
    int LSUB = 101;
    int FSUB = 102;
    int DSUB = 103;

    //左操作数先入栈，右操作数后入栈，从操作数栈中弹出两个值相乘，结果压入栈中
    int IMUL = 104;
    int LMUL = 105;
    int FMUL = 106;
    int DMUL = 107;

    //左操作数先入栈，右操作数后入栈，从操作数栈中弹出两个值相除，结果压入栈中
    int IDIV = 108;
    int LDIV = 109;
    int FDIV = 110;
    int DDIV = 111;

    //左操作数先入栈，右操作数后入栈，从操作数栈中弹出两个值求余，结果压入栈中
    int IREM = 112;
    int LREM = 113;
    int FREM = 114;
    int DREM = 115;

    //弹出操作数将操作数求反再将结果压回栈内
    int INEG = 116;
    int LNEG = 117;
    int FNEG = 118;
    int DNEG = 119;

    //无符号，左操作数先入栈，右操作数后入栈，弹出两值将左操作数左移‘右操作数’位，再将结果压回栈内
    int ISHL = 120;//int
    int LSHL = 121;//long
    //无符号，左操作数先入栈，右操作数后入栈，弹出两值将左操作数右移‘右操作数’位，再将结果压回栈内
    int ISHR = 122;
    int LSHR = 123;
    //有符号，左操作数先入栈，右操作数后入栈，弹出两值将左操作数右移‘右操作数’位，再将结果压回栈内
    int IUSHR = 124;
    int LUSHR = 125;

    //按位与
    int IAND = 126;
    int LAND = 127;

    //按位或
    int IOR = 128;
    int LOR = 129;

    //按位异或
    int IXOR = 130;
    int LXOR = 131;

    //在局部变量表中完成自增 iinc index by const
    int IINC = 132;

    //int类型通过补0拓展位数转化成
    int I2L = 133;
    int I2F = 134;
    int I2D = 135;

    //通过丢弃高位转化成其他类型
    int L2I = 136;//丢弃64-32位高位数字
    int L2F = 137;//丢弃64-23位高位数字
    int L2D = 138;//丢弃64-52位高位数字

    //有效位根据阶码右移，阶码置0转化成int类型或long类型。
    int F2I = 139;
    int F2L = 140;
    int F2D = 141;

    //有效位根据阶码右移，阶码置0转化成int类型或long类型。
    int D2I = 142;
    int D2L = 143;
    int D2F = 144;

    //通过丢弃高位转化成其他类型
    int I2B = 145;//丢弃32-8位高位数字，转化为byte
    int I2C = 146;//丢弃32-16为高位数字，转化为char
    int I2S = 147;//丢弃32-16为高位数字，转化为short

    //比较指令集，小于将-1压回栈内，相等则压回0，大于将1压回栈内
    int LCMP = 148; //long类型比较
    int FCMPL = 149; //float类型比较，遇到NaN值则压入-1
    int FCMPG = 150; //float类型比较，遇到NaN值则压入1
    int DCMPL = 151; //double类型比较，遇到NaN值则压入-1
    int DCMPG = 152; //double类型比较，遇到NaN值则压入1

    //int value
    //  if语句跳转 if(value) 弹出一个操作数
    //  'if__ index'
    int IFEQ = 153; //value == 0时 跳转到index处开始执行
    int IFNE = 154; //value != 0时 跳转到index处开始执行
    int IFLT = 155; //value < 0时 跳转到index处开始执行
    int IFGE = 156; //value ≤ 0时 跳转到index处开始执行
    int IFGT = 157; //value > 0时 跳转到index处开始执行
    int IFLE = 158; //value ≥ 0时 跳转到index处开始执行

    //int value1 ,int value2
    //条件语句跳转 if(value1 _ value2) 弹出两个操作数
    //'if_icmp__ index'
    int IF_ICMPEQ = 159; // value1 == value2 跳转到index处开始执行
    int IF_ICMPNE = 160; // value1 != value2 跳转到index处开始执行
    int IF_ICMPLT = 161; // value1 < value2 跳转到index处开始执行
    int IF_ICMPGE = 162; // value1 ≤ value2 跳转到index处开始执行
    int IF_ICMPGT = 163; // value1 > value2 跳转到index处开始执行
    int IF_ICMPLE = 164; // value1 ≥ value2 跳转到index处开始执行

    //reference value1,reference value2
    int IF_ACMPEQ = 165; // value1 == value2 跳转到index处开始执行
    int IF_ACMPNE = 166; // value1 != value2 跳转到index处开始执行

    //无条件语句跳转
    int GOTO = 167; //'goto index'跳转到index处开始执行
    int JSR = 168;  //java7废弃，被goto取代
    int RET = 169;  //java7废弃，被goto取代

    //int value1,int value2
    //'tableswitch value1 to value2(value+1~value+14)
    //      value1: index
    //      ...
    //      value2: index
    // '
    //当value1与value2的值相差14之内都会选择tableswitch指令做选择跳转指令
    int TABLESWITCH = 170;

    //'lookupswitch num' 有num个选项不包括default
    //当value1与value2的值相差14之外会选择LOOKUPSWITCH指令做选择跳转指令
    int LOOKUPSWITCH = 171;


    int IRETURN = 172;  //返回int类型的值
    int LRETURN = 173;  //返回long类型的值
    int FRETURN = 174;  //返回float类型的值
    int DRETURN = 175;  //返回double类型的值
    int ARETURN = 176;  //返回引用类型的值
    int RETURN = 177;   //无返回void


    int GETSTATIC = 178;    //通过运行时常量池获取静态变量 'getstatic 运行时常量池索引(fieldref)'
    int PUTSTATIC = 179;    //通过运行时常量池赋值静态变量
    int GETFIELD = 180;     //通过运行时常量池获取成员变量
    int PUTFIELD = 181;     //通过运行时常量池赋值成员变量

    int INVOKEVIRTUAL = 182; //通过运行时常量池调用虚方法 'invokevirtual 运行时常量池索引(methodref)'
    int INVOKESPECIAL = 183; //通过运行时常量池调用调用一些需要特殊处理的实例方法，包括实例初始化方法、私有方法和父类方法。
    int INVOKESTATIC = 184;     //通过运行时常量池调用静态方法
    int INVOKEINTERFACE = 185; //通过运行时常量池调用接口方法
    int INVOKEDYNAMIC = 186;    //调用Lambda表达式和默认方法 'invokedynamic 运行时常量池索引(invokeDynamic)'

    int NEW = 187;          //'new 运行时常量池索引(class)' 为新对象分配空间并初始化成员方法
    int NEWARRAY = 188;     //'newarray 数组类型对应的值' 从操作数栈中弹出一个int类型的值作为数组的长度
    int ANEWARRAY = 189;    //'anewarray 运行时常量池索引(class)' 从操作数栈中弹出一个int类型的值作为数组的长度
    int ARRAYLENGTH = 190;  //从操作数栈中弹出一个数组引用，并求得数组长度压回栈中
    int ATHROW = 191;       //抛出异常如果没处理则当前栈帧抛出并返回异常
    int CHECKCAST = 192;    //’checkcast 运行时常量池索引(class)‘ 判断是否可以类型转换，如果可以无操作，如果不行则抛出ClassCastException
    int INSTANCEOF = 193;   //'instanceof 运行时常量池索引(class)' 从操作数栈中弹出一个引用类型的值判断是否class的实例或子类实例
    int MONITORENTER = 194; //从操作数栈中弹出一个引用类型，通过这个引用类型找到该对象的monitor尝试获取monitor的所有权
    int MONITOREXIT = 195;  //从操作数栈中弹出一个引用类型，通过这个引用类型找到该对象的monitor尝试释放monitor的所有权
    // int WIDE = 196;      // NOT VISITED
    int MULTIANEWARRAY = 197; //创建多维数组 ’multianewarray 运行时常量池索引(class) dim 维度‘n位数组会弹出n个int类型操作数

    //if(value __ null)
    //'if___null index'
    int IFNULL = 198; //从操作数栈弹出一个值，如果等于null则跳转到index开始执行
    int IFNONNULL = 199; //从操作数栈弹出一个值，如果不等于null则跳转到index开始执行
    int GOTO_W = 200; // -
    int JSR_W = 201; // -
}
