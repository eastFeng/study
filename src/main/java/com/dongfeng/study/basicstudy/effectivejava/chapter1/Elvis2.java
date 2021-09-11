package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * 实现Singleton的第一种方法: 公有的成员是个静态工厂方法
 * @author eastFeng
 * @date 2020/1/14 - 15:32
 */
public class Elvis2 {
    private static final Elvis2 INSTANCE = new Elvis2();

    private Elvis2(){}

    public static Elvis2 getInstance(){
        return INSTANCE;
    }

    public void leaveTheBuilding(){}
}
