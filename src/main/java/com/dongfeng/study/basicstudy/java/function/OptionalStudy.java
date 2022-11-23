package com.dongfeng.study.basicstudy.java.function;

import com.dongfeng.study.basicstudy.java.otherbasic.Goods;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@link java.util.Optional}学习
 *
 * @author eastFeng
 * @date 2022-01-08 15:27
 */
public class OptionalStudy {
    public static void main(String[] args) {
        // 过多的非空（not null）判断会使我们的代码臃肿不堪

        /*
         * 使用Optional可以更加优雅的避免空指针异常
         * Optional就好像是包装类，可以把我们具体数据封装Optional对象内部，
         * 然后使用Optional中的方法操作封装进去的数据，就可以非常优雅的避免空指针异常。
         */

        Goods goods = Goods.getGoods();
        // Optional类的静态方法ofNullable可以把数据封装成一个Optional对象
        Optional<Goods> goodsOptional = Optional.ofNullable(goods);
        goodsOptional.ifPresent(g -> System.out.println(g.getId()));
        List<Integer> integerList = new ArrayList<>(10);
        goodsOptional.ifPresent(g -> integerList.add(g.getId()));
        integerList.forEach(System.out::println);
    }



}
