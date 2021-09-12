package com.dongfeng.study.bean.req;

import lombok.Data;

import java.io.Serializable;

/**
 * <b> 声明topic类型交换器、队列的参数 </b>
 *
 * @author eastFeng
 * @date 2021-03-02 15:21
 */
@Data
public class DeclareQueueReq implements Serializable {

    private static final long serialVersionUID = -3674366950523893628L;

    /**
     * 交换器名称
     */
    private String exchange;
    /**
     * 路由键（routingKey）
     */
    private String routingKey;
    /**
     * 队列名称
     */
    private String queueName;
}
