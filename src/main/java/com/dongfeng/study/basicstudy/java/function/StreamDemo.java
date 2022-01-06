package com.dongfeng.study.basicstudy.java.function;

import com.dongfeng.study.basicstudy.java.otherbasic.Goods;
import com.dongfeng.study.util.MathUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author eastFeng
 * @date 2020/4/16 - 15:48
 */
public class StreamDemo {
    public static void main(String[] args) {

        // Stream 介绍
        streamIntroduction();

        // Stream 用法
        streamTest();
    }

    public static void streamIntroduction(){
        /*
         * Java 8中的 Stream 是对集合(Collection)对象功能的增强,
         * 它专注于对集合对象进行各种非常便利、高效的聚合操作(aggregate operation),或者大批量数据操作。
         *
         * Stream流的特点：
         * 1. 不是数据结构，不会保存数据。 Java中的Stream并不会存储元素，而是按需计算。
         * 2. 数据源 : 流的来源。可以是集合，数组，I/O channel， 产生器generator等。
         * 3. 不会修改原来的数据源，它会将操作后的数据保存到另外一个对象中。
         * 4. 惰性求值，流在中间处理过程中，只是对操作进行了记录，并不会立即执行，需要等到执行终止操作的时候才会进行实际的计算。
         * 5. 聚合操作 : 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。
         *
         * 和以前的Collection操作不同，Stream操作还有两个基础的特征：
         * Pipelining: 中间操作都会返回流对象本身。 这样多个操作可以串联成一个管道， 如同流式风格（fluent style）。
         *             这样做可以对操作进行优化， 比如延迟执行(laziness)和短路( short-circuiting)。
         * 内部迭代 : 以前对集合遍历都是通过Iterator或者For-Each的方式, 显式的在集合外部进行迭代， 这叫做外部迭代。
         *           Stream提供了内部迭代的方式， 通过访问者模式(Visitor)实现。
         *
         * Stream操作分为中间操作和终结操作：
         * 1. 中间操作，可以有多个，每次返回一个新的流，可进行链式操作。
         *    比如：filter（过滤操作）、sorted（排序操作）、map（映射操作）、limit（指定数量操作）等等。
         * 2. 终结操作，只能有一个，每次执行完，这个流也就用光光了，无法执行下一个操作，因此只能放在最后。
         *    比如：collect（归约操作）、sum（求和操作）、forEach（迭代操作）等等。
         *
         *
         *
         * 在 Java 8 中, 集合接口有两个方法来生成流：
         * stream() − 为集合创建串行流。
         * parallelStream() − 为集合创建并行流。
         *
         * default Stream<E> stream() {
         *     return StreamSupport.stream(spliterator(), false);
         * }
         *
         * default Stream<E> parallelStream() {
         *     return StreamSupport.stream(spliterator(), true);
         * }
         */

        /*
         * 【Java中Stream提供的中间操作和终止操作】
         *
         * 中间操作
         * 1. 筛选与切片
         *         filter：过滤流中的某些元素
         *         limit(n)：获取n个元素
         *         skip(n)：跳过n元素，配合limit(n)可实现分页
         *         distinct：通过流中元素的 hashCode() 和 equals() 去除重复元素
         * 2. 映射
         *         map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
         *         flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。
         * 3. 排序
         *         sorted()：自然排序，流中元素需实现Comparable接口
         *         sorted(Comparator com)：定制排序，自定义Comparator排序器
         * 4. 消费
         *         peek：如同于map，能得到流中的每一个元素。但map接收的是一个Function表达式，有返回值；
         *              而peek接收的是Consumer表达式，没有返回值。
         *
         *
         * 终止操作
         * 1. 匹配、聚合操作
         *         allMatch：接收一个 Predicate 函数，当流中每个元素都符合该断言时才返回true，否则返回false
         *         noneMatch：接收一个 Predicate 函数，当流中每个元素都不符合该断言时才返回true，否则返回false
         *         anyMatch：接收一个 Predicate 函数，只要流中有一个元素满足该断言则返回true，否则返回false
         *         findFirst：返回流中第一个元素
         *         findAny：返回流中的任意元素
         *         count：返回流中元素的总个数
         *         max：返回流中元素最大值
         *         min：返回流中元素最小值
         * 2. 规约操作
         *         Optional<T> reduce(BinaryOperator<T> accumulator)：第一次执行时，accumulator函数的第一个参数为流中的第一个元素，
         *         第二个参数为流中元素的第二个元素；第二次执行时，第一个参数为第一次函数执行的结果，第二个参数为流中的第三个元素；依次类推。
         *         T reduce(T identity, BinaryOperator<T> accumulator)：流程跟上面一样，只是第一次执行时，
         *         accumulator函数的第一个参数为identity，而第二个参数为流中的第一个元素。
         *         <U> U reduce(U identity,BiFunction<U, ? super T, U> accumulator,BinaryOperator<U> combiner)：
         *         在串行流(stream)中，该方法跟第二个方法一样，即第三个参数combiner不会起作用。
         *         在并行流(parallelStream)中,我们知道流被fork join出多个线程进行执行，此时每个线程的执行流程就跟第二个方法reduce(identity,accumulator)一样，
         *         而第三个参数combiner函数，则是将每个线程的执行结果当成一个新的流，然后使用第一个方法reduce(accumulator)流程进行规约。
         * 3. 收集操作
         *         collect：接收一个Collector实例，将流中元素收集成另外一个数据结构。
         *         Collector<T, A, R> 是一个接口，有以下5个抽象方法：
         *             Supplier<A> supplier()：创建一个结果容器A
         *             BiConsumer<A, T> accumulator()：消费型接口，第一个参数为容器A，第二个参数为流中元素T。
         *             BinaryOperator<A> combiner()：函数接口，该参数的作用跟上一个方法(reduce)中的combiner参数一样，将并行流中各                                                                 个子进程的运行结果(accumulator函数操作后的容器A)进行合并。
         *             Function<A, R> finisher()：函数式接口，参数为：容器A，返回类型为：collect方法最终想要的结果R。
         *             Set<Characteristics> characteristics()：返回一个不可变的Set集合，用来表明该Collector的特征。有以下三个特征：
         *                 CONCURRENT：表示此收集器支持并发。（官方文档还有其他描述，暂时没去探索，故不作过多翻译）
         *                 UNORDERED：表示该收集操作不会保留流中元素原有的顺序。
         *                 IDENTITY_FINISH：表示finisher参数只是标识而已，可忽略。
         *    3.1 Collector工具库：Collectors
         */
    }

