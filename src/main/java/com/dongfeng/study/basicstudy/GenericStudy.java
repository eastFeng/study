package com.dongfeng.study.basicstudy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <b> Java 泛型（generic）学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-21 16:32
 */
public class GenericStudy {
    public static void main(String[] args) {

        // 泛型基本概念和原理
        conceptAndPrinciple();

        // 泛型方法
        Goods goods = genericMethod(new Goods());

        // 类型变量的限定
        boolean hhh = test("hhh");

        // 通配符
        wildcard(goods, System.out::println);

        // 泛型代码和虚拟机
        genericAndVm();

        // 泛型的约束与局限性
        limit();

        // 泛型类型的继承规则
        genericExtendsRule();

    }

    // 自定义泛型类
    public static class Self<T>{
        private T first;
        private T second;

        public Self(T first, T second){
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }
        public T getSecond() {
            return second;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public void setSecond(T second) {
            this.second = second;
        }

        /**
         * @param t param
         * @param <T> 该类型变量虽然和该方法所在的泛型类一样都是T，但是和泛型类Self的类型变量没有关系。
         */
        public static <T> T test(T t){
            return t;
        }

        /**
         * @param <U> 该类型变量泛型类Self的类型变量没有关系。
         */
        public static <U> U test2(){
            U u = null;
            return u;
        }
    }

    /**
     * 泛型基本概念和原理
     */
    public static void conceptAndPrinciple(){
        /*
         * 上面自定义的泛型类Self，与普通类的区别体现在：
         * 1. 类名后面多了一个<T>
         * 2. first和second的类型都是T
         *
         * T表示类型变量。
         * 【泛型就是类型参数化，处理的数据类型不是固定的，而是可以作为参数传入。】
         *
         * 类型参数可以有多个。多个类型之间以逗号分隔。
         * 一个泛型类（generic class）就是具有一个或多个类型变量的类。
         *
         * 类型变量使用大写形式，且比较短，这是很常见的。
         * 在Java库中，使用变量E表示集合的元素类型，K和V分别表示表的关键字与值的类型。T（需要时还可以用临近的字母U和S）表示“任意类型”。
         *
         * 用具体的类型替换类型变量就可以实例化泛型类型。
         *
         * 泛型的好处：
         * 1. 更好的安全性。
         * 2. 更好的可读性。
         * 通过使用泛型，开发环境和编译器能确保不会用错类型，为程序多设置一道安全防护网。
         * 使用泛型，还可以省去烦琐的强制类型转换，再加上明确的类型信息，代码可读性也会更好。
         *
         * 接口也可以是泛型的。比如Comparable接口，Comparator接口。
         *
         * 泛型是计算机程序中一种重要的思维方式，它将数据结构和算法与数据类型相分离，
         * 使得同一套数据结构和算法能够应用于各种数据类型，而且可以保证类型安全，提高可读性。
         */

        // 实例化泛型类型
        // Self<Integer>中的Integer就是传递的实际类型变量。
        Self<Integer> self = new Self<>(100, 1);
        Integer first = self.getFirst();
        Integer second = self.getSecond();
    }

    /**
     * 泛型方法
     *
     * @param param param
     * @param <T> 类型变量
     */
    public static <T> T genericMethod(T param){
        /*
         * 除了泛型类，方法也可以是泛型的，而且，一个方法是不是泛型的，与它所在的类是不是泛型没有什么关系。
         *
         * 泛型方法中的类型变量，放在修饰符后面，返回值前面。
         *
         * 泛型方法可以定义在普通类中，也可以定义在泛型类中。
         *
         * 当调用一个泛型方法时，在方法名前的尖括号中放入具体的类型：
         *
         */

        // 当调用一个泛型方法时，在方法名前的尖括号中放入具体的类型：
        String s = Self.<String>test2();
        // 调用一个泛型方法时，当编译器有足够的信息能够推断出具体的类型，方法调用中可以省略<Integer>类型参数
        Integer test = Self.test(233);

        return param;
    }

