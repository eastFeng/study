package com.dongfeng.study.sourcecode.java8.lang;

/**
 * @author eastFeng
 * @date 2021-01-25 11:59
 */
public class InheritableThreadLocal<T> extends ThreadLocal<T> {

    @Override
    protected T childValue(T parentValue){
        return parentValue;
    }

    @Override
    ThreadLocalMap getMap(Thread t) {
        //return t.inheritableThreadLocals;
        return null;
    }

    @Override
    void createMap(Thread t, T firstValue) {
        //t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
    }
}
