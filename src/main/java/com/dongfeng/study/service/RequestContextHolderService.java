package com.dongfeng.study.service;

import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.base.LoginUser;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.bean.vo.InterfaceA;
import com.dongfeng.study.bean.vo.TestVo;
import com.dongfeng.study.config.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

/**
 * RequestContextHolder (请求上下文持有者)的学习使用。
 *
 * <p> RequestContextHolder : 请求上下文持有者。Holder类以线程绑定的RequestAttributes对象的形式公开web请求。
 * 如果inheritable标志设置为true，则当前线程生成的任何子线程都将继承该请求。
 * <p> RequestContextHolder类中有两个ThreadLock属性用来保存当前线程下的request:
 * <ul>
 * <p> requestAttributesHolder : 得到存储进去的request
 * <li>ThreadLocal<RequestAttributes> requestAttributesHolder = new NamedThreadLocal<>("Request attributes");</li>
 * <p> inheritableRequestAttributesHolder : 可被子线程继承的request
 * <li>ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder = new NamedInheritableThreadLocal<>("Request context");</li>
 * </ul>
 * ThreadLocal<RequestAttributes>变量, 通过ThreadLocal保存每个线程自己的RequestAttributes变量。
 *
 * <p>request和response怎么和当前请求挂钩?  通过RequestContextHolder中的ThreadLock属性
 * <p>request和response等是什么时候设置进去的?  FrameworkServlet类的processRequest方法中
 *
 * <p> attribute: 属性
 * <p> RequestAttributes: 用于访问与请求相关联的属性对象的接口。 Abstraction for accessing attribute objects associated with a request.
 * 通过“global session”的可选概念，支持对请求范围属性以及会话范围属性的访问。
 *
 * <p> ServletRequestAttributes : 基于Servlet的RequestAttributes接口实现类。
 * 从servlet请求和HTTP session域中访问对象，不区分“session”和“global session”。
 *
 *
 * @author eastFeng
 * @date 2020-11-21 19:38
 */
@Slf4j
@Service
public class RequestContextHolderService {
    /**
     * --@Value("#{}") : SpEL表达式，通常用来获取bean的属性，或者调用bean的某个方法。当然还有可以表示常量
     * <p>当bean(比如Bean_A)通过@Value("#{}")获取其他bean(比如Bean_B)的属性，或者调用其他bean(比如Bean_B)的方法时，只要该bean(Bean_A)能够访问到被调用的bean(Bean_B)，
     * 即要么Bean_A和Bean_B在同一个容器中，或者Bean_B所在容器是Bean_A所在容器的父容器。
     *
     * <p>--@Value("${}") : 获取属性文件中定义的属性值
     *
     * <p>冒号：后面的false相当于默认值
     */
    @Value("${my.test.b:false}")
    private boolean aBoolean;

    @Value("#{233}")
    private int anInt;
//    @Value("#{TestVo.name}")
    private String name;

    @Autowired
    private List<InterfaceA> aList;


    public BaseResponse<LoginUser> test111(){
        log.info("【aBoolean:{}】", aBoolean);
        log.info("【anInt:{}】", anInt);
        log.info("【name:{}】", name);
        BaseResponse<LoginUser> baseResponse = new BaseResponse<>();

        // 1. 首先获取当前线程的RequestAttributes对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes==null){
            return BaseResponse.errorInstance(ResponseCodeEnum.UNKNOWN);
        }

        // 2. 从RequestAttributes对象中获取属性
        LoginUser loginUser = (LoginUser) requestAttributes.getAttribute(LoginInterceptor.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
        baseResponse.setData(loginUser);

        log.info("aList.size:{}", aList.size());
        log.info("aList:{}", aList);
        return baseResponse;
    }

    public BaseResponse<TestVo> test2(){
        BaseResponse<String> baseResponse = BaseResponse.successInstance("hhh");
        return BaseResponse.successInstance(new TestVo());
    }


}
