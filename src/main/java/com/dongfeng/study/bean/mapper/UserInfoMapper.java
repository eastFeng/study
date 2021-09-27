package com.dongfeng.study.bean.mapper;

import com.dongfeng.study.bean.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> sql语句写在xml中，在resource文件夹下新建一个mapper文件夹编写对应的xml（sql映射文件）
 *
 * @author eastFeng
 * @date 2021-09-19 11:24
 */
public interface UserInfoMapper {

    // 获取所有用户信息
    List<UserInfo> getAllUsers();

    // 根据主键获取用户信息
    UserInfo getUserById(Integer id);

    // 根据姓名模糊查询用户信息
    List<UserInfo> geUserByName(String name);

    // 根据主键删除用户信息
    int deleteById(int id);

    /*
     * 在Mybatis中，对于参数，系统提供了两套默认名字：
     * 第一套是: arg0, arg1...   第二套是: param1, param2...
     * 注意: 这两套的下标是不一样的。
     * 但是，默认的名字不好记，容易出错。如果想要使用自己写的变量的名字，
     * 可以通过给参数添加@Param注解来指定参数名（一般在有多个参数的时候需要添加）。
     */
    // 根据会员等级获取用户信息
    List<UserInfo> getUserByMemberLevel(@Param("memberLevel") Integer memberLevel, @Param("isDelete") Integer isDelete);

    // 新增用户信息
    Integer addUserInfo(@Param("userInfo") UserInfo userInfo);


    // 更具某一列进行排序
    List<UserInfo> orderBy(@Param("columnName") String columnName);

    List<UserInfo> getByIds(@Param("ids") List<Integer> ids);
}
