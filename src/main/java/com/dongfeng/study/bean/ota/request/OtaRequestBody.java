package com.dongfeng.study.bean.ota.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 编码之前的对应xml里面的body部分
 *
 * @author eastFeng
 * @date 2020-12-07 16:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtaRequestBody")
@XmlSeeAlso({
        //子类
        GetOrderByOTARequestBody.class
})
public class OtaRequestBody{
    //父类，为了后面请求ota其他接口，扩展，XmlSeeAlso注解里面可以定义多个子类
}
