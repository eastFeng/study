package com.dongfeng.study.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author eastFeng
 * @date 2020-10-22 14:08
 */
@Data
public class Good extends Thing implements Serializable {
    private static final long serialVersionUID = 7879349098715415002L;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 价格
     */
    private double price;

    private Boolean testB = false;

    private String testA;

    public Good(){}

    public Good(String id, String type, String name, Integer number, double price){
        super(id);
        this.type = type;
        this.name = name;
        this.number = number;
        this.price = price;
    }
}
