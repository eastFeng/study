package com.dongfeng.test;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eastFeng
 * @date 2020-10-12 22:15
 */
@Slf4j(topic = "------")
public class HttpTest {
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

//        donghua("1401");
//        donghua("1402");
//        donghua("1403");
//        donghua("1404");
//        donghua("1405");
//        donghua("1406");
//        donghua("1407");
//        donghua("1408");
//        donghua("1409");
//        donghua("1410");
//        donghua("1411");

//        donghua2("14");

                donghua("142");

//        String timestamp = "1599183668000";
//
//        String content = 14+"_"+timestamp;
//
//        String md5 = SecureUtil.md5(content);
//        System.out.println(md5);
    }


    @Test
    public void postTest(){
        String url = "";
        JSONObject json = new JSONObject();
        json.put("mobile", "18120412883");

        //URL
        String result = HttpRequest.post(url)
                //请求体和请求体类型
                .body(json.toJSONString(), ContentType.JSON.getValue())
                //字符集: 默认是UTF-8
                .charset("UTF-8")
                //设置超时时间(毫秒): 1. 连接超时 2. 读取响应超时
                .timeout(3000)
                //设置连接超时
                .setConnectionTimeout(6000)
                //设置读取超时
                .setReadTimeout(60000)
                .execute().body();

        ////
        System.out.println("http request result body : "+result);
    }


    @Test
    public void tianyiyunDongHua(){

//        final String someInfoUrl = "https://one.yylxjt.com/city-marketing-api-mgt-app/external/interface/someInfo";
//
//        JSONObject json = new JSONObject();
//        json.put("timestamp", "1598864400000");
//        json.put("sign", "766bff79a372fa76cb46ff20f6af4b65");
//
//        String body = HttpRequest.post(someInfoUrl)
//                .body(json.toJSONString(), ContentType.JSON.getValue())
//                .timeout(3000)
//                .execute().body();
//        log.info("result:{}", body);


        final String cityUserInfo = "https://one.yylxjt.com/city-marketing-api-mgt-app/external/interface/city/userInfo";
        JSONObject json2 = new JSONObject();
        json2.put("timestamp", "20201116115331");
        json2.put("sign", "fd5aa0aae0cf9fc8403b26e423be06b4");
        json2.put("idCardPrefix", "14");
        String body2 = HttpRequest.post(cityUserInfo)
                .body(json2.toJSONString(), ContentType.JSON.getValue())
                .timeout(30000)
                .execute().body();
        log.info("cityUserInfo result:{}", body2);
    }



    public static void donghua(String idCardPrefix){

        String url = "https://one.yylxjt.com/city-marketing-api-mgt-app/external/interface/city/userInfo";

        String timestamp = "1599183668000";

        String content = idCardPrefix+"_"+timestamp;

        String md5 = SecureUtil.md5(content);

        JSONObject json = new JSONObject();
        json.put("idCardPrefix", idCardPrefix);
        json.put("timestamp", timestamp);
        json.put("sign", md5);

        String body = HttpRequest.post(url)
                .body(json.toJSONString(), ContentType.JSON.getValue())
                .timeout(30000)
                .execute().body();
        log.info("idCardPrefix:{}, result:{}",idCardPrefix, body);
    }

    public static void donghua2(String idCardPrefix){

        String url = "http://one.joyuai.com/city-marketing-api-mgt-app/external/interface/city/userInfo";

        String timestamp = "1599183668000";

        String content = idCardPrefix+"_"+timestamp;

        String md5 = SecureUtil.md5(content);

        JSONObject json = new JSONObject();
        json.put("idCardPrefix", idCardPrefix);
        json.put("timestamp", timestamp);
        json.put("sign", md5);

        String body = HttpRequest.post(url)
                .body(json.toJSONString(), ContentType.JSON.getValue())
                .timeout(30000)
                .execute().body();
        log.info("idCardPrefix:{}, result:{}",idCardPrefix, body);
    }


    @Test
    public void countBookPerDayTotal(){
        ArrayList<String> formIdList = new ArrayList<>(10);
        formIdList.add("5f11729dd6920c45eab22253");
        formIdList.add("5f5ad9964141b9f53fad4e26");
        formIdList.add("5f12afb0d6920c45ea98d8");
        formIdList.add("5f124a85d6920c45ea693e23");
        formIdList.add("5f1266ccd6920c45eaf22b93");
        formIdList.add("5f124d09d6920c45ea735b7b");

        String url = "https://one.yylxjt.com//city-marketing-api-mgt-app/hym/batch/countBookPerDayTotal";

        Map<String, Object> param = new HashMap<>(3);
        param.put("secret", "ksmujw29023@j3jrf8929$3ijfj9203@jsj0!2");

        for (String id : formIdList) {
            param.put("formId", id);
            param.put("dateStr", "20201116");

            String result = HttpRequest.get(url).form(param).execute().body();
            System.out.println(result);
        }

    }

    @Test
    public void getOrderByOTA(){
        String url = "http://ota.ectrip.com/APIForOTA/getOrderByOTA";
        String signkey = "test";

        //signed : MD5(signkey + data)
        String signed = "";

    }


    @Test
    public void toEmail(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(1594467719000L));

        String booToEmailUrl = "http://one.yylxjt.com/city-marketing-api-mgt-app/hym/batch/bookToEmail";
        String punchToEmailUrl = "http://one.yylxjt.com/city-marketing-api-mgt-app/hym/batch/punchToEmail";

        for (int i=0; i<150; i++){
            if (i!=0){
                calendar.add(Calendar.DATE, 1);
            }
            String dateStr = dateFormat.format(calendar.getTime());
            System.out.println(dateStr);
            Map<String, Object> param = new HashMap<>();
            param.put("secret", "ksmujw29023@j3jrf8929$3ijfj9203@jsj0!2");
            param.put("startDate", dateStr);
            param.put("endDate", dateStr);
            param.put("email", "yuanhuaming@joyulf.com");

            String body = HttpRequest.get(booToEmailUrl).form(param).execute().body();
            System.out.println(body);

            try {
                //休眠一秒
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
