package com.dongfeng.study.bean.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户登录信息
 *
 * @author eastFeng
 * @date 2022-11-23 20:54
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = -8676723297426369203L;

    // 用户ID
    private String userId;

    // 用户token
    private String token;

    // 用户手机号
    private String phoneNumber;

    public LoginUser(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(userId, loginUser.userId) && Objects.equals(token, loginUser.token) && Objects.equals(phoneNumber, loginUser.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, token, phoneNumber);
    }
}
