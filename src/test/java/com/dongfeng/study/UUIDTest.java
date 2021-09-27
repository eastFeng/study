package com.dongfeng.study;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author eastFeng
 * @date 2020-10-22 11:31
 */
public class UUIDTest {
    public static void main(String[] args) {
    }

    @Test
    public void randomUUID(){
        UUID uuid = UUID.randomUUID();
        System.out.println("UUID: "+uuid);

        //总长度为36是固定的,由四个中划线"-"隔开; 第一部分长度为8, 第二、三、四部分长度为4, 第五部分长度为12
        System.out.println("length: "+uuid.toString().length());
        //UUID版本
        System.out.println("UUID Version: "+uuid.version());
    }

    /**
     * 生成32为字母全是小写字母的随机数
     */
    @Test
    public void randomLowerCase(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        System.out.println(uuid);
        System.out.println(uuid.length());
    }

    /**
     * hutool工具类 RandomUtil
     */
    @Test
    public void randomUtil_test(){
        double aDouble = RandomUtil.randomDouble(0.01, 0.1);
        System.out.println(aDouble);

        // min 最小数（包含）
        // max 最大数（不包含）
        int anInt = RandomUtil.randomInt(1, 10);
        System.out.println(anInt);

        String randomString = RandomUtil.randomString(32);
        System.out.println(randomString);
    }

    // ObjectId是MongoDB数据库的一种唯一ID生成策略，是UUID version1的变种
    @Test
    public void objectID(){
        String s = IdUtil.objectId();
        System.out.println(s);
        System.out.println();

//        //springboot mongodb下的 :org.bson.types
//        ObjectId objectId = new ObjectId();
//        System.out.println(objectId.toHexString());
//        System.out.println(objectId.toHexString().length());
    }
}
