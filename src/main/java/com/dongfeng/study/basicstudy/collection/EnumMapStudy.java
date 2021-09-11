package com.dongfeng.study.basicstudy.collection;

import com.dongfeng.study.basicstudy.EnumStudy;

import java.util.EnumMap;

/**
 * <b> {@link java.util.EnumMap}学习 </b>
 *
 *
 * @author eastFeng
 * @date 2021-04-26 3:20
 */
public class EnumMapStudy {
    public static void main(String[] args) {
        EnumMap<EnumStudy.TeaSize, Integer> enumMap = new EnumMap<>(EnumStudy.TeaSize.class);

        enumMap.put(EnumStudy.TeaSize.MEDIUM, 1);
        enumMap.put(EnumStudy.TeaSize.SMALL, 2);
        enumMap.put(EnumStudy.TeaSize.LARGE, 3);
        enumMap.put(EnumStudy.TeaSize.EXTRA_LARGE, 4);

        enumMap.forEach((k,v) -> System.out.println(k.name() +"="+ v));
    }
}
