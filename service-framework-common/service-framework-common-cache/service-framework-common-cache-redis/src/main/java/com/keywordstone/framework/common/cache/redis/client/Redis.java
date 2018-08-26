package com.keywordstone.framework.common.cache.redis.client;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author k
 */
@Component
public class Redis {

    /**
     * 默认过期时间
     */
    private final static int DEFAULT_TIMEOUT = 60*30;

    /**
     * 分隔符
     */
    private final static String PREFIX = "#";

    private Redis() {
    }

    public static <T> boolean put(String key,
                                  T t) {
        return setex(generateAppKey(key), t, DEFAULT_TIMEOUT);
    }

    public static <T> boolean put(String key,
                                  T t,
                                  int timeout) {
        return setex(generateAppKey(key), t, timeout);
    }

    public static <T> boolean tokenPut(String key,
                                       T t) {
        return hset(TokenUtils.getToken(), generateTokenKey(key), t);
    }

    public static <T> boolean appPut(String key,
                                     T t) {
        return hset(RedisConfig.getAppName(), key, t);
    }

    public static String get(String key) {
        return singleGet(generateAppKey(key), String.class);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return singleGet(generateAppKey(key), clazz);
    }

    public static <T> List<T> listGet(String key, Class<T> clazz) {
        return singleListGet(get(key), clazz);
    }

    public static List<String> listGet(String key) {
        return singleListGet(get(key), String.class);
    }

    public static String tokenGet(String key) {
        return hget(TokenUtils.getToken(), generateTokenKey(key), String.class);
    }

    public static <T> T tokenGet(String key, Class<T> clazz) {
        return hget(TokenUtils.getToken(), generateTokenKey(key), clazz);
    }

    public static <T> List<T> tokenListGet(String key, Class<T> clazz) {
        return singleListGet(tokenGet(key), clazz);
    }

    public static List<String> tokenListGet(String key) {
        return singleListGet(tokenGet(key), String.class);
    }

    public static String appGet(String key) {
        return hget(RedisConfig.getAppName(), key, String.class);
    }

    public static <T> T appGet(String key, Class<T> clazz) {
        return hget(RedisConfig.getAppName(), key, clazz);
    }

    public static List<String> appListGet(String key) {
        return singleListGet(appGet(key), String.class);
    }

    public static <T> List<T> appListGet(String key, Class<T> clazz) {
        return singleListGet(appGet(key), clazz);
    }

    public static boolean tokenOut(int timeout) {
        return expire(TokenUtils.getToken(), timeout);
    }

    public static boolean tokenClean() {
        return del(TokenUtils.getToken());
    }

    public static boolean appClean() {
        return del(RedisConfig.getAppName());
    }

    public static boolean tokenDel(String key) {
        return hdel(TokenUtils.getToken(), generateTokenKey(key));
    }

    public static boolean appDel(String key) {
        return hdel(RedisConfig.getAppName(), key);
    }

    private static String generateAppKey(String key) {
        return StringUtils.isEmpty(key) ? null : RedisConfig.getAppName() + PREFIX + key;
    }

    private static String generateTokenKey(String key) {
        String token = TokenUtils.getToken();
        return StringUtils.isEmpty(key) && StringUtils.isEmpty(token) ? null : token + PREFIX + key;
    }

    private static <T> boolean setex(String key, T t, int timeout) {
        Jedis jedis = RedisConfig.getJedis();
        if (StringUtils.isNotEmpty(key)
                && null != t) {
            if (String.class.equals(t.getClass())) {
                jedis.setex(key, timeout, (String) t);
            } else {
                jedis.setex(key, timeout, JSON.toJSONString(t));
            }
            jedis.close();
            return true;
        }
        jedis.close();
        return false;
    }

    private static <T> boolean hset(String key, String subKey, T t) {
        Jedis jedis = RedisConfig.getJedis();
        if (StringUtils.isNoneEmpty(key, subKey)
                && null != t) {
            if (String.class.equals(t.getClass())) {
                jedis.hset(key, subKey, String.valueOf(t));
            } else {
                jedis.hset(key, subKey, JSON.toJSONString(t));
            }
            jedis.close();
            return true;
        }
        jedis.close();
        return false;
    }

    private static <T> T singleGet(String key, Class<T> clazz) {
        Jedis jedis = RedisConfig.getJedis();
        T t = null;
        if (StringUtils.isNotEmpty(key)
                && null != clazz) {
            String value = jedis.get(key);
            if (String.class.equals(clazz)) {
                t = (T) value;
            } else {
                t = JSON.parseObject(value, clazz);
            }
        }
        jedis.close();
        return t;
    }

    private static <T> List<T> singleListGet(String json, Class<T> clazz) {
        if (StringUtils.isNotEmpty(json)
                && null != clazz) {
            return JSON.parseArray(json, clazz);
        }
        return null;
    }

    public static <T> T hget(String key, String subKey, Class<T> clazz) {
        Jedis jedis = RedisConfig.getJedis();
        T t = null;
        if (StringUtils.isNoneEmpty(key, subKey)
                && null != clazz) {
            String value = jedis.hget(key, subKey);
            if (String.class.equals(clazz)) {
                t = (T) value;
            } else {
                t = new Gson().fromJson(value, clazz);
            }
        }
        jedis.close();
        return t;
    }

    private static boolean expire(String key, int timeout) {
        Jedis jedis = RedisConfig.getJedis();
        if (StringUtils.isNotEmpty(key)) {
            if (0 >= timeout) {
                jedis.del(key);
            } else {
                jedis.expire(key, timeout);
            }
            jedis.close();
            return true;
        }
        jedis.close();
        return false;
    }

    private static boolean del(String key) {
        Jedis jedis = RedisConfig.getJedis();
        jedis.del(generateAppKey(key));
        jedis.close();
        return true;
    }

    private static boolean hdel(String key, String subKey) {
        Jedis jedis = RedisConfig.getJedis();
        jedis.hdel(key, subKey);
        jedis.close();
        return true;
    }
}
