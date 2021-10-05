package com.dongfeng.study.basicstudy.java.otherbasic;

/**
 * <b> {@link String} 学习 </b>
 *
 * @author eastFeng
 * @date 2020/6/15 - 15:18
 */
public class StringStudy {

    public static void main(String[] args) {
        // 创建字符串最简单方式：该方式创建的字符串存储在公共池中
        String s1 = "Runoob";
        // 用构造函数创建字符串：创建的字符串对象在堆上
        String s2 = new String("Runoob");

        // 剖析String
        analyzeString("");

        // 常量字符串
        constantString();

//        getFileType();
//        test_concat();
        getFileType();
    }

    /**
     * 剖析String
     */
    public static void analyzeString(String str){
        /*
         * 字符串操作是计算机程序中最常见的操作之一。Java中处理字符串的主要类是String和StringBuilder。
         *
         * 从概念上讲，Java字符串就是Unicode字符序列。
         * 每个用双引号括起来的字符串都是String类的一个实例。
         *
         * String 是不可变类型，String类没有提供用于修改字符串的方法。
         * 当然，可以修改字符串变量greeting，让它引用另外一个字符串，这就如同可以将存放3的数值变量改成存放4一样。
         * 不可变字符串却有一个优点：编译器可以让字符串共享。
         *
         * String类也是不可变类，即对象一旦创建，就没有办法修改了。
         * String类也声明为了final，不能被继承，内部char数组value也是final的，初始化后就不能再变了。
         *
         * String类中提供了很多看似修改的方法，其实是通过创建新的String对象来实现的，原来的String对象不会被修改。
         *
         * 一定不要使用==运算符检测两个字符串是否相等！这个运算符只能够确定两个字符串是否放置在同一个位置上。
         * 千万不要使用==运算符测试字符串的相等性，以免在程序中出现糟糕的bug。
         * equals方法检测两个字符串是否相等。
         */

        // 空串""是长度为0的字符串。可以调用以下代码检查一个字符串是否为空。空串是一个Java对象，有自己的串长度（0）和内容（空）。
        // String s = "";
        System.out.println(str.length() == 0);
        // 或者
        System.out.println(str.equals(""));

        /*
         * String类内部用一个字符数组表示字符串，实例变量定义为：private final char value[];
         *
         * String有两个构造方法，可以根据char数组创建String变量：
         * public String(char value[])
         * public String(char value[], int offset, int count)
         *
         * String会根据参数新创建一个数组，并复制内容，而不会直接用参数中的字符数组。
         * String中的大部分方法内部也都是操作的这个字符数组。比如：
         * 1）length()方法返回的是这个数组的长度。
         * 2）substring()方法是根据参数，调用构造方法String(char value[], int offset, int count)新建了一个字符串。
         * 3）indexOf()方法查找字符或子字符串时是在这个数组中进行查找。
         *
         * String内部是按UTF-16BE处理字符的，对BMP字符，使用一个char，两个字节，对于增补字符，使用两个char，四个字节。
         * 不同编码可能用于不同的字符集，使用不同的字节数目，以及不同的二进制表示。
         * 如何处理这些不同的编码呢？这些编码与Java内部表示之间如何相互转换呢？
         *
         * Java使用Charset类表示各种编码，它有两个常用静态方法：
         * 第一个方法返回系统的默认编码。
         * 第二个方法返回给定编码名称的Charset对象，
         * 其charset名称可以是US-ASCII、ISO-8859-1、windows-1252、GB2312、GBK、GB18030、Big5、UTF-8等。
         */

    }

    /**
     * 常量字符串
     */
    public static void constantString(){
        /*
         * Java中的字符串常量是非常特殊的，除了可以直接赋值给String变量外，
         * 它自己就像一个String类型的对象，可以直接调用String的各种方法
         */

        System.out.println("北京欢迎您".length());
        System.out.println("北京欢迎您".contains("北京"));
        System.out.println("北京欢迎您".indexOf("欢迎"));

        /*
         * 实际上，这些常量就是String类型的对象，在内存中，它们被放在一个共享的地方，这个地方称为字符串常量池，
         * 它保存所有的常量字符串，每个常量只会保存一份，被所有使用者共享。
         * 当通过常量的形式使用一个字符串的时候，使用的就是常量池中的那个对应的String类型的对象。
         *
         */
    }


