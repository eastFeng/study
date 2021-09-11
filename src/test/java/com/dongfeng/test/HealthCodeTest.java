package com.dongfeng.test;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhang Dongfeng
 * @date 2021-01-11 14:33
 */
@Slf4j
public class HealthCodeTest {
    public static void main(String[] args) {

//        String host="http://47.107.60.192:8081/restcloud"; //服务器地址
//        String loginUrl=host+"/rest/core/auth/login"; //登录地址无需修改
//
//        test();

        String pass123 = getAes("pass123");
        System.out.println(pass123);
    }

    public static void test(){

        String url = "https://64.97.142.213:8443/restcloud/rest/yq/getJkmzt";

        JSONObject param = new JSONObject();
        param.put("name", "张东风");
        param.put("id_no", "41142619931124033X");
        String body = HttpRequest
                .post(url)
                .body(param.toJSONString())
                .execute().body();
        System.out.println(body);
    }

    /**
     * AES : Advanced Encryption Standard (高级加密标准)
     */
    public static String getAes(String plaintext){
        // 秘钥
        String key = "xZc9hDReNPW32Qqy";
        log.info("key length:{}", key.length());
        log.info("key bytes length:{}", key.getBytes().length);
        // 偏移向量
        String iv = "0102030405060708";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());

        // 加密
        return aes.encryptHex(plaintext);
    }
}
