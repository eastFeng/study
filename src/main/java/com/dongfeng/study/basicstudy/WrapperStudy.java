package com.dongfeng.study.basicstudy;

/**
 * <b> 包装（wrapper）类学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-22 23:56
 */
public class WrapperStudy {
    public static void main(String[] args) {
        // 包装类概念
        wrapper();

        // 包装类的共同特点
        commonCharacter();

        // 剖析Integer与二进制算法
        integerAndBinary();

        // 剖析Character
        analyzeCharacter();

        int i = 100;
        int i2 = i >>> 24;
        System.out.println(i);
        System.out.println(i2);

        System.out.println("100001111111111111111".length());
    }

    /**
     * 包装类
     */
    public static void wrapper(){
        /*
         * 所有的基本类型都有一个与之对应的类。
         * 例如，Integer类对应基本类型int。通常，这些类称为包装器（wrapper）。
         * 这些对象包装器类拥有很明显的名字：Integer、Long、Float、Double、Short、Byte、Character、Void和Boolean
         * （前6个类派生于公共的超类Number）。
         * 对象包装器类是不可变的，即一旦构造了包装器，就不允许更改包装在其中的值，因为其中存储值的实例变量value是final的，只能赋值一次。
         * 同时，对象包装器类还是final，因此不能定义它们的子类。
         *
         * Java有8种基本类型，每种基本类型都有一个对应的包装类。包装类是什么呢？
         * 它是一个类，内部有一个实例变量value，保存对应的基本类型的值，这个类一般还有一些静态方法、静态变量和实例方法，以方便对数据进行操作。
         *
         * 各个包装类都可以与其对应的基本类型相互转换:
         *
         * 将基本类型转换为包装类的过程，一般称为“装箱”，而将包装类型转换为基本类型的过程，则称为“拆箱”。
         * 装箱/拆箱写起来比较烦琐，Java 5以后引入了【自动装箱和拆箱技术】，可以直接将基本类型赋值给引用类型，反之亦可。
         * 自动装箱/拆箱是Java编译器提供的能力，背后，它会替换为调用对应的valueOf/xxx-Value方法，
         * 比如：
         *       Integer a = 100;
         *       int b = a;
         * 会被Java编译器替换为：
         *       Integer a = Integer.valueOf(100);
         *       int b = a.intValue();
         *
         * 每种包装类也都有构造方法，可以通过new创建，比如：Integer x = new Integer(100);
         * 那到底应该用静态的valueOf方法，还是使用new呢？一般建议使用valueOf方法。new每次都会创建一个新对象，
         * 而除了Float和Double外的其他包装类，都会缓存包装类对象，减少需要创建对象的次数，节省空间，提升性能。
         * 实际上，从Java 9开始，这些构造方法已经被标记为过时了，推荐使用静态的valueOf方法。
         */

        /*
         * Integer的默认值为null，int的默认值是0。
         * 在广大实践中，大量的数据操作中，都是集中在有限、较小的数据范围内，
         * 在java5中新增了静态工厂方法valueOf()，在调用它的时候，会运用到缓存机制，带来明显的性能提升。
         * Integer 默认的值缓存范围 ：[-128, 127]，
         */

        // 调用静态工厂方法
        // valueOf方法会判断该数值是否是在缓存值范围内，如果是，则返回一个已经创建好的Integer对象，也就是在IntegerCache中创建的Integer缓存数组，
        // 如果不在这个范围内，就直接new 一个Integer。
        Integer integer = Integer.valueOf("1");
    }

