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
    /**
     * 请求是否加密
     * <p> 请求头中的requestEncryption参数值如果为true，则请求是加密的，否则没有加密。
     */
    String REQUEST_ENCRYPTION = "requestEncryption";
    /**
     * 请求如果是加密的，这个就是密码Base64编码后的的
     */
    String REQUEST_ENCRYPTION_CODE = "reCode";

    /* -------------  RabbitMQ相关, Start ----------------- */
    // Topic类型的Exchange的名字
    String RABBIT_TOPIC_EXCHANGE_NAME = "topic_exchange_test";
    // 消息队列（Queue）的名称
    String RABBIT_QUEUE_NAME = "queue_test";
    // 消息队列（Queue）2的名称
    String RABBIT_QUEUE2_NAME = "queue_test_2";
    // 路由键（RoutingKey）
    String RABBIT_ROUTING_KEY = "*.myTest.#";
    /* -------------  RabbitMQ相关, Start ----------------- */
}
