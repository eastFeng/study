package com.dongfeng.study.basicstudy;

import com.dongfeng.study.basicstudy.designpattern.proxypattern.IPrinter;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b> Java反射（reflective）学习 </b>
 *
 * <p> 能够分析类能力的程序称为反射（reflective）
 * <p> 反射机制可以用来：
 * <ol>
 *     <li> 在运行时分析类的能力。 </li>
 *     <li> 在运行时查看对象，例如，编写一个toString方法供所有类使用。 </li>
 *     <li> 实现通用的数组操作代码。 </li>
 *     <li> 利用Method对象，这个对象很像C++中的函数指针 </li>
 * </ol>
 *
 * <p> 反射不一样，它是在运行时，而非编译时，动态获取类型的信息，比如接口信息、成员信息、方法信息、构造方法信息等，
 * 根据这些动态获取到的信息创建对象、访问/修改成员、调用方法等
 *
 * <p> <b>在Java中，每一个类都会有专属于自己的Class对象，当我们编写完.java文件后，使用javac编译后，就会产生一个字节码文件(.class文件)
 * 在字节码文件中包含类的所有信息，比如属性、构造方法、方法...等等。
 * 当字节码文件被装载进虚拟机执行时(类加载的第一个阶段加载阶段完成后)，会在内存中生成Class对象，
 * (并没有明确规定java.lang.Class对象在java堆中，对于HotSpot虚拟机而言，Class对象比较特殊，它虽然是对象，但是存放在方法区里面)
 * 它包含了该类内部的所有信息，在程序运行时可以获取这些信息。</b>
 *
 * <p> 每个已加载的类在内存都有一份类信息，每个对象都有指向它所属类信息的引用。Java中，类信息对应的类就是{@link Class}类。
 *
 * <p> 在java.lang.reflect包中有三个类Field、Method和Constructor分别用于描述类的域、方法和构造器。
 *
 * <p> 反射的实现主要借助以下四个类：
 * <lo>
 *     <li> {@link Class}：类的对象 </li>
 *     <li> {@link Constructor}：类的构造方法 </li>
 *     <li> {@link Field}：类中的属性对象 </li>
 *     <li> {@link Method}：类中的方法对象</li>
 * <p> 作用：反射机制指的是程序在运行时能够获取自身的信息。在JAVA中，只要给定类的类的全限定名，那么就可以通过反射机制来获取类的所有信息。
 *
 * @author eastFeng
 * @date 2020/8/28 - 14:50
 */
