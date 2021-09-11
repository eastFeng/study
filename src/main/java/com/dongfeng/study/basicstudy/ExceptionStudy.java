package com.dongfeng.study.basicstudy;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dongfeng.study.bean.base.BizException;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import java.io.PrintStream;

/**
 * <b> Java异常学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-20 18:06
 */
public class ExceptionStudy {
    public static void main(String[] args) throws Exception {
        // Java异常示例
        exceptionTest();
        tryCatchTest("123");

        // 异常分类
        exceptionType();

        // 自定义异常
        BizException bizException = new BizException(ResponseCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);

        // 获取异常信息
        getExceptionInfo(bizException);

        // 关于finally，test目录下的TryCatchTest类，有经典示例

        // 如何处理异常
        handleException();

        // assert
        assertTest();
    }

    public static void exceptionTest(){
        String s = null;
        int a = s.indexOf("a");
        System.out.println(a);
        /*
         * 当执行s.indexOf("a")的时候，Java虚拟机发现s的值为null，没有办法继续执行了，这时就启用异常处理机制，
         * 首先创建一个异常对象，这里是类NullPointerException的对象，然后查找看谁能处理这个异常，
         * 在示例代码中，没有代码能处理这个异常，因此Java启用默认处理机制，即打印异常栈信息到屏幕，并退出程序。
         *
         * 异常处理机制会从当前函数开始查找看谁“捕获”了这个异常，当前函数没有就查看上一层，直到主函数，
         * 如果主函数也没有，就使用默认机制，即输出异常栈信息并退出，这正是我们在屏幕输出中看到的。
         */
    }

    public static void tryCatchTest(String intStr){
        try {
            // try后面的花括号{}内包含可能抛出异常的代码

            int anInt = Integer.parseInt(intStr);
            System.out.println("输入数字为："+anInt);

            // catch语句包含能捕获的异常和处理代码
        } catch (NumberFormatException | NullPointerException e) {
            // catch后面括号内是异常信息，包括异常类型和变量名，这里是NumberFormatException e，通过它可以获取更多异常信息

            // 花括号{}内是处理代码，这里输出了一个更为友好的提示信息。
            System.out.println("参数"+intStr +"不是有效的数字，请输入有效的数字");
        }

        // 捕获异常后，程序就不会异常退出了，但try语句内异常点之后的其他代码就不会执行了，
        // 执行完catch内的语句后，程序会继续执行catch花括号外的代码。

        /*
         * 如果一个方法声明将会抛出一个异常，而这个异常是某个特定类的实例时，
         * 则这个方法就有可能抛出一个这个类的异常，或者这个类的任意一个子类的异常。
         * 例如，FileInputStream构造器声明将有可能抛出一个IOException异常，然而并不知道具体是哪种IOException异常。
         * 它既可能是IOException异常，也可能是其子类的异常，例如，FileNotFoundException。
         *
         * 捕获异常也是如此。可以捕获一个这个类的异常，或者这个类的任意一个子类的异常。
         *
         * 异常处理机制将根据抛出的异常类型找第一个匹配的catch块，找到后，执行catch块内的代码，不再执行其他catch块，
         * 如果没有找到，会继续到上层方法中查找。需要注意的是，抛出的异常类型是catch中声明异常的子类也算匹配，
         * 所以需要将最具体的子类放在前面，如果基类Exception放在前面，则其他更具体的catch代码将得不到执行。
         *
         * 在catch块内处理完后，可以重新抛出异常，异常可以是原来的，也可以是新建的。
         */

        /*
         * 如果在try语句块中的任何代码抛出了一个在catch子句中说明的异常类，那么:
         * 1）程序将跳过try语句块的其余代码。
         * 2）程序将执行catch子句中的处理器代码。
         * 如果在try语句块中的代码没有抛出任何异常，那么程序将跳过catch子句。
         *
         *
         */
    }

