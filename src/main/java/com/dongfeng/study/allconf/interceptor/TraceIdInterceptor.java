package com.dongfeng.study.allconf.interceptor;

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
        try {
            String traceId = UUID.randomUUID().toString().replace("-", "");
            log.info("日志traceId:{}", traceId);
            MDC.put(Constants.TRACE_ID, traceId);

            String remoteAddr = request.getRemoteAddr();
            log.info("userIp:{}", remoteAddr);
        } catch (Exception e) {
            log.error("设置日志traceId异常:{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * <b> 该方法在整个请求结束之后执行，当然前提是{@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}方法的返回值为true才行。
     * <p> 该方法一般用于资源清理工作。
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
        MDC.remove(Constants.TRACE_ID);
    }

}
