package com.dongfeng.study.basicstudy.designpattern.proxypattern;

import com.dongfeng.study.basicstudy.designpattern.proxypattern.IPrinter;

/**
 * 被代理对象
 * @author eastFeng
 * @date 2020/5/6 - 15:20
 */
public class Printer implements IPrinter {
    @Override
    public void print() {
        System.out.println("打印!");
    }

    @Override
    public void hello() {
        System.out.println("hello!!!");
    }

    @Override
    public String toString(){
        return "toString : "+ super.toString();
    }
}
