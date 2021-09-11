package com.dongfeng.study.allconf.interceptor;

import com.dongfeng.study.bean.base.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * <b> 登录拦截器 </b>
 *
 * @author eastFeng
 * @date 2020-11-18 15:43
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public static final String CURRENT_USER = "dongfeng.study.currentUser";

    /**
     * <b> 方法执行前。
     * <p> 对客户端发过来的请求进行前置处理。
     * <p> 如果方法返回 true,继续执行后续操作，如果返回 false，执行中断请求处理，请求不会发送到Controller。
     *
     * @param request HttpServletRequest 当前Http请求
     * @param response HttpServletResponse 当前Http响应
     * @param handler 为类型和/或实例计算选择要执行的处理程序
     * @return true 用户已登录；false未登录，前端跳转到登录页面
     * @throws Exception 如果发生错误
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
//            log.info("------登录拦截器 方法执行前------");

            // 从请求头中获取token
            String token = request.getHeader(Constants.TOKEN);
            log.info("登录拦截器 请求头中token:{}", token);
            if (StringUtils.isBlank(token)){
                // 没有登录请重新登录
                setUnLoginJson(response);
                return false;
            }

            // RequestContextHolder: 请求上下文持有者
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                attributes.setAttribute(CURRENT_USER, token, 0);
            }

            return true;
        } catch (Exception e) {
            log.error("登录校验异常: {}", e.getMessage(), e);
        }
        setUnLoginJson(response);
        return false;
    }

    /**
     * 设置未登录响应
     * @param response 当前Http响应
     */
    private void setUnLoginJson(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print("{\"code\":2001, \"msg\":\"请重新登录\", \"data\":null}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 方法执行后
//     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("------登录拦截器 方法执行后------");
//    }
}