@Slf4j
public class ReflectStudy {
    public static void main(String[] args) {

        // 虚拟机为每个类型管理一个Class对象。因此，可以利用==运算符实现两个类对象比较的操作。
//        Goods goods = new Goods();
//        Object obj = new Object();
//        Class<? extends Goods> aClass = goods.getClass();
//        Class<?> aClass1 = obj.getClass();
//        Class<Goods> goodsClass = Goods.class;
//        Class<ClassStudy> classDemoClass = ClassStudy.class;
//
//        if (goods.getClass() == obj.getClass()){
//
//        }


        try {
//            // 获取Class对象的三种方法
//            getClassObj();
//
//            // 名称信息
            nameInfo();
//
//            // 字段信息
//            fieldInfo();
//
            // 方法信息
//            methodInfo();
//
//            // 创建对象和构造方法
//            creatObj();
//
//            // 类型检查和转换
//            typeCheckAndConversion(new ArrayList<String>());
//
//            // Class的类型信息
//            classInfo(getGoodsClass());
//
//            // 类的声明信息
//            classDeclareInfo(getGoodsClass());
//
//            // 类的加载
//            classLoad("int", null);
//
//            // 反射和数组
//            reflectAndArray(String.class);

            // 反射应用示例
            Goods goods = new Goods(1546, "hh", "hhh", 12, 6999);
//            applicationExamples(goods);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Class对象的三种方法
     * <p> 一个类在内存中至多存在一个 Class对象
     */
    public static void getClassObj() throws ClassNotFoundException {
        //1. 类名.class : 这种获取方式只有在编译前已经声明了该类的类型才能获取到 Class对象
        Class<Goods> aClass1 = Goods.class;
        // 接口也有Class对象，且这种方式对于接口也是适用的
        Class<IPrinter> iPrinterClass = IPrinter.class;
        // 基本类型没有getClass方法，但也都有对应的Class对象，类型参数为对应的包装类型
        Class<Integer> integerClass = int.class;
        // void作为特殊的返回类型，也有对应的Class
        Class<Void> voidClass = void.class;
        // 枚举类型也有对应的Class
        Class<ResponseCodeEnum> codeEnumClass = ResponseCodeEnum.class;

        /*
         * 2. 实例.getClass() : 通过实例化对象获取该实例的 Class对象。
         * 每个对象都有指向它所属类信息的引用。
         * Class是一个泛型类，有一个类型参数，getClass()并不知道具体的类型，所以返回Class<?>。
         */
        Goods goods = new Goods();
        Class<? extends Goods> aClass2 = goods.getClass();
        // 对于数组，每种类型都有对应数组类型的Class对象，每个维度都有一个，即一维数组有一个，二维数组有一个不同的类型。
        String[] stringArray = new String[10];
        int[][] twoDimArr = new int[2][3];
        int[] oneDimArr = new int[10];
        Class<? extends String[]> stringArrayClass = stringArray.getClass();
        Class<? extends int[][]> twoDimArrClass = twoDimArr.getClass();
        Class<? extends int[]> aClass = oneDimArr.getClass();


        //3. Class.forName(className) : 通过类的全限定名获取该类的 Class对象
        // Class有一个静态方法forName，可以根据类名直接加载Class，获取Class对象
        Class<?> aClass3 = Class.forName("com.javabasics.Goods");

        System.out.println(aClass1);
        System.out.println("Class.forName == Goods.class      : "+(aClass1==aClass3));
        System.out.println("Class.forName == goods.getClass() : "+(aClass2==aClass3));
        System.out.println("Goods.class   == goods.getClass() : "+(aClass1==aClass2));
    }

    /*
     * 有了Class对象后，我们就可以了解到关于类型的很多信息，并基于这些信息采取一些行动。
     * Class的方法很多，大部分比较简单直接，容易理解。
     * 信息包括：
     * 名称信息、字段信息、方法信息、创建对象和构造方法、类型信息。
     */

    /**
     * <b> 名称信息 </b>
     */
    public static void nameInfo(){
        Class<?> goodsClass = getGoodsClass();
        assert goodsClass != null;

        // Class有如下方法，可以获取与名称有关的信息：

        // getName返回的是Java内部使用的真正的名称 : com.dongfeng.study.basicstudy.Goods
        System.out.println("name : "+goodsClass.getName());

        // getSimpleName返回的名称不带包信息 : Goods
        System.out.println("simple name : "+goodsClass.getSimpleName());

        // getCanonicalName返回的名称更为友好 : com.dongfeng.study.basicstudy.Goods
        System.out.println("canonical name : "+goodsClass.getCanonicalName());

        // getPackage返回的是包信息 : com.dongfeng.study.basicstudy
        Package aPackage = goodsClass.getPackage();
        System.out.println("package name : " + aPackage.getName());
    }

    /**
     * <b> 字段信息 </b>
     *
     * <p> 类中定义的静态变量和实例变量都被称为字段，用类{@link Field}表示。
     */
    public static void fieldInfo() throws IllegalAccessException, NoSuchFieldException {
        Class<?> goodsClass = getGoodsClass();
        assert goodsClass != null;

        // Class类有4个获取字段信息的方法：

        // 返回所有public字段，包括父类的。如果没有字段，返回空数组
        Field[] fields = goodsClass.getFields();
        System.out.println("Fields: ");
        Arrays.stream(fields).forEach(t-> System.out.print(t.getName()+", "));

        // 返回本类声明的所有字段，包括非public的，但不包括父类的
        Field[] declaredFields = goodsClass.getDeclaredFields();
        System.out.println("DeclaredFields: ");
        Arrays.stream(declaredFields).forEach(t-> System.out.print(t.getName()+", "));

        try {
            // 返回本类或父类中指定名称的public字段，找不到抛出NoSuchFieldException异常
            Field name = goodsClass.getField("name");
            System.out.println("getField name : " + name.getName());
        } catch (NoSuchFieldException e) {
            log.error("fieldInfo error:{}", e.getMessage(), e);
        }

        // 返回本类中声明的指定名称的字段，找不到抛出NoSuchFieldException异常
        Field numberField = goodsClass.getDeclaredField("numberField");

        // Field也有很多方法，可以获取字段的信息，也可以通过Field访问和操作指定对象中该字段的值，基本方法有：
        // 获取字段的名称
        String name = numberField.getName();

        // 判断当前程序是否有该字段的访问权限
        boolean accessible = numberField.isAccessible();
        System.out.println("number Field accessible : " + accessible);

        // 设置该字段的访问权限，设为true表示忽略Java的访问检查机制，以允许读写非public的字段
        numberField.setAccessible(true);

        Goods goods = new Goods(1546, "hh", "hhh", 12, 6999);
        // 获取指定对象中该字段的值
        Object o = numberField.get(goods);
        System.out.println("number Field in goods is : "+ o);

        // 将指定对象goods中该字段的值设为7000
        numberField.set(goods, 7000);
        /*
         * 在上面实例中的get/set方法中，对于静态变量，第一个参数obj被忽略，可以为null，
         * 如果字段值为基本类型， get/set会自动在基本类型与对应的包装类型间进行转换；
         * 对于private字段，直接调用get/set会抛出非法访问异常IllegalAccessException，应该先调用setAccessible(true)以关闭Java的检查机制。
         */

        // Field还有很多其他方法，比如：
        // 返回字段的修饰符
        int modifiers = numberField.getModifiers();
        System.out.println("number字段修饰符 ："+ modifiers);
        boolean isPublic = Modifier.isPublic(modifiers);

        // 返回字段的类型
        Class<?> numberFieldType = numberField.getType();
        System.out.println("number字段类型 ："+ numberFieldType.getCanonicalName());

        // 以基本类型操作字段
        int anInt = numberField.getInt(goods);
        numberField.setInt(goods, 9999);

        // 查询该字段的注解信息
        Annotation[] declaredAnnotations = numberField.getDeclaredAnnotations();

    }


    /**
     * <b> 方法信息 </b>
     *
     * <p> 类中定义的静态和实例方法都被称为方法，用类{@link Method}表示。
     */
    public static void methodInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> goodsClass = getGoodsClass();
        assert goodsClass != null;

        // 获取所有的public方法，包括父类的，如果没有，返回空数组
        Method[] methods = goodsClass.getMethods();
        System.out.println("public方法: ");
        Arrays.stream(methods).forEach(m -> System.out.print(m.getName()+", "));

        // 返回本类中声明的所有方法，包括非public的，但不包括父类的
        Method[] declaredMethods = goodsClass.getDeclaredMethods();
        System.out.println();
        System.out.println("本类中声明的所有方法: ");
        Arrays.stream(declaredMethods).forEach(m -> System.out.print(m.getName()+", "));

        // 返回本类或父类中，指定名称和参数类型的public方法。找不到抛出NoSuchMethodException异常
        Method getIdMethod = goodsClass.getMethod("getId");

        // 返回本类中指定名称和参数类型的方法，找不到抛出NoSuchMethodException异常
        Method setTestB = goodsClass.getDeclaredMethod("setTestB", Boolean.class);

        // 通过Method可以获取方法的信息，也可以通过Method调用对象的方法，基本方法有：
        // 获取方法的名称
        String name = setTestB.getName();

        // 判断当前程序是否有该方法的访问权限
        boolean accessible = setTestB.isAccessible();
        // 设置该方法的访问权限
        setTestB.setAccessible(true);

        // 在指定的对象上调用Method代表的方法，传递的参数列表为args
        Goods goods = new Goods(1546, "hh", "hhh", 12, 6999);
        Object invoke = setTestB.invoke(goods, true);
        /*
         * 对invoke方法，如果Method为静态方法，obj被忽略，可以为null,
         * args可以为null，也可以为一个空的数组，方法调用的返回值被包装为Object返回，
         * 如果实际方法调用抛出异常，异常被包装为InvocationTargetException重新抛出，可以通过getCause方法得到原异常。
         */

        String id = (String) getIdMethod.invoke(goods, null);
        System.out.println("id : "+ id);

        // 返回该方法所在类的全类名
        setTestB.getDeclaringClass().getName();

        Annotation[] declaredAnnotations = setTestB.getDeclaredAnnotations();
    }

    /**
     * <b> 创建对象和构造方法 </b>
     */
    public static void creatObj() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<?> goodsClass = getGoodsClass();
        assert goodsClass != null;

        // newInstance会调用类的默认构造方法（即无参public构造方法），如果类没有该构造方法，会抛出异常InstantiationException。
        Goods instance = (Goods) goodsClass.newInstance();

        // 返回所有的public构造方法，返回值可能是长度为0的空数组
        Constructor<?>[] constructors = goodsClass.getConstructors();

        // 返回所有的构造方法，包括非public的
        Constructor<?>[] declaredConstructors = goodsClass.getDeclaredConstructors();

        // 获取指定参数类型的public构造方法，没有找到抛出NoSuchMethodException异常
        Constructor<?> constructor = goodsClass.getConstructor(Integer.class);

        // 获取指定参数类型的构造方法，没有找到抛出NoSuchMethodException异常
        Constructor<?> declaredConstructor = goodsClass.getDeclaredConstructor(Integer.class);

        // 类Constructor表示构造方法，通过它可以创建对象，方法为：
        Goods newInstance = (Goods)constructor.newInstance(4564123);
        System.out.println("商品ID："+ newInstance.getId());

        // 除了创建对象，Constructor还有很多方法，可以获取关于构造方法的很多信息，包括参数、修饰符、注解等。
        // 注解
        Annotation[] declaredAnnotations = constructor.getDeclaredAnnotations();
        // 修饰符
        int modifiers = constructor.getModifiers();
        // 参数
        Parameter[] parameters = constructor.getParameters();
    }

