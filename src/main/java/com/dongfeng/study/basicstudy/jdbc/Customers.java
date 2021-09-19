package com.dongfeng.study.basicstudy.jdbc;

import java.io.Serializable;
import java.util.Date;

/**
 * @author eastFeng
 * @date 2020/4/21 - 15:56
 */
public class Customers implements Serializable {

    private static final long serialVersionUID = -7192119881041839799L;

    private Integer id;

    private String name;

    private String email;

    private Date birth;

    public Customers(){}

    public Customers(Integer id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
