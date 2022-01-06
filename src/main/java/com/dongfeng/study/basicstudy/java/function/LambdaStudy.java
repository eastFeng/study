package com.dongfeng.study.basicstudy.java.function;

import com.dongfeng.study.basicstudy.java.otherbasic.Goods;
import com.dongfeng.study.basicstudy.java.otherbasic.InterfaceStudy;

import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 函数式编程
 *
 * @author eastFeng
 * @date 2020-12-23 17:17
 */
public class LambdaStudy {
    /**
     * λ演算
     * <ol>
     * <li> λ演算中的函数都是匿名的，没有显式的名称。比如函数 sum(x, y) = x + y 可以写成 (x, y)|-> x + y。
     * 由于函数本身仅由其映射关系来确定，函数名称实际上并没有意义。因此使用匿名也是合理的。
     * <li> λ演算中的函数都只有一个输入。有多个输入的函数可以转换成多个只包含一个输入的函数的嵌套调用。
     * 这个过程就是通常所说的柯里化（currying）。
     * 如 (x, y)|-> x + y 可以转换成 x |-> (y |-> x + y)。右边的函数的返回值是另外一个函数。这一限定简化了 λ 演算的定义。
     * </ol>
     *
     * <p> 对函数简化之后，就可以开始定义 λ演算。λ演算是基于 λ项（λ-term）的语言。λ项是 λ演算的基本单元。λ演算在 λ项上定义了各种转换规则。
     *
     *
     */
    public static void main(String[] args) {

        // 引入lambda表达式的动机
        introduceLambdaMotive();

        // 函数式接口
        functionInterface();

        // 目标类型
        targetType();

        // lambda表达式语法
        lambdaSyntax();

        // 方法引用
        methodReference();

        // 函数的复合

        //
    }

    /**
     * 引入lambda表达式的动机
     */
    public static void introduceLambdaMotive(){
        /*
         * 我们先从清单1中的代码开始谈起。该示例的功能非常简单，只是启动一个线程并输出文本到控制台。
         * 虽然该Java程序一共有6行代码，但真正有价值的只有其中的第4行。
         * 剩下的代码全部都是为了满足语法要求而必须添加的冗余代码。
         * 代码中的第1到第6行，使用java.lang.Runnable接口的实现创建了一个新的
         * java.lang.Thread对象，并调用Thread对象的start方法来启动它。Runnable接口是
         * 通过一个匿名内部类实现的。
         */

        // 清单1：传统启动线程的方式（匿名内部类实现Runnable接口）
        new Thread(new Runnable() {                                   // 1
            @Override                                                 // 2
            public void run() {                                       // 3
                System.out.println("Hello Anonymous Inner Class!");   // 4
            }                                                         // 5
        }).start();                                                   // 6

        /*
         * 从简化代码的角度出发，清单1中第 1 行和第 6 行的 new Runnable()可以被删除，
         * 因为接口类型 Runnable 可以从类 Thread 的构造方法中推断出来。
         * 第 3 和第 5 行同样可以被删除，因为方法 run 是接口 Runnable 的唯一抽象方法。把第 6 行代码作为 run方法的实现不会出现歧义。
         * 把第 1，3，5 和 6 行的代码删除掉之后，就得到了使用Lambda 表达式的实现方式，如清单 2 所示。
         * 只用一行代码就完成了清单 1 中 6 行代码完成的工作。这是令人兴奋的变化。
         * 更少的代码意味着更高的开发效率和更低的维护成本。这也是 Lambda 表达式深受欢迎的原因。
         */
        // 清单2：lambda表达式启动线程
        new Thread(()-> System.out.println("Hello Lambda Expression!")).start();

        /*
         * 在对清单 1 的代码进行简化时，我们定义了两个前提条件。
         * 第一个前提是要求接口类型，如示例中的 Runnable，可以从当前上下文中推断出来；
         * 第二个前提是要求接口中有且只有一个抽象方法。
         * 如果一个接口仅有一个抽象方法（除了来自Object 的方法之外），它被称为函数式接口（functional interface）。
         * 函数式接口的特别之处在于其实例可以通过 Lambda 表达式或方法引用来创建。
         * Java 8 的java.util.function 包中添加了很多新的函数式接口。
         * 如果一个接口被设计为函数式接口，应该添加@FunctionalInterface 注解。编译器会确保该接口确实是函数式接口。
         * 当尝试往该接口中添加新的抽象方法时，编译器会报错。
         */
    }

