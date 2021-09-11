package com.dongfeng.study.basicstudy.jdbc;

import java.util.ResourceBundle;

/**
 * @author eastFeng
 * @date 2020/4/20 - 11:42
 */
public class ResourceUtil {
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        RESOURCE_BUNDLE = ResourceBundle.getBundle("jdbc.properties");
    }

    public static String getValue(String key){
        return RESOURCE_BUNDLE.getString(key);
    }
}
