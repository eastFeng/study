package com.dongfeng.study.bean.ota.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 对应返回值（解码之后的xml格式）中的header部分
 *
 * @author eastFeng
 * @date 2020-12-08 19:12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtaResponseHeader", propOrder = {
        "application",
        "processor",
        "version",
        "bodyType",
        "createUser",
        "createTime",
        "code",
        "describe"
})
public class OtaResponseHeader {

    /**
     * 系统版本
     * tour.ectrip.com  不变
     */
    @XmlElement(required = true)
    private String application;

    /**
     * 处理程序
     * DataExchangeProcessor 不变
     */
    @XmlElement(required = true)
    private String processor;

    /**
     * 版本
     * v1.0.0 不变
     */
    @XmlElement(required = true)
    private String version;

    /**
     * 消息体类型：依据BodyType实际类型填写
     */
    @XmlElement(required = true)
    private String bodyType;

    /**
     * 创建用户：分销商用户名
     */
    @XmlElement(required = true)
    private String createUser;

    /**
     * 创建时间
     * yyyy-MM-dd HH:mm:ss
     */
    @XmlElement(required = true)
    private String createTime;

    /**
     * 响应码：1000是成功
     */
    @XmlElement(required = true)
    private String code;

    /**
     * 对响应的描述
     */
    @XmlElement(required = true)
    private String describe;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
