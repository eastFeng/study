package com.dongfeng.study.bean.enums;

import com.dongfeng.study.bean.base.BaseResponse;

import java.util.Arrays;

/**
 * <b> 响应枚举 </b>
 *
 * @see BaseResponse
 * @see com.dongfeng.study.bean.base.BizException
 * @author eastFeng
 * @date 2020-10-24 14:08
 */
public enum ResponseCodeEnum {
    SUCCESS(200, "成功"),
    UNKNOWN(500, "未知错误"),
    SYSTEM_BUSY(300, "系统繁忙，请稍后再试"),

    /**
     * 参数错误: 1001-1999
     */
    PARAM_IS_EMPTY(1001, "参数为空"),
    PARAM_IS_INVALID(1002, "参数无效"),
    PARAM_IS_ILLEGAL(1003, "参数异常"),
    PARAM_TYPE_ERROR(1004, "参数类型错误"),
    PARAM_NOT_COMPLETE(1005, "参数不完整"),
    FILE_IS_EMPTY(1006, "文件为空, 请重新上传"),
    FILE_NAME_IS_EMPTY(1006, "文件名称为空, 请重新上传"),


    /**
     * 用户账号错误: 2001-2999
     */
    USER_NOT_LOGGED_IN(2001, "未登录，请重新登录"),
    ACCOUNT_OR_PASSWORD_ERROR(2002, "账号或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),

    /**
     * 数据库错误: 3001-3999
     */
    DATABASE_INSERT_ERROR(3001, "数据库插入失败"),
    DATABASE_DELETE_ERROR(3002, "数据库删除失败"),
    DATABASE_UPDATE_ERROR(3003, "数据库更新失败"),
    DATABASE_SELECT_ERROR(3004, "数据库查询失败"),
    DATABASE_BATCH_INSERT_ERROR(3005, "数据库批量插入失败"),


    // 其他：4001-4999
    FILE_UPLOAD_FIELD(4001, "文件上传失败"),
    FILE_NAME_IS_TOO_LONG(4002, "文件名太长"),
    FILE_IS_TOO_LARGE(4003, "文件太大"),
    DIRECTORY_LEVEL_JUMP_UP(4004, "禁止目录上跳级别"),

    ;

    private final int code;
    private final String msg;

    ResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据响应码找到ResponseCodeEnum枚举
     * @param code 响应码
     * @return ResponseCodeEnum
     */
    public static ResponseCodeEnum getEnumByCode(int code){
        return Arrays.asList(ResponseCodeEnum.values()).stream().filter(e -> e.code==code).findFirst().orElse(null);
    }

    /**
     * 根据响应码找到对应的msg
     * @param code 响应码
     * @return msg
     */
    public static String getMsgByCode(int code){
        for (ResponseCodeEnum codeEnum : ResponseCodeEnum.values()) {
            if (codeEnum.code == code){
                return codeEnum.msg;
            }
        }
        return "";
    }
}
