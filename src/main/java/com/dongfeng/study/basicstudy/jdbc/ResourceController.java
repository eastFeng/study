package com.dongfeng.study.basicstudy.jdbc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eastFeng
 * @date 2020/4/20 - 11:45
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @RequestMapping("/test")
    public void test(){
        ResourceUtil.getValue("url");
    }
}
