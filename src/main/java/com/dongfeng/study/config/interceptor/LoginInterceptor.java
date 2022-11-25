package com.dongfeng.study.config.interceptor;

import cn.hutool.core.date.DateUtil;
import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.bean.base.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

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
            log.info("------登录拦截器 方法执行前,path={},URL={}",request.getPathInfo(), request.getRequestURI());

            // 登录凭证根据业务而定。
            // 1.把用户token放入请求头中，根据用户token获取用户ID信息。
            // 2.登录成功的用户会在session中加入loginUser对象。

            // 1.从请求头中获取token
            String token = request.getHeader(Constants.TOKEN);
            log.info("登录拦截器 请求头中token:{}", token);
            if (StringUtils.isBlank(token)){
                // 没有登录请重新登录
                setUnLoginJson(response);
                return false;
            }
            LoginUser loginUser = getLoginUserByToken(token);
            if (null==loginUser || !checkAuth(loginUser.getUserId(),request.getRequestURI())){
                // 无UserId 或者 权限校验不通过
                setUnLoginJson(response);
                return false;
            }


            // 2. 判断session中是否存在loginUser，登录成功的用户会在session中加入loginUser对象
            HttpSession session = request.getSession();
            LoginUser loginUser1 = (LoginUser)session.getAttribute("loginUser");
            log.info("loginUser1:{}",loginUser1);
//            if (null==loginUser1 || StringUtils.isBlank(loginUser1.getUserId()) ||
//                    checkAuth(loginUser1.getUserId(), request.getRequestURI())){
//                // 无UserId 或者 权限校验不通过
//                setUnLoginJson(response);
//                return false;
//            }


            // 登录校验通过

            // RequestContextHolder: 请求上下文持有者
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                attributes.setAttribute(CURRENT_USER, loginUser, RequestAttributes.SCOPE_REQUEST);
            }

            return true;
        } catch (Exception e) {
            log.error("登录校验异常: {}", e.getMessage(), e);
        }
        setUnLoginJson(response);
        return false;
    }

    /**
     * 根据用户token获取用户登录信息
     *
     * @param token 用户token
     * @return 用户登录信息
     */
    private LoginUser getLoginUserByToken(String token){
        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        // 模拟获取用户信息过程
        String nowTimeString =
                DateUtil.format(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");
        loginUser.setUserId(nowTimeString);
        loginUser.setPhoneNumber("18888888888");

        if (StringUtils.isBlank(loginUser.getUserId())){
            return null;
        }

        return loginUser;
    }

    /**
     * 校验用户访问权限
     *
     * @param userId 用户ID
     * @param requestURI request请求地址中的URI
     * @return true:校验通过，false:校验不通过
     */
    public boolean checkAuth(String userId, String requestURI){
        return true;
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
