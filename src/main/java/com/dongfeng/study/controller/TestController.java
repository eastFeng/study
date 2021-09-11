package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.bean.base.Response;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.bean.vo.TestVo;
import com.dongfeng.study.allconf.config.ConfigurationPropertiesDemo;
import com.dongfeng.study.service.RequestContextHolderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author eastFeng
 * @date 2020-10-12 22:20
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private RequestContextHolderService requestContextHolderService;

    @Autowired
    private ConfigurationPropertiesDemo propertiesDemo;

    @Autowired
    private TestVo testVo;

    @GetMapping("/hello")
    public String hello(){
        log.info("hello测试开始======");
        return "HELLO";
    }


    @GetMapping("/cpdTest")
    public Response<String> cpdTest(HttpServletRequest request, @RequestParam String userKey){
        Response<String> response = new Response<>();
        String token = request.getHeader(Constants.TOKEN);
        log.info("【token:{}, userKey:{}】", token, userKey);

        if (StringUtils.isBlank(userKey)){
            return Response.setError(response, ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        String s = propertiesDemo.toString();
        response.setData(s);
        return response;
    }


    @GetMapping("/demoConfigTest")
    public Response<String> demoConfigTest(){
        Response<String> response = new Response<>();

        String s = testVo.toString();
        response.setData(s);
        return response;
    }

    @GetMapping("/test111")
    public Response<String> test111(){
        return requestContextHolderService.test111();
    }

    @GetMapping("/testNull")
    public Response<String> testNull(){
        log.info("log test!!!");
        return new Response<>();
    }
}
