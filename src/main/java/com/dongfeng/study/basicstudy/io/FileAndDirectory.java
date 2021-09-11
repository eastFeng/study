package com.dongfeng.study.basicstudy.io;

import java.io.File;
import java.io.IOException;

/**
 * <b> 文件和目录操作 </b>
 * @author eastFeng
 * @date 2021-05-01 19:48
 */
public class FileAndDirectory {
    public static void main(String[] args) throws IOException {
        /*
         * 文件和目录操作最终是与操作系统和文件系统相关的，不同系统的实现是不一样的，
         * 但Java中的java.io.File类提供了统一的接口，底层会通过本地方法调用操作系统和文件系统的具体实现，
         * File类中的操作大概可以分为三类：文件元数据、文件操作、目录操作。
         *
         * File既可以表示文件，也可以表示目录。
         * File中的路径可以是已经存在的，也可以是不存在的。
         * 通过new新建一个File对象，不会实际创建一个文件，只是创建一个表示文件或目录的对象，
         * new之后，File对象中的路径是不可变的。
         */

        String path = "D:\\Wstudy\\ctest.dat";
        File file = new File(path);

        // 文件元数据
        fileMetadata(file);

        // 文件操作
        fileOperator(file);

    }

    /**
     * 文件元数据
     */
    public static void fileMetadata(File file) throws IOException {
        /*
         * 文件元数据主要包括文件名和路径、文件基本信息以及一些安全和权限相关的信息。
         */

        // 返回文件或目录名称，不含路径名
        String fileName = file.getName();

        // 判断file中的路径是否是绝对路径
        final boolean absolute = file.isAbsolute();

        // 返回构造File对象的完整路径名，包括路径和文件名称
        final String path = file.getPath();

        // 返回完整的绝对路径名
        final String absolutePath = file.getAbsolutePath();

        // 返回标准的完整路径名，它会去掉路径中冗余名称如：".",".."
        final File canonicalFile = file.getCanonicalFile();

        // 返回父目录路径
        final String parent = file.getParent();

        // 返回父目录的File对象
        final File parentFile = file.getParentFile();

        /**
         * File类中有4个静态变量，表示路径分隔符：
         * public static final char separatorChar = fs.getSeparator();
         * public static final String separator = "" + separatorChar;
         * public static final char pathSeparatorChar = fs.getPathSeparator();
         * public static final String pathSeparator = "" + pathSeparatorChar;
         *
         * separator和separatorChar表示文件路径分隔符，在Windows系统中，一般为'\',Linux系统中一般为'/'。
         *
         * pathSeparator和pathSeparatorChar表示多个文件路径中的分隔符，
         * 比如，环境变量PATH中的分隔符，Java类路径变量classpath中的分隔符，
         * 在执行命令时，操作系统会从PATH指定的目录中寻找命令，Java运行时加载class文件时，会从classpath指定的路径中寻找类文件。
         * 在Windows系统中，这个分隔符一般为';'，在Linux系统中，这个分隔符一般为':'。
         */

        // 文件或目录是否存在
        final boolean exists = file.exists();
        // 是否为目录
        final boolean directory = file.isDirectory();
        // 文件长度，字节数，对目录没有意义
        final long length = file.length();
        // 最后修改时间
        final long l = file.lastModified();
        // 需要说明的是，File对象没有返回创建时间的方法，因为创建时间不是一个公共概念， Linux/Unix就没有创建时间的概念。


    }

    /**
     * 文件操作
     */
    public static void fileOperator(File file) throws IOException {
        /**
         * 文件操作主要有创建、删除、重命名。
         */

        // 新建一个File对象不会实际创建文件，创建成功返回true，否则返回false，
        // 新创建的文件内容为空。如果文件已存在，不会创建。
        boolean newFile = file.createNewFile();

        // File还有两个静态方法，可以创建临时文件：
        File tempFile = File.createTempFile("dongfengTest", "txt");
        File directory = new File("\\Wstudy");
        File tempFile1 = File.createTempFile("createTempFileTest", "txt", directory);
        // 临时文件的完整路径名是系统指定的、唯一的，但可以通过参数指定前缀（prefix）、后缀（suffix）和目录（directory）。
        // prefix是必需的，且至少要三个字符；suffix如果为null，则默认为．tmp; directory如果不指定或指定为null，则使用系统默认目录。

        // File类的删除方法为：
        // final boolean delete = file.delete();
        // file.deleteOnExit();
        // delete删除文件或目录，删除成功返回true，否则返回false。
        // 如果File是目录且不为空，则delete不会成功，返回false，换句话说，要删除目录，先要删除目录下的所有子目录和文件。
        // deleteOnExit将File对象加入到待删列表，在Java虚拟机正常退出的时候进行实际删除。
    }


}
















































































