package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.vo.Good;
import com.dongfeng.study.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <b> 常见注解学习 </b>
 *
 * @author eastFeng
 * @date 2020-12-30 17:31
 */
@Slf4j
@RequestMapping("/common/annotation")
@Controller
public class CommonAnnotationController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * {@link ResponseBody} : Java对象序列化为Json
     * <p> {@link RequestBody} : Json反序列化为Java对象
     */
    @GetMapping("/noRsTest")
    @ResponseBody
    public BaseResponse<String> noRsTest(@RequestBody Good good){
        log.info("------noRsTest start------");
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setData("没有ResponseBody注解测试");
        return baseResponse;
    }
}
