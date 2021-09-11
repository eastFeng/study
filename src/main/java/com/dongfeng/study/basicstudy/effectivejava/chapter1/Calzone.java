package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * @author eastFeng
 * @date 2020/1/14 - 14:47
 */
public class Calzone extends AbstractPizza {
    private final boolean sauceInside;

    public static class Builder extends AbstractPizza.Builder<Builder>{
        private boolean sauceInside = false; //Default

        public Builder sauceInside(){
            sauceInside = true;
            return this;
        }

        @Override
        Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    /**
     * 子类是不继承父类的构造器（构造方法或者构造函数）的，它只是调用（隐式或显式）。
     * <p>
     * 如果父类的构造器带有参数，则必须在子类的构造器中显式地通过 super 关键字调用父类的构造器并配以适当的参数列表。
     * <p>
     * 如果父类构造器没有参数，则在子类的构造器中不需要使用 super 关键字调用父类构造器，系统会自动调用父类的无参构造器。
     *
     * @param builder
     */
    Calzone(Builder builder) {
        super(builder);
        this.sauceInside = builder.sauceInside;
    }

    @Override
    public String toString() {
        return "Calzone{" +
                "sauceInside=" + sauceInside +
                ", toppings=" + toppings +
                '}';
    }
}
