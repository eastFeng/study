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
    /**
     * 上传到本地的文件存储路径
     */
    String UPLOAD_FILE_STORAGE_PATH = "D:\\CodingStudy\\UploadFile\\springboot-study\\";

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


    // --------------  我本人电脑一些图片文件或者文本文件路径 start -------------------
    // 文本文件路径
    String TEXT_TEST1_PATH = "D:\\CodingStudy\\someFile\\Test1.txt";
    String TEXT_TEST2_PATH = "D:\\CodingStudy\\someFile\\Test2.txt";
    String TEXT_TEST3_PATH = "D:\\CodingStudy\\someFile\\Test3.txt";
    String TEXT_TEST4_PATH = "D:\\CodingStudy\\someFile\\Test4.txt";
    // 图片路径
    String IMAGE_TEST1_PATH = "D:\\MyFiles\\Pictures\\work\\test1.jpg";
    String IMAGE_TEST2_PATH = "D:\\MyFiles\\Pictures\\work\\test2.jpg";
    String IMAGE_TEST3_PATH = "D:\\MyFiles\\Pictures\\work\\test3.jpg";
    String IMAGE_TEST4_PATH = "D:\\MyFiles\\Pictures\\work\\test4.jpg";
    // --------------  我本人电脑一些图片文件或者文本文件路径 end ---------------------
}
