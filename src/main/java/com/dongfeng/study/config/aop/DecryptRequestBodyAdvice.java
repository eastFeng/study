package com.dongfeng.study.config.aop;

import cn.hutool.core.codec.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * RequestBodyAdvice : 进行统一参数处理。
 * <p>在实际项目中，往往需要对请求参数做一些统一的操作，例如参数的过滤，字符的编码，第三方的解密等等。
 * Spring提供了RequestBodyAdvice一个全局的解决方案 , 免去了我们在Controller处理的繁琐。
 *
 * <p>RequestBodyAdvice仅对使用了@RqestBody注解（请求体）的生效，因为它原理上还是AOP，所以GET方法是不会操作的。
 *
 * <p>允许在读取请求体并将其转换为对象之前自定义请求，
 * 还允许在将结果对象作为@RequestBody或HttpEntity方法参数传递到控制器方法之前对其进行处理。
 *
 * <p>可与@ControllerAdvice相结合
 *
 * @author eastFeng
 * @date 2020-11-27 15:04
 */
@ControllerAdvice(basePackages = "com.dongfeng.study.controller") //basePackages: 设置需要当前Advice执行的域 , 省略默认全局生效
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 首先调用以确定此拦截器是否适用。
     * @param methodParameter 方法参数
     * @param targetType 目标类型，不一定与方法参数类型相同，例如对于HttpEntity<String>。
     * @param converterType 所选转换器类型
     * @return 是否应调用此拦截器
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        //如果返回false，就不会调用此拦截器的业务了
        return true;
    }

    /**
     * 在读取和转换请求主体之前调用。
     * <p>在此做些编码，解密，封装参数为对象的操作。
     * <p>入参放在HttpInputMessage里面  这个方法的返回值也是HttpInputMessage
     * @param inputMessage 请求
     * @param parameter 目标方法参数
     * @param targetType 目标类型，不一定与方法参数类型相同，例如对于HttpEntity<String>。
     * @param converterType 用于反序列化请求体的转换器
     * @return 输入请求或者一个新实力，从不为null
     * @throws IOException IO异常
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        //1. 请求是否是加密的，如果不是，不用处理请求体
        String ccEnable = inputMessage.getHeaders().getFirst("ccEnable");
        if (!"true".equals(ccEnable)){
            //请求没有加密
            return inputMessage;
        }

        //2. 转换入参
        StringBuilder stringBuilder = new StringBuilder();
        //BufferedReader：字符流读取的效率，引入了缓冲机制，进行字符批量的读取。拥有8192个字符的缓冲区
        BufferedReader bufferedReader = null;
        try {
            //获取请求体数据流
            InputStream bodyInputStream = inputMessage.getBody();
            bufferedReader = new BufferedReader(new InputStreamReader(bodyInputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead=bufferedReader.read(charBuffer)) > 0){
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            //关闭流
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //获取前端传入的ccCode（前端进行过Base64编码）
        String ccCode = inputMessage.getHeaders().getFirst("ccCode");
        //Base64编码：基于64个可打印字符来表示二进制数据，Base64编码是从二进制到字符的过程，采用Base64编码具有不可读性，需要解码后才能阅读。
        //Base64编码是用64（2的6次方）个ASCII字符来表示256（2的8次方）个ASCII字符，也就是三位二进制数组经过编码后变为四位的ASCII字符显示，长度比原来增加1/3。
        //编码规则：1.把3个字节变成4个字节  2.每76个字符加一个换行符  3.最后的结束符也要处理
        String nk = Base64.decodeStr(ccCode);
        int length = nk.length();
        //substring: 开始下标包括，结束下标不包括
        //后面3个放在前面
        String password = nk.substring(length - 3, length) + nk.substring(0, length - 3);

        //请求体数据
        String body = stringBuilder.toString();


        return null;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body,
                                  HttpInputMessage inputMessage,
                                  MethodParameter parameter,
                                  Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 实现了HttpInputMessage接口，封装自己的HttpInputMessage
     */
    public static class MyHttpInputMessage implements HttpInputMessage{
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpHeaders headers, InputStream body) {
            this.headers = headers;
            this.body = body;
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }


}
