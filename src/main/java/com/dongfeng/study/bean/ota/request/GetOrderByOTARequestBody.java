package com.dongfeng.study.bean.ota.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 【畅游通提供】【订单】OTA获取订单信息接口（getOrderByOTA）
 * xml请求体对应的请求参数实体类
 *
 * @author eastFeng
 * @date 2020-12-07 16:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOrderByOTARequestBody", propOrder = {
        "partnerOrderId",
        "orderId"
})
public class GetOrderByOTARequestBody extends OtaRequestBody{
    // partnerOrderId和orderId这俩参数传一个就可以请求到订单信息，订单信息里面包含入园二维码链接
    /**
     * 畅游通生成的订单ID
     */
    @XmlElement(required = true)
    private String partnerOrderId;
    /**
     * OTA订单号
     */
    @XmlElement(required = true)
    private String orderId;

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
