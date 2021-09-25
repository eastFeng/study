package com.dongfeng.study.config.rabbit;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.sourcecode.java8.util.Map;
import com.dongfeng.study.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author eastFeng
 * @date 2021-09-25 18:46
 */
@Slf4j
@Component
// @RabbitListener : 监听消息队列, 如果注解在类上, 需要配合注解了@RabbitHandler的方法使用
@RabbitListener(queues = Constants.RABBIT_QUEUE_NAME)
public class TestConsumer {

    @Autowired
    private RedisUtil redisUtil;

    // 表示接收消息后的处理方法
    @RabbitHandler
    public void receiveString(String str){
        log.info("消费者接收到消息 String_message:{}", str);

        // 进行一些其他耗时业务 : 比如发邮件，下订单等等
    }

    // 表示接收消息后的处理方法 , 根据消息的类型区分不同的方法
    @RabbitHandler
    public void receiveMap(Map<String, Object> map){
        log.info("消费者接收到消息 Map_message:{}", map);
    }

    @RabbitHandler
    public void receiveList(List<Object> list){
        log.info("消费者接收到消息 List_message:{}", list);
    }

    @RabbitHandler
    public void receiveMessage(Message message){
        String logID = "logID-"+UUID.randomUUID().toString();
        log.info("消费者接收到消息 logID:{},Message:{}", logID, message);
        try {
            // 消息ID
            String messageId = message.getMessageProperties().getMessageId();
            // 真正的消息
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);

            String messageIdRedis = redisUtil.get("rabbit_message_id");
            if (StringUtils.equals(messageId, messageIdRedis)){
                return;
            }

            //
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String email = jsonObject.getString("email");
            String content = jsonObject.getString("timestamp");

            String httpUrl = "http://127.0.0.1:8080/email?email"+email+"&content="+content;

            // http请求
            String result = HttpUtil.createPost(httpUrl)
                    .body(jsonObject.toJSONString(), ContentType.JSON.toString())
                    .header("header1", "value1")
                    .setConnectionTimeout(5000)
                    .execute().body();
            log.info("receiveMessage logID:{}, result:{}", logID, result);
            //
            if (StringUtils.isBlank(result)){
            }
        } catch (Exception e) {
            log.info("receiveMessage logID:{}, error:{}",logID, e.getMessage(), e);
        }
    }
}