    /**
     * 异常分类
     *
     * <p> throws：声明异常
     * @exception ClassNotFoundException 没找到类
     */
    public static void exceptionType() throws ClassNotFoundException {
        /*
         *
         * 以Throwable为根，Java定义了非常多的异常类，表示各种类型的异常。
         *
         * Throwable是所有异常的基类，它有两个子类：Error和Exception。
         *
         * Error表示系统错误或资源耗尽，由Java系统自己使用，应用程序不应抛出和处理，这种情况很少出现。
         * 比如虚拟机错误（VirtualMacheError）及其子类内存溢出错误（OutOfMemory-Error）和栈溢出错误（StackOverflowError）。
         *
         * Exception表示应用程序错误，它有很多子类，应用程序也可以通过继承Exception或其子类创建自定义异常，
         * JDK中它有三个直接子类：IOException（输入输出I/O异常）、RuntimeException（运行时异常）、SQLException（数据库SQL异常）。
         *
         * RuntimeException比较特殊，它的名字有点误导，因为其他异常也是运行时产生的，它表示的实际含义是未受检异常（unchecked exception），
         * 相对而言，Exception的其他子类和Exception自身则是受检异常（checked exception）,Error及其子类也是未受检异常。
         *
         * Java语言规范将派生于Error类或RuntimeException类的所有异常称为非受查（unchecked）异常，所有其他的异常称为受查（checked）异常。
         *
         * 受检（checked）和未受检（unchecked）的区别在于Java如何处理这两种异常：
         * 对于受检异常，Java会强制要求程序员进行处理，否则会有编译错误，而对于未受检异常（RuntimeException）则没有这个要求。
         * 未受检异常和受检异常的区别如下：受检异常必须出现在throws语句中，调用者必须处理，Java编译器会强制这一点，而未受检异常则没有这个要求。
         *
         * 未受检异常表示编程的逻辑错误，编程时应该检查以避免这些错误，
         * 比如空指针异常，如果真的出现了这些异常，程序退出也是正常的，程序员应该检查程序代码的bug而不是想办法处理这种异常。
         * 受检异常表示程序本身没问题，但由于I/O、网络、数据库等其他不可预测的错误导致的异常，调用者应该进行适当处理。
         *
         *
         * 派生于RuntimeException的异常包含下面几种情况：
         * ● 错误的类型转换。
         * ● 数组访问越界。
         * ● 访问null指针。
         * 不是派生于RuntimeException的异常包括：
         * ● 试图在文件尾部后面读取数据。
         * ● 试图打开一个不存在的文件。
         * ● 试图根据给定的字符串查找Class对象，而这个字符串表示的类并不存在。
         *
         * “如果出现RuntimeException异常，那么就一定是你的问题”是一条相当有道理的规则。
         *
         * 下面4种情况时应该抛出异常：
         * 1）调用一个抛出受查异常的方法
         * 2）程序运行过程中发现错误，并且利用throw语句抛出一个受查异常
         * 3）程序出现错误，例如，a[-1]=0会抛出一个ArrayIndexOutOfBoundsException这样的非受查异常。
         * 4）Java虚拟机和运行时库出现的内部错误。
         *
         * 总之，一个方法必须声明所有可能抛出的受查异常，而非受查异常要么不可控制（Error），要么就应该避免发生（RuntimeException）。
         * 如果方法没有声明所有可能发生的受查异常，编译器就会发出一个错误消息。除了声明异常之外，还可以捕获异常。
         * 这样会使异常不被抛到方法之外，也不需要throws规范。
         *
         * 无论是受检异常还是未受检异常，无论是否出现在throws声明中，都应该在合适的地方以适当的方式进行处理。
         *
         * 如此多不同的异常类其实并没有比Throwable这个基类多多少属性和方法，大部分类在继承父类后只是定义了几个构造方法，
         * 这些构造方法也只是调用了父类的构造方法，并没有额外的操作。那为什么定义这么多不同的类呢？
         * 主要是为了名字不同。异常类的名字本身就代表了异常的关键信息，无论是抛出还是捕获异常，使用合适的名字都有助于代码的可读性和可维护性。
         *
         * 如果Java中内置的异常类不能够满足需求，用户可以创建自己的异常类。
         */

        Class<?> hhh = Class.forName("hhh");
    }

