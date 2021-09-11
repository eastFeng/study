package com.dongfeng.test;

import cn.hutool.core.util.IdcardUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eastFeng
 * @date 2020-12-10 15:51
 */
@Slf4j
public class SortTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>(10);

//        for (String s : list) {
//            System.out.println("hhh");
//            System.out.println(s);
//        }

        list.add("beijing");
        list.add("aaaaa");
        list.add("cccc");
        list.add("adbcd");
        list.add("上海");
        list.add("北京jing");
        list.add("上海jing");

        list.sort(Comparator.naturalOrder());

        List<String> sorted = list.stream().sorted().collect(Collectors.toList());
        sorted.forEach(System.out::println);
    }

    @Test
    public void getAge(){
        //
        int ageByIdCard = IdcardUtil.getAgeByIdCard("41142619931124033X");
        System.out.println(ageByIdCard);
    }

    // 冒泡排序
    public static void bubble(int[] arr){
        int length = arr.length;
        for (int i=0; i<length-1; i++){
            for (int j=0; j<length-1-i; j++){
                if (arr[j] > arr[j+1]){
                    exchange(arr, j, j+1);
                }
            }
        }
    }

    // 选择排序
    public static void selection(int[] arr){
        int length = arr.length;
        for (int i=0; i<length; i++){
            // 最小值索引
            int min = i;
            for (int j=i+1; j<length; j++){
                if (arr[j] < arr[min]){ // 选择最小值
                    min = j;
                }
            }
            // 交换
            exchange(arr, i, min);
        }
    }

    // 插入排序
    public static void insertion(int[] arr){
        int length = arr.length;

        for (int i=1; i<length; i++){
            for (int j=i; j>0&&arr[j]<arr[j-1]; j--){
                exchange(arr, j, j-1);
            }
        }
    }

    // 插入排序优化
    public static void insertionOptimize(int[] a){
        int length = a.length;
        for (int i=1; i<length; i++){
            int temp = a[i];
            int j = i;
            while (j>0 && temp<a[j-1]){
                a[j] = a[j-1];
                j--;
            }
            if (j!=i){
                a[j] = temp;
            }
        }
    }

    // 希尔排序
    public static void shell(int[] a){
        int length = a.length;

        // 增量h
        int h = 1;
        while (h < length){
            // 序列: 1, 4, 13, 40, 121, 364, ...
            h = 3*h + 1;
        }

        while (h >= 1){

            for (int i=h; i<length; i++){
                for (int j=i; j>=h&&a[j]<a[j-h]; j-=h){
                    exchange(a, j, j-h);
                }
            }

            h = h/3;
        }
    }

    // 交换数组i和j位置元素
    public static void exchange(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    /**
     * 插入排序
     * @param a 待排序的元素
     */
    public static void insertionSort(int[] a){
        int length = a.length;
        for (int i=1; i<length; i--){
            for (int j=i; j>0&&a[j]<a[j-1]; j--){
                exchange(a, j, j-1);
            }
        }
    }

    /**
     * 插入排序优化
     * @param a 要排序的数组
     */
    public static void insertionSortOptimize(int[] a){
        int length = a.length;
        for (int i=1; i<length; i++){
            int temp = a[i];
            int j=i;
            while (j>0 && temp<a[j-1]){
                // 找到比要插入的数小的数a[j-1]，这个数需要往后（右）移
                a[j] = a[j-1];
                // 查看下一个
                j--;
            }
            if (j!=i){
                a[j] = temp;
            }
        }
    }

    @Test
    public void logTest(){
//        log.trace("this is trace");
//        log.info("this is info");
//        log.error("this is error");
//        log.debug("this is debug");

//        List<Integer> list = Arrays.asList(1, 3, 0, 777, 34, 2, 4, 9, 6);
//        list.sort(Comparator.comparingInt(t-> (int) t).reversed());
//        list.forEach(System.out::println);

        int[] arr = {1, 3, 0, 777, 34, 2, 4, 9, 6};
        Arrays.stream(arr).forEach(System.out::println);
        Arrays.sort(arr, 1, 4);
        System.out.println("/-----------排序后:");
        Arrays.stream(arr).forEach(System.out::println);
    }
}
