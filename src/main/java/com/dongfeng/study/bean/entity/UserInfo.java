package com.dongfeng.study.bean.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author eastFeng
 * @date 2021-09-19 2:59
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 2720363901857944312L;

    private Integer id;

    private Integer userLoginId;

    private String name;

    private Integer sex;

    private Long phoneNumber;

    private Integer idCardType;

    private String idCardNo;

    private String email;

    private Date birthday;

    private String country;

    private String nation;

    private Integer memberLevel;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;
}
