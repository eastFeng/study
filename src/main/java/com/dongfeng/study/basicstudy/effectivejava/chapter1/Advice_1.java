package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * 第一条: 用静态工厂方法代替构造器
 * @author eastFeng
 * @date 2020/1/11 - 15:00
 */
public class Advice_1 {

    public static void main(String[] args) {
        Boolean aTrue = Boolean.valueOf("true");

    }

    /**
     * 静态工厂方法与构造器不同的五大优势:
     * 第一: 它们有名称;
     * 第二: 不必再每次调用它们的时候都调用一个新的对象;
     * 第三: 它们可以返回原返回类型的任何子类型的对象;
     * 第四: 所返回的对象的类可以随着每次调用而发生变化,这取决于静态工厂方法的参数值;
     * 第五: 方法返回的对象所属的类,在编写包含该静态工厂方法的类时可以不存在;
     *
     * 静态工厂方法的主要缺点:
     * 第一: 类如果不含公有的或者受保护的构造器,就不能被子类化;
     * 第二: 程序员很难发现它们;
     */

    /**
     * 静态工厂方法和公有构造器都各有用处, 我们需要理解它们各自的长处;
     * 静态工厂方法经常更加适合, 切忌第一反应就是提供公有的构造器,而不考虑静态工厂方法;
     */

    /**
     * 静态工厂方法:
     * 这个方法将boolean基本类型值转换成了一个Boolean对象引用
     * @param b
     * @return
     */
    public static Boolean valueOf(boolean b){
        return b ? Boolean.TRUE : Boolean.FALSE;
    }
}
