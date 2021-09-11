package com.dongfeng.study.bean.ota.request;

import javax.xml.bind.annotation.*;


/**
 * 编码之前的xml请求参数对应的实体类
 *
 * @author eastFeng
 * @date 2020-12-07 16:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "body"
})
@XmlRootElement(name = "request")
public class OtaRequest {
    /**
     * 对应xml里面的header部分
     */
    @XmlElement(required = true)
    private OtaRequestHeader header;
    /**
     * 对应xml里面的body部分
     */
    @XmlElement(required = true)
    private OtaRequestBody body;

    /**
     * Gets the value of the header property.
     *
     * @return
     *     possible object is
     *     {@link OtaRequestHeader }
     *
     */
    public OtaRequestHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value
     *     allowed object is
     *     {@link OtaRequestHeader }
     *
     */
    public void setHeader(OtaRequestHeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the body property.
     *
     * @return
     *     possible object is
     *     {@link OtaRequestBody }
     *
     */
    public OtaRequestBody getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     *
     * @param value
     *     allowed object is
     *     {@link OtaRequestBody }
     *
     */
    public void setBody(OtaRequestBody value) {
        this.body = value;
    }

}