    /**
     * <b> 类型检查和转换 </b>
     */
    public static void typeCheckAndConversion(List<String> list) throws ClassNotFoundException {
        Class<?> goodsClass = getGoodsClass();
        if (list instanceof ArrayList){
            ArrayList<String> other = (ArrayList<String>) list;
            System.out.println("instanceof array list");
        }

        /*
         * instanceof关键字可以用来判断变量指向的实际对象类型。
         * instanceof后面的类型是在代码中确定的，如果要检查的类型是动态的，可以使用Class类的isInstance方法。
         *
         * isInstance方法：
         * 确定指定的对象是否与此Class表示的对象兼容。这个方法是Java语言instanceof操作符的动态等价物。
         * 如果指定的对象参数为非null，并且可以强制转换为此类对象表示的引用类型而不引发ClassCastException，
         * 则该方法返回true。否则返回false。
         * 具体地说，如果这个Class对象表示一个声明的类，那么如果指定的对象参数是所表示的类（或其任何子类）的实例，那么这个方法返回true；否则返回false。
         * 如果此类对象表示数组类，则如果指定的对象参数可以通过标识转换或加宽引用转换转换为数组类的对象，则此方法返回true；否则返回false。
         * 如果这个类对象表示一个接口，那么如果指定对象参数的类或任何超类实现了这个接口，那么这个方法返回true；否则返回false。
         * 如果该类对象表示基元类型，则该方法返回false。
         */
        Class<?> cls = Class.forName("java.util.ArrayList");
        if (cls.isInstance(list)){
            // 类型强转
            // cast：将对象强制转换为此Class对象表示的类或接口。
            Object cast = cls.cast(list);
            System.out.println("array list");
        }

        // 判断Class之间的关系：检查参数里面的类型是否可以赋值为当前Class类型（cls类型）的变量。
        boolean assignableFrom = cls.isAssignableFrom(list.getClass());
        if (assignableFrom){
            Object cast = cls.cast(list);
        }
    }

