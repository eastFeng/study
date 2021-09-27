package com.dongfeng.study.bean.base;

import com.dongfeng.study.bean.enums.ResponseCodeEnum;

/**
 * <b> 业务异常 </b>
 *
 * <p> 除了Java API中定义的异常类，也可以自己定义异常类，一般是继承Exception或者它的某个子类。
 * <p> 如果父类是RuntimeException或它的某个子类，则自定义异常也是未受检异常；
 * <p> 如果是Exception或Exception的其他子类，则自定义异常是受检异常。
 *
 * @author eastFeng
 * @date 2021-04-22 16:45
 */
public class BizException extends Exception{
    private static final long serialVersionUID = -3535576095529746910L;
    private int code;

    public BizException(){}

    public BizException(int code, String message){
        super(message);
        this.code = code;
    }

    public BizException(int code, String message, Throwable cause){
        /*
         * Throwable类有两个主要参数：
         * 一个是message，表示异常消息；
         * 另一个是cause，表示触发该异常的其他异常。
         * 异常可以形成一个异常链，上层的异常由底层异常触发，cause表示底层异常。
         */
        super(message, cause);
        this.code = code;
    }

    public BizException(ResponseCodeEnum codeEnum){
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
    }

    public BizException(ResponseCodeEnum codeEnum, Throwable cause){
        super(codeEnum.getMsg(), cause);
        this.code = codeEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
