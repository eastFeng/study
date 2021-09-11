package com.dongfeng.study.basicstudy.effectivejava.chapter1;

import java.io.Serializable;

/**
 * @author zhang-dongfeng
 * @create 2020-03-23 11:28
 */
public class Person implements Serializable {

    /**
     * 姓名
     */
    private String name;
    private int age;
    private String nationality;
    private String ethnic;
    private IdCardType idCardType;
    private String idNumber;
    private String address;
    private String phoneNumber;

    private Person(){}

    private Person(Builder builder){
        this.name        = builder.name;
        this.age         = builder.age;
        this.nationality = builder.nationality;
        this.ethnic      = builder.ethnic;
        this.idCardType  = builder.idCardType;
        this.idNumber    = builder.idNumber;
        this.address     = builder.address;
        this.phoneNumber = builder.phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return nationality;
    }

    public String getEthnic() {
        return ethnic;
    }

    public IdCardType getIdCardType() {
        return idCardType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                ", ethnic='" + ethnic + '\'' +
                ", idCardType=" + idCardType +
                ", idNumber='" + idNumber + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int age;
        private String nationality;
        private String ethnic;
        private IdCardType idCardType;
        private String idNumber;
        private String address;
        private String phoneNumber;

        private Builder(){
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setAge(int age){
            this.age = age;
            return this;
        }

        public Builder setNationality(String nationality){
            this.nationality = nationality;
            return this;
        }

        public Builder setEthnic(String ethnic){
            this.ethnic = ethnic;
            return this;
        }

        public Builder setIdCardType(IdCardType idCardType){
            this.idCardType = idCardType;
            return this;
        }

        public Builder setIdNumber(String idNumber){
            this.idNumber = idNumber;
            return this;
        }

        public Builder setAddress(String address){
            this.address = address;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Person build(){
            return new Person(this);
        }
    }
}
