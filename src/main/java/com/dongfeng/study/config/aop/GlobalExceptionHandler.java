package com.dongfeng.study.config.aop;

import com.dongfeng.study.bean.base.BizException;
import com.dongfeng.study.bean.base.Response;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <b> 全局异常处理 </b>
 *
 * @author eastFeng
 * @date 2021-04-22 17:15
 */
@Slf4j
@Order(-1)
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
     * @ControllerAdvice，是Spring3.2提供的新注解,它是一个Controller增强器,
     * 可对controller中被 @RequestMapping注解的方法加一些逻辑处理。最常用的就是异常处理。
     *
     * ControllerAdvice拆分开来就是Controller Advice，关于Advice，前面我们讲解Spring Aop时讲到，
     * 其是用于封装一个切面所有属性的，包括切入点和需要织入的切面逻辑。
     * 这里ControllerAdvice也可以这么理解，其抽象级别应该是用于对Controller进行“切面”环绕的，而具体的业务织入方式则是通过结合其他的注解来实现的。
     * @ControllerAdvice是在类上声明的注解，其用法主要有三点：
     *
     * 1. 结合方法型注解@ExceptionHandler，用于捕获Controller中抛出的指定类型的异常，从而达到不同类型的异常区别处理的目的；
     * 2. 结合方法型注解@InitBinder，用于request中自定义参数解析方式进行注册，从而达到自定义指定格式参数的目的；
     * 3. 结合方法型注解@ModelAttribute，表示其标注的方法将会在目标Controller方法执行之前执行。
     *
     *
     *
     */

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public <T> Response<T> business(HttpServletRequest request, Exception e){
        String uri = request.getRequestURI();
        log.error(uri + "controller exception error:{}", e.getMessage(), e);

        if (e instanceof BizException){
            BizException biz = (BizException) e;
            return Response.errorInstance(biz.getCode(), biz.getMessage());
        }else {
            return Response.errorInstance(ResponseCodeEnum.UNKNOWN.getCode(), e.getMessage());
        }
    }
}
