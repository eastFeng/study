package com.dongfeng.study.dao.mapper;

import com.dongfeng.study.bean.entity.UserInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p> sql语句直接写在class中（全注解方式）,不写XML文件。
 * <p> @Select、@Insert、@Update以及@Delete四个注解分别对应XML中的select、insert、update以及delete标签
 * <p> @Results注解类似于XML中的ResultMap映射文件
 *
 * <p> UserInfoMapper2创建好之后，还要配置mapper扫描，有两种方式:
 * <lo>
 * <li> 一种是直接在UserMapper2上面添加@Mapper注解，这种方式有一个弊端就是所有的Mapper都要手动添加，要是落下一个就会报错，
 * <li> 还有一个一劳永逸的办法就是直接在启动类上添加Mapper扫描注解: @MapperScan(basePackages = "com.dongfeng.study.bean.mapper")
 * </lo>
 * @author eastFeng
 * @date 2021-09-19 11:33
 */
public interface UserInfoMapper2 {

    // 获取所有用户信息
    @Select("SELECT * FROM user_info")
    List<UserInfo> getAllUsers();

    // 根据主键获取用户信息
    @Select("SELECT * FROM user_info WHERE id = #{id}")
    UserInfo getUserById(int id);

    // 根据姓名模糊查询用户信息
    @Select("SELECT * FROM user_info WHERE name LIKE concat('%',#{name},'%')")
    List<UserInfo> geUserByName();

    // 根据主键更新用户信息
    @Update("UPDATE user_info SET phone_number=#{phoneNumber} WHERE id=#{id}")
    int updateById(UserInfo userInfo);

    // 根据主键删除用户信息
    @Delete("DELETE FROM user_info WHERE id=#{id}")
    int deleteById(int id);
}
