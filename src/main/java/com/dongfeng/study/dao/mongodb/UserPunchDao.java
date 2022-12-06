package com.dongfeng.study.dao.mongodb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.dongfeng.study.bean.entity.UserPunch;
import com.dongfeng.study.bean.enums.PunchTypeEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户扫码记录
 *
 * @author eastFeng
 * @date 2022-04-19 22:27
 */
@Slf4j
@Repository
public class UserPunchDao {

//    @Resource
//    private MongoTemplate mongoTemplate;
//
//    public void add(UserPunch hymUserPunch) {
//        mongoTemplate.insert(hymUserPunch);
//    }
//
//    /**
//     * 时间倒序查询5条游客扫码记录
//     *
//     * @param userId 用戶ID
//     * @return 掃碼記錄
//     */
//    public List<UserPunch> selectByFormId(String userId) {
//        return mongoTemplate.find(Query.query(Criteria.where("userId").is(userId))
//                // 根据createTime字段倒序查詢
//                .with(Sort.by(Sort.Direction.DESC, "createTime")).
//                // 最多查询5条记录
//                limit(5),
//                UserPunch.class);
//    }
//
//    public List<UserPunch> pageByFormId(String formId, String lastId) {
//        lastId = StringUtils.isBlank(lastId) ? "000000000000000000000000" : lastId;
//        return mongoTemplate.find(Query.query(Criteria.where("formId").is(formId).and("_id").gt(new ObjectId(lastId)))
//                .with(Sort.by(Sort.Direction.ASC, "_id")).limit(100), UserPunch.class);
//    }
//
//
//    /**
//     *
//     * 打卡时多个游玩人  虽然在库中生成多条预约记录， 但是打卡时间（createTime）一样的
//     */
//    public List<UserPunch> findByCreateTime(String userId, String formId, Date createTime){
//        Criteria criteria = Criteria.where("userId").is(userId)
//                .and("formId").is(formId)
//                .and("createTime").is(createTime);
//        return mongoTemplate.find(Query.query(criteria), UserPunch.class);
//    }
//
//    /**
//     * 打卡时多个游玩人  虽然在库中生成多条预约记录， 但是打卡时间（createTime）一样的
//     */
//    public List<UserPunch> findByCreateTime(String userId, Date createTime){
//        Criteria criteria = Criteria.where("userId").is(userId)
//                .and("createTime").is(createTime);
//        return mongoTemplate.find(Query.query(criteria), UserPunch.class);
//    }
//
//
//    public long countByUserId(String userId) {
//        return mongoTemplate.count(Query.query(Criteria.where("userId").is(userId)), UserPunch.class);
//    }
//
//    public long count4detail(String formId) {
//        return mongoTemplate.count(Query.query(Criteria.where("formId").is(formId)), UserPunch.class);
//    }
//
//    /**
//     * 查询用户的扫码总数
//     *
//     * @param userId 用户ID
//     * @return 扫码总数
//     */
//    public int countGroupByUserId(String userId){
//        // 条件
//        Criteria criteria = Criteria.where("userId").is(userId);
//        MatchOperation match = Aggregation.match(criteria);
//
//        // 根据createTime分组
//        GroupOperation group = Aggregation.group("createTime");
//
//        Aggregation aggregation = Aggregation.newAggregation(match, group);
//        // 每个createTime都会放入一个map
//        List<Map> mapList =
//                mongoTemplate.aggregate(aggregation, "hymUserPunch", Map.class).getMappedResults();
//        // list的个数就是分组后createTime（不同creatTime）的总数
//        return mapList.size();
//    }
//
//    /**
//     * 分组分页查询用户的扫码记录,,,,根据createTime分组
//     * <p> 根据createTime判定一次打卡选择了多少游玩人
//     *
//     * @param pageNum 页码
//     * @param pageSize 每页数量
//     * @param userId 用户ID
//     * @return
//     */
//    public List<Map> groupPage(Integer pageNum, Integer pageSize, String userId){
//        // 1. 条件
//        Criteria criteria = Criteria.where("userId").is(userId);
//        MatchOperation match = Aggregation.match(criteria);
//
//        // 2. 根据createTime分组
//        GroupOperation group = Aggregation.group("createTime");
//
//        // createTime倒序排序: 根据createTime分组之后，createTime就赋值给了_id字段
//        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "_id"));
//
//        // 3. 分页
//        SkipOperation skip = Aggregation.skip((long) (pageNum - 1) * pageSize);
//        LimitOperation limit = Aggregation.limit(pageSize);
//
//        Aggregation aggregation = Aggregation.newAggregation(match, group, sort, skip, limit);
//
//        // map中，key是_id,对应的value就是createTime
//        return mongoTemplate.aggregate(aggregation, "hymUserPunch", Map.class).getMappedResults();
//    }
//
//    /**
//     * 时间倒序查询1条游客扫码记录
//     */
//    public UserPunch findByIdAndUserId(String punchId, String userId) {
//        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(punchId).and("userId").is(userId)), UserPunch.class);
//    }
//
//    public UserPunch findById(String id) {
//        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), UserPunch.class);
//    }
//
//    /**
//     * 查询时间范围内的数据
//     */
//    public List<UserPunch> queryByTimeRange(String userRegInfoId, String formId, Date start, Date end){
//        // 条件
//        Criteria criteria = Criteria.where("userRegInfoId").is(userRegInfoId)
//                .and("formId").is("formId")
//                .andOperator(Criteria.where("createTime").gte(start), Criteria.where("createTime").lt(end));
//
//        Query query = Query.query(criteria);
//        // 根据创建时间倒序排序，并且限制最多查询1000_000条记录
//        query.with(Sort.by(Sort.Direction.DESC, "createTime")).limit(1000_000);
//
//        return mongoTemplate.find(query, UserPunch.class);
//    }
//
//
//    /**
//     * 此方法是查看用户预约记录是否已打卡用的
//     * <p>根据用户登记信息表主键ID和场所ID查询打卡记录
//     *
//     * @param userRegInfoId 登记信息表主键ID
//     * @param formId 场所表ID
//     * @param start 开始时间
//     * @param end 结束时间
//     * @return 打卡记录
//     */
//    public UserPunch findLastOne(String userRegInfoId, String formId, Date start, Date end){
//        DateTime beginOfDay = cn.hutool.core.date.DateUtil.beginOfDay(start);
//        DateTime endOfDay = cn.hutool.core.date.DateUtil.endOfDay(end);
//
//        Criteria criteria = Criteria.where("userRegInfoId").is(userRegInfoId).and("formId").is(formId)
//                .andOperator(Criteria.where("createTime").gte(beginOfDay), Criteria.where("createTime").lte(endOfDay));
//        List<UserPunch> punches =
//                mongoTemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "createTime")).limit(1)
//                        , UserPunch.class);
//        if (CollectionUtil.isEmpty(punches)){
//            return null;
//        }else {
//            return punches.get(0);
//        }
//    }
//
//    /**
//     * 根据bookId查找打卡类型查找打卡记录
//     *
//     * @param bookId 预约ID（HymUserBook主键ID）
//     * @return 打卡记录
//     */
//    public UserPunch findByBookIdAndPunchType(String bookId, PunchTypeEnum punchTypeEnum){
//        Criteria criteria = Criteria.where("bookId").is(bookId).and("punchType").is(punchTypeEnum.name());
//        return mongoTemplate.findOne(Query.query(criteria), UserPunch.class);
//    }
//
//    public List<UserPunch> queryByBookIds(Collection<String> bookIds){
//        if (CollectionUtils.isEmpty(bookIds)) {
//            return Lists.newArrayList();
//        }
//        return mongoTemplate.find(
//                Query.query(Criteria.where("bookId").in(bookIds))
//                , UserPunch.class);
//    }
}
