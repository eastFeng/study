package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * 静态工厂方法和构造器有个共同的局限性: 他们都不能很好地扩展到大量的可选参数.
 * 建造者(Builder)模式: 既能保证像重叠构造器模式那样的安全性,又能保证像JavaBeans模式那么好的可读性;
 * @author eastFeng
 * @date 2020/1/11 - 15:54
 */
public class NutritionFacts {
    /**
     * 构造对象时必传的参数
     */
    private final int servingSize;
    private final int servings;
    /**
     * 构造对象时可选的参数
     */
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    /**
     * 静态内部类
     */
    public static class Builder{
        //必需的参数
        private final int servings;
        private final int servingSize;

        //可选的参数
        private int calories     = 0;
        private int fat          = 0;
        private int sodium       = 0;
        private int carbohydrate = 0;

        public Builder(int servings,int servingSize){
            this.servings = servings;
            this.servingSize = servingSize;
        }


        public Builder calories(int val){
            calories = val;
            return this;
        }

        public Builder fat(int val){
            fat = val;
            return this;
        }

        public Builder soudium(int val){
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val){
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build(){
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder){
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    @Override
    public String toString() {
        return "NutritionFacts{" +
                "servingSize=" + servingSize +
                ", servings=" + servings +
                ", calories=" + calories +
                ", fat=" + fat +
                ", soudium=" + sodium +
                ", carbohydrate=" + carbohydrate +
                '}';
    }
}
