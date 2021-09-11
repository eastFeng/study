package com.dongfeng.study.basicstudy.effectivejava.chapter1;

/**
 * @author zhang-dongfeng
 * @create 2020-03-23 12:27
 */
public enum IdCardType {
    China_Resident_Identity_Card(1,"中国居民身份证"),
    Foreign_Permanent_Resident_ID_Card(2,"外国人永久居留身份证"),
    HongKong_Macao_Taiwan_Residents(3,"港澳台居民居住证")
    ;

    private final int type;
    private final String name;

    IdCardType(int type, String name){
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public IdCardType getIdCardType(int type){
        IdCardType[] idCardTypes = values();
        for (IdCardType idCardType : idCardTypes){
            if (type==idCardType.type){
                return idCardType;
            }
        }
        return null;
    }
}
