package com.dongfeng.study.service;

import cn.hutool.core.collection.CollectionUtil;
import com.dongfeng.study.bean.base.Response;
import com.dongfeng.study.bean.entity.UserInfo;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.bean.mapper.UserInfoMapper;
import com.dongfeng.study.bean.mapper.UserInfoMapper2;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author eastFeng
 * @date 2021-09-19 12:22
 */
@Slf4j
@Service
public class UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserInfoMapper2 userInfoMapper2;


    public Response<List<UserInfo>> getAllUsers(){
        List<UserInfo> allUsers = userInfoMapper.getAllUsers();
        return Response.successInstance(allUsers);
    }

    public Response<List<UserInfo>> getAllUsers2(){
        List<UserInfo> allUsers = userInfoMapper2.getAllUsers();
        return Response.successInstance(allUsers);
    }

    public Response<UserInfo> getUserById(Integer id){
        log.info("getUserById id:{}", id);

        if (id == null){
            return Response.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        UserInfo userInfo = userInfoMapper.getUserById(id);
        return Response.successInstance(userInfo);
    }

    public Response<List<UserInfo>> getUserByMemberLevel(Integer memberLevel, Integer isDelete){
        log.info("getUserByMemberLevel memberLevel:{},isDelete:{}", memberLevel, isDelete);

        if (memberLevel == null){
            return Response.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        List<UserInfo> userInfos = userInfoMapper.getUserByMemberLevel(memberLevel, isDelete);
        return Response.successInstance(userInfos);
    }

    public Response<Integer> addUserInfo(UserInfo userInfo){
        log.info("addUserInfo userInfo:{}", userInfo);
        if (userInfo==null){
            return Response.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }
        Date now = new Date();
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);
        Integer integer = userInfoMapper.addUserInfo(userInfo);
        return Response.successInstance(integer);
    }

    public Response<List<UserInfo>> getByIds(List<Integer> ids){
        log.info("getByIds ids:{}", ids);
        if (CollectionUtil.isEmpty(ids)){
            return Response.errorInstance(ResponseCodeEnum.PARAM_IS_EMPTY);
        }

        List<UserInfo> userInfos = userInfoMapper.getByIds(ids);

        return Response.successInstance(userInfos);
    }

    /**
     * PageHelper : MyBatis分页插件。
     * <p> PageHelper的使用非常便利快捷，仅通过PageInfo + PageHelper两个类，就足以完成分页功能。
     * @param pageNumber 页码
     * @param pageSize 每页显示数量
     */
    public void testSelectByPage(Integer pageNumber, Integer pageSize){
        if (pageNumber == null){
            pageNumber = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        // 必须写在查询前
        PageHelper.startPage(pageNumber, pageSize);

        // 查询所有
        List<UserInfo> allUsers = userInfoMapper.getAllUsers();

        PageInfo<UserInfo> pageInfo = new PageInfo<>(allUsers);
        List<UserInfo> list = pageInfo.getList();
        log.info("分页查询结果: list:{}", list);
    }
}































