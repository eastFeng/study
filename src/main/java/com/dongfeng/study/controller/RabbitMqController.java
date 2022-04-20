package com.dongfeng.study.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.bean.req.DeclareQueueReq;
import com.dongfeng.study.bean.req.MqSendReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 * RabbitMq
 *
 * @author eastFeng
 * @date 2021-03-02 14:43
 */
@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private AmqpAdmin amqpAdmin;


    @PostMapping("/sendList")
    public BaseResponse<String> sendList(@RequestBody MqSendReq req){
        log.info("sendList req:{}", req);
        if (CollectionUtil.isEmpty(req.getListMsg())){
            return BaseResponse.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        try {
//            Message message = MessageBuilder.
//                    withBody(req.getMsg().getBytes(StandardCharsets.UTF_8)) // 真正要发送的消息内容
//                    .setMessageId(UUID.randomUUID().toString()) // 设置消息ID
//                    .build();
//
//            rabbitTemplate.convertAndSend(Constants.RABBIT_TOPIC_EXCHANGE_NAME, // 指定交换机
//                    Constants.RABBIT_ROUTING_KEY, // 指定路由键
//                    message); // 消息


            rabbitTemplate.convertAndSend(Constants.RABBIT_TOPIC_EXCHANGE_NAME, // 指定交换机
                    Constants.RABBIT_ROUTING_KEY, // 指定路由键
                    req.getListMsg()); // 消息
            return BaseResponse.successInstance("success");
        } catch (Exception e) {
            log.error("sendList error:{}", e.getMessage(), e);
        }

        return BaseResponse.errorInstance(ResponseCodeEnum.UNKNOWN);
    }


     /**
     * 发送消息
     */
    @PostMapping("/sendString")
    public BaseResponse<String> sendString(@RequestBody MqSendReq req){
        log.info("sendString req:{}", req);
        if (StringUtils.isBlank(req.getMsg())){
            return BaseResponse.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        try {
            rabbitTemplate.convertAndSend(Constants.RABBIT_TOPIC_EXCHANGE_NAME, // 指定交换机
                                          Constants.RABBIT_ROUTING_KEY, // 指定路由键
                                          req.getMsg()); // 消息
            return BaseResponse.successInstance("success");
        } catch (Exception e) {
            log.error("sendString error:{}", e.getMessage(), e);
        }

        return BaseResponse.errorInstance(ResponseCodeEnum.UNKNOWN);
    }

    // ----------------------------------------------

    /**
     * 声明队列
     */
    @PostMapping("/declareQueue")
    public BaseResponse<String> declareQueue(@RequestBody DeclareQueueReq req){
        log.info("declareQueue req:{}", req);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        try {
            // 新建一个topic类型交换器
            TopicExchange topicExchange = new TopicExchange(req.getExchange());
            // 新建一个队列
            Queue queue = new Queue(req.getQueueName(), true, false, false, null);
            // 用路由键（routingKey）将队列和交换器绑定起来
            Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(req.getRoutingKey());

            // 声明交换器、队列、绑定关系
            declare(amqpAdmin, topicExchange, queue, binding);

            QueueInformation queueInfo = amqpAdmin.getQueueInfo(req.getQueueName());
            log.info("declareQueue success queueInfo:{}", JSON.toJSONString(queueInfo));

            baseResponse.setData(JSON.toJSONString(queueInfo));
            return baseResponse;
        } catch (Exception e) {
            log.error("declareQueue Exception req:{}, error:{}",req, e.getMessage(), e);
        }

        return BaseResponse.setError(baseResponse, ResponseCodeEnum.UNKNOWN);
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public BaseResponse<String> send(@RequestBody MqSendReq req){
        log.info("send req:{}", req);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        if (StringUtils.isAnyBlank(req.getExchange(), req.getRoutingKey(), req.getMsg())){
            return BaseResponse.setError(baseResponse, ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        try {
            rabbitTemplate.convertAndSend(req.getExchange(), req.getRoutingKey(), req.getMsg());

            baseResponse.setData("SUCCESS");
            return baseResponse;
        } catch (Exception e) {
            log.error("send Exception req:{}, error:{}",req, e.getMessage(), e);
        }

        return BaseResponse.setError(baseResponse, ResponseCodeEnum.UNKNOWN);
    }

    @GetMapping("receive")
    public BaseResponse<String> receive(@RequestParam String queueName){
        log.info("receive queueName:{}", queueName);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        if (StringUtils.isBlank(queueName)){
            return BaseResponse.setError(baseResponse, ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        try {
            Object result = rabbitTemplate.receiveAndConvert(queueName);
            log.info("receive result:{}", JSON.toJSONString(result));

            baseResponse.setData(JSON.toJSONString(result));
            return baseResponse;
        } catch (Exception e) {
            log.error("receive Exception queueName:{}, error:{}",queueName, e.getMessage(), e);
        }

        return BaseResponse.setError(baseResponse, ResponseCodeEnum.UNKNOWN);
    }

    /**
     * 声明队列等等
     */
    private void declare(AmqpAdmin amqpAdmin, Declarable... declares) {
        if (declares != null) {
            for (Declarable declarable : declares) {
                if (declarable instanceof Exchange) {
                    amqpAdmin.declareExchange((Exchange) declarable);
                } else if (declarable instanceof Queue) {
                    amqpAdmin.declareQueue((Queue) declarable);
                } else if (declarable instanceof Binding) {
                    amqpAdmin.declareBinding((Binding) declarable);
                } else {
                    throw new RuntimeException("Declarable is unknown.");
                }
            }
        }
    }
}
