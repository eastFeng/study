package com.dongfeng.study.basicstudy.designpattern.compositepattern;

/**
 * @author eastFeng
 * @date 2021-01-27 14:14
 */
public abstract class Car {
    /**
     * 每个车都有一个颜色
     */
    protected Color color;

    public String getCarColor(){
        return color.getColor();
    }
}
