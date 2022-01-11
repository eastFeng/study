package com.dongfeng.test;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.isoftstone.sign.SM4Utils;
import com.isoftstone.sign.SignGeneration;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2020-12-26 17:34
 */
@Slf4j
public class HubeiSignTest {
    public static void main(String[] args) {
        //name : 姓名 ， code：身份证

        String name = "张东风";
        String code = "41142619931124033X";
        String url = "http://data.hb.cegn.cn/irsp/openApi/jkmxxdetail/v1";

        //会话key (Access Key ID)
        String ak = "a2fd01de7ea6419fb9996f14e4360557";
        String sk = "2d5ba4aee8ef45eb";
        //数据服务标识
        String serviceId = "ff808081747113aa0174725a64a10736";
        //服务调用者唯一标识、可在平台内查询。
        String appId = "BB0620245339414F82556429EFD6FA63";

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String timeMillis = format.format(date);

            Map<String, String> params = new HashMap<>();
            params.put("serviceId", serviceId);
            params.put("ak", ak);
            params.put("appId", appId);
            params.put("timestamp", timeMillis);
            params.put("method", "POST");
            params.put("name", name);
            params.put("code", code);
//            params.put("pageIndex", "1");
//            params.put("pageSize", "10");
            //生成数字签名
            String sign = SignGeneration.generationSign(params, sk);
            log.info("sign: {}",sign);

            Map<String, Object> param22 = new HashMap<>();
            param22.put("serviceId", serviceId);
            param22.put("ak", ak);
            param22.put("appId", appId);
            param22.put("timestamp", timeMillis);
            param22.put("method", "POST");
            param22.put("name", name);
            param22.put("code", code);
            param22.put("sign", sign);
//            param22.put("pageIndex", "1");
////            param22.put("pageSize", "10");

            String jsonString = JSON.toJSONString(param22);
            log.info("jsonString :{}", jsonString);

//            String body = HttpRequest.post(url).form(param22)
//                    .execute().body();

            String body = HttpRequest.post(url).body(JSON.toJSONString(param22), ContentType.JSON.getValue()).execute().body();
            log.info("body : {}", body);

//            String cipherText = "7458C762E0E64C0890B2EDDC2B7952F4FA6B38E23D93F71341E6AB36FC527B6B";
//            //返回参数解密
//            SM4Utils sm4 = new SM4Utils();
//            sm4.secretKey = sk;
//            //解密出来明文
//            String plainText = sm4.decryptData_ECB(cipherText);
//            log.info("明文: {}", plainText);
        } catch (Exception e) {
            log.error("error:{}",e.getMessage(), e);
        }
    }
}
