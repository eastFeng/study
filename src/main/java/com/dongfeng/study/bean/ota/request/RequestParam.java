package com.dongfeng.study.bean.ota.request;

/**
 * @author eastFeng
 * @date 2020-12-08 9:57
 */
public class RequestParam {
    /**
     * data:
     * 由QMRequestDataSchema.xsd与QMResponseDataSchema.xsd进行数据格式的规范的XML格式字符串，
     * 经过XML Base64处理后的结果字符串。
     */
    private String data;
    /**
     * signed: MD5(singkey+data)
     * singkey: ota提供的安全认证（16位）
     */
    private String signed;
    /**
     * 验证类型: md5
     */
    private String securityType;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSigned() {
        return signed;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }
}
