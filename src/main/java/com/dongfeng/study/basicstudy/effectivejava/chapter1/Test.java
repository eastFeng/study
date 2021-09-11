package com.dongfeng.study.basicstudy.effectivejava.chapter1;

import com.alibaba.fastjson.JSON;

/**
 * @author eastFeng
 * @date 2020/4/16 - 16:07
 */
public class Test {
    public static void main(String[] args) {
        test_person();
    }

    private static void test_person(){
        Person person = Person.newBuilder()
                .setName("东风")
                .setAge(26)
                .setNationality("中国")
                .setEthnic("汉族")
                .setIdCardType(IdCardType.China_Resident_Identity_Card)
                .setIdNumber("123456789001234")
                .setAddress("河南省商丘市夏邑县郭店镇")
                .setPhoneNumber("18120412883")
                .build();
        String jsonString = JSON.toJSONString(person);
        System.out.println(jsonString);
    }
}
