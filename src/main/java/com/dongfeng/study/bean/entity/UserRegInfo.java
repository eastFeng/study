package com.dongfeng.study.bean.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author eastFeng
 * @date 2022-04-19 22:32
 */
@Data
@ToString
@EqualsAndHashCode
@Document
@CompoundIndexes({
        @CompoundIndex(name = "createTime_id", def = "{'createTime': -1, _id: -1}")
})
public class UserRegInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 登记用户信息原始id
     */
    @Indexed
    private String userOriRegId;
    /**
     * 用户ID
     */
    @Indexed
    private String userId;
    /**
     * 场所表ID
     */
    private String formId;
    /**
     * 姓名
     */
    @Indexed
    private String name;
    /**
     * 身份证号码
     */
    @Indexed
    private String idCardNum;

    /**
     * 身份证号码密文
     */
    private String idCardNumCipherText;

    /**
     * 证件类型：1身份证，2港澳回乡证，3台胞证
     */
    private String idType;

    /**
     * 手机号
     */
    private String mobile;

    private Boolean isolateEnd;
    /**
     * 记录是否有效
     */
    private Boolean valid;

    private ComeInfo comeInfo;
    /**
     * 身体是否有不适症状
     */
    private Boolean discomfort;

    private String licensePlate;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否实名认证  已实名认证:Y
     * 无此字段或者是N则是未实名认证
     */
    private String isRealName;
    /**
     * 实名认证结果
     */
    private RealNameResult realNameResult;
    /**
     * 健康码图片url
     */
    private String healthCodeUrl;
    /**
     * 健康码图片, cdn加速后的url
     */
    private String cdnHealthCodeUrl;

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


    @Data
    @EqualsAndHashCode
    @ToString
    public static class ComeInfo {
        private Region fromWhere;
        /**
         * 交通方式
         */
        private List<ComeTransportInfo> transportInfoList;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class ComeTransportInfo {
        /**
         * 车次/船次/航班/车牌
         */
        private String trainNum;
    }

    @Data
    @EqualsAndHashCode
    public static class RealNameResult{
        //此身份证已经被实名认证所在的id
        private String authUserRegInfoId;
        //实名认证结果
        private String result;
    }
}
