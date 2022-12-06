package com.dongfeng.study.basicstudy.sourcecode.java8.util.concurrent.locks;

import java.io.Serializable;

/**
 * 【抽象独占同步器】
 * <p> 线程独占的同步器。此类提供了创建锁和相关同步器的基础，这些同步器可能包含所有权的概念。
 * AbstractOwnableSynchronizer类本身不管理或使用此信息。
 * 但是，子类和工具可以使用适当维护的值来帮助控制和监视访问并提供诊断。
 * @author eastFeng
 * @date 2021-01-11 10:37
 */
public abstract class AbstractOwnableSynchronizer implements Serializable {

    private static final long serialVersionUID = -2445206222624881010L;

    /**
     * 子类使用的空构造函数。
     */
    protected AbstractOwnableSynchronizer() { }

    /**
     * 独占模式同步的当前所有者线程。
     */
    private transient Thread exclusiveOwnerThread;

    /**
     * 设置当前拥有独占访问权限的线程。null参数表示没有线程拥有访问权限。
     * 此方法不会以其他方式强制任何同步或易失性字段访问。
     * @param thread 所有者线程
     */
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    /**
     * 返回setExclusiveOwnerThread上次设置的线程，如果从未设置，则返回null。
     * 此方法不会以其他方式强制任何同步或易失性字段访问。
     * @return 所有者线程
     */
    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
