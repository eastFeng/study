package com.dongfeng.study.bean.base;

/**
 * <b>业务常量</b>
 *
 * @author eastFeng
 * @date 2020/8/15 - 14:31
 */
public interface Constants {
    /**
     * 系统
     */
    String SYSTEM = "SYSTEM";
    /**
     * 我方私钥
     */
    String MY_PRIVATE_KEY = "";
    String CEBBANK_PUBLIC_KEY = "";
    /**
     * 登录token
     */
    String TOKEN = "token";
    /**
     * 日志的traceId
     */
    String TRACE_ID = "traceId";

    /* -------------  RabbitMQ相关, Start ----------------- */
    // Topic类型的Exchange的名字
    String RABBIT_TOPIC_EXCHANGE_NAME = "topic_exchange_test";
    // 消息队列（Queue）的名称
    String RABBIT_QUEUE_NAME = "queue_test";
    // 路由键（RoutingKey）
    String RABBIT_ROUTING_KEY = "*.myTest.#";
    /* -------------  RabbitMQ相关, Start ----------------- */
}