    /**
     * 获取异常信息
     */
    public static void getExceptionInfo(Exception e){
        if (e == null){
            return;
        }

        // Throwable有一些常用方法用于获取异常信息：

        // 打印异常栈信息到标准错误输出流
        e.printStackTrace();

        PrintStream printStream = null;
        // 打印栈信息到指定的流
        e.printStackTrace(printStream);

        // 获取设置的异常message
        String message = e.getMessage();

        // 获取异常的cause
        Throwable cause = e.getCause();

        // 获取异常栈每一层的信息
        StackTraceElement[] stackTrace = e.getStackTrace();

        // hutool工具类ExceptionUtil有一些比较好的方法：
        ExceptionUtil.getMessage(e);
        ExceptionUtil.stacktraceToString(e);
        ExceptionUtil.stacktraceToOneLineString(e);
    }

    /**
     * 如何处理异常
     */
    public static void handleException(){
        /*
         *
         * 异常大概可以分为三种来源：用户、程序员、第三方。
         * 用户是指用户的输入有问题；程序员是指编程错误；第三方泛指其他情况，如I/O错误、网络、数据库、第三方服务等。
         *
         * 处理的目标可以分为恢复和报告。
         *
         * 对用户，如果用户输入不对，可以提示用户具体哪里输入不对，
         * 如果是编程错误，可以提示用户系统错误、建议联系客服，
         * 如果是第三方连接问题，可以提示用户稍后重试。
         *
         * 用户输入或编程错误一般都是难以通过程序自动解决的，第三方错误则可能可以，甚至很多时候，程序都不应该假定第三方是可靠的，应该有容错机制。
         * 比如，某个第三方服务连接不上（比如发短信），可能的容错机制是换另一个提供同样功能的第三方试试，
         * 还可能是间隔一段时间进行重试，在多次失败之后再报告错误。
         *
         * 异常处理的一般逻辑：
         * 如果自己知道怎么处理异常，就进行处理；如果可以通过程序自动解决，就自动解决；如果异常可以被自己解决，就不需要再向上报告。
         * 如果自己不能完全解决，就应该向上报告。如果自己有额外信息可以提供，有助于分析和解决问题，就应该提供，可以以原异常为cause重新抛出一个异常。
         *
         *
         */
    }

    /**
     * 断言（assert）学习
     */
    public static void assertTest(){
        /*
         * 在一个具有自我保护能力的程序中，断言很常用。
         *
         * 断言机制允许在测试期间向代码中插入一些检查语句。当代码发布时，这些插入的检测语句将会被自动地移走。
         *
         * Java语言引入了关键字assert。这个关键字有两种形式：
         * 1. assert 条件;
         * 2. assert 条件:表达式;
         *
         * 这两种形式都会对条件进行检测，如果结果为false，则抛出一个AssertionError异常。
         * 在第二种形式中，表达式将被传入AssertionError的构造器，并转换成一个消息字符串。
         * “表达式”部分的唯一目的是产生一个消息字符串。AssertionError对象并不存储表达式的值，因此，不可能在以后得到它。
         *
         * 什么时候应该选择使用断言呢？请记住下面几点：
         * ● 断言失败是致命的、不可恢复的错误。
         * ● 断言检查只用于开发和测阶段（这种做法有时候被戏称为“在靠近海岸时穿上救生衣，但在海中央时就把救生衣抛掉吧”）。
         *
         * 因此，不应该使用断言向程序的其他部分通告发生了可恢复性的错误，或者，不应该作为程序向用户通告问题的手段。
         * 断言只应该用于在测试阶段确定程序内部的错误位置。
         */

        Class<?> goodsClass = ReflectStudy.getGoodsClass();
        assert goodsClass != null;

        String name = goodsClass.getName();
    }
}





































































































