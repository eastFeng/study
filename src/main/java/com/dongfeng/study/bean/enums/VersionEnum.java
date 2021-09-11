package com.dongfeng.study.bean.enums;

/**
 * <b> 版本枚举 </b>
 * @author eastFeng
 * @date 2020/8/15 - 16:53
 */
public enum VersionEnum {

    /**
     * 新版本（大版本）要写在之前版本的后面，比较版本的时候是根据枚举的下标（序号）进行比较的
     */
    UNKNOWN("0"),
    V0921("0.9.21"), V093("0.9.3"), V094("0.9.4"), V095("0.9.5"), V096("0.9.6"),
    V097("0.9.7"), V098("0.9.8"), V099("0.9.9")
    ;

    private final String code;

    VersionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static VersionEnum getEnumByCode(String code){
        for (VersionEnum ve : VersionEnum.values()){
            if (code.equals(ve.getCode())){
                return ve;
            }
        }
        return UNKNOWN;
    }

    private static void test(){
        for (VersionEnum ve: VersionEnum.values()){
            System.out.println(ve.name());
            // 获取当前枚举的下标
            System.out.println(ve.ordinal());
        }
    }

    public static void main(String[] args) {
        test();
    }
}
