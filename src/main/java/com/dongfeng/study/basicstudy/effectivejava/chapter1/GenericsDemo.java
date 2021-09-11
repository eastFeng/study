package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * @author eastFeng
 * @date 2020/9/21 - 19:33
 */
public class GenericsDemo<T, V> {
    private T key;
    private V value;

    public T getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static class Builder<T, V>{

    }
}
