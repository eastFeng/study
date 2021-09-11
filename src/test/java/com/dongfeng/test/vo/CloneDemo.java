package com.dongfeng.test.vo;

/**
 * 浅拷贝: 基本数据类型是进行值传递, 引用数据类型进行引用传递拷贝
 *
 * 深拷贝: 基本数据类型是进行值传递, 引用数据类型, 创建一个新的对象, 并复制其内容
 *
 *
 * 无论是深拷贝还是浅拷贝，都需要实现clone方法  都必须实现Cloneable接口
 *
 * @author eastFeng
 * @date 2020-10-24 13:52
 */
public class CloneDemo implements Cloneable{
    private String name;
    private int num;

    @Override
    public Object clone(){
        try {
            //浅拷贝
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
