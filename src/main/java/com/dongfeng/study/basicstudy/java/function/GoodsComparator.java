package com.dongfeng.study.basicstudy.java.function;

import com.dongfeng.study.basicstudy.java.otherbasic.Goods;

import java.util.Comparator;

/**
 * 自定义的{@link Goods}比较器
 *
 * @author eastFeng
 * @date 2020/4/21 - 17:48
 */
public class GoodsComparator implements Comparator<Goods> {

    @Override
    public int compare(Goods o1, Goods o2) {
        // 先按价格进行比较
        if (o1.getPrice() != o2.getPrice()){
            return o1.getPrice() > o2.getPrice() ? 1 : -1;
        }

        // 价格一样, 再按数量进行比较
        if (!o1.getNumber().equals(o2.getNumber())){
            return o1.getNumber() > o2.getNumber() ? 1 : -1;
        }

        // 价格和数量都一样
        return 0;
    }
}
