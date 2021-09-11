package com.dongfeng.study.bean.vo;

import org.springframework.stereotype.Component;

/**
 * @author eastFeng
 * @date 2020-11-27 17:01
 */
@Component
public class IA2 implements InterfaceA {
    @Override
    public void say() {
        System.out.println("IA2---hhhh");
    }
}
