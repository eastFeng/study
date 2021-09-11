package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * 第二条: 遇到多个构造器参数时要考虑使用构造器
 * @author eastFeng
 * @date 2020/1/11 - 15:41
 */
public class Advice_2 {
    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * 静态工厂方法和构造器有个共同的局限性: 他们都不能很好地扩展到大量的可选参数.
     * 建造者(Builder)模式: 既能保证像重叠构造器模式那样的安全性,又能保证像JavaBeans模式那么好的可读性;
     */
    private static void test1(){
        //Builder模式的例子
        NutritionFacts nutritionFacts = new NutritionFacts.Builder(240, 8).carbohydrate(27).build();
        System.out.println(nutritionFacts.toString());
    }

    /**
     * Builder模式也适用于类层次结构
     */
    private static void test2(){
        NyPizza nyPizza = new NyPizza.Builder(NyPizza.Size.SMALL).addTopping(AbstractPizza.Topping.SAUSAGE).addTopping(AbstractPizza.Topping.ONION).build();
        Calzone calzone = new Calzone.Builder().sauceInside().addTopping(AbstractPizza.Topping.SAUSAGE).build();
        System.out.println(nyPizza);
        System.out.println(calzone);
    }
}
