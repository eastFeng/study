package com.dongfeng.study.basicstudy;

/**
 * <b> Java内部类学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-24 2:57
 */
public class InnerClassStudy {
    public static void main(String[] args) {

        // 四种内部类学习
        study("study");
    }

    public static final String H = "hello";

    private static void test(){}

    /**
     * 四种内部类学习
     */
    public static void study(String param){
        /*
         * 内部类（inner class）是定义在另一个类中的类。
         * 为什么需要使用内部类呢？其主要原因有以下三点：
         * 1. 内部类方法可以访问该类定义所在的作用域中的数据，包括私有的数据。
         * 2. 内部类可以对同一个包中的其他类隐藏起来。
         * 3. 当想要定义一个回调函数且不想编写大量代码时，使用匿名（anonymous）内部类比较便捷。
         *
         * 一般而言，内部类与包含它的外部类有比较密切的关系，而与其他类关系不大，
         * 定义在类内部，可以实现对外部完全隐藏，可以有更好的封装性，代码实现上也往往更为简洁。
         *
         * 不过，内部类只是Java编译器的概念，对于Java虚拟机而言，它是不知道内部类这回事的，
         * 每个内部类最后都会被编译为一个独立的类，生成一个独立的字节码文件。
         *
         * 在Java中，根据定义的位置和方式不同，主要有4种内部类。
         * 1. 静态内部类。
         * 2. 成员内部类。
         * 3. 方法内部类。
         * 4. 匿名内部类。
         * 其中，方法内部类是在一个方法内定义和使用的；匿名内部类使用范围更小，它们都不能在外部使用；
         * 成员内部类和静态内部类可以被外部使用，不过它们都可以被声明为private，这样，外部就不能使用了。
         *
         * 局部类不能(方法内部类)用public或private访问说明符进行声明。它的作用域被限定在声明这个局部类的块中。
         * 局部类(方法内部类)有一个优势，即对外部世界可以完全地隐藏起来。
         *
         * 内部类中声明的所有静态域都必须是final。原因很简单。我们希望一个静态域只有一个实例，
         * 不过对于每个外部对象，会分别有一个单独的内部类实例。如果这个域不是final，它可能就不是唯一的。
         *
         */

        // 静态内部类
        StaticInner staticInner = new StaticInner();

        // 与静态内部类不同，成员内部类对象总是与一个外部类对象相连的。
        // 在外部使用时，它不能直接通过new JavaBasic.Inner()的方式创建对象，而是要先将创建一个Outer类对象
        InnerClassStudy study = new InnerClassStudy();
        Inner inner = study.new Inner();

        // 方法内部类
        class MethodInner{
            /*
             * 方法内部类只能在定义的方法内被使用。
             * 如果方法是实例方法，则除了静态变量和方法，内部类还可以直接访问外部类的实例变量和方法。
             * 如果方法是静态方法，则方法内部类只能访问外部类的静态变量和方法。
             * 方法内部类还可以直接访问方法的参数和方法中的局部变量，不过，这些变量必须被声明为final。
             *
             * 方法内部类可以访问方法中的参数和局部变量，这是通过在构造方法中传递参数来实现的，
             * 方法内部类操作的并不是外部的变量，而是它自己的实例变量。
             */

            public void test(){
                // 访问外部类的静态变量
                String h = H;
                // 访问外部类的静态方法
                test();

                System.out.println(param);
            }
        }
        MethodInner methodInner = new MethodInner();
        methodInner.test();

        /*
         * 匿名内部类(anonymous inner class)
         * 匿名内部类没有单独的类定义，它在创建对象的同时定义类。
         * 假如只创建这个类的一个对象，就不必命名了。这种类被称为匿名内部类（anonymous inner class）。
         *
         * 语法格式：
         * new SuperType(construction parameters) {
         *   // 匿名内部类实现部分
         * }
         * 其中，SuperType可以是Runnable这样的接口，于是内部类就要实现这个接口。
         * SuperType也可以是一个类，于是内部类就要扩展它。
         *
         * 由于构造器的名字必须与类名相同，而匿名类没有类名，所以，匿名类不能有构造器。
         * 取而代之的是，将构造器参数传递给超类（superclass）构造器。
         * 尤其是在内部类实现接口的时候，不能有任何构造参数。
         *
         * 匿名内部类是与new关联的，在创建对象的时候定义类，new后面是父类或者父接口，
         * 然后是圆括号()，里面可以是传递给父类构造方法的参数，
         * 最后是大括号{}，里面是类的定义。
         *
         * 匿名内部类只能被使用一次，用来创建一个对象。
         *
         * 匿名内部类是怎么实现的呢？每个匿名内部类也都被生成为一个独立的类，只是类的名字以外部类加数字编号，没有有意义的名字。
         *
         * 匿名内部类能做的，方法内部类都能做。但如果对象只会创建一次，且不需要构造方法来接受参数，则可以使用匿名内部类，这样代码书写上更为简洁。
         */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        };

