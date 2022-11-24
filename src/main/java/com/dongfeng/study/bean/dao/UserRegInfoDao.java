package com.dongfeng.study.bean.dao;

import com.dongfeng.study.bean.entity.UserRegInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author eastFeng
 * @date 2022-04-19 22:27
 */
@Repository
public class UserRegInfoDao {

//    @Resource
//    private MongoTemplate mongoTemplate;
//
//    /**
//     * 新增一条登记信息
//     */
//    public void add(UserRegInfo userRegInfo) {
//        mongoTemplate.insert(userRegInfo);
//    }
//
//    /**
//     * 根据主键ID查询登记信息
//     * @param id 主键
//     * @return UserRegInfo
//     */
//    public UserRegInfo findById(String id){
//        return mongoTemplate.findById(id, UserRegInfo.class);
//    }
//
//    /**
//     * 按userId查询登记信息
//     * @param userId userId
//     * @return 登记信息
//     */
//    public List<UserRegInfo> listByUserId(String userId){
//        // 查询对象
//        Query query = new Query();
//        // 条件
//        Criteria criteria = Criteria.where("userId").is(userId).and("valid").is(true);
//        // 查询对象添加查询条件
//        query.addCriteria(criteria);
//
//        return mongoTemplate.find(query, UserRegInfo.class);
//    }
//
//    /**
//     * 将登记信息设为有效或者无效
//     * @param id 主键ID
//     * @param valid true：有效，false：无效
//     */
//    public void updateValidByID(String id, boolean valid){
//        // 更新对象
//        Update update = new Update();
//        update.set("valid", false);
//        update.set("updateTime", new Date());
//
//        // 查詢
//        Query query = Query.query(Criteria.where("_id").is(id));
//
//        // 更新
//        mongoTemplate.updateFirst(query, update, UserRegInfo.class);
//    }



}
