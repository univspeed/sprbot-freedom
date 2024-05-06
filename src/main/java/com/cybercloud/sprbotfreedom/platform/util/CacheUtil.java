package com.cybercloud.sprbotfreedom.platform.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存操作类
 * @author liuyutang
 */
@Slf4j
@Component
public class CacheUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 维护一个本类的静态变量
    private static CacheUtil cacheUtil;

    @PostConstruct
    public void init() {
        cacheUtil = this;
        cacheUtil.redisTemplate = this.redisTemplate;
    }

    /**
     * 将参数中的字符串值设置为键的值，不设置过期时间
     *
     * @param key
     * @param value 必须要实现 Serializable 接口
     */

    public static void set(String key, String value) {

        cacheUtil.redisTemplate.opsForValue().set(key, value);

    }

    /**
     * 将参数中的字符串值设置为键的值，设置过期时间
     *
     * @param key
     * @param value   必须要实现 Serializable 接口
     * @param timeout
     */

    public static void set(String key, String value, Long timeout) {

        cacheUtil.redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);

    }

    /**
     * 获取与指定键相关的值
     *
     * @param key
     * @return
     */

    public static Object get(String key) {

        return cacheUtil.redisTemplate.opsForValue().get(key);

    }

    /**
     * 设置某个键的过期时间
     *
     * @param key 键值
     * @param ttl 过期秒数
     */

    public static boolean expire(String key, Long ttl) {

        return cacheUtil.redisTemplate.expire(key, ttl, TimeUnit.SECONDS);

    }

    /**
     * 判断某个键是否存在
     *
     * @param key 键值
     */

    public static boolean hasKey(String key) {

        return cacheUtil.redisTemplate.hasKey(key);

    }

    /**
     * 向集合添加元素
     *
     * @param key
     * @param value
     * @return 返回值为设置成功的value数
     */

    public static Long sAdd(String key, String... value) {

        return cacheUtil.redisTemplate.opsForSet().add(key, value);

    }


    public static boolean isMember(String key, String value) {

        return cacheUtil.redisTemplate.opsForSet().isMember(key,value);

    }


    /**
     * 获取集合中的某个元素
     *
     * @param key
     * @return 返回值为redis中键值为key的value的Set集合
     */
    public static Set<String> sGetMembers(String key) {

        return cacheUtil.redisTemplate.opsForSet().members(key);

    }

    /**
     * 将给定分数的指定成员添加到键中存储的排序集合中
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static Boolean zAdd(String key, String value, double score) {

        return cacheUtil.redisTemplate.opsForZSet().add(key, value, score);

    }

    /**
     * 返回指定排序集中给定成员的分数
     *
     * @param key
     * @param value
     * @return
     */
    public static Double zScore(String key, String value) {

        return cacheUtil.redisTemplate.opsForZSet().score(key, value);

    }

    /**
     * 删除指定的键
     *
     * @param key
     * @return
     */
    public static Boolean delete(String key) {

        return cacheUtil.redisTemplate.delete(key);

    }

    /**
     * 删除多个键
     *
     * @param keys
     * @return
     */
    public static Long delete(Collection<String> keys) {

        return cacheUtil.redisTemplate.delete(keys);

    }
    public static int keysNumber(String pattern) {

        return cacheUtil.redisTemplate.keys(pattern).size();

    }

    /**
     * 自增
     *
     * @param key
     */
    public static Long increment(String key) {

        return  cacheUtil.redisTemplate.opsForValue().increment(key);

    }

    public static boolean  hasListValue(String key,String value) {
        return  cacheUtil.redisTemplate.boundListOps(key).equals(value);
    }

    public static Long  setListValue(String key,String value) {
        return  cacheUtil.redisTemplate.boundListOps(key).leftPush(value);
    }

    /**
     * 自减
     *
     * @param key
     */

    public static Long decrement(String key) {
        return  cacheUtil.redisTemplate.opsForValue().decrement(key);
    }

    public static boolean  hasObjectKey(String key,String subKey) {
        try {
            return cacheUtil.redisTemplate.boundHashOps(key).hasKey(subKey);
        }catch (Exception ex){
            return  false ;
        }
    }


    public Long incrBy(String key, String filed, long val) {
        Long v = null;
        try {
            v = redisTemplate.opsForHash().increment(key, filed, val);
        } catch (Exception ex) {
            log.error("取缓存异常, key = {}, ex = {}", key, ex);
            throw new RuntimeException("redis缓存发生异常");
        }
        return v;
    }

    public static long  getObjectSize(String key) {
        return  cacheUtil.redisTemplate.boundHashOps(key).size();
    }
    public static void setObject(String key,String subKey, Object value) {
        cacheUtil.redisTemplate.boundHashOps(key).put(subKey,value);
    }
    public static void setObject(String key,String subKey, Object value, long expire) {
        cacheUtil.redisTemplate.boundHashOps(key).expire(expire,TimeUnit.SECONDS);
        setObject(key,subKey,value);
    }

    public static void deleteBoundHash(String key,Object subKey) {

        cacheUtil.redisTemplate.boundHashOps(key).delete(subKey);

    }
    public static Object getObject(String key, String subKey) {

        return cacheUtil.redisTemplate.boundHashOps(key).get(subKey);
    }
}