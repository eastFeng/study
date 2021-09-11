package com.dongfeng.study.basicstudy.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <b> 常见文件类型处理 </b>
 * @author eastFeng
 * @date 2021-05-01 21:30
 */
public class CommonFileTypes {
    public static void main(String[] args) {

        String path = "D:\\Wstudy\\ctest.dat";
        // 五中常见文件类型
        commonFileType();

        //
    }


    /**
     * 五中常见文件类型
     */
    public static void commonFileType(){
        /*
         * 利用Java API和一些第三方类库，来处理如下5种类型的文件：
         *
         * 1. 属性文件
         * 属性文件是常见的配置文件，用于在不改变代码的情况下改变程序的行为。
         *
         * 2. CSV
         * CSV是Comma-Separated Values的缩写，表示逗号分隔值，是一种非常常见的文件类型。
         * 大部分日志文件都是CSV, CSV也经常用于交换表格类型的数据，CSV看上去很简单，但处理的复杂性经常被低估。
         *
         * 3. Excel
         * 在编程中，经常需要将表格类型的数据导出为Excel格式，以方便用户查看，也经常需要接受Excel类型的文件作为输入以批量导入数据。
         *
         * 4. HTML
         * 所有网页都是HTML格式，我们经常需要分析HTML网页，以从中提取感兴趣的信息。
         *
         * 5. 压缩文件：压缩文件有多种格式，也有很多压缩工具，大部分情况下，我们可以借助工具而不需要自己写程序处理压缩文件，
         * 但某些情况下，需要自己编程压缩文件或解压缩文件。
         */
    }

    /**
     * 属性文件
     */
    public static void propertyFile() throws IOException {
        /*
         * 属性文件一般很简单，一行表示一个属性，属性就是键值对，键和值用等号（=）或冒号（:）分隔，一般用于配置程序的一些参数。
         * 在需要连接数据库的程序中，经常使用配置文件配置数据库信息。比如，设有文件config.properties，内容大概如下所示：
         * db.host=192.168.10.100
         * db.port:3306
         * db.username=zhangsan
         *
         * 处理这种文件使用字符流是比较容易的，但Java中有一个专门的类java.util.Properties，它的使用也很简单，有如下主要方法：
         * public synchronized void load(InputStream inStream)
         * public String getProperty(String key)
         * public String getProperty(String key, String defaultValue)
         * load用于从流中加载属性，getProperty用于获取属性值，可以提供一个默认值，如果没有找到配置的值，则返回默认值。
         *
         * 使用类Properties处理属性文件的好处是：
         * 1. 可以自动处理空格，分隔符=前后的空格会被自动忽略。
         * 2. 可以自动忽略空行。
         * 3. 可以添加注释，以字符#或！开头的行会被视为注释，进行忽略。
         *
         * 使用Properties也有限制，它不能直接处理中文，在配置文件中，所有非ASCII字符需要使用Unicode编码。
         */
        String path = "D:\\Wstudy\\ctest.dat";
        final Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        properties.<Integer>get("h");
        final String hh = properties.getProperty("HH");
        properties.getProperty("hh", "hh");
    }

    /**
     * CSV文件
     */
    public static void csv(){
        /*
         * CSV是Comma-Separated Values的缩写，表示逗号分隔值。一般而言，一行表示一条记录，一条记录包含多个字段，字段之间用逗号分隔。
         * 不过，一般而言，分隔符不一定是逗号，可能是其他字符，如tab符'\t'、冒号':'、分号';'等。
         * 程序中的各种日志文件通常是CSV文件，在导入导出表格类型的数据时，CSV也是经常用的一种格式。
         *
         * CSV格式看上去很简单。比如，保存学生列表时，使用CSV格式：
         * 张三，18，80.9
         * 李四，17，67.5
         *
         * 使用之前介绍的字符流，看上去就可以很容易处理CSV文件，按行读取，对每一行，使用String.split进行分隔即可。
         * 但其实CSV有一些复杂的地方，最重要的是：
         * 1. 字段内容中包含分隔符怎么办？
         * 2. 字段内容中包含换行符怎么办？
         * 对于这些问题，CSV有一个参考标准：RFC-4180（https://tools.ietf.org/html/rfc4180），
         * 但实践中不同程序往往有其他处理方式，所幸的是，处理方式大体类似，大概有以下两种处理方式。
         * 1. 使用引用符号比如"，在字段内容两边加上"，如果内容中包含"本身，则使用两个"。
         * 2. 使用转义字符，常用的是\，如果内容中包含\，则使用两个\。
         *
         */
    }
}

































