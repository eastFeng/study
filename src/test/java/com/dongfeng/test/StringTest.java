package com.dongfeng.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author eastFeng
 * @date 2020-09-26 21:03
 */
public class StringTest {
    public static void main(String[] args) {
        boolean equals = "https://yjy.yylxjt.com/city-marketing-api-h5-app/form/huyouma/tourism/".equals("https://yjy.yylxjt.com/city-marketing-api-h5-app/form/huyouma/tourism/");
        System.out.println(equals);
    }

    @Test
    public void reverse_test(){
        StringBuilder stringBuilder = new StringBuilder("LGD是不可战胜的");
        StringBuilder reverse = stringBuilder.reverse();
        System.out.println(reverse.toString());

        String str = "LGD是不可战胜的";
    }
}
