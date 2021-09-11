package com.dongfeng.study.bean.base;

import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import me.zhyd.oauth.log.Log;
import org.slf4j.MDC;

/**
 * 方法返回基类
 * @author eastFeng
 * @date 2020/8/15 - 12:49
 */
public class Response<T> {
    /**
     * 响应码
     */
    private int code = ResponseCodeEnum.SUCCESS.getCode();
    /**
     * 响应信息
     */
    private String msg = ResponseCodeEnum.SUCCESS.getMsg();
    /**
     * 用户一次请求，打印的日志traceId相同，即跟踪id，方便定位问题。该值可有可无。
     */
    private String traceId;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 构造方法
     */
    public Response(){
        try {
            this.traceId = MDC.get(Constants.TRACE_ID);
        } catch (Exception ignored) {
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTraceId() {
        return traceId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 设置并返回成功的{@link Response}
     *
     * @param data {@link #data}
     * @param <T> 类型变量（参数类型）
     */
    public static <T> Response<T> successInstance(T data){
        Response<T> tResponse = new Response<>();
        tResponse.setData(data);
        return tResponse;
    }

    /**
     * 设置并返回失败的{@link Response}
     *
     * @param codeEnum {@link ResponseCodeEnum}
     * @param <T> 类型变量（参数类型）
     * @return {@link Response}
     */
    public static <T> Response<T> errorInstance(ResponseCodeEnum codeEnum){
        Response<T> tResponse = new Response<>();
        tResponse.setCode(codeEnum.getCode());
        tResponse.setMsg(codeEnum.getMsg());
        return tResponse;
    }

    /**
     * 设置并返回失败的{@link Response}
     *
     * @param code 响应码{@link #code}
     * @param msg 响应信息{@link #msg}
     * @param <T> 类型变量（参数类型）
     * @return {@link Response}
     */
    public static <T> Response<T> errorInstance(int code, String msg){
        Response<T> tResponse = new Response<>();
        tResponse.setCode(code);
        tResponse.setMsg(msg);
        return tResponse;
    }

    /**
     * 对 {@link Response} 设置code和msg并返回
     *
     * @param response {@link Response}
     * @param codeEnum {@link ResponseCodeEnum}
     * @param <T> 类型变量（参数类型）
     * @return {@link Response}
     */
    public static <T> Response<T> setError(Response<T> response, ResponseCodeEnum codeEnum){
        response.setCode(codeEnum.getCode());
        response.setMsg(codeEnum.getMsg());
        return response;
    }

    /**
     * 对 {@link Response} 设置code和msg并返回
     *
     * @param response {@link Response}
     * @param code 响应码
     * @param msg 响应信息
     * @param <T> 类型变量（参数类型）
     * @return {@link Response}
     */
    public static <T> Response<T> setError(Response<T> response, int code, String msg){
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    /**
     * 将source的code和msg字段赋值给target
     *
     * @param source 类型参数为T的Response
     * @param target 类型参数为R的Response
     * @param <T> source的类型
     * @param <R> target的类型
     * @return Response<T>
     */
    public static <T, R> Response<R> copyError(Response<T> source, Response<R> target){
        target.setCode(source.getCode());
        target.setMsg(source.getMsg());
        return target;
    }
}
