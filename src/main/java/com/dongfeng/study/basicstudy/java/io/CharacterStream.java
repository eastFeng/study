package com.dongfeng.study.basicstudy.java.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <b> 文本文件和字符流 </b>
 *
 * @author eastFeng
 * @date 2021-04-30 21:27
 */
public class CharacterStream {
    public static void main(String[] args) {
        /*
         * 对于文本文件，字节流没有编码的概念，不能按行处理，使用不方便，更适合的是使用字符流。
         *
         * 写文件时，可以优先考虑PrintWriter，因为它使用方便，支持自动缓冲、指定编码类型、类型转换等。
         * 读文件时，如果需要指定编码类型，需要使用InputStreamReader；
         * 如果不需要指定编码类型，可使用FileReader，但都应该考虑在外面包上缓冲类BufferedReader。
         */

        // Reader 和 Writer
        readerAndWriter();

        // InputStreamReader 和 OutputStreamWriter
        inputStreamReader();

        // FileReader和FileWriter
        fileReaderAndWriter();

        // CharArrayReader和CharArrayWriter
        charArrayReaderAndWriter();

        // StringReader 和 StringWriter
        stringReaderAndWriter();
    }

    /**
     * 字符流
     */
    public static void characterStream(){
        /*
         * Java中的主要字符流有：
         * 1）Reader/Writer：字符流的基类，它们是抽象类；
         * 2）InputStreamReader/OutputStreamWriter：适配器类，将字节流转换为字符流；
         * 3）FileReader/FileWriter：输入源和输出目标是文件的字符流；
         * 4）CharArrayReader/CharArrayWriter：输入源和输出目标是char数组的字符流；
         * 5）StringReader/StringWriter：输入源和输出目标是String的字符流；
         * 6）BufferedReader/BufferedWriter：装饰类，对输入/输出流提供缓冲，以及按行读写功能；
         * 7）PrintWriter：装饰类，可将基本类型和对象转换为其字符串形式输出的类。
         */
    }