    /**
     * 包装类的共同特点
     */
    public static void commonCharacter(){
        /*
         * 各个包装类有很多共同点，比如，都重写了Object中的一些方法，都实现了Comparable接口，
         * 都有一些与String有关的方法，大部分都定义了一些静态常量，都是不可变的。
         *
         * 1. 所有包装类都重写了Object类的equals、hashCode和toString方法。
         *
         * 1) equals: 所有包装类都重写了该实现，实际比较用的是其包装的基本类型值。
         *    Float有一个静态方法floatToIntBits()，将float的二进制表示看作int。
         *    需要注意的是，只有两个float的二进制表示完全一样的时候，equals才会返回true。Double的equals方法与Float类似
         *
         * 2) hashCode: hashCode和equals方法联系密切，对两个对象，如果equals方法返回true，则hashCode也必须一样。
         *    反之不要求，equal方法返回false时，hashCode可以一样，也可以不一样，但应该尽量不一样。
         *    包装类都重写了hashCode，根据包装的基本类型值计算hashCode。
         *
         * 3) toString: 每个包装类也都重写了toString方法，返回对象的字符串表示。
         *
         * 2. 每个包装类都实现了Java API中的Comparable接口。
         *    各个包装类的实现基本都是根据基本类型值进行比较。对于Boolean,false小于true。
         *
         * 3．包装类和String。
         *    除了Character外，每个包装类都有一个静态的valueOf(String)方法，根据字符串表示返回包装类对象。
         *    也都有一个静态的parseⅩⅩⅩ(String)方法，根据字符串表示返回基本类型值。
         *    都有一个静态的toString方法，根据基本类型值返回字符串表示。
         *    对于Integer类型，字符串表示除了默认的十进制外，还可以表示为其他进制，如二进制、八进制和十六进制，包装类有静态方法进行相互转换。
         *
         * 4．常用常量。
         *    包装类中除了定义静态方法和实例方法外，还定义了一些静态常量。
         *    例如，Boolean中有: public static final Boolean TRUE = new Boolean(true);
         *                     public static final Boolean FALSE = new Boolean(false);
         *
         * 5. Number。
         *    6种数值类型包装类有一个共同的父类Number。Number是一个抽象类。
         *
         * 6．不可变性。
         *    包装类都是不可变类。所谓不可变是指实例对象一旦创建，就没有办法修改了。这是通过如下方式强制实现的：
         *    1) 所有包装类都声明为了final，不能被继承。
         *    2) 内部基本类型值是私有的，且声明为了final。
         *    3) 没有定义setter方法。 // 个人观点：这个不算吧，实例变量value都声明final了，有setter方法，也不能更新成功。
         * 不可变使得程序更为简单安全，
         */


    }

    /**
     * 剖析Integer与二进制算法
     */
    public static void integerAndBinary(){
        /*
         * Integer有一些二进制操作，包括位翻转和循环移位等，另外，也分析一下它的valueOf实现。为什么要关心实现代码呢？
         * 大部分情况下，确实不用关心，会用它就可以了，主要是学习其中的二进制操作。
         * 二进制是计算机的基础，但代码往往晦涩难懂，希望对其有一个更为清晰深刻的理解。
         * Long与Integer类似，就不再单独介绍了。
         */

        // 1. 位翻转：位翻转就是将int当作二进制，左边的位与右边的位进行互换。

        // reverse是按位进行互换
        int reverse = Integer.reverse(100);
        // reverseBytes是按byte进行互换（按字节翻转）
        int i = Integer.reverseBytes(100);
        // Integer.reverseBytes源码：
        int i1 = reverseBytes(100);
        // Integer.reverse源码：
        int reverse1 = reverse(100);


        // 2．循环移位：所谓循环移位，是相对于普通的移位而言的，普通移位，比如左移2位，原来的最高两位就没有了，右边会补0，
        // 而如果是循环左移两位，则原来的最高两位会移到最右边，就像一个左右相接的环一样。

        // rotateLeft：循环左移，distance是移动的位数
        int i2 = Integer.rotateLeft(100, 2);
        // rotateRight：循环右移，distance是移动的位数
        int i3 = Integer.rotateRight(100, 2);



        /*
         * 3. valueOf的实现
         * 它使用了IntegerCache，这是一个私有静态内部类。
         *
         * IntegerCache表示Integer缓存，其中的cache变量是一个静态Integer数组，在静态初始化代码块中被初始化，
         * 默认情况下，保存了-128～127共256个整数对应的Integer对象。
         *
         * 在valueOf代码中，如果数值位于被缓存的范围，即默认-128～127，则直接从Integer-Cache中获取已预先创建的Integer对象，
         * 只有不在缓存范围时，才通过new创建对象。
         *
         * 通过共享常用对象，可以节省内存空间，由于Integer是不可变的，所以缓存的对象可以安全地被共享。
         * Boolean、Byte、Short、Long、Character都有类似的实现。
         * 这种共享常用对象的思路，是一种常见的设计思路，它有一个名字，叫享元模式，英文叫Flyweight，即共享的轻量级元素。
         */

    }

