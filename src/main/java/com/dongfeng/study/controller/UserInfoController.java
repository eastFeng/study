package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.Response;
import com.dongfeng.study.bean.entity.UserInfo;
import com.dongfeng.study.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author eastFeng
 * @date 2021-09-19 12:20
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    // 不在类上面用@Slf4j注解也可以日志
    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/getAllUsers")
    public Response<List<UserInfo>> getAllUsers(){
        LOGGER.info("getAllUsers start--------------");
        return userInfoService.getAllUsers();
    }

    @GetMapping("/getAllUsers2")
    public Response<List<UserInfo>> getAllUsers2(){
        return userInfoService.getAllUsers2();
    }

    @GetMapping("/getUserById")
    public Response<UserInfo> getUserById(Integer id){
        return userInfoService.getUserById(id);
    }

    @GetMapping("/getUserByMemberLevel")
    public Response<List<UserInfo>> getUserByMemberLevel(Integer memberLevel, Integer isDelete){
        return userInfoService.getUserByMemberLevel(memberLevel, isDelete);
    }

    @PostMapping("/addUserInfo")
    public Response<Integer> addUserInfo(@RequestBody UserInfo userInfo){
        return userInfoService.addUserInfo(userInfo);
    }

    @PostMapping("/getByIds")
    public Response<List<UserInfo>> getByIds(@RequestBody List<Integer> ids){
        return userInfoService.getByIds(ids);
    }
}