    /**
     * 基本概念
     */
    public static void baseConcept(){
        // 1．文本文件
        // 之前提到，处理文件要有二进制思维。从二进制的角度，通过一个简单的例子解释下文本文件与二进制文件的区别。
        // 比如，要存储整数123，使用二进制形式保存到文件ctest.dat，代码为：
        String path = "D:\\Wstudy\\ctest.dat";
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(path))) {
            output.writeInt(123);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 使用UltraEdit打开该文件，显示的却是：{
        // 对int类型，二进制文件保存的直接就是int的二进制形式。这个二进制形式，如果当成字符来解释，显示成什么字符则与编码有关，
        // 如果当成UTF-32BE编码，解释成的就是一个字符，即{
        // 如果使用文本文件保存整数123，则代码为：
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            String data = "123";
            fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }
        // 代码将整数123转换为字符串，然后将它的UTF-8编码输出到了文件中，使用Ultra-Edit打开该文件，显示的就是期望的：123

        /*
         * 2. 编码
         * 在文本文件中，编码非常重要，同一个字符，不同编码方式对应的二进制形式可能是不一样的。
         *
         * 3. 字符流
         * 字节流是按字节读取的，而字符流则是按char读取的，一个char在文件中保存的是几个字节与编码有关，但字符流封装了这种细节，
         * 我们操作的对象就是char。
         * 需要说明的是，一个char不完全等同于一个字符，对于绝大部分字符，一个字符就是一个char，
         * 但之前介绍过，对于增补字符集中的字符，需要两个char表示，对于这种字符，Java中的字符流是按char而不是一个完整字符处理的。
         */
    }

    /**
     * {@link java.io.Reader} 和 {@link java.io.Writer}
     */
    public static void readerAndWriter(){
        /*
         * Reader与字节流的InputStream类似，也是抽象类，部分主要方法有：
         * public int read() throws IOException
         * public int read(char cbuf[]) throws IOException
         * abstract public int read(char cbuf[], int off, int len) throws IOException
         * abstract public void close() throws IOException
         * public long skip(long n) throws IOException
         * public boolean ready() throws IOException
         *
         * 方法的名称和含义与InputStream中的对应方法基本类似，但Reader中处理的单位是char，比如read读取的是一个char，取值范围为0～65535。
         * Reader没有available方法，对应的方法是ready()。
         *
         * Writer与字节流的OutputStream类似，也是抽象类，部分主要方法有：
         * public void write(int c) throws IOException
         * public void write(char cbuf[]) throws IOException
         * abstract public void write(char cbuf[], int off, int len) throws IOException;
         * public void write(String str) throws IOException
         * public Writer append(CharSequence csq) throws IOException
         * abstract public void flush() throws IOException;
         * abstract public void close() throws IOException;
         * 含义与OutputStream的对应方法基本类似，但Writer处理的单位是char, Writer还接受String类型，
         * String的内部就是char数组，处理时，会调用String的getChar方法先获取char数组。
         */

        String path = "D:\\Wstudy\\ctest.dat";
    }

    /**
     * {@link java.io.InputStreamReader} 和 {@link java.io.OutputStreamWriter}
     */
    public static void inputStreamReader(){
        /*
         * InputStreamReader和OutputStreamWriter是适配器类，能将InputStream/OutputStream转换为Reader/Writer。
         * （字节流转换为字符流）
         *
         * 1. OutputStreamWriter
         * 主要构造方法为：
         * public OutputStreamWriter(OutputStream out)
         * public OutputStreamWriter(OutputStream out, Charset cs)
         * 一个重要的参数是编码类型，可以通过名字charsetName或Charset对象传入，
         * 如果没有传入，则为系统默认编码，默认编码可以通过Charset.defaultCharset()得到。
         * OutputStreamWriter内部有一个类型为StreamEncoder的编码器，能将char转换为对应编码的字节。
         *
         * 2. InputStreamReader
         * 主要构造方法为：
         * public InputStreamReader(InputStream in)
         * public InputStreamReader(InputStream in, Charset cs)
         * 与OutputStreamWriter一样，一个重要的参数是编码类型。
         * InputStreamReader内部有一个类型为StreamDecoder的解码器，能将字节根据编码转换为char。
         */

        String path = "D:\\Wstudy\\ctest.dat";
        try (OutputStreamWriter streamWriter =
                     new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            String str = "OutputStreamWriter test";
            streamWriter.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }


        try (InputStreamReader streamReader
                     = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            char[] charBuf = new char[1024];
            int readChars = streamReader.read(charBuf);
            System.out.println(new String(charBuf, 0, readChars));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * {@link FileReader}和{@link FileWriter}
     */
    public static void fileReaderAndWriter(){
        /*
         * FileReader/FileWriter的数据源和目的媒介是文件。
         *
         * FileReader是InputStreamReader的子类，它的主要构造方法有：
         * public FileReader(File file) throws FileNotFoundException
         * public FileReader(String fileName) throws FileNotFoundException
         *
         * FileWriter是OutputStreamWriter的子类，它的主要构造方法有：
         * public FileWriter(String fileName) throws IOException
         * public FileWriter(String fileName, boolean append) throws IOException
         * public FileWriter(File file, boolean append) throws IOException
         *
         *
         * append参数指定是追加还是覆盖，如果没传，则为覆盖。
         * 需要注意的是，FileReader/FileWriter不能指定编码类型，只能使用默认编码，
         * 如果需要指定编码类型，可以使用InputStreamReader/OutputStreamWriter。
         */
        String path = "D:\\Wstudy\\ctest.dat";
        try (FileReader reader = new FileReader(new File(path));
             FileWriter writer = new FileWriter(path)) {
            int read = reader.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * {@link CharArrayReader}和{@link CharArrayWriter}
     */
    public static void  charArrayReaderAndWriter(){
        /*
         * CharArrayWriter与ByteArrayOutputStream类似，它的输出目标是char数组，这个数组的长度可以根据数据内容动态扩展。
         *
         * CharArrayWriter有如下方法，可以方便地将数据转换为char数组或字符串：
         * public char[] toCharArray()
         * public String toString()
         *
         * CharArrayReader与上节介绍的ByteArrayInputStream类似，它将char数组包装为一个Reader，是一种适配器模式，它的构造方法有：
         * public CharArrayReader(char buf[])
         * public CharArrayReader(char buf[], int offset, int length)
         */
        String path = "D:\\Wstudy\\ctest.dat";
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
             CharArrayWriter charArrayWriter = new CharArrayWriter()) {
            char[] charBuf = new char[1024];
            int readChars;
            while ((readChars=reader.read(charBuf)) != -1){
                charArrayWriter.write(charBuf, 0, readChars);
            }
            System.out.println(charArrayWriter.size());
            String str = charArrayWriter.toString();
            System.out.println(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * {@link StringReader} 和 {@link StringWriter}
     */
    public static void stringReaderAndWriter(){
        /*
         * StringReader/StringWriter与CharArrayReader/CharArrayWriter类似，
         * 只是输入数据源为String，输出目标为StringBuffer，
         * 而且，String/StringBuffer内部是由char数组组成的，所以它们本质上是一样的。
         * 之所以要将char数组和String与Reader/Writer进行转换，也是为了能够方便地参与Reader/Writer构成的协作体系，复用代码。
         *
         */

        String path = "D:\\Wstudy\\ctest.dat";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path),StandardCharsets.UTF_8);
             StringWriter stringWriter = new StringWriter()) {
            char[] charBuf = new char[1024];
            int readChars;
            while ((readChars=reader.read(charBuf)) != -1){
                stringWriter.write(charBuf, 0, readChars);
            }
            System.out.println(stringWriter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * {@link BufferedReader} 和 {@link BufferedWriter}
     */
    public static void bufferedReaderAndWriter(){
        /*
         * BufferedReader/BufferedWriter是装饰类，提供缓冲，以及按行读写功能。
         *
         * BufferedWriter的构造方法有：
         * public BufferedWriter(Writer out)
         * public BufferedWriter(Writer out, int sz)
         * 参数sz是缓冲大小，如果没有提供，默认为8192。
         * 它有如下方法，可以输出平台特定的换行符：
         * public void newLine()
         *
         * BufferedReader的构造方法有：
         * public BufferedReader(Reader in)
         * public BufferedReader(Reader in, int sz)
         * 参数sz是缓冲大小，如果没有提供，默认为8192。
         * 它有如下方法，可以读入一行：
         * public String readLine() throws IOException
         * 字符'\r'或'\n'或'\r\n'被视为换行符，readLine返回一行内容，但不会包含换行符，当读到流结尾时，返回null。
         *
         * FileReader/FileWriter是没有缓冲的，也不能按行读写，所以，一般应该在它们的外面包上对应的缓冲类。
         *
         */
    }

    /**
     * {@link PrintWriter}
     */
    public static void printWriterStudy(){
    }
}





































































