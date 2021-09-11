package com.dongfeng.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void EnableAutoConfiguration_Test(){
        Class<EnableAutoConfiguration> eClass = EnableAutoConfiguration.class;
        System.out.println(eClass.getName());
    }
}
