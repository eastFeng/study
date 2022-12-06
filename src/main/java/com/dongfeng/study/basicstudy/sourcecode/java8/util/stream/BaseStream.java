package com.dongfeng.study.basicstudy.sourcecode.java8.util.stream;

import com.dongfeng.study.basicstudy.sourcecode.java8.lang.AutoCloseable;

import java.util.Iterator;
import java.util.Spliterator;

/**
 * @author eastFeng
 * @date 2021-01-22 10:18
 */
public interface BaseStream<T, S extends BaseStream<T, S>> extends AutoCloseable {

    Iterator<T> iterator();

    Spliterator<T> spliterator();

    boolean isParallel();

    S sequential();

    S parallel();

    S unordered();

    S onClose(Runnable closeHandler);

    @Override
    void close();
}
