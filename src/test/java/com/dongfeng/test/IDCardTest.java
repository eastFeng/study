package com.dongfeng.test;

import cn.hutool.core.util.IdcardUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author eastFeng
 * @date 2020-12-13 22:01
 */
@Slf4j
public class IDCardTest {
    public static void main(String[] args) {
    }

    @Test
    public void getAgeByType(){

        try {
            int myAge = IdcardUtil.getAgeByIdCard("41142619931124033X");
            try {
                int age = IdcardUtil.getAgeByIdCard("144240219570409005");
                System.out.println(age);
            } catch (Exception e) {
                log.error("内层tryCatch error:{}", e.getMessage(), e);
            }
            System.out.println(myAge);
        } catch (Exception e) {
            log.error("外层tryCatch error:{}", e.getMessage(), e);
        }
    }
}
