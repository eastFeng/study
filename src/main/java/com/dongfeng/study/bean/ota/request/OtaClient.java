package com.dongfeng.study.bean.ota.request;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.dongfeng.study.bean.ota.response.GetOrderByOTAResponseBody;
import com.dongfeng.study.bean.ota.response.OrderInfo;
import com.dongfeng.study.bean.ota.response.OtaResponse;
import com.dongfeng.study.bean.ota.response.OtaResponseHeader;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2020-12-07 20:22
 */
public class OtaClient {
    public static void main(String[] args) {
        GetOrderByOTARequestBody order = new GetOrderByOTARequestBody();
        order.setPartnerOrderId("20201208112341834");

        OtaResponse response = execute("getOrderByOTA", order);
        if (response ==null){
            System.out.println("hhh");
        }else {
            OtaResponseHeader header = response.getHeader();
            System.out.println(JSON.toJSONString(header));
            GetOrderByOTAResponseBody body = (GetOrderByOTAResponseBody) response.getBody();
            System.out.println(JSON.toJSONString(body));

            OrderInfo orderInfo = body.getOrderInfo();
            // 入园二维码
            String qrCode = orderInfo.getOtherQRCode();
        }
    }

    /**
     * 请求OTA接口
     *
     * <p>请求参数：
     * 1.method : 请求接口方法名称
     * 2.requestParam : 请求参数的Json字符串
     *
     * <p>requestParam以及返回值均由Json数据串组成，Json数据包括以下属性:
     * 1.data(String) : data是由QMRequestDataSchema.xsd与QMResponseDataSchema.xsd进行数据格式的规范的XML格式字符串，
     *                  经过XML Base64处理后的结果字符串。
     * 2.signed(String) : MD5(signkey+data)  ,,  singkey:ota提供的安全认证（16位）
     * 3.securityType : MD5
     *
     * @param method 请求接口方法名称
     * @param requestBody 组装requestParam中的body的请求参数
     */
    public static OtaResponse execute(String method, OtaRequestBody requestBody){
        JSON.toJSONString(requestBody);
        // requestParam的request报文
        OtaRequest request = getRequest(requestBody);
        try {
            // 转为xml
            String requestString = marshall(request);
            System.out.println(requestString);
            // data : base64编码之后的字符串
            String data = Base64.encode(requestString);

            String signkey = "ec30824b5dg6tyR2v";
            // signed : MD5(singkey+data)
            String signed = SecureUtil.md5(signkey+data);
            System.out.println("signed.length : "+signed.length());

            // 组装requestParam
            RequestParam requestParam = new RequestParam();
            requestParam.setData(data);
            requestParam.setSigned(signed);
            // securityType固定不变
            requestParam.setSecurityType("MD5");

            String url = "http://js.yylxjt.com/service_/distributor_.do";
            Map<String, Object> param = new HashMap<>(2);
            param.put("method", method);
            param.put("requestParam", JSON.toJSONString(requestParam));
            // post 表单请求
            String result = HttpRequest.post(url).form(param).execute().body();
            System.out.println(result);
            if (StringUtils.isBlank(result)){
                return null;
            }
            RequestParam responseObj = JSON.parseObject(result, RequestParam.class);
            // 返回的data是xml形式的数据(字符串)base64编码之后的字符串
            String dataBase64 = responseObj.getData();
            // base64解码--->解码后为xml形式
            String resData = Base64.decodeStr(dataBase64);
            System.out.println();
            System.out.println(resData);
            // xml转java对象并返回
            return (OtaResponse) unmarshal(resData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组装请求数据
     * @param body 请求体
     * @return OtaRequest
     */
    private static OtaRequest getRequest(OtaRequestBody body){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OtaRequest request = new OtaRequest();
        // 设置body : Request Body
        request.setBody(body);

        // 设置header : Request Header
        OtaRequestHeader header = new OtaRequestHeader();
        // 系统版本，固定
        header.setApplication("tour.ectrip.com");
        // 处理程序，固定
        header.setProcessor("DataExchangeProcessor");
        // 版本，固定
        header.setVersion("v1.0.0");
        // 创建时间
        String createTime = dateFormat.format(new Date());
        header.setCreateTime(createTime);
        // 消息体类型
        header.setBodyType(body.getClass().getSimpleName());
        // 创建用户
        header.setCreateUser("wllvmama");
        // 供应商用户名
        header.setSupplierIdentity("wlyylx");

        request.setHeader(header);
        return request;
    }

    /**
     * Java对象转换为XML---【OtaRequest对象转换为XML】
     *
     * <p>JAXBContext : 建立XML和Java类之间的映射关系
     * 需要通过它来创建用于转换Java对象到XML的Marshaller或是创建用于转换XML到Java对象的Unmarshaller
     */
    private static String marshall(OtaRequest request) throws JAXBException {
        // 创建JAXBContext
        JAXBContext jaxbContext = JAXBContext.newInstance(request.getClass());
        // 创建Marshaller对象
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(request, writer);
        return writer.toString();
    }

    /**
     * XML转换为Java对象---【转换为OtaResponse对象】
     *
     * <p>JAXBContext : 建立XML和Java类之间的映射关系
     * 需要通过它来创建用于转换Java对象到XML的Marshaller或是创建用于转换XML到Java对象的Unmarshaller
     */
    private static Object unmarshal(String data) throws JAXBException {
        //创建JAXBContext对象
        JAXBContext jaxbContext = JAXBContext.newInstance(OtaResponse.class);
        //创建Unmarshaller对象
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(data);
        return unmarshaller.unmarshal(reader);
    }
}