    /**
     * 类型变量的限定
     *
     * @param param param
     * @param <T> 类型变量
     * @return true or false
     */
    public static <T extends Comparable<T>> boolean test(T param){
        /*
         * 有时，类或方法需要对类型变量加以约束。
         *
         * <T extends BoundingType>
         * 表示T应该是绑定类型的子类型（subtype）。T和绑定类型可以是类，也可以是接口，还可以是其他类型变量。
         * 选择关键字extends的原因是更接近子类的概念，并且Java的设计者也不打算在语言中再添加一个新的关键字（如sub）。
         *
         * // 其他类型变量
         * <T extends E>
         *
         * 一个类型变量或通配符可以有多个限定：
         * <T extends Comparable<T> & Serializable>
         *
         * 在Java的继承中，可以根据需要拥有多个接口超类型，但限定中至多有一个类。如果用一个类作为限定，它必须是限定列表中的第一个。
         *
         */
        return param == null;
    }


    /**
     * 通配符
     */
    public static <T> void wildcard(T param, Consumer<? super T> action){
        /*
         * 通配符类型中，允许类型参数变化。
         * 子类型通配符：<? extends Comparable>
         *
         * 更简洁的参数类型限定：
         * 同样是extends关键字，同样应用于泛型，<T extends E>和<?extends E>到底有什么关系？
         * 1. <T extends E>用于【定义】类型变量，它声明了一个类型变量T，可放在泛型类定义中类名后面、泛型方法返回值前面。
         * 2. <? extends E>用于【实例化】类型变量，它用于实例化泛型变量中的类型变量，只是这个具体类型是未知的，只知道它是E或E的某个子类型。
         *
         * Self<?>，称为无限定通配符。
         * 无限定通配符形式也可以改为使用类型参数。也就是说，下面的写法：
         * public static void eachTest(Self<?> self)
         * 可以改成:
         * public static <T> void eachTest(Self<T> self)
         * 不过，通配符形式更为简洁。虽然通配符形式更为简洁，但上面两种通配符都有一个重要的限制：只能读，不能写。
         *
         * 1）通配符形式都可以用类型参数的形式来替代，通配符能做的，用类型参数都能做。
         * 2）通配符形式可以减少类型参数，形式上往往更为简单，可读性也更好，所以，能用通配符的就用通配符。
         * 3）如果类型参数之间有依赖关系，或者返回值依赖类型参数，或者需要写操作，则只能用类型参数。
         * 4）通配符形式和类型参数往往配合使用。
         *
         */

        // 无限定通配符例子
        eachTest(new Self<>(100, 1));

        // 只能读，不能写
        Self<Integer> self1 = new Self<>(3, 4);
        Self<? extends Number> self = new Self<>(1, 2);
        self = self1;
        // self.setFirst(100);  // 错误
        // self.setFirst((Number) 100); // 错误
        Object obj = new Object();
        // self.setFirst(obj); // 错误
        /*
         * 问号就是表示类型安全无知，? extends Number表示是Number的某个子类型，但不知道具体子类型，
         * 如果允许写入，Java就无法确保类型安全性，所以干脆禁止。
         *
         * 如果允许写入Object或Number类型，则后面两个setFirst语句编译就是正确的，
         * 也就是说，Java将允许把Double或String对象放入Integer容器，这显然违背了Java关于类型安全的承诺。
         */

        /*
         *
         * 通配符限定与类型变量限定十分类似，但是，还有一个附加的能力，即可以指定一个超类型限定（supertype bound）。
         * 超类型通配符 :
         * 与形式<? extends E>正好相反，它的形式为<? super E>，表示E的某个父类型
         * 【超类型通配符可以为方法提供参数，但不能使用返回值。】
         *
         * 三种通配符形式<? >、<? super E>和<? extends E>
         * 它们比较容易混淆，总结比较如下：
         * 1）它们的目的都是为了使方法接口更为灵活，可以接受更为广泛的类型。
         * 2）<? super E>用于灵活【写入或比较】，使得对象可以写入父类型的容器，使得父类型的比较方法可以应用于子类对象，它不能被类型参数形式替代。
         * 3）<? >和<? extends E>用于灵活【读取】，使得方法可以读取E或E的任意子类型的容器对象，它们可以用类型参数的形式替代，但通配符形式更为简洁。
         *
         * Java容器类的实现中，有很多使用通配符的例子，比如，类Collections中就有如下方法：
         * sort，copy，max等。
         */



        action.accept(param);
    }

