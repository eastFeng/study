package com.dongfeng.study.basicstudy.io;

import com.dongfeng.study.util.IoUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * <b> 二进制文件和字节流 </b>
 *
 * @author eastFeng
 * @date 2021-04-29 19:51
 */
public class ByteStream {
    public static void main(String[] args) {
        // Java IO中流的概念
        ioStream();

        // 字节流
        binaryStream();

        // InputStream
        inputStream();

        // OutputSteam
        outputStream();

        // FileInputStream和FileOutputStream
//        fileInputAndOutputStream();

        // ByteArrayInputStream 和 ByteArrayOutputStream
//        byteArrayInputAndOutputStream();

        // DataInputStream 和 DataOutputStream
//        dataInputAndOutputStream();

        // BufferedInputStream 和 BufferedOutputStream
//        bufferedInputAndOutputStream();

        byte[] bytes = new byte[10];
        for (int i=0; i<bytes.length; i++){
            System.out.println("byte is :"+bytes[i]+"LLLL");
        }
    }

    /**
     * Java IO中流的概念
     */
    public static void ioStream(){
        /*
         * 在Java IO中，流是一个核心的概念。
         * 【流从概念上来说是一个连续的数据流】。【既可以从流中读取数据，也可以往流中写数据。】
         * 在Java IO中流既可以是字节流(以字节为单位进行读写)，也可以是字符流(以char为单位进行读写)。
         *
         * 流与数据源或者数据流向的媒介相关联：
         * 输入流（InputStream或Reader）是和数据源相关的，输出流（OutputStream或Writer）是和目标媒介相关的。
         * 一个程序需要InputStream或者Reader从数据源读取数据，需要OutputStream或者Writer将数据写入到目标媒介中。
         * 如下所示：
         * Source ---> InputStream/Reader ---> Program
         * Program ---> OutputStream/Write ---> Destination
         */
    }

    /**
     * 字节流
     */
    public static void binaryStream(){
        /*
         * 以【二进制方式】读写的主要流有：
         * 1. InputStream/OutputStream：这是基类，它们是抽象类。
         * 2. FileInputStream/FileOutputStream：输入源和输出目标是文件的流。
         * 3. ByteArrayInputStream/ByteArrayOutputStream：输入源和输出目标是字节数组的流。
         * 4. DataInputStream/DataOutputStream：装饰类，按基本类型和字符串而非只是字节读写流。
         * 5. BufferedInputStream/BufferedOutputStream：装饰类，对输入输出流提供缓冲功能。
         *
         */

        /*
         * 装饰器设计模式
         * 基本的流按字节读写，没有缓冲区，这不方便使用。
         * Java解决这个问题的方法是使用装饰器设计模式，引入了很多装饰类，对基本的流增加功能，以方便使用。
         * 一般一个类只关注一个方面，实际使用时，经常会需要多个装饰类。
         * Java中有很多装饰类，有两个基类：过滤器输入流FilterInputStream和过滤器输出流FilterOutputStream。
         * 过滤类似于自来水管道，流入的是水，流出的也是水，功能不变，或者只是增加功能。
         * 它有很多子类，这里列举一些：
         * 1）对流起缓冲装饰的子类是BufferedInputStream和BufferedOutputStream。
         * 2）可以按8种基本类型和字符串对流进行读写的子类是DataInputStream和DataOutput-Stream。
         * 3）可以对流进行压缩和解压缩的子类有GZIPInputStream、ZipInputStream、GZIPOutput-Stream和ZipOutputStream。
         * 4）可以将基本类型、对象输出为其字符串表示的子类有PrintStream。
         *
         * 众多的装饰类使得整个类结构变得比较复杂，完成基本的操作也需要比较多的代码；
         * 其优点是非常灵活，在解决某些问题时也很优雅。
         */
    }