    public static void streamTest(){
        List<Goods> goodsList = new ArrayList<>(10);
        goodsList.add(new Goods(10006,"手机","小米10",10,6500));
        goodsList.add(new Goods(10007,"电脑","联想小新Air14",20,6500));
        goodsList.add(new Goods(10007,"电脑","联想拯救者Y7000P",20,12999));
        goodsList.add(new Goods(10008,"键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
        goodsList.add(new Goods(10009,"键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
        goodsList.add(new Goods(10010,"鼠标","戴尔MS116有线商务办公鼠标",40,30.90));

//        // Stream: filter 过滤操作
//        streamFilter(goodsList);
//
//        // Stream: forEach 迭代操作
//        streamForEach(goodsList);
//
//        // Stream: sorted 排序操作
//        streamSorted(goodsList);
//
//        // Stream: map 映射操作
//        streamMap(goodsList);
//
//        // Stream: sum 求和操作
//        streamSum(goodsList);
//
//        // Stream: collect 收集操作
//        streamCollectorsList(goodsList);
//        streamCollectorsSet(goodsList);
//        streamCollectorsMap(goodsList);
//        streamCollectorsJoining(goodsList);
//        streamCollectorsGroup(goodsList);
//        streamCollectorsPartitioningBy(goodsList);


        // ----------------  list --------------- //
        // list 遍历
//        listForEach(goodsList);

        List<Goods> goodsList1 = new ArrayList<>(10);
        goodsList1.add(new Goods(10006,"手机","小米10",10,6500));
        goodsList1.add(new Goods(10007,"电脑","联想小新Air14",20,6500));
        goodsList1.add(new Goods(10007,"电脑","联想拯救者Y7000P",20,12999));
        goodsList1.add(new Goods(10008,"键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
        goodsList1.add(new Goods(10009,"键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
        goodsList1.add(new Goods(10010,"鼠标","戴尔MS116有线商务办公鼠标",40,30.90));

        List<Goods> goodsList2 = new ArrayList<>(10);
        goodsList2.add(new Goods(10006,"手机","小米10",10,6500));
        goodsList2.add(new Goods(10007,"电脑","联想小新Air14",20,6500));
        goodsList2.add(new Goods(10007,"电脑","联想拯救者Y7000P",20,12999));
        goodsList2.add(new Goods(10008,"键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
        goodsList2.add(new Goods(10009,"键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
        goodsList2.add(new Goods(10010,"鼠标","戴尔MS116有线商务办公鼠标",40,30.90));

        List<Goods> goodsList3 = new ArrayList<>(10);
        goodsList3.add(new Goods(10006,"手机","小米10",10,6500));
        goodsList3.add(new Goods(10007,"电脑","联想小新Air14",20,6500));
        goodsList3.add(new Goods(10007,"电脑","联想拯救者Y7000P",20,12999));
        goodsList3.add(new Goods(10008,"键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
        goodsList3.add(new Goods(10009,"键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
        goodsList3.add(new Goods(10010,"鼠标","戴尔MS116有线商务办公鼠标",40,30.90));
        // list 排序
        listSort(goodsList1, goodsList2, goodsList3);
    }


    private static boolean isNormal(double price){
        return price<1000;
    }


    /**
     * list forEach 遍历
     */
    public static void listForEach(List<Goods> goodsList){
        goodsList.forEach(System.out::println);
    }

    /**
     * stream forEach 遍历
     */
    public static void streamForEach(List<Goods> goodsList){
        goodsList.stream().forEach(System.out::println);
    }

    /**
     * list sort 排序
     */
    public static void listSort(List<Goods> list1, List<Goods> list2, List<Goods> list3){
        List<Integer> integerList = Arrays.asList(1, 2, 7, 8, 9, 3, 2);
        integerList.sort(Comparator.comparingInt(Integer::intValue));
        System.out.println(integerList);

        // 根据商品价格进行正序排序
        list1.sort(Comparator.comparingDouble(Goods::getPrice));
        System.out.println("根据商品价格进行正序排序 ：");
        list1.forEach(goods -> System.out.println("价格："+goods.getPrice()+"  "+ goods));
        System.out.println("-----------------------------------------------");
        System.out.println();

        // 根据商品价格进行倒序排序
        list2.sort(Comparator.comparingDouble(Goods::getPrice).reversed());
        System.out.println("根据商品价格进行倒序排序 ：");
        list2.forEach(goods -> System.out.println("价格："+goods.getPrice()+"  "+ goods));
        System.out.println("-----------------------------------------------");
        System.out.println();

        // 自定义比较器
        list3.sort(new GoodsComparator().reversed());
        System.out.println("自定义比较器 : ");
        list3.forEach(goods -> System.out.println("价格："+goods.getPrice()+"  "+ goods));
        System.out.println("-----------------------------------------------");

        // goodsList.sort((g1, g2)->{
//            // 先按价格进行比较
//            if (g1.getPrice() != g2.getPrice()){
//                return g1.getPrice() > g2.getPrice() ? 1 : -1;
//            }
//
//            // 价格一样, 再按数量进行比较
//            if (!g1.getNumber().equals(g2.getNumber())){
//                return g1.getNumber() > g2.getNumber() ? 1 : -1;
//            }
//
//            // 价格和数量都一样
//            return 0;
//        });
//        goodsList.forEach(System.out::println);
//        goodsList.forEach(t->{
//            System.out.println(t.getName() +" : "+t.getPrice());
//        });
    }

    /**
     * stream filter 根据条件过滤
     * <p> Stream接口的filter方法:
     * <p> Stream<T> filter(Predicate<? super T> predicate);
     * <p> filter方法需传入一个Predicate类型的参数, 返回值是T类型的流Stream<T>,所以这是个中间操作。
     * <p> Predicate<T>接口是一个函数式接口,只有一个抽象接口:  boolean test(T t); 传入类型的值,返回boolean值
     * 所以可以用lambda表达式提供一个Predicate类型的对象。
     *
     * <p> 注: 因为可以根据T的所有属性进行过滤,所以是输入是: ? super T
     */
    private static void streamFilter(List<Goods> goodsList){
        // 过滤出所有满足条件的数据
        List<Goods> goods = goodsList.stream().filter(T -> T.getPrice() <= 10).collect(Collectors.toList());
        System.out.println(goods);

        // 根据条件返回一个数据
        Goods goods1 = goodsList.stream().filter(T -> T.getType().equals("电脑")).findFirst().orElse(null);
        if (goods1 == null){
            System.out.println("no goods type is 电脑");
        }else {
            System.out.println(goods1);
        }

        List<String> stringList = new ArrayList<>(10);
        // 根据条件返回一个数据
        String hh = stringList.stream().filter(T -> T.equals("hh")).findFirst().orElse(null);
        System.out.println(hh);

        List<Goods> collect = goodsList.stream().filter(g -> !g.getTestB()).collect(Collectors.toList());
        for (Goods goods2 : collect) {
            System.out.println(goods2);
        }
    }

    /**
     * stream sorted 根据条件排序
     * <p> 1. Stream接口中的sorted方法:
     * <p> Stream<T> sorted(Comparator<? super T> comparator)
     * 该方法需要传入一个Comparator类型的参数
     *
     * <p> 2. Comparator接口中的静态方法comparing方法:
     * <pre> public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
     *             Function<? super T, ? extends U> keyExtractor)
     *     {
     *         Objects.requireNonNull(keyExtractor);
     *         return (Comparator<T> & Serializable)
     *             (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
     *     } </pre>
     * 该方法是静态方法,所以可以直接被Comparator接口调用, 该方法返回一个Comparator对象
     */
    public static void streamSorted(List<Goods> goodsList){
        // test1
        List<Goods> goods = goodsList.stream().sorted(Comparator.comparingDouble(Goods::getPrice)).collect(Collectors.toList());
        System.out.println(goods);

        List<Integer> integerList = Arrays.asList(1, 2, 7, 8, 9, 3, 2);
        // test2
        List<Integer> collect =
                integerList.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * stream map 映射(转换)元素到对应的结果
     *
     * <p> Stream接口的map方法:
     * <p> <R> Stream<R> map(Function<? super T, ? extends R> mapper);
     * <p> map方法需传入一个Function类型的参数, 返回值是R类型的流Stream<R>,所以这是个中间操作。
     * <p> Function<T, R>接口是一个函数式接口,只有一个抽象接口: R apply(T t); 传入T类型的值返回R类型的值
     * 所以可以用lambda表达式提供一个Function类型的对象。
     *
     * <p> 注: 因为可以对T所有的属性进行映射(操作),所以输入是: ? super T
     *    指定返回的Stream类型是R,所以输出类型的上界是R: ? extends R
     */
    private static void streamMap(List<Goods> goodsList){
        List<String> nameList = goodsList.stream().map(Goods::getName).collect(Collectors.toList());
        System.out.println(nameList);

        List<String> stringList = goodsList.stream().map(T -> "Hello " + T.getName()).collect(Collectors.toList());
        System.out.println(stringList);

        List<Double> doubleList = goodsList.stream().map(T -> T.getPrice() * 10).collect(Collectors.toList());
        System.out.println(doubleList);
    }

    /**
     * stream sum 求和
     */
    private static void streamSum(List<Goods> goodsList){
        // 计算所有商品的价格总和
        Double totalPrices = goodsList.stream().collect(Collectors.summingDouble(Goods::getPrice));
        System.out.println("计算所有商品的价格总和:");
        System.out.println("totalPrices: "+ MathUtil.round(totalPrices,2));
        System.out.println();

        // 计算所有商品的数量总和
        int totalNumber = goodsList.stream().mapToInt(Goods::getNumber).sum();
        System.out.println("计算所有商品的数量总和:");
        System.out.println("totalNumber: "+totalNumber);
    }


    /**
     * Collectors.toList
     * <p> 收集操作，将流转换成List集合
     */
    private static void streamCollectorsList(List<Goods> goodsList){
        // 形式一: 默认的List接口的实现类是ArrayList
        List<Goods> sortedList = goodsList.stream().sorted(Comparator.comparing(Goods::getPrice)).collect(Collectors.toList());

        // 形式二: 自定义List接口的实现类
        LinkedList<Goods> goodsLinkedList =
                goodsList.stream().
                        sorted(Comparator.comparing(Goods::getPrice))
                        .collect(Collectors.toCollection(LinkedList::new));

        // Accumulate names into a List
        List<String> nameList = goodsList.stream().map(Goods::getName).collect(Collectors.toList());
        System.out.println(nameList);
    }


    /**
     * Collectors.toSet
     * <p> 收集操作，将流转换成Set集合
     */
    private static void streamCollectorsSet(List<Goods> goodsList){
        // 形式一: 默认的set接口的实现类是HashSet
        Set<String> stringSet = goodsList.stream().map(Goods::getType).collect(Collectors.toSet());
        System.out.println(stringSet);

        // 形式二: 可以自定义Set接口的实现类
        TreeSet<String> typeTreeSet =
                goodsList.stream().map(Goods::getType)
                        .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(typeTreeSet);
    }

    /**
     * Collectors.toMap
     * <p> 收集操作，将流转换成Map集合
     */
    private static void streamCollectorsMap(List<Goods> goodsList){
        Map<Integer, Double> collect = goodsList.stream().
                collect(Collectors.toMap(goods -> goods.getNumber(), Goods::getPrice));
        collect.forEach((k,v)->{
            System.out.println(k+" = "+"["+v+"]");
        });
        System.out.println();

        // key是Goods的name, Value是Goods本身(Function.identity())
        Map<String, Goods> goodsMap = goodsList.stream()
                .collect(Collectors.toMap(Goods::getName, Function.identity()));
        goodsMap.forEach((k,v)->{
            System.out.println(k+" = "+v);
        });
    }

    /**
     * Collectors.joining
     * <p> 收集操作，返回字符串
     */
    private static void streamCollectorsJoining(List<Goods> goodsList){
        // 将元素转换为字符串并将它们连接起来，用逗号分隔
        String joinString = goodsList.stream().map(Goods::toString).collect(Collectors.joining(" , "));
        System.out.println("将元素转换为字符串并将它们连接起来，用逗号分隔 :");
        System.out.println(joinString);
        System.out.println("------------------------------------------------");
        System.out.println();

        // 将商品名称连接起来,并用---符号隔开
        String nameJoin = goodsList.stream().map(Goods::getName).collect(Collectors.joining("---"));
        System.out.println("将商品名称连接起来,并用---符号隔开 :");
        System.out.println(nameJoin);
    }

    /**
     * Collectors.groupingBy
     * <p> 收集操作，分组 ----挺有用
     */
    private static void streamCollectorsGroup(List<Goods> goodsList){
        List<String> stringList = Arrays.asList("河南", "河南", "河南", "上海", "上海", "山西");
        // 根据字符串分组并计算每个分组的个数
        Map<String, Long> stringLongMap = stringList.stream()
                .collect(Collectors.groupingBy(String::new, Collectors.counting()));
        System.out.println("根据字符串分组并计算每个分组的个数:");
        stringLongMap.forEach((k,v)->{
            System.out.println(k+" : "+v);
        });
        System.out.println();

        // 根据type字段对Goods进行分组
        Map<String, List<Goods>> byType = goodsList.stream().collect(Collectors.groupingBy(Goods::getType));
        System.out.println("根据type字段对Goods集合进行分组:");
        byType.forEach((k,v) -> {
            System.out.println(k+" : "+v);
        });
        System.out.println();

        // 按类型计算商品总数量
        Map<String, Integer> totalNumByType =
                goodsList.stream()
                        .collect(Collectors.groupingBy(Goods::getType, Collectors.summingInt(Goods::getNumber)));
        System.out.println("按类型计算商品总数量:");
        totalNumByType.forEach((k,v) -> {
            System.out.println(k+" : "+v);
        });
    }

    /**
     * Collectors.partitioningBy
     * <p> 按照条件分割(分隔)为两部分
     */
    private static void streamCollectorsPartitioningBy(List<Goods> goodsList){
        // 把商品分为价格大于5000的和小于等于5000的
        final Map<Boolean, List<Goods>> pricePartition =
                goodsList.stream().collect(Collectors.partitioningBy(T -> T.getPrice() > 5000));
        System.out.println("把商品分为价格大于5000的和小于等于5000的:");
        pricePartition.forEach((k,v) -> System.out.println(k+" : "+v));
    }
}
