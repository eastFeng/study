package com.dongfeng.study.bean.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author eastFeng
 * @date 2021-03-02 15:29
 */
@Data
public class MqSendReq implements Serializable {

    private static final long serialVersionUID = -7386676012399142347L;

    /**
     * 交换器名称
     */
    private String exchange;
    /**
     * 路由键（routingKey）
     */
    private String routingKey;
    /**
     * 要往RabbitMq队列发送的信息
     */
    private String msg;
}