    /**
     * {@link InputStream}
     */
    public static void inputStream() {
        /*
         * InputStream是字节输入流的基类，是抽象类。
         *
         * InputStream主要方法有：
         * 1. public int read() throws IOException;
         * 【从输入流中读取】下一个字节的数据。返回类型是int，但取值是0~255（11111111=2^8-1=255）。
         * 如果由于到达流的结尾而没有字节可用，则返回值-1。
         * 此方法会阻塞，直到数据到来、检测到流结束或引发异常为止。
         * read是一个抽象方法，具体子类必须实现。
         * IOException是一个受检异常，调用者必须进行处理。
         *
         * 2. public int read(byte b[]) throws IOException
         * 该方法可以一次读取多个字节。（批量读取）
         * 【读取的字节放入参数的字节数组b中】，第一个字节存入b[0]，第二个存入b[1]，以此类推，
         * 一次最多读入的字节个数为数组b的长度，但实际读入的个数可能小于数组长度，【返回值为实际读入的字节个数】。
         * 如果刚开始读取时已到流结尾，则返回-1；否则，只要数组长度大于0，该方法都会尽力至少读取一个字节，
         * 如果流中一个字节都没有，它会阻塞，异常出现时也是抛出IOException。
         * 该方法不是抽象方法，InputStream有一个默认实现，主要就是循环调用读一个字节的read方法，
         * 但子类如FileInputStream往往会提供更为高效的实现。
         *
         * 3. public int read(byte b[], int off, int len) throws IOException
         * 这是批量读取更为通用的方法。
         * 读入的第一个字节放入b[off]，最多读取len个字节，InputStream中read(byte b[])就是调用了该方法。
         *
         * 4. public void close() throws IOException
         * 关闭流，以释放相关资源。
         * 不管read方法是否抛出了异常，都应该调用close方法，所以close方法通常应该放在finally语句内。
         * close方法自己可能也会抛出IOException，但通常可以捕获并忽略。
         *
         * InputStream的高级方法：
         * public long skip(long n) throws IOException
         * public int available() throws IOException
         * public synchronized void mark(int readlimit)
         * public synchronized void reset() throws IOException
         * public boolean markSupported()
         *
         * skip跳过输入流中n个字节，因为输入流中剩余的字节个数可能不到n，所以返回值为实际略过的字节个数。
         * InputStream的默认实现就是尽力读取n个字节并扔掉，子类往往会提供更为高效的实现，FileInputStream会调用本地方法。
         * 在处理数据时，对于不感兴趣的部分，skip往往比读取然后扔掉的效率要高。
         *
         * available返回下一次不需要阻塞就能读取到的大概字节个数。
         * InputStream的默认实现是返回0，子类会根据具体情况返回适当的值，FileInputStream会调用本地方法。
         * 在文件读写中，这个方法一般没什么用，但在从网络读取数据时，可以根据该方法的返回值在网络有足够数据时才读，以避免阻塞。
         *
         * 一般的流读取都是一次性的，且只能往前读，不能往后读，但有时可能希望能够先看一下后面的内容，根据情况再重新读取。
         * 比如，处理一个未知的二进制文件，不确定它的类型，但可能可以通过流的前几十个字节判断出来，
         * 判读出来后，再重置到流开头，交给相应类型的代码进行处理。
         *
         * InputStream定义了三个方法：mark、reset、markSupported，用于支持从读过的流中重复读取。
         * 怎么重复读取呢？
         * 先使用mark()方法将当前位置标记下来，在读取了一些字节，希望重新从标记位置读时，调用reset方法。
         * 能够重复读取不代表能够回到任意的标记位置，mark方法有一个参数readLimit，
         * 表示在设置了标记后，能够继续往后读的最多字节数，如果超过了，标记会无效。
         * 为什么会这样呢？因为之所以能够重读，是因为流能够将从标记位置开始的字节保存起来，而保存消耗的内存不能无限大，流只保证不会小于readLimit。
         *
         * 不是所有流都支持mark、reset方法，是否支持可以通过markSupported的返回值进行判断。
         * InputStream的默认实现是不支持，FileInputStream也不直接支持，但BufferedInputStream和ByteArrayInputStream可以支持。
         */
    }