    /**
     * 函数式接口
     */
    public static void functionInterface(){
        /*
         * 对于只有一个抽象方法的接口，需要这种接口的对象的时候，就可以提供一个 lambda 表达式，这种接口称为函数式接口（function interface）。
         *
         * 函数式接口：
         * 1. 有且只有一个抽象方法；
         * 2. 有 N 个非抽象方法(N>=0)；
         * 3. 有 M 个静态常量(M>=0)；
         *
         * 如果一个接口被设计为函数式接口，应该添加@FunctionalInterface 注解。如果该接口中抽象方法超过一个就会报错。
         *
         * 最好把 lambda 表达式看做是一个函数，而不是一个对象, 另外要接受lambda 表达式可以传递到函数式接口。
         * lambda 表达式可以转换为接口，这一点让 lambda 表达式很有吸引力，实际上，在 Java 中，对 lambda 所做的也只能是转换为函数式接口。
         * 甚至不能把lambda表达式赋给类型为Object的变量，Object不是一个函数式接口。
         */
    }

    /**
     * 目标类型
     */
    public static void targetType(){
        /*
         * Lambda表达式没有类型信息。一个 Lambda 表达式的类型由编译器根据其上下文环境在编译时刻推断得来。
         * 举例来说，Lambda表达式 () -> System.out.println("Hello World!") 可以出现在任何要求一个函数式接口实例的上下文中，
         * 只要该函数式接口的唯一抽象方法不接受任何参数，并且返回值是 void。
         * 这可能是 Runnable 接口，也可能是来自第三方库或应用代码的其他函数式接口。
         * 由上下文环境所确定的类型称为目标类型。
         * Lambda 表达式在不同的上下文环境中可以有不同的类型。
         * 类似 Lambda表达式这样，类型由目标类型确定的表达式称为多态表达式（poly expression）。
         *
         * Lambda表达式的语法很灵活。它们的声明方式类似 Java 中的方法，有形式参数列表和主体。
         * 参数的类型是可选的。在不指定类型时，由编译器通过上下文环境来推断。
         * Lambda表达式的主体可以返回值或 void。返回值的类型必须与目标类型相匹配。
         * 当Lambda表达式的主体抛出异常时，异常的类型必须与目标类型的throws 声明相匹配。
         *
         * 由于Lambda表达式的类型由目标类型确定，在可能出现歧义的情况下，可能有多个类型满足要求，编译器无法独自完成类型推断。
         * 这个时候需要对代码进行改写，以帮助编译器完成类型推断。
         * 一个常见的做法是显式地把Lambda表达式赋值给一个类型确定的变量。
         * 另外一种做法是显示的为Lambda表达式指定类型。
         *
         * 函数式接口 A 和 B 分别有方法 say 和 des。两个方法 say 和 des 的类型是相同的。
         * 类 LambdaStudy 的 use 方法有两个重载形式，分别接受类型 A 和 B 的对象作为参数。
         * LambdaStudy 的对象如果直接使用 () -> System.out.println("hello world") 来调用 use 方法，会出现编译错误。
         * 这是因为编译器无法推断该 Lambda 表达式的类型，类型可能是 A 或 B。
         * 这里通过显式的赋值操作为 Lambda 表达式指定了类型 A，
         * 以及显式的指定 lambda 表达式的类型为类型 B，从而可以编译通过。
         * 1. 赋值给类型确定的变量
         * 2. 显式指定类型
         */
        LambdaStudy study = new LambdaStudy();
//        study.use(() -> System.out.println("hello world"));  // 会出现编译错误 Ambiguous method call.

        // 赋值给类型确定的变量
        // 把lambda表达式赋值给类型为A的变量a
        A a = () -> System.out.println("hello world");
        study.use(a);

        // 显式指定类型
        // 把lambda表达式显式指定类型B
        study.use((B) () -> System.out.println("nihao"));
    }

    public void use(A a){
        System.out.println("user interface A");
        a.say();
    }

    public void use(B b){
        System.out.println("user interface B");
        b.des();
    }