    /**
     * <b> Class的类型信息 </b>
     *
     * <p> Class代表的类型既可以是普通的类，也可以是内部类，还可以是基本类型、数组等，对于一个给定的Class对象，它到底是什么类型呢？
     */
    public static <T> void classInfo(Class<T> cls){
        if (cls == null){
            System.out.println("参数cls为null");
            return;
        }

        // Class代表的类型既可以是普通的类，也可以是内部类，还可以是基本类型、数组等，
        // 对于一个给定的Class对象，它到底是什么类型呢？可以通过以下方法进行检查：
        // 是否是数组
        if (cls.isArray()){
            System.out.println("是数组");
        }
        // 是否是基本类型
        if (cls.isPrimitive()){
            System.out.println("是基本类型");
        }
        // 是否是枚举
        if (cls.isEnum()){
            System.out.println("是枚举");
        }
        // 是否是注解
        if (cls.isAnnotation()){
            System.out.println("是注解");
        }
        // 是否是匿名内部类
        if (cls.isAnonymousClass()){
            System.out.println("是匿名内部类");
        }
        // 是否是成员类，成员类定义在方法外，不是匿名类
        if (cls.isMemberClass()){
            System.out.println("是成员类");
        }
        // 是否是本地类，本地类定义在方法内，不是匿名类
        if (cls.isLocalClass()){
            System.out.println("是本地类");
        }
    }

