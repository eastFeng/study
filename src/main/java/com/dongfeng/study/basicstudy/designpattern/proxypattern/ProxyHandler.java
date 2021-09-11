package com.dongfeng.study.basicstudy.designpattern.proxypattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理: 通过反射实现
 *
 * <p> 原理:  被代理类和代理类实现相同的接口, 代理类持有被代理类对象, 来达到方法拦截的作用;
 *
 * <p> 弊端:  一是必须保证被代理类实现了接口; 二是如果想对被代理类的方法进行处理拦截, 要保证这些方法都要在接口中声明;
 *
 * <p> 调用处理器实现类
 * @author eastFeng
 * @date 2020/5/6 - 15:28
 */
public class ProxyHandler implements InvocationHandler {
    private final String PRINT = "print";
    private final String HELLO = "hello";

    /**
     * 被代理的对象
     */
    private Object targetObject;

    /**
     * 将被代理的对象传入获得它的类加载器和所有实现的接口作为Proxy.newProxyInstance方法的参数
     *
     * <p><pre>
     * Proxy类的静态newProxyInstance方法:
     * InvocationHandler h: 实现InvocationHandler接口的对象;
     * public static Object newProxyInstance(ClassLoader loader,
     *                                           Class<?>[] interfaces,
     *                                           InvocationHandler h) throws IllegalArgumentException
     * ClassLoader loader: 被代理对象的类加载器;
     * Class<?>[] interfaces: 被代理类实现的全部接口;
     * </pre>
     *
     * @param targetObject 被代理对象
     * @return 动态代理对象: 指定接口的代理类的实例,每个代理实例都有一个关联的调用处理程序对象，它实现了接口InvocationHandler 。
     */
    public Object newProxyInstance(Object targetObject){
        this.targetObject = targetObject;

        // targetObject.getClass().getClassLoader(): 被代理对象的类加载器;
        // targetObject.getClass().getInterfaces(): 被代理对象实现的所有接口;
        // this: 当前对象, 该对象实现了InvocationHandler接口,所以有invoke方法, 通过invoke方法可以调用被代理对象的方法;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(),
                this);
    }

    /**
     * 该方法负责集中处理动态代理类上的所有方法调用。
     * 该方法在代理对象调用方法时调用
     * 在代理类实例上调用其代理的接口中所声明的方法时，这些方法最终都会由调用处理器的invoke方法执行
     * @param proxy 方法所调用的代理实例
     * @param method 要调用的方法
     * @param args 方法调用时所需的参数
     * @return
     * @throws Throwable 理论上能够抛出任何异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //
        if ("toString".equals(method.getName())){
            return "toString invoked !!!";
        }


        // 在这里可以通过判断方法名来决定执行什么功能
        // 如果是print方法
        if (PRINT.equals(method.getName())){
            System.out.println("print方法!");
        }
        // 如果是hello方法
        if (HELLO.equals(method.getName())){
            System.out.println("say:");
        }

        // 调用被代理对象的方法
        return method.invoke(targetObject, args);
    }
}
