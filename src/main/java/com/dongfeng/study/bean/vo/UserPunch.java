package com.dongfeng.study.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author eastFeng
 * @date 2020-10-22 16:22
 */
@Data
public class UserPunch {
    private String name;
    private String mobile;
    private String sex;
    private Integer age;
    private String province;
    private String idCardNum;
    private Date punchTime;
}
