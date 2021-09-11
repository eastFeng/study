package com.dongfeng.study.bean.vo;

import lombok.Data;

/**
 * @author eastFeng
 * @date 2020-10-22 14:12
 */
@Data
public class Thing {
    /**
     * 物品唯一ID
     */
    private String id;

    public Thing(){}

    public Thing(String id){
        this.id = id;
    }
}
