package com.dongfeng.study.basicstudy.java.io;

/**
 * 序列化
 * @author eastFeng
 * @date 2022-12-02 13:46
 */
public class Serializable {
    /**
     * 序列化就是一种用来处理对象流的机制，将对象的内容进行流化。
     * 可以对流化后的对象进行读写操作，可以将流化后的对象传输于网络之间。
     * 序列化是为了解决在对象流读写操作时所引发的问题。
     * <p> 方便存储和传输
     *
     * <p> 序列化：ObjectOutputStream.writeObject()
     * <p> 反序列化：ObjectInputStream.readObject()
     *
     * <p> 被序列化的类要实现Serialize接口
     * <p> transient 关键字可以使一些属性不会被序列化
     */
    public static void main(String[] args) {

    }
}
