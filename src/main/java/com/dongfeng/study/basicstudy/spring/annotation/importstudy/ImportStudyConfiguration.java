package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Import;

/**
 * @author eastFeng
 * @date 2022-12-02 17:47
 */


@EnableImportTest_2(beanClass = {Person_06.class},
              beanClassName = {"com.dongfeng.study.basicstudy.spring.annotation.importstudy.Person_07"}
             )
@EnableImportTest_1
@Import(value = {
        Person_01.class,
        ImportSelector_Test_1.class,
        MyRegistrar.class,
        BeanConfiguration.class})
//@Configuration
public class ImportStudyConfiguration {
}
