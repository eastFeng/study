package com.dongfeng.study.config.interceptor;

import com.dongfeng.study.bean.base.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <b> 用户一次请求，打印的日志traceId（单次请求有唯一标示）相同，即跟踪id，方便定位问题。
 *
 * <p> MDC定义 Mapped Diagnostic Context，即：映射诊断环境。
 * <p> MDC是 log4j 和 logback 提供的一种方便在多线程条件下记录日志的功能。
 * <p> MDC 可以看成是一个与当前线程绑定的哈希表，可以往其中添加键值对。
 * <p> MDC的使用方法
 * <lo>
 * <li> 向MDC设置值: MDC.put(key, value);
 * <li> 从MDC中取值: MDC.get(key);
 * <li> 将MDC中的内容打印到日志中: %X{key};
 * </lo>
 *
 * @author eastFeng
 * @date 2020-11-18 20:53
 */
@Slf4j
@Component
public class TraceIdInterceptor extends HandlerInterceptorAdapter {

    /**
     * <b> 对客户端发过来的请求进行前置处理
     * <p> 如果方法返回 true,继续执行后续操作，如果返回 false，执行中断请求处理，请求不会发送到Controller。
     * @param request 当前Http请求
     * @param response 当前Http响应
     * @param handler 为类型和/或实例计算选择要执行的处理程序
     * @return 无论设置日志traceId是否成功都返回true
     * @throws Exception 如果发生错误
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 初始化TraceId并向MDC设置值
         * 方法执行前设置MDC，方法执行后擦除MDC。
         * 具体实现方式有很多，如过滤器、拦截器、AOP等等。个人比较推荐Filter实现，因为Filter是请求最先碰到的，也是响应给前端前最后一个碰到的。
         */
        try {
            // 从请求头中获取token
            String token = request.getHeader(Constants.TOKEN);

            String traceId = UUID.randomUUID().toString().replace("-", "");
            log.info("token:{},日志traceId:{}", token, traceId);
            MDC.put(Constants.TRACE_ID, traceId);

            String ipAddress = request.getRemoteAddr();
            log.info("userIp:{}", ipAddress);
        } catch (Exception e) {
            log.error("设置日志traceId异常:{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * <b> 该方法在整个请求结束之后执行，当然前提是{@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}方法的返回值为true才行。
     * <p> 该方法一般用于资源清理工作。
     * @param request 当前Http请求
     * @param response 当前Http响应
     * @param handler 为类型和/或实例计算选择要执行的处理程序
     * @throws Exception  如果发生错误
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
        // 方法执行后擦除MDC
        MDC.remove(Constants.TRACE_ID);
    }

}
