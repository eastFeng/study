package com.dongfeng.study.sourcecode.java8.lang.reflect;

import java.lang.reflect.Method;

/**
 * @author eastFeng
 * @date 2021-02-02 18:44
 */
public interface InvocationHandler {

    /**
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}
