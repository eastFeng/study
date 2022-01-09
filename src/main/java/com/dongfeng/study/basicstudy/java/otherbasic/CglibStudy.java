package com.dongfeng.study.basicstudy.java.otherbasic;

import com.dongfeng.study.bean.vo.Good;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.beans.ImmutableBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2022-01-09 12:03
 */
public class CglibStudy {
    public static void main(String[] args) {

        /*
         *
         * CGLIB是一个功能强大，高性能的代码生成包。它为没有实现接口的类提供代理，为JDK的动态代理提供了很好的补充。
         * 通常可以使用Java的动态代理创建代理，但当要代理的类没有实现接口或者为了更好的性能，CGLIB是一个好的选择。
         *
         * 其被广泛应用于AOP框架（Spring、dynaop）中，用以提供方法拦截操作。
         *
         * CGLIB底层使用了ASM（一个短小精悍的字节码操作框架）来操作字节码生成新的类。
         *
         * CGLIB代理主要通过对字节码的操作，为对象引入间接级别，以控制对象的访问
         * CGLIB相比于JDK动态代理更加强大，JDK动态代理虽然简单易用，但是其有一个致命缺陷是，只能对接口进行代理。
         * 如果要代理的类为一个普通类、没有接口，那么Java动态代理就没法使用了。
         */

        /*
         * 有几个功能非常好用：
         * 1. 对象拷贝 （对象复制）
         * 2. 创建不可变Bean，非常好用，避免缓存被其他人篡改
         * 3. Map和对象互相转换
         */

        // 1. 对象拷贝 （对象复制）
//        beanCopy();

        // 2. 创建不可变Bean，非常好用，避免缓存被其他人篡改
//        immutableBean();

        // 3. Map和对象互相转换
        mapAndBeanConvert();
    }

    /**
     * 1. 对象拷贝 （对象复制）
     */
    private static void beanCopy(){
        // 方法一：使用Apache的工具类BeanUtils#copyPropertie
        /*
         * Apache BeanUtils#copyPropertie
         * 特点：
         * 浅拷贝，属性还是指向原本对象的引用
         * 字段名称相同，类型不同无法进行赋值
         * 基本类型字段和引用对象可以映射
         */
        Goods goods = Goods.getGoods();
        Good good = new Good();
        goods.setId(123);
        goods.setName("hh");
        goods.setNumber(456);
        goods.setPrice(12);
        goods.setTestA(2222);

        // 第一个参数是source，第二个参数是target
        BeanUtils.copyProperties(goods, good);
        System.out.println("使用Apache的工具类BeanUtils#copyPropertie:");
        System.out.println(good);
        System.out.println();

        // 使用Spring自带的cglib 进行对象复制
        // 第一个参数是source，第二个参数是target
        BeanCopier copier = BeanCopier.create(Goods.class, Good.class, false);
        Good good1 = new Good();
        // 第一个参数是source，第二个参数是target
        copier.copy(goods, good1, null);
        System.out.println("使用Spring自带的cglib 进行对象复制：");
        System.out.println(good);
    }

    /**
     * 2. 创建不可变Bean，非常好用，避免缓存被其他人篡改
     */
    private static void immutableBean(){
        Good good = new Good();
        good.setType("手机");
        Good immutableGood = (Good) ImmutableBean.create(good);

        // 下面这步会报错
//        immutableGood.setType("电脑");
        // 下面这步也会报错
        immutableGood.setName("huawei");


    }

    /**
     * 3. Map和对象互相转换
     */
    private static void mapAndBeanConvert(){
        // 对象转map，可以重新封装，也可以直接用
        Goods goods = Goods.getGoods();
        goods.setId(123);
        goods.setName("hh");
        goods.setNumber(456);
        goods.setPrice(12);
        goods.setTestA(2222);

        Map<String, Object> map = new HashMap<>();
        map.putAll(BeanMap.create(goods));
        Map<String, Object> beanMap = BeanMap.create(goods);
        System.out.println(map);
        System.out.println("-----------------------------------");
        System.out.println(beanMap);


        // map转对象
        Good good = new Good();
        BeanMap.create(good).putAll(map);
        System.out.println("map转对象---------------------------");
        System.out.println(good);
    }
}
