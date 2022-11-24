package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.base.LoginUser;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.bean.vo.TestVo;
import com.dongfeng.study.config.properties.ConfigurationPropertiesDemo;
import com.dongfeng.study.service.RequestContextHolderService;
import com.dongfeng.study.service.UserInfoService;
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

    /**
     * @Autowired : 属性注入（不推荐）
     */
    @Autowired
    private ConfigurationPropertiesDemo propertiesDemo;

    /**
     * @Autowired : 构造方法注入（推荐）
     */
    private UserInfoService userInfoService;
    @Autowired
    public TestController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;
    }

    @Autowired
    private TestVo testVo;

    @GetMapping("/hello")
    public String hello(){
        log.info("hello测试开始======");
        return "HELLO";
    }


    @GetMapping("/cpdTest")
    public BaseResponse<String> cpdTest(HttpServletRequest request, @RequestParam String userKey){
        BaseResponse<String> baseResponse = new BaseResponse<>();
        String token = request.getHeader(Constants.TOKEN);
        log.info("【token:{}, userKey:{}】", token, userKey);

        if (StringUtils.isBlank(userKey)){
            return BaseResponse.setError(baseResponse, ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        String s = propertiesDemo.toString();
        baseResponse.setData(s);
        return baseResponse;
    }


    @GetMapping("/demoConfigTest")
    public BaseResponse<String> demoConfigTest(){
        BaseResponse<String> baseResponse = new BaseResponse<>();

        String s = testVo.toString();
        baseResponse.setData(s);
        return baseResponse;
    }

    @GetMapping("/test111")
    public BaseResponse<LoginUser> test111(){
        return requestContextHolderService.test111();
    }

    @GetMapping("/testNull")
    public BaseResponse<String> testNull(){
        log.info("log test!!!");
        return new BaseResponse<>();
    }
}
