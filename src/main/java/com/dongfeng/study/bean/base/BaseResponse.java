package com.dongfeng.study.bean.base;

import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 方法返回基类
 * @author eastFeng
 * @date 2020/8/15 - 12:49
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1075989307245937233L;

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
    public BaseResponse(){
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
     * 设置并返回成功的{@link BaseResponse}
     *
     * @param data {@link #data}
     * @param <T> 类型变量（参数类型）
     */
    public static <T> BaseResponse<T> successInstance(T data){
        BaseResponse<T> tBaseResponse = new BaseResponse<>();
        tBaseResponse.setData(data);
        return tBaseResponse;
    }

    /**
     * 设置并返回失败的{@link BaseResponse}
     *
     * @param codeEnum {@link ResponseCodeEnum}
     * @param <T> 类型变量（参数类型）
     * @return {@link BaseResponse}
     */
    public static <T> BaseResponse<T> errorInstance(ResponseCodeEnum codeEnum){
        BaseResponse<T> tBaseResponse = new BaseResponse<>();
        tBaseResponse.setCode(codeEnum.getCode());
        tBaseResponse.setMsg(codeEnum.getMsg());
        return tBaseResponse;
    }

    /**
     * 设置并返回失败的{@link BaseResponse}
     *
     * @param code 响应码{@link #code}
     * @param msg 响应信息{@link #msg}
     * @param <T> 类型变量（参数类型）
     * @return {@link BaseResponse}
     */
    public static <T> BaseResponse<T> errorInstance(int code, String msg){
        BaseResponse<T> tBaseResponse = new BaseResponse<>();
        tBaseResponse.setCode(code);
        tBaseResponse.setMsg(msg);
        return tBaseResponse;
    }

    public static <S, T> BaseResponse<T> errorInstance(BaseResponse<S> source){
        BaseResponse<T> tBaseResponse = new BaseResponse<>();
        tBaseResponse.setCode(source.getCode());
        tBaseResponse.setMsg(source.getMsg());
        return tBaseResponse;
    }

    /**
     * 对 {@link BaseResponse} 设置code和msg并返回
     *
     * @param baseResponse {@link BaseResponse}
     * @param codeEnum {@link ResponseCodeEnum}
     * @param <T> 类型变量（参数类型）
     * @return {@link BaseResponse}
     */
    public static <T> BaseResponse<T> setError(BaseResponse<T> baseResponse, ResponseCodeEnum codeEnum){
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setMsg(codeEnum.getMsg());
        return baseResponse;
    }

    /**
     * 对 {@link BaseResponse} 设置code和msg并返回
     *
     * @param baseResponse {@link BaseResponse}
     * @param code 响应码
     * @param msg 响应信息
     * @param <T> 类型变量（参数类型）
     * @return {@link BaseResponse}
     */
    public static <T> BaseResponse<T> setError(BaseResponse<T> baseResponse, int code, String msg){
        baseResponse.setCode(code);
        baseResponse.setMsg(msg);
        return baseResponse;
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
    public static <S, T> BaseResponse<T> copyError(BaseResponse<S> source, BaseResponse<T> target){
        target.setCode(source.getCode());
        target.setMsg(source.getMsg());
        return target;
    }
}