    /**
     * <b> 类的声明信息 </b>
     *
     * <p> Class还有很多方法，可以获取类的声明信息，如修饰符、父类、接口、注解等
     */
    public static <T> void classDeclareInfo(Class<T> cls){
        if (cls == null){
            System.out.println("参数cls为null");
            return;
        }

        // 获取修饰符，返回值可以通过Modifier类进行解读
        int modifiers = cls.getModifiers();
        if (Modifier.isPublic(modifiers)){
            System.out.println("是公有类");
        }

        // 获取父类，如果父类为Object，返回值为null
        Class<? super T> superclass = cls.getSuperclass();

        // 对于类，返回自己声明实现的所有接口
        // 对于接口，返回自己直接扩展的接口，不包括通过父类继承的
        Class<?>[] interfaces = cls.getInterfaces();
        Type[] genericInterfaces = cls.getGenericInterfaces();

        // 自己声明的注解（本类声明的注解）
        Annotation[] declaredAnnotations = cls.getDeclaredAnnotations();
        // 所有的注解，包括继承得到的
        Annotation[] annotations = cls.getAnnotations();

        // 获取指定类型的注解，包括继承得到的
        MyAnnotation annotation = cls.getAnnotation(MyAnnotation.class);
        // 检查是声明了指定的注解，包括继承得到的
        boolean annotationPresent = cls.isAnnotationPresent(MyAnnotation.class);
    }

    /**
     * <b> 类的加载 </b>
     */
    public static Class<?> classLoad(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (StringUtils.isBlank(className)){
            return null;
        }
        /*
         * Class有两个静态方法，可以根据类名加载类
         *
         * 1. public static Class<?> forName(String className)
         *
         * 2. public static Class<?> forName(String name, boolean initialize, ClassLoader loader)
         * 参数initialize表示加载后，是否执行类的初始化代码（如static语句块）
         * loader表示类加载器
         *
         * 第一个方法中没有传这些参数，相当于调用：Class.forName(className, true, currentClassLoader)
         * currentLoader表示加载当前类的ClassLoader。
         *
         * 需要注意的是，基本类型不支持forName方法。如果这样写：Class.forName("int"); 会抛出异常ClassNotFoundException。
         */
        // 基本类型使用这种形式
        if ("int".equals(className)){
            return int.class;
        }
        // 其他基本类型类似，略。

        if (classLoader == null){
            return Class.forName(className);
        }else {
            return Class.forName(className, true, classLoader);
        }
    }


    /**
     * <b> 反射和数组 </b>
     *
     */
    public static void reflectAndArray(Class<?> cls){
        /*
         * 对于数组类型，有一个专门的方法，可以获取它的元素类型：
         * public native Class<?> getComponentType();
         */
        if (cls.isArray()){
            // 获取数组元素的类型
            Class<?> componentType = cls.getComponentType();
        }

        // java.lang.reflect包中有一个针对数组的专门的类Array（注意不是java.util中的Arrays)，
        // 提供了对于数组的一些反射支持，以便于统一处理多种类型的数组，主要方法有：

        // 创建指定元素类型、指定长度的数组
        Object o = Array.newInstance(cls, 10);
        // 创建多维数组
        Object o1 = Array.newInstance(cls, 2, 3);
        // 获取数组array指定索引位置index处的值value
        Object o2 = Array.get(o, 1);
        // 修改数组array指定的索引位置index处的值为value
        Array.set(o, 1, "love");
        // 返回数组的长度
        int length = Array.getLength(o);

        /*
         * 需要注意的是，在Array类中，数组是用Object而非Object[]表示的，这是为什么呢？这是为了方便处理多种类型的数组。
         * int[]、String[]都不能与Object[]相互转换，但可以与Object相互转换，
         */
    }

