package com.dongfeng.study.bean.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * 用户扫码记录
 * @author eastFeng
 * @date 2022-04-19 22:27
 */
@Data
@ToString
@EqualsAndHashCode
@Document
public class UserPunch {
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 用户ID
     */
    @Indexed
    private String userId;
    /**
     * B端申请表ID
     */
    @Indexed
    private String formId;
    /**
     * C端用户登记信息ID
     */
    private String userRegInfoId;
    /**
     * 预约ID(UserBook主键)
     * 如果bookId为null, 说明场所没有开启预约
     */
    @Indexed
    private String bookId;
    /**
     * 场所名称
     */
    private String placeName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 证件类型：1身份证，2港澳回乡证，3台胞证
     */
    private String idType;
    /**
     * 证件号码
     */
    private String idCardNum;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 省份(省级行政区)
     */
    private String province;
    /**
     * 身体是否有不适症状（服务端计算）
     */
    private Boolean discomfort;

    private Boolean isolateEnd;

    /**
     * 来自哪里
     */
    private Region comeFrom;
    /**
     * 渠道号
     */
    private String channelCode;

    /**
     * 创建时间 : 打卡时间
     * DESCENDING：倒序索引
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 打卡日期是否是工作日
     * Y:是，N:不是
     */
    private String isWorkDay;
    /**
     * 打卡日期所在年份的第几周
     */
    private Integer weekOfYear;
    /**
     * 打卡日期的月份：所在年的月份
     */
    private Integer month;
    /**
     * 健康码
     */
    private String healthCode;
    /**
     * 健康码
     */
    private String productCode;
    /**
     * 打卡类型
     * <p>详见 {@link com.dongfeng.study.bean.enums.PunchTypeEnum} 枚举</p>
     */
    private String punchType;


    /**
     * 地区
     */
    @Data
    @EqualsAndHashCode
    @ToString
    public static class Region {
        /**
         * 省
         */
        private String provinceCode;
        /**
         * 市
         */
        private String cityCode;
        /**
         * 区
         */
        private String areaCode;
    }
}
