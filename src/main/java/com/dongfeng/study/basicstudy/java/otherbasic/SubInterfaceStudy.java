package com.dongfeng.study.basicstudy.java.otherbasic;

import com.dongfeng.study.basicstudy.java.otherbasic.InterfaceStudy;

/**
 * @author eastFeng
 * @date 2021-04-24 16:02
 */
public interface SubInterfaceStudy extends InterfaceStudy {

    @Override
    default void des(){
        System.out.println();
    };

    @Override
    String getUrl();


}
