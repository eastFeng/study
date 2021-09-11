package com.dongfeng.study.basicstudy.effectivejava.chapter1;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * 抽象类有抽象的builder, 具体类有具体的builder
 */

/**
 * Builder模式也适用于类层次结构
 * @author eastFeng
 * @date 2020/1/14 - 11:55
 */
public abstract class AbstractPizza {
    public enum Topping{HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}

    final Set<Topping> toppings;

    /**
     * 抽象静态内部类
     * @param <T>
     */
    abstract static class Builder<T extends Builder<T>>{
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping){
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        //抽象方法: 子类要么重写 要么也声明为abstract类
        abstract AbstractPizza build();

        protected abstract T self();
    }

    /**
     * 子类是不继承父类的构造器（构造方法或者构造函数）的，它只是调用（隐式或显式）。
     *
     * 如果父类的构造器带有参数，则必须在子类的构造器中显式地通过 super 关键字调用父类的构造器并配以适当的参数列表。
     *
     * 如果父类构造器没有参数，则在子类的构造器中不需要使用 super 关键字调用父类构造器，系统会自动调用父类的无参构造器。
     * @param builder
     */
    AbstractPizza(Builder<?> builder){
        toppings = builder.toppings.clone();
    }
}
