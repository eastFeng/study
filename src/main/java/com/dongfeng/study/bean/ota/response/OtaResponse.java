package com.dongfeng.study.bean.ota.response;

import com.dongfeng.study.bean.ota.request.OtaRequestBody;

import javax.xml.bind.annotation.*;

/**
 * 对应返回值（解码之后的xml格式）
 *
 * @author eastFeng
 * @date 2020-12-08 19:07
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "header",
        "body"
})
@XmlRootElement(name = "response")
public class OtaResponse {
    /**
     * 对应xml里面的header部分
     */
    @XmlElement(required = true)
    private OtaResponseHeader header;
    /**
     * 对应xml里面的body部分
     */
    @XmlElement(required = true)
    private OtaResponseBody body;

    public OtaResponseHeader getHeader() {
        return header;
    }

    public void setHeader(OtaResponseHeader header) {
        this.header = header;
    }

    public OtaResponseBody getBody() {
        return body;
    }

    public void setBody(OtaResponseBody body) {
        this.body = body;
    }
}
