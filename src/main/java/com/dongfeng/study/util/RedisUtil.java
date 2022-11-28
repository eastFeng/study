package com.dongfeng.study.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <b> 操作redis工具类 </b>
 *
 * @author ZhangDongfeng
 * @date 2020/6/23 - 20:24
 */
@Slf4j
@Component
public class RedisUtil {
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Autowired  构造方法注入
     */
    @Autowired
    public RedisUtil(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取redis分布式锁
     *
     * @param key 键
     * @param value 值
     * @param lockTime 上锁时间（单位秒）
     * @param waitTime 最多等待时间（单位秒）
     * @return true: 获取分布式锁成功 ， false: 获取锁失败
     */
    public boolean getDistributedLock(String key, String value, long lockTime, long waitTime){
        if (StringUtils.isAnyBlank(key,value) || lockTime<=0 || waitTime<=0){
            return false;
        }

        // 获取redis分布式锁开始
        long start = System.currentTimeMillis();

        try {
            // setIfAbsent : 将key的值设为value，当且仅当key不存在。设置成功返回true，否则返回false。
            // 自旋的方式加锁
            while (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, lockTime, TimeUnit.SECONDS))) {
                // 进入while循环，说明setIfAbsent方法返回false，没有获取到锁

                // 睡眠2毫秒, 然后重新尝试获取锁
                Thread.sleep(2);
                long time = System.currentTimeMillis() - start;
                // 如果超过waitTime秒，都没有获取到key，则获取失败
                if (time > (waitTime*1000)) {
                    return false;
                }
            }

            // redis set成功（加锁成功）跳出循环
            return true;
        }catch (Exception e){
            log.error("getDistributedLock Error key:{}, value:{}, error:{}", key, value, e.getMessage(), e);
        }

