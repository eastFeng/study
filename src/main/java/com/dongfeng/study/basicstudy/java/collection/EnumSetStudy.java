package com.dongfeng.study.basicstudy.java.collection;

import com.dongfeng.study.bean.enums.ResponseCodeEnum;

import java.util.EnumSet;

/**
 * <b> {@link java.util.EnumSet}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-29 1:00
 */
public class EnumSetStudy {
    public static void main(String[] args) {
        /*
         * Set接口的实现类HashSet/TreeSet，它们内部都是用对应的HashMap/TreeMap实现的，
         * 但EnumSet不是，它的实现与EnumMap没有任何关系，而是用极为精简和高效的位向量实现的。
         * 位向量是计算机程序中解决问题的一种常用方式，有必要理解和掌握。
         *
         * 除了实现机制，EnumSet的用法也有一些不同。EnumSet可以说是处理枚举类型数据的一把利器，
         * 在一些应用领域，它非常方便和高效。
         */
        // EnumSet的基本用法
        basicUsage();

        /**
         *
         */
    }

    /**
     * EnumSet的基本用法
     */
    public static void basicUsage(){
        /*
         * 与TreeSet和HashSet不同，EnumSet是一个抽象类，不能通过new新建。
         * 不过EnumSet提供了若干静态工厂方法，可以创建EnumSet类型的对象。
         */
        // noneOf方法会创建一个指定枚举类型的EnumSet，不含任何元素。创建的EnumSet对象的实际类型是EnumSet的子类
        EnumSet<ResponseCodeEnum> noneOf = EnumSet.noneOf(ResponseCodeEnum.class);

        // 初始集合包括指定枚举类型的所有枚举值
        EnumSet<ResponseCodeEnum> allOf = EnumSet.allOf(ResponseCodeEnum.class);

        EnumSet<ResponseCodeEnum> success = EnumSet.of(ResponseCodeEnum.SUCCESS,
                ResponseCodeEnum.PARAM_IS_EMPTY,
                ResponseCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
    }
}


















































