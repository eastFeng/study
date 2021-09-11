package com.dongfeng.study.util;

/**
 * @author eastFeng
 * @date 2020/8/15 - 14:38
 */
public class MathUtil {

    /**
     *  四舍五入
     * @param num   要进行四舍五入的数字
     * @param scale 四舍五入保留的小数位
     * @return      四舍五入处理后的结果
     */
    public static double round(double num, int scale){
        return Math.round(num * Math.pow(10,scale)) / Math.pow(10,scale);
    }
}
