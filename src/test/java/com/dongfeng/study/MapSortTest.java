package com.dongfeng.study;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eastFeng
 * @date 2020-10-22 11:46
 */
public class MapSortTest {
    public static void main(String[] args) {
        HashMap<String, Integer> areaMap = new HashMap<>(10);
        areaMap.put("北京", 10);
        areaMap.put("上海", 2);

        areaMap.get("hhh");

        System.out.println(areaMap.get("上海"));

        HashMap<String, Integer> hh = new HashMap<>();

        boolean equals = areaMap.equals(hh);

        HashSet<String> stringSet = new HashSet<>();
        boolean equals1 = stringSet.equals(hh);
    }

    //----------------------------------map排序 start------------------------------------------
    /**
     * 缺点：没办法倒序
     */
    @Test
    public void mapSort(){
        Map<String, Integer> areaMap = new HashMap<>(10);
        areaMap.put("北京", 10);
        areaMap.put("上海", 2);
        areaMap.put("山西省", 1000);
        areaMap.put("陕西省", 100);
        areaMap.put("河南省", 600);
        areaMap.put("广东省", 222);
        areaMap.put("海南省", 1);
        areaMap.put("西藏藏族自治区", 3);
        areaMap.put("河北省", 10);
        areaMap.put("安徽省", 300);

        //排序好之后放入LinkedHashMap
        HashMap<String, Integer> finalOut = new LinkedHashMap<>(10);

        areaMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList())
                .forEach(ele->finalOut.put(ele.getKey(), ele.getValue()));

        for (Map.Entry<String, Integer> entry : finalOut.entrySet()) {
            System.out.println("【"+entry.getKey()+"】: "+entry.getValue());
        }
    }

    /**
     * 缺点：没办法倒序
     */
    @Test
    public void mapSort2(){
        Map<String, Integer> areaMap = new HashMap<>(10);
        areaMap.put("北京", 10);
        areaMap.put("上海", 2);
        areaMap.put("山西省", 1000);
        areaMap.put("陕西省", 100);
        areaMap.put("河南省", 600);
        areaMap.put("广东省", 222);
        areaMap.put("海南省", 1);
        areaMap.put("西藏藏族自治区", 3);
        areaMap.put("河北省", 10);
        areaMap.put("安徽省", 300);


        List<Map.Entry<String, Integer>> list = areaMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("【"+entry.getKey()+"】: "+entry.getValue());
        }
    }

    /**
     * 把map中的数据放入list中，可以正序排序也可以倒序排序
     */
    @Test
    public void mapSort3(){
        Map<String, Integer> areaMap = new HashMap<>(10);
        areaMap.put("北京", 10);
        areaMap.put("上海", 2);
        areaMap.put("山西省", 1000);
        areaMap.put("陕西省", 100);
        areaMap.put("河南省", 600);
        areaMap.put("广东省", 222);
        areaMap.put("海南省", 1);
        areaMap.put("西藏藏族自治区", 3);
        areaMap.put("河北省", 10);
        areaMap.put("安徽省", 300);

        List<MapSortTest.NameNumber> provinceList = new ArrayList<>(10);
        for (Map.Entry<String, Integer> entry : areaMap.entrySet()) {
            NameNumber province = new NameNumber();
            province.setName(entry.getKey());
            province.setNumber(entry.getValue());
            provinceList.add(province);
        }

        //根据数量倒序排序
        provinceList.sort(Comparator.comparing(NameNumber::getNumber).reversed());

        //取前三个
        if (provinceList.size()>3){
            provinceList = provinceList.subList(0, 3);
        }

        provinceList.forEach(System.out::println);
    }
    @Data
    public static class NameNumber{
        /**
         * 名称
         */
        private String name;
        /**
         * 数量
         */
        private Integer number;
    }
    //----------------------------------map排序 end  ------------------------------------------
}