    public static void eachTest(Self<?> self){
        System.out.println(self.getFirst());
        System.out.println(self.getSecond());
    }

    /**
     * 泛型代码和虚拟机
     */
    public static void genericAndVm(){
        /**
         * 虚拟机没有泛型类型对象——所有对象都属于普通类。
         *
         * 类型擦除
         * 无论何时定义一个泛型类型，都自动提供了一个相应的原始类型（raw type）。
         * 原始类型的名字就是删去类型参数后的泛型类型名。擦除（erased）类型变量，并替换为限定类型（无限定的变量用Object）。
         *
         * 原始类型用第一个限定的类型变量来替换，如果没有给定限定就用Object替换。
         *
         * 当程序调用泛型方法时，如果擦除返回类型，编译器插入强制类型转换。
         * 当存取一个泛型域时也要插入强制类型转换。
         *
         * 类型擦除也会出现在泛型方法中。
         *
         * 需要记住有关Java泛型转换的事实：
         * ● 虚拟机中没有泛型，只有普通的类和方法。
         * ● 所有的类型参数都用它们的限定类型替换。
         * ● 桥方法被合成来保持多态。
         * ● 为保持类型安全性，必要时插入强制类型转换。
         */
    }

    /**
     * 泛型的约束与局限性
     */
    public static void limit(){
        /*
         *
         * 在使用泛型类、方法和接口时，有一些值得注意的地方：
         *
         * 1. 不能用基本类型实例化类型参数。
         *
         * 因此，没有Self<double>，只有Self<Double>，
         * 当然，其原因是类型擦除。擦除之后，Pair类含有Object类型的域，而Object不能存储double值。
         *
         * 2. 运行时类型信息不适用于泛型。运行时类型查询只适用于原始类型。
         *
         * 学习反射时，我们知道内存中每个类都有一份类型信息，而每个对象也都保存着其对应类型信息的引用。
         * 在Java中，这个类型信息也是一个对象，它的类型为Class, Class本身也是一个泛型类，
         * 每个类的类型对象可以通过类名.class的方式引用，比如String.class、Integer.class。
         * 这个类型对象也可以通过对象的getClass()方法获得。
         * 对于每个类型这个Class对象只有一份，与泛型无关，所以Java不支持类似如下写法：
         * Self<Integer>.class  // 错误
         * Self<T>.class        // 错误
         * 支持这种写法：Self.class
         *
         * 一个泛型对象的getClass方法的返回值与原始类型对象也是相同的：
         * Self<String> stringSelf = new Self<>("a","b");
         * Self<Integer> integerSelf = new Self<>(8, 16);
         * boolean b = stringSelf.getClass() == integerSelf.getClass(); // true
         *
         * instanceof是运行时判断，也与泛型无关，也不支持如下写法：
         * if (o instanceof Self<String>)  // 错误
         * if (o instanceof Self<T>)       // 错误
         *
         * 3. 类型擦除可能会引发一些冲突。
         *
         * 假定像下面这样将equals方法添加到Self类中:
         * public boolean equals(T value){
         *     return first.equals(value) && second.equals(value);
         * }
         * 考虑一个Self<String>。从概念上讲，它有两个equals方法：
         * public boolean equals(String) // 在Self类中定义的
         * public boolean equals(Object) // 从Object类继承的
         * 但是，方法擦除后，public boolean equals(T) 就是 public boolean equals(Object)
         * 与Object.equals方法发生冲突。编译不通过，Java不允许。
         *
         * 下述代码也是非法的：
         * class Employee implements Comparable<Employee> {...}
         * class Manager extends Employee implements Comparable<Manager> {...}
         * Java编译器会提示错误，Comparable接口不能被实现两次（Manager类实现了两次），
         * 且两次实现的类型参数还不同，一次是Comparable<Base>，一次是Comparable<Child>。
         * 为什么不允许呢？因为类型擦除后，实际上只能有一个。
         * 那Manager有什么办法修改比较方法呢？只能是重写Employee类的实现：
         * public static class Manager extends Employee{
         *         @Override
         *         public int compareTo(Employee o) {
         *             if (!(o instanceof Manager)){
         *                 throw new IllegalArgumentException();
         *             }
         *             Manager other = (Manager) o;
         *             // 比较代码
         *             return 0;
         *         }
         * }
         *
         * 另外，也不能如下定义重载方法：
         * public static void test(Self<Integer> param) {}
         * public static void test(Self<String> param) {}
         * 虽然参数都是DynamicArray，但实例化类型不同，一个是DynamicArray<Integer>，另一个是DynamicArray<String>，
         * 同样，遗憾的是，Java不允许这种写法，理由同样是类型擦除后它们的声明是一样的。
         *
         */

        // 一个泛型对象的getClass方法的返回值与原始类型对象也是相同的：
        Self<String> stringSelf = new Self<>("a","b");
        Self<Integer> integerSelf = new Self<>(8, 16);
        System.out.println(stringSelf.getClass() == integerSelf.getClass());

        /*
         * 在定义泛型类、方法和接口时，也有一些需要注意的地方：
         * 1. 不能通过类型变量创建对象。
         *    比如，T是类型参数，下面的写法都是非法的：
         *    T ele = new T();
         *    T[] arr = new T[10];
         *    T.class;
         * 2. 泛型类类型参数不能用于静态变量和方法。
         * 不过，对于静态方法，它可以是泛型方法，可以声明自己的类型变量，这个变量与泛型类的类型变量是没有关系的。
         */

        /*
         * 泛型与数组：
         * 1. 不能创建泛型数组。
         * 下述代码也是非法的：new Self<String>[10];
         * 需要说明的是，只是不允许创建这些数组，而声明类型为Pair<String>[]的变量仍是合法的。比如Self<String> array;
         * 不过不能用new Pair<String>[10]初始化这个变量array。
         *
         */
        Self<String> array;

        /*
         * 不能抛出或捕获泛型类的实例
         * 既不能抛出也不能捕获泛型类对象。实际上，甚至泛型类扩展Throwable都是不合法的。
         * 以下定义就不能正常编译：
         * // 泛型类不能继承Throwable
         * public static class Problem<T> extends Exception {}
         *
         * catch子句中不能使用类型变量：
         * try {
         *    int i = 5/0;
         * }catch (T t){  // 不合法
         * }
         *
         */
    }

    //
    private static class Employee implements Comparable<Employee>{
        @Override
        public int compareTo(Employee o) {
            return 0;
        }
    }

    private static class Manager extends Employee{
        @Override
        public int compareTo(Employee o) {
            if (!(o instanceof Manager)){
                throw new IllegalArgumentException();
            }
            Manager other = (Manager) o;
            // 比较代码
            return 0;
        }
    }

    /**
     * 泛型类型的继承规则
     */
    public static void genericExtendsRule(){
        /*
         * 考虑一个类和一个子类，如上面的Employee和Manager。Self<Manager>是Self<Employee>的一个子类吗？
         * 答案是：不是。
         *
         * 无论S与T有什么联系，通常，Self<S>与Self<T>没有什么联系。
         *
         * 最后，泛型类可以扩展或实现其他的泛型类。就这一点而言，与普通的类没有什么区别。
         * 例如，ArrayList<T>类实现List<T>接口。这意味着，一个ArrayList<Manager>可以被转换为一个List<Manager>。
         * 但是，如前面所见，一个ArrayList<Manager>不是一个ArrayList<Employee>或List<Employee>。
         */

        List<String> list = new ArrayList<>(8);
    }

}
