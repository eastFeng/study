package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.BaseResponse;
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
    public BaseResponse<List<UserInfo>> getAllUsers(){
        LOGGER.info("getAllUsers start--------------");
        return userInfoService.getAllUsers();
    }

    @GetMapping("/getAllUsers2")
    public BaseResponse<List<UserInfo>> getAllUsers2(){
        return userInfoService.getAllUsers2();
    }

    @GetMapping("/getUserById")
    public BaseResponse<UserInfo> getUserById(Integer id){
        return userInfoService.getUserById(id);
    }

    @GetMapping("/getUserByMemberLevel")
    public BaseResponse<List<UserInfo>> getUserByMemberLevel(Integer memberLevel, Integer isDelete){
        return userInfoService.getUserByMemberLevel(memberLevel, isDelete);
    }

    /**
     * {@link ResponseBody} : Java对象序列化为Json
     * <p> {@link RequestBody} : Json反序列化为Java对象
     * @param userInfo 要添加的用户信息
     */
    @PostMapping("/addUserInfo")
    public BaseResponse<Integer> addUserInfo(@RequestBody UserInfo userInfo){
        return userInfoService.addUserInfo(userInfo);
    }

    @PostMapping("/getByIds")
    public BaseResponse<List<UserInfo>> getByIds(@RequestBody List<Integer> ids){
        return userInfoService.getByIds(ids);
    }
}
