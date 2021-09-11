package com.dongfeng.study.basicstudy.effectivejava.chapter1;

import java.util.Objects;

/**
 * @author eastFeng
 * @date 2020/1/14 - 14:11
 */
public class NyPizza extends AbstractPizza {
    public enum Size{SMALL, MEDIUM, LARGE}

    private final Size size;

    public static class Builder extends AbstractPizza.Builder<Builder>{
        private final Size size;

        public Builder(Size size){
            this.size = Objects.requireNonNull(size);
        }

        //重写父类中的抽象方法
        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        //重写父类中的抽象方法
        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        this.size = builder.size;
    }

    @Override
    public String toString() {
        return "NyPizza{" +
                "size=" + size +
                ", toppings=" + toppings +
                '}';
    }
}
