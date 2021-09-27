package com.dongfeng.study;

import com.dongfeng.study.bean.entity.UserInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author eastFeng
 * @date 2021-09-19 13:49
 */
public class UserInfoTest {

    @Test
    public void testGetAll(){
        SqlSession sqlSession = null;
        try {
            // 1. 读取Mybatis主配置文件
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            // 2. 根据配置文件创建SqlSessionFactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            // 3. 用SqlSessionFactory工厂去创建SqlSession : SqlSession就等同于jdbc中的Connection数据库连接
            sqlSession = sqlSessionFactory.openSession();
            // 4. 通过连接向数据库发送sql语句
            // UserInfoMapper.xml映射文件的命名空间 + 方法id
            List<UserInfo> list = sqlSession.selectList("com.dongfeng.study.bean.mapper.UserInfoMapper.getAllUsers");

            // 5. 遍历结果
            list.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
