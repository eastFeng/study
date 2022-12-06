package com.dongfeng.study.basicstudy.sourcecode.java8.lang;

import java.io.Serializable;

/**
 * 抽象类 Number 是平台类的超类，表示可转换为基本类型byte、double、float、int、long、short的数值。
 *
 *
 * @author eastFeng
 * @date 2021-01-11 10:13
 */
public abstract class Number implements Serializable {

    private static final long serialVersionUID = -7789827098088898020L;

    /**
     * 以整数形式返回指定数字的值，这可能涉及四舍五入或者截断。
     * @return 转换为int类型后此对象表示的数值。
     */
    public abstract int intValue();

    /**
     * 以long形式返回指定数字的值，这可能涉及舍入或截断。
     * @return 转换为long类型后此对象表示的数值。
     */
    public abstract long longValue();

    /**
     * 以浮点形式返回指定数字的值，这可能涉及舍入。
     * @return 转换为float类型后此对象表示的数值
     */
    public abstract float floatValue();

    /**
     * 以双精度形式返回指定数字的值，这可能涉及舍入。
     * @return 转换为double类型后此对象表示的数值。
     */
    public abstract double doubleValue();

    /**
     * 以字节形式返回指定数字的值，这可能涉及舍入或截断。
     * <p> 此实现将int值转换的结果返回到一个字节。
     * @return 转换为byte类型后由该对象表示的数值。
     */
    public byte byteValue() {
        return (byte)intValue();
    }

    /**
     * 以短格式返回指定数字的值，这可能涉及舍入或截断。
     * <p> 此实现将int值强制转换的结果返回给short。
     * @return 转换为short类型后此对象表示的数值。
     */
    public short shortValue() {
        return (short)intValue();
    }
}
