package com.dongfeng.study.basicstudy;

import com.dongfeng.study.basicstudy.function.GoodsComparator;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <b> {@link java.util.Arrays} 学习</b>
 *
 * @author eastFeng
 * @date 2021-04-23 15:04
 */
public class ArraysStudy {
    public static void main(String[] args) {
        /*
         * 数组操作是计算机程序中的常见基本操作。Java中有一个类Arrays，包含一些对数组操作的静态方法。
         */

        int[] ints = new int[10];
        Goods[] goods = new Goods[10];

        // Arrays的二分查找（binarySearch）
        int i = binarySearch(ints, 0, ints.length, 1);

        // Arrays的排序
        sort(ints, goods, new GoodsComparator());
    }

    /**
     * Arrays的二分查找（binarySearch）
     *
     * <p> 可以在已排序的数组中进行二分查找。
     * <p> 所谓二分查找就是从中间开始查找，如果小于中间元素，则在前半部分查找，
     * 否则在后半部分查找，每比较一次，要么找到，要么将查找范围缩小一半，所以查找效率非常高。
     *
     * <p> 二分查找既可以针对基本类型数组，也可以针对对象数组，对对象数组，也可以传递Comparator，也可以指定查找范围。
     * @param a 要被查找的数组
     * @param fromIndexInclusive 开始位置（包含）
     * @param toIndexExclusive 结束位置（不包含）
     * @param key 要搜索的值
     * @return 搜索的值在数组中的索引，如果它包含在指定范围内的数组中；否则返回一个负数。
     */
    public static int binarySearch(int[] a,
                                    int fromIndexInclusive,
                                    int toIndexExclusive,
                                    int key) {
        checkArray(a, fromIndexInclusive, toIndexExclusive);

        /*
         * 下面代码中有两个标志：low和high，表示查找范围，
         * 在while循环中，与中间值进行对比，大于则在后半部分查找（提高low），否则在前半部分查找（降低high）。
         */
        int low = fromIndexInclusive;
        int high = toIndexExclusive - 1;

        while (low <= high) {
            // 无符号右移一位：除以2
            int mid = (low + high) >>> 1;
            // 中间值
            int midVal = a[mid];

            if (midVal < key)
                // key大于中间值，在后半部分查找
                low = mid + 1;
            else if (midVal > key)
                // key小于中间值，在前半部分查找
                high = mid - 1;
            else
                // key找到，返回索引
                return mid;
        }
        // key没找到
        return -(low + 1);
    }

    /**
     * Arrays的排序
     *
     * @param intArray 基本类型的数组
     * @param objArray 对象类型的数组
     * @param comparator 比较器
     * @param <T> 类型参数
     */
    public static <T> void sort(int[] intArray, T[] objArray, Comparator<? super T> comparator){
        /*
         * 对于基本类型的数组，Java采用的算法是双枢轴快速排序（Dual-PivotQuicksort）。
         * 双枢轴快速排序是对快速排序的优化，新算法的实现代码位于类java.util.DualPivotQuicksort中。
         *
         * 对于对象类型，Java采用的算法是TimSort。TimSort实际上是对归并排序的一系列优化。
         * TimSort也属于归并排序。TimSort的实现代码位于类java.util.TimSort中。
         *
         * 为什么基本类型和对象类型的算法不一样呢？
         * 排序算法有一个稳定性的概念，所谓稳定性就是对值相同的元素，如果排序前和排序后，
         * 算法可以保证它们的相对顺序不变，那算法就是稳定的，否则就是不稳定的。
         *
         * 快速排序更快，但不稳定，而归并排序是稳定的。对于基本类型，值相同就是完全相同，所以稳定不稳定没有关系。
         * 但对于对象类型，相同只是比较结果一样，它们还是不同的对象，其他实例变量也不见得一样，稳定不稳定可能就很有关系了，所以采用归并排序。
         */
        Arrays.sort(intArray);
        Arrays.sort(objArray, comparator);
    }

    public static void checkArray(int[] arr, int fromIndexInclusive, int toIndexExclusive){
        if (arr == null){
            throw new NullPointerException("array can not null");
        }
        checkArrayRange(arr.length, fromIndexInclusive, toIndexExclusive);
    }

    public static void checkArrayRange(int arrayLength,
                                       int fromIndexInclusive,
                                       int toIndexExclusive){
        if (fromIndexInclusive > toIndexExclusive){
            throw new IllegalArgumentException(
                    "fromIndex(" + fromIndexInclusive + ") > toIndex(" + toIndexExclusive + ")");
        }
        if (fromIndexInclusive < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndexInclusive);
        }
        if (toIndexExclusive > arrayLength) {
            throw new ArrayIndexOutOfBoundsException(toIndexExclusive);
        }
    }
}
