package com.dongfeng.study.util;

import com.dongfeng.study.bean.base.LoginUser;
import com.dongfeng.study.config.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link org.springframework.web.context.request.RequestContextHolder}请求上下文持有者工具类
 *
 * @see com.dongfeng.study.config.interceptor.LoginInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)
 * @author eastFeng
 * @date 2022-11-28 11:29
 */
@Slf4j
public class RequestContextHolderUtil {

    public static LoginUser getLoginUser(){
        log.info("getLoginUser start -------------------");
        // 1. 首先从【请求上下文持有者】中首先获取当前线程的RequestAttributes对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }

        // 2. 从RequestAttributes对象中获取属性
        LoginUser loginUser = (LoginUser) requestAttributes.getAttribute(
                        LoginInterceptor.CURRENT_USER,
                        RequestAttributes.SCOPE_REQUEST);
        log.info("getLoginUser loginUser:{}", loginUser);
        return loginUser;
    }
}