    /**
     * <b> 应用示例 </b>
     */
    public static void applicationExamples(Object obj){
        /*
         * 利用反射实现一个简单的通用序列化/反序列化类SimpleMapper ，它提供两个静态方法：
         * public static String toString(Object obj)
         * public static Object parse(String str)
         *
         * 为简单起见，我们只支持最简单的类，即有默认构造方法，成员类型只有基本类型、包装类或String。
         * 另外，序列化的格式也很简单，第一行为类的名称，后面每行表示一个字段，用字符'='分隔，表示字段名称和字符串形式的值
         */
        String string = SimpleMapper.toString(obj);
        System.out.println(string);

        Object parse = SimpleMapper.parse(string);
        if (parse!=null){
            System.out.println(parse);
        }
    }
    private static class SimpleMapper{
        /**
         * 将对象obj转换为字符串，序列化
         */
        public static String toString(Object obj){
            try {
                Class<?> cls = obj.getClass();
                StringBuilder sb = new StringBuilder();
                // 类名
                sb.append(cls.getName()).append("\n");
                // 组装本类的所有字段
                for (Field field : cls.getDeclaredFields()) {
                    if (!field.isAccessible()){
                        // 如果该字段不可访问，设为可访问
                        field.setAccessible(true);
                    }
                    if (Modifier.isStatic(field.getModifiers())){
                        // 静态字段不序列化
                        continue;
                    }
                    // 字段名称
                    sb.append(field.getName())
                            .append("=")
                            // 字段对应的值
                            .append(field.get(obj).toString())
                            .append("\n");
                }
                return sb.toString();
            } catch (Exception e) {
                log.error("toString序列化异常 error:{}", e.getMessage(), e);
            }
            return StringUtils.EMPTY;
        }

        /**
         * 将字符串转换为（反序列化为）对象
         */
        public static Object parse(String str){
            if (StringUtils.isBlank(str)){
                return null;
            }

            try {
                String[] lines = str.split("\n");
                if (lines.length < 1){
                    throw new IllegalArgumentException(str);
                }

                // 通过类名称加载类
                Class<?> cls = Class.forName(lines[0]);
                // 通过默认构造方法实例化一个对象
                Object obj = cls.newInstance();
                if (lines.length > 1){
                    for (int i=1; i<lines.length; i++){
                        // 分割得到字段名称和对应的值
                        String[] fv = lines[i].split("=");
                        if (fv.length != 2){
                            throw new IllegalArgumentException(lines[i]);
                        }
                        // 通过字段名称获取Field对象
                        Field field = cls.getDeclaredField(fv[0]);
                        if (!field.isAccessible()){
                            // 当前字段不可访问，设为可访问
                            field.setAccessible(true);
                        }
                        // 通过Field对象将指定对象obj中该字段的值设为fv[1]
                        setFieldValue(field, obj, fv[1]);
                    }
                }
                return obj;
            } catch (Exception e) {
                log.error("parse反序列化异常 error:{}", e.getMessage(), e);
            }
            return null;
        }

        /**
         * 通过Field对象将指定对象obj中该字段的值设为value
         * <p> setFieldValue根据字段的类型，将字符串形式的值转换为了对应类型的值，对于基本类型和String以外的类型，
         * 它假定该类型有一个以String类型为参数的构造方法。
         */
        private static void setFieldValue(Field field, Object obj, String value)throws Exception{
            // 获取该字段的类型
            Class<?> type = field.getType();

            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers)){
                // 如果是静态字段不用设值：特别是静态常量，设值会出错
                return;
            }

            if (type == int.class){
                field.setInt(obj, Integer.parseInt(value));
            }else if (type == byte.class){
                field.setByte(obj, Byte.parseByte(value));
            }else if (type == short.class){
                field.setShort(obj, Short.parseShort(value));
            }else if (type == long.class){
                field.setLong(obj, Long.parseLong(value));
            }else if (type == float.class){
                field.setFloat(obj, Float.parseFloat(value));
            }else if (type == double.class){
                field.setDouble(obj, Double.parseDouble(value));
            }else if (type == char.class){
                field.setChar(obj, value.charAt(0));
            }else if (type == boolean.class){
                field.setBoolean(obj, Boolean.parseBoolean(value));
            }else if (type == String.class){
                field.set(obj, value);
            }else {
                Constructor<?> constructor = type.getConstructor(new Class[]{String.class});
                field.set(obj, constructor.newInstance(value));
            }
        }
    }


    /**
     * <b> 反射和泛型 </b>
     */
    public static void reflectAndGeneric(){}


    public static Class<?> getGoodsClass(){
        try {
            return Class.forName("com.dongfeng.study.basicstudy.Goods");
        } catch (ClassNotFoundException e) {
            log.error("getGoodsClass error:{}", e.getMessage(), e);
        }
        return null;
    }

}