    /**
     * 剖析Character
     */
    public static void analyzeCharacter(){
        /*
         * Character类除了封装了一个char外，还有什么可介绍的呢？
         * 它有很多静态方法，封装了Unicode字符级别的各种操作，是Java文本处理的基础，注意不是char级别，Unicode字符并不等同于char。
         *
         * Unicode是统一码，也叫万国码、单一码（Unicode）是计算机科学领域里的一项业界标准，包括字符集、编码方案等。
         * Unicode 是为了解决传统的字符编码方案的局限而产生的，它为每种语言中的每个字符设定了统一并且唯一的二进制编码，
         * 以满足跨语言、跨平台进行文本转换、处理的要求。1990年开始研发，1994年正式发布1.0版本，2020年发布13.0版本。
         *
         * Unicode给世界上每个字符分配了一个编号，编号范围为0x000000～0x10FFFF（0~1114111）。
         * 编号范围在0x0000～0xFFFF（65535，也就是2的16次方）的字符为常用字符集，称BMP（Basic MultilingualPlane）字符。
         * 编号范围在0x10000～0x10FFFF的字符叫做增补字符（supplementary character）。
         *
         * Unicode主要规定了编号，但没有规定如何把编号映射为二进制。
         *
         * UTF-16是一种编码方式，或者叫映射方式，它将编号映射为两个或4个字节，
         * 对BMP字符，它直接用两个字节表示，对于增补字符，使用4个字节表示，
         * 前两个字节叫高代理项（high surrogate），范围为0xD800～0xDBFF，
         * 后两个字节叫低代理项（low surrogate），范围为0xDC00～0xDFFF。
         * UTF-16定义了一个公式，可以将编号与4字节表示进行相互转换。
         *
         * Java内部采用UTF-16编码，char（占两个字节）表示一个字符，但只能表示BMP中的字符，
         * 对于增补字符，需要使用两个char表示，一个表示高代理项，一个表示低代理项。
         *
         * 0x10FFFF = 0b100001111111111111111（二进制）， 0x10FFFF转换为二进制刚好21位。
         * 使用int可以表示任意一个Unicode字符，低21位表示Unicode编号，高11位设为0。
         * 整数编号在Unicode中一般称为代码点（code point），表示一个Unicode字符，
         * 与之相对，还有一个词代码单元（code unit）表示一个char。
         *
         */

        // 1. 检查code point和char

        // 2. code point与char的转换

        // 3. 按code point处理char数组或序列

        // 4. 字符属性

        // 5. 字符转换
    }


    /**
     * 按byte进行互换（按字节翻转）int类型整数
     */
    public static int reverseBytes(int i) {
        // int类型总共占4字节，32位
        // 无符号右移24位（3字节），因为int总共32位（4字节），所以最高字节挪到最低位。
        return ((i >>> 24)           ) |
                // 无符号右移8位：左边第二个字节挪到右边第二个；& 0xFF00：保留的是右边第二个字节
                ((i >>   8) &   0xFF00) |
                // 左移8位：右边第二个字节挪到左边第二个；& 0xFF0000：保留的是右边第三个字节
                ((i <<   8) & 0xFF0000) |
                // 左移24位：最右字节挪到最左边
                ((i << 24));

    }

    /**
     * 按位翻转int类型整数
     */
    public static int reverse(int i) {
        // HD, Figure 7-1 : HD表示的是一本书，书名为Hacker's Delight，中文版为《算法心得：高效算法的奥秘》,
        // HD是它的缩写，Figure 7-1是书中的图7-1, reverse的代码就是复制了这本书中图7-1的代码

        // 高效实现位翻转的基本思路是：首先交换相邻的单一位，然后以两位为一组，再交换相邻的位，
        // 接着是4位一组交换、然后是8位、16位，16位之后就完成了。
        // 这个思路不仅适用于二进制，而且适用于十进制

        // 交换相邻的单一位
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        // 以两位为一组，再交换相邻的位
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        // 以4位为一组进行交换
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        // 8位为单位交换时，就是字节翻转了，可以写为如下更直接的形式，代码和reverseBytes基本完全一样
        i = (i << 24) | ((i & 0xff00) << 8) |
                ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;

        /*
         * reverse代码为什么要写得这么晦涩呢？或者说不能用更容易理解的方式写吗？
         * 比如，实现翻转，一种常见的思路是：第一个和最后一个交换，第二个和倒数第二个交换，直到中间两个交换完成。
         * 如果数据不是二进制位，这个思路是好的，但对于二进制位，这个思路的效率比较低。
         *
         * CPU指令并不能高效地操作单个位，它操作的最小数据单位一般是32位（32位机器），
         * 另外，CPU可以高效地实现移位和逻辑运算，但实现加、减、乘、除运算则比较慢。
         *
         * reverse是在充分利用CPU的这些特性，并行高效地进行相邻位的交换。
         *
         */
    }

    /**
     * 循环左移
     */
    public static int rotateLeft(int i, int distance) {
        /*
         * 这两个函数中令人费解的是负数，如果distance是8，那i>>>-8是什么意思呢？
         * 其实，实际的移位个数不是后面的直接数字，而是直接数字的最低5位的值，或者说是直接数字&0x1f的结果。
         * 之所以这样，是因为5位最大表示31，移位超过31位对int整数是无效的。
         *
         *
         * i>>>-distance就是i>>>(32-distance)。
         *
         */
        return (i << distance) | (i >>> -distance);
    }

}


















