    /**
     * lambda表达式语法
     */
    public static void lambdaSyntax(){
        /*
         * Lambda表达式核心原则：可推导可省略。
         * Lambda表达式关注的是数据处理，而不是对象。
         *
         * lambda表达式是一个可传递的代码块，可以在以后执行一次或多次。
         * 它是一种紧凑的传递代码的方式，利用它，可以实现简洁灵活的函数式编程。
         *
         * 传入代码来检查一个字符串是否比另一个字符串短，这里要计算：
         *     first.length() - second.length()
         * first和second 是什么？他们都是字符串。Java是一种强类型语言，所以我们要指定他们的类型：
         *   (String first, String second) -> first.length() - second.length()
         * 这就是一个lambda表达式，Lambda表达式就是一个代码块，以及必须传入代码的变量规范。
         *
         * 相比匿名内部类，传递代码变得更为直观，不再有实现接口的模板代码，不再声明方法，也没有名字，而是直接给出了方法的实现代码。
         * Lambda表达式由->分隔为两部分，前面是方法的参数，后面{}内是方法的代码。
         *
         * 1. 当主体代码只有一条语句的时候，括号和return语句也可以省略。
         *    没有括号的时候，主体代码是一个表达式，这个表达式的值就是函数的返回值，结尾不能加分号，也不能加return语句。
         *    (String first, String second) -> first.length() - second.length()
         *
         * 2. 如果代码要完成的计算无法放在一个表达式中，就可以像写方法一样，把这些代码放在{}中。并包含显式的return语句（如果需要返回值的话），例如：
         *  (String str1, String str2) -> {
         *       if (str1.length() < str2.length()) {
         *           return -1;
         *       } else if (str1.length() > str2.length()) {
         *           return 1;
         *       } else {
         *           return 0;
         *       }
         *  }
         *
         * 3. 即使 lambda 表达式中没有参数，仍然要提供空括号。就像无参方法一样：
         *  () -> {
         *     for (int i = 100; i >= 0; i--) {
         *        System.out.println(i);
         *     }
         *  }
         *
         * 4. 如果可以推导出一个 lambda 表达式的参数类型，则可以忽略其类型。例如：
         *  Comparator<String> stringComparator = (first, second) -> first.length()-second.length();
         *  first 和 second 的类型都可以推导出是 String 类型，所以可以忽略其类型，不用写。
         *
         * 5. 如果只有一个参数（前提：只有一个参数，0 个或多个参数都不行），而且这个参数的类型可以推导出，那么还可以省略小括号：
         *  ActionListener listener = event-> System.out.println("This time is "+new Date());
         * 只有一个参数 event，而且 event 的类型可以推导出是 java.awt.event.ActionEvent；参数的类型可以不写，而且参数外面的小括号可以省略。
         *
         * 6. 不用指定 lambda 表达式的返回类型，lambda 表达式的返回类型总是会由上下文推导出。例如下面的表达式：
         *      (String first, String second) -> first.length() second.length()
         *    可以在需要 int 类型结果的上下文中使用。
         *
         * 7. 如果一个lambda表达式只在某些分支返回一个值，而在另外一些分支不返回值，这是不合法的。例如：
         *      (int x) -> { if (x >= 0) return 1; }就不合法。
         */

        // 如果可以推导出一个 lambda 表达式的参数类型，则可以忽略其类型。例如：
        // first 和 second 的类型都可以推导出是 String 类型，所以可以忽略其类型，不用写。
        Comparator<String> stringComparator = (first, second) -> first.length()-second.length();


        // 如果只有一个参数（前提：只有一个参数，0 个或多个参数都不行），而且这个参数的类型可以推导出，那么还可以省略小括号：
        // 只有一个参数 event，而且 event 的类型可以推导出是 java.awt.event.ActionEvent；参数的类型可以不写，而且参数外面的小括号可以省略。
        ActionListener listener = event-> System.out.println("This time is "+new Date());

        // 与匿名内部类类似，Lambda表达式也可以访问定义在主体代码外部的变量，但对于局部变量，它也只能访问final类型的变量，
        // 与匿名内部类的区别是，它不要求变量声明为final，但变量事实上不能被重新赋值。
        List<String> list = new ArrayList<>(10);
        new Thread(() -> {
            for (int i=0; i<10; i++){
                list.add(i+"");
            }
        }).start();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        String msg = "hello world";
        // 可以访问局部变量msg，但msg不能被重新赋值，Java编译器会提示错误：
        // msg = "good morning";
        // Java编译器会提示错误：Variable used in lambda expression should be final or effectively final
        executor.submit(() -> System.out.println(msg));
        /*
         * 这个原因与匿名内部类是一样的，Java会将msg的值作为参数传递给Lambda表达式，
         * 为Lambda表达式建立一个副本，它的代码访问的是这个副本，而不是外部声明的msg变量。
         * 如果允许msg被修改，则程序员可能会误以为Lambda表达式读到修改后的值，引起更多的混淆。
         *
         * 为什么非要建立副本，直接访问外部的msg变量不行吗？不行，因为msg定义在栈中，当Lambda表达式被执行的时候，msg可能早已被释放了。
         */

        /*
         * Lambda表达式与匿名内部类很像，主要就是简化了语法，那它是不是语法糖，内部实现其实就是内部类呢？
         * 答案是否定的，Java会为每个匿名内部类生成一个类，但Lambda表达式不会。
         * Lambda表达式通常比较短，为每个表达式生成一个类会生成大量的类，性能会受到影响。
         * Lambda表达式不是匿名内部类，而是函数式接口。
         */
    }


