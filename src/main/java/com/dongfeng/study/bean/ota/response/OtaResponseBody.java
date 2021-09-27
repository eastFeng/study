package com.dongfeng.study.bean.ota.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * 对应返回值（解码之后的xml格式）中的body部分
 *
 * @author eastFeng
 * @date 2020-12-08 19:18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtaResponseBody")
@XmlSeeAlso({
        // 子类
        GetOrderByOTAResponseBody.class
})
public class OtaResponseBody {
    // 父类，为了后面请求ota其他接口，扩展，XmlSeeAlso注解里面可以定义多个子类
}
