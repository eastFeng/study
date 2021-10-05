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
        List<Goods> goodsList = new ArrayList<>(10);
        goodsList.add(new Goods(10006,"手机","小米10",10,6500));
        goodsList.add(new Goods(10007,"电脑","联想小新Air14",20,6500));
        goodsList.add(new Goods(10007,"电脑","联想拯救者Y7000P",20,12999));
        goodsList.add(new Goods(10008,"键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
        goodsList.add(new Goods(10009,"键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
        goodsList.add(new Goods(10010,"鼠标","戴尔MS116有线商务办公鼠标",40,30.90));

        List<Goods> collect =
                goodsList.stream().filter(goods -> !goods.getTestB()).collect(Collectors.toList());
        for (Goods goods : collect) {
            System.out.println(goods);
        }

//        List<Goods> collect1 = goodsList.stream().sorted(Comparator.comparingDouble(Goods::getPrice).reversed())
//                .collect(Collectors.toList());
//
//        System.out.println(collect1.size());
//
//        test_Collectors_group(goodsList);
//
//        //自定义比较器
//        goodsList.sort(new GoodsComparator().reversed());
//        goodsList.sort((g1, g2)->{
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


    private static boolean isNormal(double price){
        return price<1000;
    }


    /**
     * list遍历
     * @param goodsList
     */
    public static void testListForEach(List<Goods> goodsList){
        goodsList.forEach(System.out::println);
    }

    /**
     * 遍历
     */
    public static void test_forEach(List<Goods> goodsList){
        goodsList.stream().forEach(System.out::println);
    }


    /**
     * 根据条件过滤
     * <p> Stream接口的filter方法:
     * <p> Stream<T> filter(Predicate<? super T> predicate);
     * <p> filter方法需传入一个Predicate类型的参数, 返回值是T类型的流Stream<T>,所以这是个中间操作。
     * <p> Predicate<T>接口是一个函数式接口,只有一个抽象接口:  boolean test(T t); 传入类型的值,返回boolean值
     * 所以可以用lambda表达式提供一个Predicate类型的对象。
     *
     * <p> 注: 因为可以根据T的所有属性进行过滤,所以是输入是: ? super T
     */
    private static void testFilter(List<Goods> goodsList){
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
    }

    /**
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
    public static void test_sorted(List<Goods> goodsList){
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
     * list排序
     */
    public static void test_list_sort(List<Goods> goodsList){
        List<Integer> integerList = Arrays.asList(1, 2, 7, 8, 9, 3, 2);
        integerList.sort(Comparator.comparingInt(Integer::intValue));
        System.out.println(integerList);

        goodsList.sort(Comparator.comparingDouble(Goods::getPrice));
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

    /**
     * 映射(转换)元素到对应的结果
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
    private static void test_map(List<Goods> goodsList){
        List<String> nameList = goodsList.stream().map(Goods::getName).collect(Collectors.toList());
        System.out.println(nameList);

        List<String> stringList = goodsList.stream().map(T -> "Hello " + T.getName()).collect(Collectors.toList());
        System.out.println(stringList);

        List<Double> doubleList = goodsList.stream().map(T -> T.getPrice() * 10).collect(Collectors.toList());
        System.out.println(doubleList);
    }


    /**
     * Collectors list
     */
    private static void test_Collectors_list(List<Goods> goodsList){
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
     * Collectors set
     */
    private static void test_Collectors_set(List<Goods> goodsList){
        //形式一: 默认的set接口的实现类是HashSet
        Set<String> stringSet = goodsList.stream().map(Goods::getType).collect(Collectors.toSet());
        System.out.println(stringSet);

        //形式二: 可以自定义Set接口的实现类
        TreeSet<String> typeTreeSet =
                goodsList.stream().map(Goods::getType)
                        .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(typeTreeSet);
    }

    /**
     * Collectors.toMap
     */
    private static void test_Collectors_map(List<Goods> goodsList){
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
     * Collectors.summingXXX  求和
     */
    private static void test_Collectors_summing(List<Goods> goodsList){
        // 计算所有商品的价格总和
        Double totalPrices = goodsList.stream().collect(Collectors.summingDouble(Goods::getPrice));
        System.out.println("totalPrices: "+ MathUtil.round(totalPrices,2));

        // 计算所有商品的数量总和
        int totalNumber = goodsList.stream().mapToInt(Goods::getNumber).sum();
        System.out.println("totalNumber: "+totalNumber);
    }

    /**
     * <p> 分组 </p>
     * Collectors.groupingBy ----挺有用
     */
    private static void test_Collectors_group(List<Goods> goodsList){
        List<String> stringList = Arrays.asList("河南", "河南", "河南", "上海", "上海", "山西");
        // 根据字符串分组并计算每个分组的个数
        Map<String, Long> stringLongMap = stringList.stream()
                .collect(Collectors.groupingBy(String::new, Collectors.counting()));
        stringLongMap.forEach((k,v)->{
            System.out.println(k+" : "+v);
        });
        System.out.println();

        // 根据type字段对Goods进行分组
        Map<String, List<Goods>> byType = goodsList.stream().collect(Collectors.groupingBy(Goods::getType));
        byType.forEach((k,v) -> {
            System.out.println(k+" : "+v);
        });
        System.out.println();

        // 按类型计算商品总数量
        Map<String, Integer> totalNumByType =
                goodsList.stream()
                        .collect(Collectors.groupingBy(Goods::getType, Collectors.summingInt(Goods::getNumber)));
        totalNumByType.forEach((k,v) -> {
            System.out.println(k+" : "+v);
        });
    }

    /**
     * Collectors.joining
     */
    private static void test_Collectors_joining(List<Goods> goodsList){
        //将元素转换为字符串并将它们连接起来，用逗号分隔
        String joinString = goodsList.stream().map(Goods::toString).collect(Collectors.joining(" , "));
        System.out.println(joinString);

        //将商品名称连接起来,并用---符号隔开
        String nameJoin = goodsList.stream().map(Goods::getName).collect(Collectors.joining("---"));
        System.out.println(nameJoin);
    }

    /**
     * 按照条件分割(分隔)为两部分
     */
    private static void test_Collectors_partitioningBy(List<Goods> goodsList){
        //把商品分为价格大于5000的和小于等于5000的
        final Map<Boolean, List<Goods>> pricePartition =
                goodsList.stream().collect(Collectors.partitioningBy(T -> T.getPrice() > 5000));
        pricePartition.forEach((k,v) -> System.out.println(k+" : "+v));
    }
}
