package com.dongfeng.study.basicstudy.sourcecode.java8.lang;

/**
 * @author eastFeng
 * @date 2021-01-22 10:19
 */
public interface AutoCloseable {

    /**
     *
     * @throws Exception 如果无法关闭此资源
     */
    void close() throws Exception;
}
