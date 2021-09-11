package com.dongfeng.study.bean.ota.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 订单信息
 * @author eastFeng
 * @date 2020-12-08 20:01
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderInfo", propOrder = {
        "partnerorderId",
        "orderStatus",
        "orderQuantity",
        "eticketSended",
        "useQuantity",
        "consumeInfo",
        "otherQRCode"
})
public class OrderInfo {
    /**
     * 订单ID
     */
    @XmlElement(required = true)
    private String partnerorderId;

    /**
     * 订单状态
     */
    @XmlElement(required = true)
    private String orderStatus;

    /**
     * 订单剩余票数
     */
    @XmlElement(required = true)
    private String orderQuantity;

    /**
     * 取票码发送状态
     * TRUE: 电子票已发送
     */
    @XmlElement(required = true)
    private String eticketSended;

    /**
     * 已消费票数
     */
    @XmlElement(required = true)
    private String useQuantity;

    /**
     * 消费信息
     */
    @XmlElement(required = true)
    private String consumeInfo;

    /**
     * 第三方票码（入园二维码）
     */
    @XmlElement(required = true)
    private String otherQRCode;

    public String getPartnerorderId() {
        return partnerorderId;
    }

    public void setPartnerorderId(String partnerorderId) {
        this.partnerorderId = partnerorderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getEticketSended() {
        return eticketSended;
    }

    public void setEticketSended(String eticketSended) {
        this.eticketSended = eticketSended;
    }

    public String getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(String useQuantity) {
        this.useQuantity = useQuantity;
    }

    public String getConsumeInfo() {
        return consumeInfo;
    }

    public void setConsumeInfo(String consumeInfo) {
        this.consumeInfo = consumeInfo;
    }

    public String getOtherQRCode() {
        return otherQRCode;
    }

    public void setOtherQRCode(String otherQRCode) {
        this.otherQRCode = otherQRCode;
    }
}
