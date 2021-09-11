package com.dongfeng.study.basicstudy.jdbc;

import java.io.Serializable;
import java.util.Date;

/**
 * @author eastFeng
 * @date 2020/4/21 - 20:27
 */
public class Order implements Serializable {
    private Integer orderId;

    private Integer orderType;

    private String orderName;

    private Date createTime;

    private Date updateTime;

    public Order() {}

    public Order(Integer orderId, Integer orderType, String orderName, Date createTime, Date updateTime) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderName = orderName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderType=" + orderType +
                ", orderName='" + orderName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
