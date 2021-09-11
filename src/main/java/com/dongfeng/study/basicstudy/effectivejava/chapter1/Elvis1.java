package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * 实现Singleton的第一种方法: 公有静态成员是个final域
 * @author eastFeng
 * @date 2020/1/14 - 15:25
 */
public class Elvis1 {
    //公有静态成员是个final域
    public static final Elvis1 INSTANCE = new Elvis1();

    private Elvis1(){}

    public void leaveTheBuilding(){}
}