    /**
     * 测试null的字符串啥样子
     */
    public static void nullTest(){
        String s = "ab" + null;
        System.out.println(s);
    }

    /**
     * trim()的作用：去掉字符串首尾的空格
     */
    public static void trimTest(){
        String str1 = " dong feng ";
        System.out.println(str1.trim());

        String str2 = "    ";
        System.out.println("".equals(str2.trim()));
    }

    /**
     * <p> public String replace(CharSequence target, CharSequence replacement)
     * <p> public String replace(char oldChar, char newChar)
     *
     * <p> replace() 方法通过用 newChar 字符替换字符串中出现的所有 oldChar 字符，并返回替换后的新字符串。
     */
    public static void replaceTest(){
        String str = "{schoolname/}";
        String str1 = "{schoolname/}{schoolname/}{schoolname/}{schoolname/}{schoolname/}";

        String replaceStr = str.replace("{", "").replace("/}", "");

        String replaceStr1 = str1.replace("{", "").replace("/}", "");

        System.out.println(replaceStr);
        System.out.println(replaceStr1);

        if (replaceStr.equals("schoolname")){
            System.out.println("equal");
        }
    }

    /**
     * <p> public String substring(int beginIndex)
     * <p> public String substring(int beginIndex, int endIndex)
     *
     * <p> beginIndex -- 起始索引（包括）, 索引从 0 开始。
     * <p> endIndex -- 结束索引（不包括）。
     */
    public static void substringTest(){
        String str = "ILOVEYOU";

        System.out.println(str.substring(1, 5));

        System.out.println(str.substring(5));

        System.out.println(str.substring(0, 8));
    }

    /**
     * <p> public boolean startsWith(String prefix)
     * <p> public boolean startsWith(String prefix, int toffset)
     *
     * <p> startsWith() 方法用于检测字符串是否以指定的前缀开始。
     * <p> prefix -- 前缀
     * <p> toffset -- 字符串中开始查找的位置(下标)
     */
    public static void startsWithTest(){
        String str = "ILOVEYOU";

        System.out.println(str.startsWith("I"));
        System.out.println(str.startsWith("IL"));
        System.out.println(str.startsWith("i"));

        System.out.println("toffset start ===");

        System.out.println(str.startsWith("O", 2));
        System.out.println(str.startsWith("o", 2));
    }

    /**
     * <p> public int indexOf(String str)
     * <p> 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1;
     *
     * <p> public int indexOf(String str, int fromIndex)
     * <p> 返回从 fromIndex 位置开始查找指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1;
     *
     * <p> public int indexOf(int ch)
     * <p> 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1;
     *
     * <p> public int indexOf(int ch, int fromIndex)
     * <p> 返回从 fromIndex 位置开始查找指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1;
     *
     * <p> ch -- 字符，Unicode 编码。
     * <p> str -- 要搜索的子字符串。
     * <p> fromIndex -- 开始搜索的索引位置，第一个字符是 0 ，第二个是 1 ，以此类推。
     */
    public static void indexOfTest(){
        String str = "ILOVEYOU";

        String str1 = "aaa456abc";

        System.out.println("String start ===");

        System.out.println(str.indexOf("V"));
        System.out.println(str.indexOf("M"));

        System.out.println("char start ===");

        System.out.println(str.indexOf('E'));
        // 字符a对应的数字就是97
        System.out.println(str1.indexOf(97));
    }

    /**
     * <p> public int lastIndexOf(String str)
     * <p> 返回指定子字符串在此字符串中最右边出现处的索引，如果此字符串中没有这样的字符，则返回 -1;
     */
    public static void lastIndexOfTest(){
        String originalName = "test1.jpg";

        System.out.println(originalName.lastIndexOf("."));
    }

    /**
     * 从文件原始名称中获取文件类型（后缀）和 名称
     */
    public static void getFileType(){
        String originalName = "test1.jpg";

        // 名称
        String prefix = originalName.substring(0, originalName.lastIndexOf("."));
        // 类型
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        System.out.println("名称: "+prefix);
        System.out.println("类型: "+suffix);
    }

    /**
     * concat() 方法用于将指定的字符串参数连接到字符串上
     */
    public static void concatTest(){
        String str = "I LOVE YOU ";
        System.out.println(str.concat("CHINA"));
    }
}