    /**
     * {@link OutputStream}
     */
    public static void outputStream(){
        /*
         * OutputStream是字节输出流的基类，也是抽象类。
         *
         * OutputStream的基本方法有：
         * public abstract void write(int b) throws IOException;
         * 【向流中写入】一个字节，参数类型虽然是int，但其实只会用到最低的8位。
         * 这个方法是抽象方法，具体子类必须实现，FileInputStream会调用本地方法。
         *
         * OutputStream还有两个批量写入的方法：
         * public void write(byte b[]) throws IOException
         * public void write(byte b[], int off, int len) throws IOException
         * 第一个方法中，会把整个字节数组b写入输出流，无论数组b中的每一个元素是否都有设置值，因为就算没有设置值，会有默认值0，
         * 所以，除非确定数组b中每个元素都已经赋值，否则尽量选用第二个方法。
         * 在第二个方法中，第一个写入的字节是b[off]，写入个数为len，最后一个是b[off+len-1]，第一个方法等同于调用write(b, 0, b.length)。
         * OutputStream的默认实现是循环调用单字节的write()方法，子类往往有更为高效的实现，FileOutputStream会调用对应的批量写本地方法。
         *
         * OutputStream还有两个方法：
         * public void flush() throws IOException
         * public void close() throws IOException
         * flush方法将缓冲而未实际写的数据进行实际写入，
         * 比如，在BufferedOutputStream中，调用flush方法会将其缓冲区的内容写到其装饰的流中，并调用该流的flush方法。
         * 基类OutputStream没有缓冲，flush方法代码为空。
         * close方法一般会首先调用flush方法，然后再释放流占用的系统资源。同InputStream一样，close方法一般应该放在finally语句内。
         */

        // OutputStream第一个write方法测试
        byte[] bytes = new byte[1024];
//        for(int i=0; i<bytes.length; i++){
//            bytes[i] = 1;
//        }
        String path = "D:\\Wstudy\\JavaIOTest.txt";
        try (FileOutputStream output = new FileOutputStream(path, true)) {
            System.out.println("bytes length:"+bytes.length);
            output.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * {@link FileInputStream}和{@link FileOutputStream}
     */
    public static void fileInputAndOutputStream(){
        /*
         * FileInputStream和FileOutputStream的数据源和目标媒介都是文件。
         *
         * 1. FileOutputStream
         * FileOutputStream有多个构造方法，其中两个为：
         * public FileOutputStream(String name) throws FileNotFoundException
         * public FileOutputStream(String name, boolean append) throws FileNotFoundException
         * File类型的参数file和字符串的类型的参数name都表示文件路径，路径可以是绝对路径，也可以是相对路径，
         * 如果文件已存在，append参数指定是追加还是覆盖，true表示追加， false表示覆盖，
         * 第二个构造方法没有append参数，表示覆盖。
         *
         * new一个FileOutputStream对象会实际打开文件，操作系统会分配相关资源。
         * 如果当前用户没有写权限，会抛出异常SecurityException，它是一种RuntimeException。
         * 如果指定的文件是一个已存在的目录，或者由于其他原因不能打开文件，会抛出异常FileNotFoundException，它是IOException的一个子类。
         *
         * 2. FileInputStream
         * FileInputStream的主要构造方法有：
         * public FileInputStream(String name) throws FileNotFoundException
         * public FileInputStream(File file) throws FileNotFoundException
         * 参数与FileOutputStream类似，可以是文件路径或File对象，但必须是一个已存在的文件，不能是目录。
         *
         * new一个FileInputStream对象也会实际打开文件，操作系统会分配相关资源，
         * 如果文件不存在，会抛出异常FileNotFoundException，
         * 如果当前用户没有读的权限，会抛出异常SecurityException。
         */

        // 字节输入流，从数据源中读取数据
        FileInputStream input = null;
        // 字节输出流，往目标媒介中写入数据
        FileOutputStream output = null;
        String path = "D:\\Wstudy\\JavaIOTest.txt";
        try {
            // 新建一个文件字节输入流
            input = new FileInputStream(path);
            // 新建一个文件字节输出流
            output = new FileOutputStream(path, true);

            // OutputStream只能以byte或byte数组写文件，
            // 为了写字符串，需要调用String的getBytes方法得到它的UTF-8编码的字节数组，再调用write()方法
            String data = "Hello World!";
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            System.out.println(Arrays.toString(bytes));
            output.write(bytes);

            // 文件中的内容读到内存并输出
            // 从输入流中读取的字节放入buf数组中，一次最多读取1024个字节
            byte[] buf = new byte[1024];
            int bytesRead = 0;
            StringBuilder sb = new StringBuilder();
            // 循环读取保证读完
            while ((bytesRead=input.read(buf)) != -1){
                System.out.println("读取的字节数："+bytesRead);
                // 读入到的是byte数组，使用String的带编码参数的构造方法将其转换为了String
                String s = new String(buf, 0, bytesRead, StandardCharsets.UTF_8);
                sb.append(s);
            }
            System.out.println(sb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * {@link ByteArrayInputStream} 和 {@link ByteArrayOutputStream}
     */
    public static void byteArrayInputAndOutputStream(){
        /*
         * ByteArrayInputStream和ByteArrayOutputStream的数据源和目标媒介都是字节数组。
         * ByteArrayInputStream相当于适配器，ByteArrayOutputStream作为输出封装了动态数组，便于使用。
         *
         * 1. ByteArrayOutputStream
         * ByteArrayOutputStream的输出目标媒介是其内部的byte数组buf，这个数组的长度是根据数据内容动态扩展的。
         * 它有两个实例变量：
         * protected byte buf[];  // 存储数据的缓冲区。这就是ByteArrayOutputStream的输出的目标媒介。
         * protected int count;   // 缓冲区中的有效字节数。就是已经写入的实际字节数量。
         *
         * ByteArrayOutputStream有两个构造方法：
         * public ByteArrayOutputStream()
         * public ByteArrayOutputStream(int size)
         * 第二个构造方法中的size指定的就是初始的数组大小，如果没有指定，则长度为32。
         * 在调用write方法的过程中，如果数组大小不够，会进行扩展，扩展策略同样是指数扩展，每次至少增加一倍。
         *
         * ByteArrayOutputStream有如下方法，可以方便地将数据转换为字节数组或字符串：
         * public synchronized byte toByteArray()[]
         * public synchronized String toString(String charsetName)
         * public synchronized String toString()
         *
         * public synchronized int size()  ： 返回已经写入的字节个数count
         * public synchronized void reset() ：重置字节个数count为0
         */

        String path = "D:\\Wstudy\\JavaIOTest.txt";
        // 使用ByteArrayOutputStream，我们可以改进前面的读文件代码，确保将所有文件内容读取：
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             FileInputStream input = new FileInputStream(path)) {

            // 每次从输入流中读取的数据先写入ByteArrayOutputStream中，读完后，再调用其toString方法获取完整数据。

            byte[] buf = new byte[1024];
            int bytesRead = 0;
            // 循环读取保证读完
            while ((bytesRead=input.read(buf)) != -1){
                // 将从输入流中读取到的字节写入输出流
                System.out.println("读取的字节数：" + bytesRead);
                output.write(buf);
            }
            String s = output.toString(StandardCharsets.UTF_8.name());
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
         * ByteArrayInputStream将byte数组包装为一个输入流，是一种适配器模式。
         * 它的构造方法有：
         * public ByteArrayInputStream(byte buf[])
         * public ByteArrayInputStream(byte buf[], int offset, int length)
         * 第二个构造方法以buf中offset开始的length个字节为背后的数据。
         * ByteArrayInputStream的所有数据都在内存，支持mark/reset重复读取。
         *
         * 为什么要将byte数组转换为InputStream呢？
         * 这与容器类中要将数组、单个元素转换为容器接口的原因是类似的，有很多代码是以InputStream/OutputSteam为参数构建的，
         * 它们构成了一个协作体系，将byte数组转换为InputStream可以方便地参与这种体系，复用代码。
         *
         */
        byte[] buf = new byte[1024];
        ByteArrayInputStream input = new ByteArrayInputStream(buf);
    }

    /**
     * {@link DataInputStream} 和 {@link DataOutputStream}
     */
    public static void dataInputAndOutputStream(){
        /*
         * 上面介绍的类都只能以字节为单位读写，如何以其他类型读写呢？
         * 比如int、double。可以使用DataInputStream/DataOutputStream，这俩都是装饰类。
         *
         * 1. DataOutputStream
         * DataOutputStream是装饰类基类FilterOutputStream的子类，FilterOutputStream是OutputStream的子类，
         * FilterOutputStream的构造方法是：
         * public FilterOutputStream(OutputStream out)
         * 它接受一个已有的OutputStream，基本上将所有操作都代理给了它。
         * DataOutputStream实现了DataOutput接口，可以以各种基本类型和字符串写入数据，部分方法如下：
         * public final void writeBoolean(boolean v) throws IOException {
         *     out.write(v ? 1 : 0);
         *     incCount(1);
         * }
         * public final void writeChar(int v) throws IOException
         * public final void writeUTF(String str) throws IOException
         * 在写入时，DataOutputStream会将这些类型的数据转换为其对应的二进制字节：
         * 1）writeBoolean：写入一个字节，如果值为true，则写入1，否则0。
         * 2）writeInt：写入4个字节，最高位字节先写入，最低位最后写入。
         * 3）writeUTF：将字符串的UTF-8编码字节写入，这个编码格式与标准的UTF-8编码略有不同，不过，我们不用关心这个细节。
         *
         * 与FilterOutputStream一样，DataOutputStream的构造方法也是接受一个已有的Output-Stream：
         * public DataOutputStream(OutputStream out) {
         *      super(out);
         * }
         */
        String path = "D:\\Wstudy\\test.txt";
        try (FileOutputStream fileOutputStream = new FileOutputStream(path, true);
             DataOutputStream output = new DataOutputStream(fileOutputStream)) {
            output.writeBoolean(Boolean.TRUE);
            output.writeBoolean(Boolean.FALSE);
            output.writeChar('A');
            output.writeInt(15646141);
            output.writeUTF("writeUTF");
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
         * DataInputStream是装饰类基类FilterInputStream的子类，FilterInputStream是InputStream的子类。
         * DataInputStream实现了DataInput接口，可以以各种基本类型和字符串读取数据，部分方法有：
         * public final boolean readBoolean() throws IOException
         * public final char readChar() throws IOException
         * public final float readFloat() throws IOException
         * ...
         * 在读取时，DataInputStream会先按字节读进来，然后转换为对应的类型。
         *
         * DataInputStream的构造方法接受一个InputStream：
         * public DataInputStream(InputStream in) {
         *    super(in);
         * }
         *
         * 使用DataInputStream/DataOutput-Stream读写对象，非常灵活，但比较麻烦，所以Java提供了序列化机制。
         */
    }

    /**
     * {@link BufferedInputStream} 和 {@link BufferedOutputStream}
     */
    public static void bufferedInputAndOutputStream(){
        /*
         * FileInputStream/FileOutputStream是没有缓冲的，按单个字节读写时性能比较低，虽然可以按字节数组读取以提高性能，
         * 但有时必须要按字节读写，怎么解决这个问题呢？
         * 方法是将文件流包装到缓冲流中。BufferedInputStream内部有个字节数组作为缓冲区，
         * 读取时，先从这个缓冲区读，缓冲区读完了再调用包装的流读，它的构造方法有两个：
         * public BufferedInputStream(InputStream in) {
         *     this(in, DEFAULT_BUFFER_SIZE);
         * }
         * public BufferedInputStream(InputStream in, int size) {
         *     super(in);
         *     if (size <= 0) {
         *         throw new IllegalArgumentException("Buffer size <= 0");
         *     }
         *     buf = new byte[size];
         * }
         * size表示缓冲区大小，如果没有，默认值为8192。除了提高性能，BufferedInputStream也支持mark/reset，可以重复读取。
         * 与BufferedInputStream类似，BufferedOutputStream的构造方法也有两个，默认的缓冲区大小也是8192，
         * 它的flush方法会将缓冲区的内容写到包装的流中。
         *
         * 在使用FileInputStream/FileOutputStream时，应该几乎总是在它的外面包上对应的缓冲类。
         *
         */
        String path = "D:\\Wstudy\\JavaIOTest.txt";
        try (BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(path))) {
            System.out.println("test");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 实用方法
     */
    public static void practicalMethod(){
        /*
         * 可以看出，即使只是按二进制字节读写流，Java也包括了很多的类，虽然很灵活，但对于一些简单的需求，却需要写很多代码。
         * 实际开发中，经常需要将一些常用功能进行封装，提供更为简单的接口。
         */

        try {
            String path = "D:\\Wstudy\\test.txt";
            FileInputStream input = new FileInputStream(path);
            FileOutputStream output = new FileOutputStream(path, true);

            // 复制输入流的内容到输出流
            long copy = IoUtil.copy(input, output);
            System.out.println(copy);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }



}
