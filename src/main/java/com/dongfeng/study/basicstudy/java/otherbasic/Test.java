package com.dongfeng.study.basicstudy.java.otherbasic;

import com.dongfeng.study.basicstudy.designpattern.proxypattern.IPrinter;
import com.dongfeng.study.basicstudy.designpattern.proxypattern.Printer;
import com.dongfeng.study.basicstudy.designpattern.proxypattern.ProxyHandler;
import com.dongfeng.study.basicstudy.designpattern.proxypattern.StaticPrinterProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eastFeng
 * @date 2020/5/6 - 15:24
 */
public class Test {
    public static void main(String[] args) {
//        Class<Printer> printerClass = Printer.class;
//        Class<?>[] interfaces = printerClass.getInterfaces();
//        for (Class i : interfaces){
//            System.out.println(i);
//        }

//        testDynamicProxy();

        List<String> list = new ArrayList<>(16);

        testVariable();
    }


    public static void testVariable(){
        VariableDemo variableDemo = new VariableDemo();
        variableDemo.name1 = "hhhh";
        System.out.println(variableDemo.name1);
    }


    private static void testStaticProxy(){
        StaticPrinterProxy staticPrinterProxy = new StaticPrinterProxy();
        staticPrinterProxy.print();
    }

    /**
     *
     */
    private static void testDynamicProxy(){
        // 调用处理器对象
        ProxyHandler proxyHandler = new ProxyHandler();

        // 获取动态代理对象
        IPrinter printer = (IPrinter) proxyHandler.newProxyInstance(new Printer());

        // 获取代理对象所关联的调用处理器(InvocationHandler接口的实现类): printer代理对象关联的调用处理器是ProxyHandler
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(printer);
        System.out.println(invocationHandler.getClass().getName());

        // 在代理类实例上调用其代理的接口中所声明的方法时，这些方法最终都会由调用处理器的invoke方法执行
//        printer.hello();
//        System.out.println();
//        printer.print();


        System.out.println( printer.toString());
    }
}
