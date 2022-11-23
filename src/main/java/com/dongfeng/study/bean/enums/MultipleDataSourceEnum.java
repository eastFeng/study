package com.dongfeng.study.bean.enums;

/**
 * 多数据源枚举
 *
 * @author eastFeng
 * @date 2022-11-22 14:03
 */
public enum MultipleDataSourceEnum {
    DATA_SOURCE_1(1, "数据源/数据库1"),
    DATA_SOURCE_2(2, "数据源/数据库2")
    ;

    private final int code;
    private final String msg;

    MultipleDataSourceEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
