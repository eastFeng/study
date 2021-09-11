package com.dongfeng.study.bean.base;

import com.dongfeng.study.bean.enums.VersionEnum;
import org.springframework.util.Assert;

/**
 * @author eastFeng
 * @date 2020/8/15 - 16:56
 */
public class VersionHelper {
    /**
     * left版本是否大于right版本: 根据枚举的下标（序号）进行判断
     */
    public static boolean greater(VersionEnum left, VersionEnum right) {
        Assert.notNull(left, "版本号判断左边参数为空");
        Assert.notNull(right, "版本号判断右边参数为空");

        return left.ordinal() > right.ordinal();
    }

    /**
     * left版本是否小于right版本
     */
    public static boolean less(VersionEnum left, VersionEnum right) {
        Assert.notNull(left, "版本号判断左边参数为空");
        Assert.notNull(right, "版本号判断右边参数为空");

        return left.ordinal() < right.ordinal();
    }

    /**
     * left版本是否大于等于right版本
     */
    public static boolean greaterEqual(VersionEnum left, VersionEnum right) {
        Assert.notNull(left, "版本号判断左边参数为空");
        Assert.notNull(right, "版本号判断右边参数为空");

        return left.ordinal() >= right.ordinal();
    }

    /**
     * left版本是否小于等于right版本
     */
    public static boolean lessEqual(VersionEnum left, VersionEnum right) {
        Assert.notNull(left, "版本号判断左边参数为空");
        Assert.notNull(right, "版本号判断右边参数为空");

        return left.ordinal() <= right.ordinal();
    }

    /**
     * left版本是否等于right版本
     */
    public static boolean equal(VersionEnum left, VersionEnum right) {
        Assert.notNull(left, "版本号判断左边参数为空");
        Assert.notNull(right, "版本号判断右边参数为空");

        return left.getCode().equals(right.getCode());
    }

    public static void main(String[] args) {
        System.out.println(VersionHelper.greaterEqual(VersionEnum.V0921, VersionEnum.UNKNOWN));
        System.out.println(VersionHelper.less(VersionEnum.V0921, VersionEnum.V093));
        System.out.println(VersionHelper.equal(VersionEnum.V0921, VersionEnum.V0921));
    }
}
