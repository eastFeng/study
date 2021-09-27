package com.dongfeng.study.basicstudy.io;

import java.io.File;
import java.io.IOException;

/**
 * <b> 文件概述 </b>
 *
 * @author eastFeng
 * @date 2021-04-29 2:41
 */
public class FileOverview {
    public static void main(String[] args) {

        // 二进制思维
        binaryThinking();

        // 文件类型
        fileType();

        // 文本文件的编码
        codingOfTextFile();

        // 文件系统
        fileSystem();

        // 文件读写
        fileReadingAndWriting();


        try {
            File tempFile = File.createTempFile("0429test", ".txt");
            System.out.println(tempFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二进制思维
     */
    public static void binaryThinking(){
        /*
         * 为了透彻理解文件，我们首先要有一个二进制思维。
         * 所有文件，不论是可执行文件、图片文件、视频文件、Word文件、压缩文件、txt文件，都没什么可神秘的，
         * 它们都是以0和1的二进制形式保存的。
         * 我们所看到的图片、视频、文本，都是应用程序对这些二进制的解析结果。
         */
    }

    /**
     * 文件类型
     */
    public static void fileType(){
        /*
         * 虽然所有数据都是以二进制形式保存的，但为了方便处理数据，高级语言引入了数据类型的概念。
         * 文件处理也类似，所有文件都是以二进制形式保存的，但为了便于理解和处理文件，文件也有文件类型的概念。
         *
         * 文件类型通常以扩展名的形式体现，比如，PDF文件类型的扩展名是.pdf，图片文件的一种常见扩展名是.jpg，压缩文件的一种常见扩展名是.zip。
         * 每种文件类型都有一定的格式，代表着文件含义和二进制之间的映射关系。
         * 比如一个Word文件，其中有文本、图片、表格，文本可能有颜色、字体、字号等，doc文件类型就定义了这些内容和二进制表示之间的映射关系。
         * 有的文件类型的格式是公开的，有的可能是私有的，我们也可以定义自己私有的文件格式。
         *
         * 对于一种文件类型，往往有一种或多种应用程序可以解读它，进行查看和编辑，一个应用程序往往可以解读一种或多种文件类型。
         * 在操作系统中，一种扩展名往往关联一个应用程序，比如.doc后缀关联Word应用。
         * 用户通过双击试图打开某扩展名的文件时，操作系统查找关联的应用程序，启动该程序，传递该文件路径给它，程序再打开该文件。
         *
         * 需要说明的是，给文件加正确的扩展名是一种惯例，但并不是强制的，
         * 如果扩展名和文件类型不匹配，应用程序试图打开该文件时可能会报错。
         * 另外，一个文件可以选择使用多种应用程序进行解读，在操作系统中，一般通过右键单击文件，选择打开方式即可。
         *
         * 文件类型可以粗略分为两类：一类是文本文件；另一类是二进制文件。
         * 文本文件的例子有普通的文本文件（.txt），程序源代码文件（.java）、HTML文件（.html）等；
         * 二进制文件的例子有压缩文件（.zip）、PDF文件（.pdf）、MP3文件（.mp3）、Excel文件（.xlsx）等。
         *
         * 基本上，文本文件里的每个二进制字节都是某个可打印字符的一部分，都可以用最基本的文本编辑器进行查看和编辑，
         * 如Windows上的notepad、Linux上的vim。
         * 二进制文件中，每个字节就不一定表示字符，可能表示颜色、字体、声音大小等，
         * 如果用基本的文本编辑器打开，一般都是满屏的乱码，需要专门的应用程序进行查看和编辑。
         */
    }

    /**
     * 文本文件的编码
     */
    public static void codingOfTextFile(){
        /*
         * 对于文本文件，我们还必须注意文件的编码方式。
         * 文本文件中包含的基本都是可打印字符，但字符到二进制的映射（即编码）却有多种方式，
         * 如GB18030、UTF-8。
         *
         * 对于一个给定的文本文件，它采用的是什么编码方式呢？
         * 一般而言，我们是不知道的。
         * 那应用程序用什么编码方式进行解读呢？
         * 一般使用某种默认的编码方式，可能是应用程序默认的，也可能是操作系统默认的，
         * 当然也可能采用一些比较智能的算法自动推断编码方式。
         *
         * 对于UTF-8编码的文件，需要特别说明。
         * 有一种方式，可以标记该文件是UTF-8编码的，那就是在文件最开头加入三个特殊字节（0xEF 0xBB 0xBF），
         * 这三个特殊字节被称为BOM头，BOM是Byte Order Mark（即字节序标记）的缩写。
         *
         * 需要注意的是，不是所有应用程序都支持带BOM头的UTF-8编码文件，
         * 比如PHP就不支持BOM，如果PHP源代码文件带BOM头，PHP运行就会出错。
         * 碰到这种问题时，前面介绍的二进制思维就特别重要，不要只看文件的显示，还要看文件背后的二进制。
         */
    }

    /**
     * 文件系统
     */
    public static void fileSystem(){
        /*
         * 文件一般是放在硬盘上的，一个机器上可能有多个硬盘，但各种操作系统都会隐藏物理硬盘概念，提供一个逻辑上的统一结构。
         * 在Windows中，可以有多个逻辑盘，如C、D、E等，每个盘可以被格式化为一种不同的文件系统，常见的文件系统有FAT32和NTFS。
         * 在Linux中，只有一个逻辑的根目录，用斜线/表示。Linux支持多种不同的文件系统，如Ext2/Ext3/Ext4等。
         * 不同的文件系统有不同的文件组织方式、结构和特点，不过，一般编程时，语言和类库为我们提供了统一的API，我们并不需要关心其细节。
         *
         * 在逻辑上，Windows中有多个根目录，Linux中有一个根目录，每个根目录下有一棵子目录和文件构成的树。
         * 每个文件都有文件路径的概念，路径有两种形式：一种是绝对路径，另一种是相对路径。
         *
         * 所谓绝对路径，是从根目录开始到当前文件的完整路径，
         * 在Windows中，目录之间用反斜线分隔，如C:\code\hello.java，
         * 在Linux中，目录之间用斜线分隔，如/Users/laoma/Desktop/code/hello.java。
         * 在Java中，java.io.File类定义了一个静态变量File.separator，表示路径分隔符，编程时应使用该变量而避免硬编码。
         *
         * 所谓相对路径，是相对于当前目录而言的。
         * 在命令行终端上，通过cd命令进入的目录就是当前目录；
         * 在Java中，通过System.getProperty("user.dir")可以得到运行Java程序的当前目录。
         * 相对路径不以根目录开头，
         * 比如在Windows上，当前目录为D:\laoma，相对路径为code\hello.java，则完整路径为D:\laoma\code\hello.java。
         *
         * 每个文件除了有具体内容，还有元数据信息，如文件名、创建时间、修改时间、文件大小等。
         * 文件还有一个是否隐藏的性质。在Linux系统中，如果文件名以．开头，则为隐藏文件；
         * 在Windows系统中，隐藏是文件的一个属性，可以进行设置。
         *
         * 大部分文件系统的文件和目录具有访问权限的概念，对所有者、用户组可以有不同的权限，具体权限包括读、写、执行。
         *
         * 文件名有大小写是否敏感的概念。
         * 在Windows系统中，一般是大小写不敏感的，而Linux则一般是大小写敏感的。
         * 也就是说，同一个目录下，abc.txt和ABC.txt在Windows中被视为同一个文件，而在Linux中则被视为不同的文件。
         *
         * 操作系统中有一个临时文件的概念。临时文件位于一个特定目录，
         * 比如Windows 10中，临时文件一般位于“C:\Users\46037\AppData\Local\Temp\”;
         * Linux系统中，临时文件位于/tmp。操作系统会有一定的策略自动清理不用的临时文件。
         * 临时文件一般不是用户手工创建的，而是应用程序产生的，用于临时目的。
         *
         *
         */
        final String separator = File.separator;
        System.out.println(separator);

        try {
            final File hhhh = File.createTempFile("hhhh", ".txt");
            System.out.println(hhhh.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件读写
     */
    public static void fileReadingAndWriting(){
        /*
         * 文件是放在硬盘上的，程序处理文件需要将文件读入内存，修改后，需要写回硬盘。
         * 操作系统提供了对文件读写的基本API，不同操作系统的接口和实现是不一样的，不过，有一些共同的概念。
         * Java封装了操作系统的功能，提供了统一的API。
         *
         * 一个基本常识是：硬盘的访问延时，相比内存，是很慢的。
         * 操作系统和硬盘一般是按块批量传输，而不是按字节，以摊销延时开销，块大小一般至少为512字节，
         * 即使应用程序只需要文件的一个字节，操作系统也会至少将一个块读进来。
         * 一般而言，应尽量减少接触硬盘，接触一次，就一次多做一些事情。
         * 对于网络请求和其他输入输出设备，原则都是类似的。
         *
         * 另一个基本常识是：一般读写文件需要两次数据复制，
         * 比如读文件，需要先从硬盘复制到操作系统内核，再从内核复制到应用程序分配的内存中。
         * 操作系统运行所在的环境和应用程序是不一样的，操作系统所在的环境是内核态，应用程序是用户态，
         * 应用程序调用操作系统的功能，需要两次环境的切换，先从用户态切到内核态，再从内核态切到用户态。
         * 这种用户态/内核态的切换是有开销的，应尽量减少这种切换。
         *
         * 为了提升文件操作的效率，应用程序经常使用一种常见的策略，即使用缓冲区。
         * 读文件时，即使目前只需要少量内容，但预知还会接着读取，就一次读取比较多的内容，放到读缓冲区，
         * 下次读取时，如果缓冲区有，就直接从缓冲区读，减少访问操作系统和硬盘。
         * 写文件时，先写到写缓冲区，写缓冲区满了之后，再一次性调用操作系统写到硬盘。
         * 不过，需要注意的是，在写结束的时候，要记住将缓冲区的剩余内容同步到硬盘。
         * 操作系统自身也会使用缓冲区，不过，应用程序更了解读写模式，恰当使用往往可以有更高的效率。
         *
         * 操作系统操作文件一般有打开和关闭的概念。
         * 打开文件会在操作系统内核建立一个有关该文件的内存结构，这个结构一般通过一个整数索引来引用，这个索引一般称为文件描述符。
         * 这个结构是消耗内存的，操作系统能同时打开的文件一般也是有限的，在不用文件的时候，应该记住关闭文件。
         * 关闭文件一般会同步缓冲区内容到硬盘，并释放占据的内存结构。
         *
         * 操作系统一般支持一种称为内存映射文件的高效的随机读写大文件的方法，将文件直接映射到内存，操作内存就是操作文件。
         * 在内存映射文件中，只有访问到的数据才会被实际复制到内存，且数据只会复制一次，被操作系统以及多个应用程序共享。
         */
    }
}