    /**
     * 方法引用：Java 8引入的一种新语法
     */
    public static void methodReference(){
        /*
         * 方法引用：Java 8引入的一种新语法。
         *
         * 方法引用是Lambda表达式的一种简写方法，由::分隔为两部分，前面是类名或变量名，后面是方法名。
         * 方法可以是实例方法，也可以是静态方法，但含义不同。
         *
         * 要用::操作符分隔方法名与对象或类名。主要有3种情况：
         * object::instanceMethod      // 变量::实例方法   [变量（类或者接口的实例）只能引用实例方法]
         * Class::staticMethod         // 类名::实例方法
         * Class::instanceMethod       // 类名（接口名）::静态方法
         * 在前2种情况中，方法引用等价于提供方法参数的lambda表达式。比如：
         * System.out::println等价于x -> System.out.println(x)。类似地，Math::pow等价于(x, y) ->Math.pow(x, y)。
         * 对于第3种情况，第1个参数会成为方法的目标。
         * 例如，String::compareToIgnoreCase等同于(x, y) -> x.compareToIgnoreCase(y)。
         */

        // 对于静态方法，如下两条语句是等价的：
        Supplier<String> supplier1 = () -> InterfaceStudy.getStr();
        Supplier<String> supplier2 = InterfaceStudy::getStr;

        // 而对于实例方法，它的第一个参数就是该类型的实例，比如，如下两条语句是等价的：
        Function<Goods, Integer> function1 = Goods::getId;
        Function<Goods, Integer> function2 = g -> g.getId();

        // 对于Goods::setName，它是一个BiConsumer，即如下两条语句是等价的：
        BiConsumer<Goods, String> biConsumer1 = Goods::setName;
        BiConsumer<Goods, String> biConsumer2 = (g, name) -> g.setName(name);
        BiConsumer<PrintStream, String> b = PrintStream::println;
        BiConsumer<PrintStream, String> b2 = (p, s) -> p.println(s);

        Consumer<String> consumer = System.out::println;

        List<String> list = new ArrayList<>(10);
        for (int i=0; i<10; i++){
            list.add(""+i);
        }
        PrintStream out = System.out;
        // 如果方法引用的第一部分是变量名，则相当于调用那个对象的方法
        list.forEach(out::println);
        list.forEach(System.out::println);
        Goods goods = new Goods(364561);
        Supplier<Integer> supplier = () -> goods.getId();

        // 静态comparing方法取一个“键提取器”函数，它将类型T映射为一个可比较的类型（如String）
        Comparator.comparing(Goods::getId)
                .thenComparing(Goods::getPrice)
                .thenComparing(Goods::getNumber);
    }

    /**
     * 函数的复合
     */
    public static void functionComposition(){
        /*
         * 在前面的例子中，函数式接口都用作方法的参数，其他部分通过Lambda表达式传递具体代码给它。
         * 函数式接口和Lambda表达式还可用作方法的返回值，传递代码回调用者，将这两种用法结合起来，可以构造复合的函数，使程序简洁易读。
         *
         *
         */

        // 1. Comparator中的复合方法
        // Comparator接口定义了如下静态方法：它构建并返回了一个符合Comparator接口的Lambda表达式
        // public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
        //            Function<? super T, ? extends U> keyExtractor)
        //    {
        //        Objects.requireNonNull(keyExtractor);
        //        return (Comparator<T> & Serializable)
        //            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
        //    }
        Comparator.comparing(Goods::getId)
                .thenComparing(Goods::getPrice)
                .thenComparing(Goods::getNumber);

        // 2. function包中的复合方法
        // 先将T类型的参数转化为类型R，再调用after将R转换为V，最后返回类型V。
        // default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        //        Objects.requireNonNull(after);
        //        return (T t) -> after.apply(apply(t));
        //    }
    }







}