        // 发生异常，获取失败
        return false;
    }

    /**
     * Redis：Key
     * <p> 判断key是否存在
     *
     * @param key 键
     * @return true：存在，false：不存在
     */
    public Boolean hasKey(String key){
        if (StringUtils.isBlank(key)){
            return false;
        }
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * Redis：Key
     * <p> 删除key
     * @param key 键
     */
    public Boolean deleteKey(String key){
        if (StringUtils.isBlank(key)){
            return false;
        }
        return stringRedisTemplate.delete(key);
    }

    /**
     * Redis：Key
     * <p> 根据键删除缓存
     *
     * @param keys 可以传一个key，或者多个key
     */
    public void del(String... keys){
        if (keys!=null && keys.length>0){
            if (keys.length==1){
                stringRedisTemplate.delete(keys[0]);
            }else {
                stringRedisTemplate.delete(Arrays.asList(keys));
            }
        }
    }

    /**
     * Redis：Key
     * <p> 指定缓存失效时间
     *
     * @param key 键
     * @param secondTime 失效时间，单位秒，secondTime要大于0
     * @return true：设置成功，false：设置失败
     */
    public Boolean expire(String key, long secondTime){
        if (StringUtils.isBlank(key) || secondTime<=0){
            return false;
        }
        return stringRedisTemplate.expire(key, secondTime, TimeUnit.SECONDS);
    }

    /**
     * Redis：Key
     * <p> 根据key获取过期时间
     *
     * @param key 键，不能为null
     * @return 时间（秒），返回0代表永久有效
     */
    public Long getExpire(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * Redis：Key
     * <p> 查找所有符合给定模式pattern的key。
     *
     * @param pattern 模式
     * @return key的集合
     */
    public Set<String> keys(String pattern){
        if (StringUtils.isBlank(pattern)){
            return Collections.<String>emptySet();
        }

        return stringRedisTemplate.keys(pattern);
    }

    /**
     * Redis：String
     * <p> 普通缓存放入
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value){
        if (StringUtils.isAnyBlank(key, value)){
            return;
        }
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * Redis：String
     * <p> 普通缓存放入并设置时间（单位：秒）
     *
     * @param key 键
     * @param value 值
     * @param timeout 失效时间，单位秒，timeout要大于0，否则设置为不限期
     */
    public void set(String key, String value, long timeout){
        if (StringUtils.isAnyBlank(key, value)){
            return;
        }

        if (timeout > 0){
            stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        }else {
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * Redis：String
     * <p> 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param timeout 缓存时间
     * @param timeUnit 缓存时间单位
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit){
        if (StringUtils.isAnyBlank(key, value) || timeUnit==null || timeout<=0){
            return;
        }

        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * Redis：String
     * <p> 普通缓存，将key的值设为value，当且仅当key不存在。
     *
     * @param key 键
     * @param value 值
     * @return true：设置成功 ， false：设置失败
     */
    public Boolean setIfAbsent(String key, String value){
        if (StringUtils.isAnyBlank(key, value)){
            return null;
        }
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * Redis：String
     * <p> 普通缓存，将key的值设为value，当且仅当key不存在。
     *
     * @param key 键
     * @param value 值
     * @param timeout 缓存时间
     * @param timeUnit 缓存时间单位
     * @return true：设置成功 ， false：设置失败
     */
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit){
        if (StringUtils.isAnyBlank(key, value) || timeUnit==null || timeout<=0){
            return null;
        }

        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }


    /**
     * Redis：String
     * <p> 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key){
        return key==null? null : stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * Redis：String
     * <p> 普通缓存 自增1
     *
     * @param key 键
     */
    public Long incr(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * Redis：String
     * <p> 普通缓存 自减1
     *
     * @param key 键
     */
    public Long decr(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return stringRedisTemplate.opsForValue().decrement(key);
    }


    /**
     * Redis：String
     * <p> 普通缓存的递增
     *
     * @param key 键
     * @param delta 要增加几（大于0）
     * @return 增加之后的数值
     */
    public Long incr(String key, long delta){
        if (StringUtils.isBlank(key) || delta<0){
            return null;
        }

        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Redis：String
     * <p> 普通缓存的递增
     *
     * @param key 键
     * @param delta 要增加几
     * @return 增加之后的数值
     */
    public Double incr(String key, double delta) {
        if (StringUtils.isBlank(key) || delta<0){
            return null;
        }

        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Redis：String
     * <p> 普通缓存的递减
     *
     * @param key 键
     * @param delta 要减少几（大于0）
     * @return 减少之后的值
     */
    public Long decr(String key, long delta) {
        if (StringUtils.isBlank(key) || delta<0){
            return null;
        }
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * Redis：Hash
     * <p> 将哈希表key中的域field的值设为value
     * <p> 如果域field已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key 键
     * @param field 哈希表中的域
     * @param value 域对应的值
     */
    public void hPut(String key, String field , String value){
        if (StringUtils.isAnyBlank(key, field, value)){
            return;
        }

        stringRedisTemplate.opsForHash().put(key, field , value);
    }

    /**
     * Redis：Hash
     * <p> 将哈希表key中的域field的值设置为value，当且仅当域field不存在。
     * <p> 若域field已经存在，该操作无效。
     *
     * @param key 键
     * @param field 域
     * @param value 值
     * @return true 成功，false 失败
     */
    public Boolean hPutIfAbsent(String key, String field , String value){
        if (StringUtils.isAnyBlank(key, field, value)){
            return false;
        }

        return stringRedisTemplate.opsForHash().putIfAbsent(key, field, value);
    }

    /**
     * Redis：Hash
     * <p> 同时将多个field-value (域-值)对设置到哈希表key中。
     * <p> 会覆盖哈希表中已存在的域。
     *
     * @param key 键
     * @param map (域-值)map
     */
    public void hPutAll(String key, Map<String, String> map){
        if (StringUtils.isBlank(key) || CollectionUtil.isEmpty(map)){
            return;
        }

        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Redis：Hash
     * <p> 返回哈希表key中给定域field的值。
     *
     * @param key 键
     * @param field 域
     * @return 哈希表中域对应的值
     */
    public Object hGet(String key, String field){
        if (StringUtils.isAnyBlank(key, field)){
            return null;
        }

        return stringRedisTemplate.opsForHash().get(key, field);
    }


    /**
     * Redis：Hash
     * <p> 删除哈希表key中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key 键
     * @param field 域
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    public Long hDel(String key, String... field){
        if (StringUtils.isBlank(key) || ArrayUtil.isEmpty(field) || ArrayUtil.hasNull(field)){
            return null;
        }

        return stringRedisTemplate.opsForHash().delete(key, (Object) field);
    }

    /**
     * Redis：Hash
     * 返回哈希表key中的所有域。
     * 当key不存在时，返回一个空表。
     *
     * @param key 键
     * @return 一个包含哈希表中所有域的表。
     */
    public Set<Object> hKeys(String key){
        if (StringUtils.isBlank(key)){
            return Collections.emptySet();
        }

        return stringRedisTemplate.opsForHash().keys(key);
    }

    /**
     * Redis：Hash
     * <p> 返回哈希表key中域的数量。
     *
     * @param key 键
     */
    public Long hSize(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * Redis：Hash
     * <p> 为哈希表key中的域field的值加上增量delta
     *
     * @param key 键
     * @param field 域
     * @param delta 增量delta
     * @return
     */
    public Long hIncr(String key, String field, long delta){
        if (StringUtils.isAnyBlank(key, field)){
            return null;
        }
        if (delta < 0){
            return null;
        }

        return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * Redis：Hash
     * <p> 为哈希表key中的域field的值加上增量delta
     *
     * @param key 键
     * @param field 域
     * @param delta 增量delta
     * @return
     */
    public Double hIncr(String key, String field, double delta){
        if (StringUtils.isAnyBlank(key, field)){
            return null;
        }
        if (delta < 0){
            return null;
        }

        return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * Redis：List
     * <p> 将一个值value插入到列表key的表头(从表的左边插入)
     *
     * @param key 键
     * @param value 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long lPush(String key, String value){
        if (StringUtils.isAnyBlank(key, value)){
            return null;
        }

        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * Redis：List
     * <p> 将一个值value插入到列表key的表尾(最右边)。
     *
     * @param key 键
     * @param value 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long rPush(String key, String value){
        if (StringUtils.isAnyBlank(key, value)){
            return null;
        }

        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * Redis：List
     * <p> 多个value值，那么各个value值按从左到右的顺序依次插入到表头。
     * <p> 比如说，对空列表 myList 执行命令LPUSH myList a b c，
     * 列表的值将是c b a，这等同于原子性地执行LPUSH myList a、LPUSH myList b和LPUSH myList c三个命令。
     *
     * @param key 键
     * @param values 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long lPushAll(String key, String... values){
        if (StringUtils.isBlank(key) || StringUtils.isAnyBlank(values)){
            return null;
        }

        return stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * Redis：List
     * <p> 多个value值，那么各个value值按从左到右的顺序依次插入到表头。
     * <p> 比如说，对空列表myList执行命令LPUSH myList a b c，
     * 列表的值将是c b a，这等同于原子性地执行LPUSH myList a、LPUSH myList b和LPUSH myList c三个命令。
     *
     * @param key 键
     * @param values 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long lPushAll(String key, List<String> values){
        if (StringUtils.isBlank(key) || CollectionUtil.isEmpty(values)){
            return null;
        }

        return stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * Redis：List
     * <p> 多个value值，那么各个value值按从左到右的顺序依次插入到表尾：
     * <p> 比如对一个空列表 myList 执行 RPUSH myList a b c，
     * 得出的结果列表为a b c，等同于执行命令RPUSH myList a、RPUSH myList b、RPUSH myList c。
     *
     * @param key 键
     * @param values 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long rPushAll(String key, String... values){
        if (StringUtils.isBlank(key) || StringUtils.isAnyBlank(values)){
            return null;
        }
        return stringRedisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * Redis：List
     * <p> 多个value值，那么各个value值按从左到右的顺序依次插入到表尾
     * <p> 比如对一个空列表 mylist执行RPUSH mylist a b c，
     * 得出的结果列表为a b c，等同于执行命令RPUSH mylist a、RPUSH mylist b、RPUSH mylist c。
     *
     * @param key 键
     * @param values 值
     * @return 执行插入操作后，列表的长度。
     */
    public Long rPushAll(String key, List<String> values){
        if (StringUtils.isBlank(key) || CollectionUtil.isEmpty(values)){
            return null;
        }

        return stringRedisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * Redis：List
     * <p> 返回列表key的长度。
     * <p> 如果key不存在，则key被解释为一个空列表，返回0.
     *
     * @param key 键
     * @return 列表key的长度
     */
    public Long lSize(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * Redis：List
     * <p> 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * <p> 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * <p> 当start=0 并且 end=-1的时候，返回整个列表
     *
     * @param key 键
     * @param start 开始下标
     * @param end 结束下标
     */
    public List<String> lRange(String key, long start, long end){
        if (StringUtils.isBlank(key)){
            return EMPTY_STRING_LIST;
        }

        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    public static final List<String> EMPTY_STRING_LIST = new ArrayList<>();

    /**
     * Redis：List
     * <p> 移除并返回列表 key 的头元素。
     *
     * @param key 键
     * @return 头元素
     */
    public String lPop(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * Redis：List
     * <p> 移除并返回列表 key 的尾元素。
     *
     * @param key 键
     * @return 尾元素
     */
    public String rPop(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * Redis：SortedSet
     * <p> 将一个member元素及其score值加入到有序集key当中
     * <p> 如果某个member已经是有序集的成员，那么更新这个member的score值，并通过重新插入这个member元素，来保证该member在正确的位置上。
     *
     * @param key 键
     * @param member String类型member
     * @param score score值可以是整数值或双精度浮点数
     * @return true 成功，false 失败
     */
    public Boolean zAdd(String key, String member, double score){
        if (StringUtils.isAnyBlank(key, member)){
            return false;
        }

        return stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    /**
     * Redis：SortedSet
     * <p> 通过TypedTuple方式新增数据。
     *
     * @param key 键
     * @param typedTupleSet typedTupleSet
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> typedTupleSet){
        if (StringUtils.isBlank(key) || CollectionUtil.isEmpty(typedTupleSet)){
            return null;
        }

        return stringRedisTemplate.opsForZSet().add(key, typedTupleSet);
    }

    /**
     * Redis：SortedSet
     * <p> 为有序集key的成员member的score值加上增量increment
     * <p> 可以通过传递一个负数值increment，让score减去相应的值
     *
     * @param key 键
     * @param member key下的member（String类型）
     * @param delta member对应的score要加的值
     * @return member成员的新score值
     */
    public Double zIncr(String key, String member, double delta){
        if (StringUtils.isAnyBlank(key, member)){
            return null;
        }

        //newScore: member成员的新score值
        return stringRedisTemplate.opsForZSet().incrementScore(key, member, delta);
    }

    /**
     * Redis：SortedSet
     * <p> 返回有序集key中，指定区间内的成员。=== 返回的是成员列表
     * <p> 其中成员的位置按score值递增(从小到大)来排序。==== 正序
     * <p> 具有相同score值的成员按字典序(lexicographical order)来排列。
     * <p> 下标参数 start和stop都以0为底，也就是说，以0表示有序集第一个成员，以1表示有序集第二个成员，以此类推。
     * <p> 也可以使用负数下标，以-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
     * <p> 当start=0 并且 end=-1的时候，显示整个有序集成员
     *
     * @param key 键
     * @param start 开始下标，以0表示有序集第一个成员
     * @param end 结束下标，以-1表示最后一个成员
     * @return 指定区间内的有序集成员的列表
     */
    public Set<String> zRange(String key, long start, long end){
        if (StringUtils.isBlank(key)){
            return Collections.<String>emptySet();
        }

        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }


    /**
     * Redis：SortedSet
     * <p> 返回有序集key中，指定区间内的成员。=== 返回的是成员列表
     * <p> 其中成员的位置按 score 值递减(从大到小)来排列。=== 倒序
     * <p> 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
     *
     * @param key 键
     * @param start 开始下标，以0表示有序集第一个成员
     * @param end 结束下标，以-1表示最后一个成员
     * @return 指定区间内的有序集成员的列表
     */
    public Set<String> zReverseRange(String key, long start, long end){
        if (StringUtils.isBlank(key)){
            return Collections.<String>emptySet();
        }

        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }


    /**
     * Redis：SortedSet
     * <p> 返回有序集key中，所有score值介于min和max之间(包括等于min或max)的成员。=== 返回的是成员列表
     * <p> 有序集成员按score值递增(从小到大)次序排列。=== 正序
     * <p> 具有相同score值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     *
     * @param key 键
     * @param min score下限
     * @param max score上限
     * @return 有序集成员的列表
     */
    public Set<String> zRangByScore(String key, double min, double max){
        if (StringUtils.isBlank(key)){
            return Collections.<String>emptySet();
        }

        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * Redis：SortedSet
     * <p> 返回有序集key中，score值介于max和min之间(默认包括等于max或min)的所有的成员。
     * <p> 有序集成员按 score 值递减(从大到小)的次序排列。=== 倒序
     *
     * @param key 键
     * @param min score下限
     * @param max score上限
     */
    public Set<String> zReverseRangeByScore(String key, double min, double max){
        if (StringUtils.isBlank(key)){
            return Collections.<String>emptySet();
        }

        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * Redis：SortedSet
     * <p> 获取TypedTuple数据。=== 正序
     * <p> member以及对应的score值
     *
     * @param key 键
     * @param start 开始下标
     * @param end 结束下标
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end){
        if (StringUtils.isBlank(key)){
            return Collections.emptySet();
        }

        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * Redis：SortedSet
     * <p> 获取TypedTuple数据。=== 倒序
     * <p> member以及对应的score值
     *
     * @param key 键
     * @param start 开始下标
     * @param end 结束下标
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key, long start, long end){
        if (StringUtils.isBlank(key)){
            return Collections.emptySet();
        }

        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * Redis：SortedSet
     * <p> 返回有序集key中，成员member的score值。
     *
     * @param key 键
     * @param member 成员member
     * @return score值
     */
    public Double zScore(String key, Object member){
        if (StringUtils.isBlank(key) || member==null){
            return null;
        }
        return stringRedisTemplate.opsForZSet().score(key, member);
    }

    /**
     * Redis：SortedSet
     * <p> 返回有序集key中成员member的排名。
     * <p> 其中有序集成员按score值递增(从小到大)顺序排列。=== 正序
     * <p> 排名以0为底，也就是说， score值最小的成员排名为 0 。
     *
     * @param key 键
     * @param member 有序集key中成员
     */
    public Long zRank(String key, String member){
        if (StringUtils.isAnyBlank(key, member)){
            return null;
        }

        return stringRedisTemplate.opsForZSet().rank(key, member);
    }

    /**
     * Redis：SortedSet
     * <p> 返回有序集key中成员member的排名。
     * <p> 其中有序集成员按score值递减(从大到小)排序。=== 倒序
     * <p> 排名以0为底，也就是说，score值最大的成员排名为 0 。
     *
     * @param key 键
     * @param member 有序集key中成员
     */
    public Long zReverseRank(String key, String member){
        if (StringUtils.isAnyBlank(key, member)){
            return null;
        }
        return stringRedisTemplate.opsForZSet().reverseRank(key, member);
    }

}
