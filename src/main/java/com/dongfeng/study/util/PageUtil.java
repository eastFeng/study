package com.dongfeng.study.util;

import java.util.List;

/**
 * 分页工具类
 *
 * @author eastFeng
 * @date 2020/8/15 - 14:07
 */
public class PageUtil {

    /**
     * <b> list分页 </b>
     *
     * @param list 需要进行分页的list集合
     * @param pageIndex 页码
     * @param pageSize 每页个数
     * @param <T> 泛型（list集合中元素类型）
     * @return 分页后的list数据
     */
    public static <T> List<T> listPage(List<T> list, int pageIndex, int pageSize){
        if (list == null || list.isEmpty()){
            return null;
        }
        // 默认第一页
        if (pageIndex <=0){
            pageIndex = 1;
        }
        // 默认每页十条数据
        if (pageSize <= 0){
            pageSize = 10;
        }

        // 总条数
        final int count = list.size();
        // 总页数
        int pageCount;
        // 计算总页数
        if (count % pageSize == 0){
            // 能除尽
            pageCount = count / pageSize;
        }else {
            // 不能除尽
            pageCount = count/pageSize + 1;
        }

        // 如果传入的页码大于最大的页码，页码就是最大的页码
        if (pageCount < pageIndex){
            pageIndex = pageCount;
        }

        // 开始索引
        int fromIndex;
        // 结束索引
        int toIndex;
        // 计算子list的起始索引
        if (pageIndex != pageCount){
            // 不是最后一页
            fromIndex = (pageIndex-1)*pageSize;
            toIndex = fromIndex + pageSize;
        }else {
            fromIndex = (pageIndex-1)*pageSize;
            toIndex = count;
        }

        // List<E> subList(int fromIndex, int toIndex)
        // 返回一个以fromIndex为起始索引(包含)，以toIndex为终止索(不包含)的子列表(List)
        return list.subList(fromIndex, toIndex);
    }
}