        new Thread(runnable).start();
    }

    /**
     * 静态内部类
     */
    public static class StaticInner{
        /*
         * 有时候，使用内部类只是为了把一个类隐藏在另外一个类的内部，并不需要内部类引用外围类对象。
         * 为此，可以将内部类声明为static，以便取消产生的引用。
         *
         * 静态内部类与静态变量和静态方法定义的位置一样，也带有static关键字。
         * 声明在接口中的内部类自动成为static和public类。
         *
         * 【语法上，静态内部类除了位置放在其他类内部外，它与一个独立的类差别不大，可以有静态变量、静态方法、成员方法、成员变量、构造方法等。】
         *
         * 【静态内部类可以访问外部类的静态变量和方法，但不可以访问实例变量和方法。】
         * 内部类可以访问外部类的私有静态变量，而我们知道私有变量是不能被类外部访问的，
         * Java的解决方法是：自动为外部类生成一个非私有访问方法access$0，它返回这个私有静态变量。
         *
         * public静态内部类可以被外部使用：只是需要通过“外部类.静态内部类”的方式使用。
         *
         * 静态内部类是怎么实现的呢？实际上会生成两个类：一个是JavaBasic，另一个是JavaBasic$StaticInner。
         *
         * 静态内部类的使用场景是很多的，如果它与外部类关系密切，且不依赖于外部类实例，则可以考虑定义为静态内部类。
         * 比如，一个类内部，如果既要计算最大值，又要计算最小值，可以在一次遍历中将最大值和最小值都计算出来，但怎么返回呢？
         * 可以定义一个类Pair，包括最大值和最小值，但Pair这个名字太普遍，而且它主要是类内部使用的，就可以定义为一个静态内部类。
         * Java API中使用静态内部类的例子：
         * 1. Integer类内部有一个私有静态内部类IntegerCache，用于支持整数的自动装箱。
         * 2. 表示链表的LinkedList类内部有一个私有静态内部类Node，表示链表中的每个节点。
         * 3. Character类内部有一个public静态内部类UnicodeBlock，用于表示一个Unicode block。
         * 4. HashMap中静态内部类Node，表示链表中的每个节点。
         */

        // 静态变量
        public static int anInt;
        // 常量
        public static final String COUNTRY = "China";
        // 成员变量（实例变量）
        private String province;

        // 构造方法
        public StaticInner(){}

        // 静态内部类可以访问外部类的静态变量和方法
        public static void test(){
            // 内部类访问了外部类的私有静态变量H，而我们知道私有变量是不能被类外部访问的，
            // Java的解决方法是：自动为外部类JavaBasic生成一个非私有访问方法access$0，它返回这个私有静态变量H。
            System.out.println(H);
            test();
        }

        // 静态方法
        public static int getAnInt() {
            return anInt;
        }
        public static void setAnInt(int anInt) {
            StaticInner.anInt = anInt;
        }
        // 成员方法
        public String getProvince() {
            return province;
        }
        public void setProvince(String province) {
            this.province = province;
        }
    }

    /**
     * 成员内部类
     */
    private class Inner{
        /*
         * 与静态内部类相比，成员内部类没有static修饰符，少了一个static修饰符，含义有很大不同。
         *
         * 【除了静态变量和方法，成员内部类还可以直接访问外部类的实例变量和方法。】
         * （注释：在一个类当中，静态方法也只能访问静态变量。实例方法可以访问实例变量和静态变量，静态变量是所有对象共享的一份。）。
         * 成员内部类还可以通过“外部类.this.xxx”的方式引用外部类的实例变量和方法，如JavaBasic.this.getName()，
         * 这种写法一般在重名的情况下使用，如果没有重名，那么“外部类.this.”是多余的。
         *
         * 【与静态内部类不同，成员内部类对象总是与一个外部类对象相连的。】
         * 在外部使用时，它不能直接通过new JavaBasic.Inner()的方式创建对象，而是要先将创建一个JavaBasic类对象：
         *       JavaBasic basic = new JavaBasic(1);
         *       Inner inner = basic.new Inner();
         * 创建内部类对象的语法是 : 外部类对象.new 内部类();
         *
         * 【与静态内部类不同，成员内部类中不可以定义静态变量和方法（final变量例外，它等同于常量）】。方法内部类和匿名内部类也都不可以。
         * Java为什么要有这个规定呢？
         * 可以这么理解，这些内部类是与外部类实例相连的，不应独立使用，
         * 而静态变量和方法作为类型的属性和方法，一般是独立使用的，在内部类中意义不大，
         * 而如果内部类确实需要静态变量和方法，那么也可以挪到外部类中。
         *
         * 成员内部类背后是怎么实现的呢？会生成两个类：一个是JavaBasic，另一个是JavaBasic$Inner。
         *
         * 成员内部类有哪些应用场景呢？
         * 如果内部类与外部类关系密切，需要访问外部类的实例变量或方法，则可以考虑定义为成员内部类。
         * 外部类的一些方法的返回值可能是某个接口，为了返回这个接口，
         * 外部类方法可能使用内部类实现这个接口，这个内部类可以被设为private，对外完全隐藏。
         *
         * 比如，在Java API的类LinkedList中，它的两个方法listIterator和descendingIterator的返回值都是接口Iterator，
         * 调用者可以通过Iterator接口对链表遍历，
         * listIterator方法和descendingIterator方法内部分别使用了成员内部类ListItr和DescendingIterator，
         * 这两个内部类都实现了接口Iterator。
         */

        // final变量例外，它等同于常量
        public static final int I = 1;
    }
}
