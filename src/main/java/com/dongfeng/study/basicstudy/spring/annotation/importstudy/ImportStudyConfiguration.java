package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Import;

/**
 * @author eastFeng
 * @date 2022-12-02 17:47
 */
@Import(value = {
        Person_01.class,
        MyImportSelector.class,
        MyRegistrar.class,
        BeanConfiguration.class})
//@Configuration
public class ImportStudyConfiguration {
}
