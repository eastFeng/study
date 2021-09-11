package com.dongfeng.study.basicstudy.designpattern.proxypattern;

/**
 * 静态代理类
 * @author eastFeng
 * @date 2020/5/6 - 15:22
 */
public class StaticPrinterProxy implements IPrinter {
    private IPrinter printer = new Printer();

    @Override
    public void print() {
        System.out.println("记录日志!");
        printer.print();
    }

    @Override
    public void hello() {
        System.out.println("say: ");
        printer.hello();
    }
}
