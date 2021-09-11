package com.dongfeng.study.bean.ota.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 【畅游通提供】【订单】OTA获取订单信息接口（getOrderByOTA）
 * 返回参数实体类
 *
 * @author eastFeng
 * @date 2020-12-08 19:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOrderByOTAResponseBody", propOrder = {
        "orderInfo"
})
public class GetOrderByOTAResponseBody extends OtaResponseBody{

    @XmlElement(required = true)
    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
