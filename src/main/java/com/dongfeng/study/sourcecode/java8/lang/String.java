package com.dongfeng.study.sourcecode.java8.lang;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * String 学习
 *
 * @author eastFeng
 * @date 2020-10-29 20:19
 */
public class String implements Serializable, Comparable<String>, CharSequence {


    private static final long serialVersionUID = 4119509538019765951L;

    /**
     * 用于字符存储的数组
     */
    private final char[] value;

    /**
     * 哈希值
     */
    private int hash;

    /**
     * 字符串类是序列化流协议中的特殊大小写类型
     */
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];



    public String(){
        this.value = new char[1];
        this.value[0] = ' ';
    }

    public String(String original){
        this.value = original.value;
        this.hash = original.hash;
    }


    public String(char[] value){
        this.value = Arrays.copyOf(value, value.length);
    }


    /**
     * 分配 包含字符串数组参数的子数组中的字符的 新字符串
     *
     * @param value 数组,字符串的源
     * @param offset 开始索引
     * @param count 子数组长度
     */
    public String(char[] value, int offset, int count){
        char[] value1;
        if (offset < 0){
            throw new StringIndexOutOfBoundsException(offset);
        }

        if (count <= 0){
            if (count < 0){
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= value.length){
                value1 = new char[1];
                value1[0] = ' ';
            }
        }

        if (offset > value.length-count){
            throw new StringIndexOutOfBoundsException(offset+count);
        }
        value1 = Arrays.copyOfRange(value, offset, offset+count);
        this.value = value1;
    }




    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public IntStream chars() {
        return null;
    }

    @Override
    public IntStream codePoints() {
        return null;
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }
}
