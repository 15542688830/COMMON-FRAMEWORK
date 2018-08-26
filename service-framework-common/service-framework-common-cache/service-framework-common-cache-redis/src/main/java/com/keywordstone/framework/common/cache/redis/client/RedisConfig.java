package com.keywordstone.framework.common.cache.redis.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author k
 */
@Component
@Slf4j
public class RedisConfig {

//    private static JedisCluster jedis;

    private static Jedis jedis;

    private static JedisPool pool;

    /**
     * 连接池配置
     */
    private static JedisPoolConfig config;

    /**
     * 工程名
     */
    private static String appName;

    @Autowired
    private RedisProperties redisProperties;

//    protected static JedisCluster getJedis() {
//        return jedis;
//    }

    protected static Jedis getJedis() {
        return pool.getResource();
    }

    protected static String getAppName() {
        return appName;
    }

    @Value("${spring.application.name}")
    private void setAppName(String appName) {
        RedisConfig.appName = appName;
    }

    private JedisPoolConfig config() {
        if (null == config) {
            config = new JedisPoolConfig();
            config.setMaxTotal(redisProperties.getPool().getMaxActive());
            config.setMaxIdle(redisProperties.getPool().getMaxIdle());
            config.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        }
        return config;
    }

//    @Bean
//    protected JedisCluster init() {
//        if (null == redisProperties.getCluster()
//                || null == redisProperties.getCluster().getNodes()
//                || 0 == redisProperties.getCluster().getNodes().size()) {
//            return null;
//        }
//        if (null == config) {
//            config = config();
//        }
//        Set<HostAndPort> hostAndPortSet = redisProperties.getCluster().getNodes().stream()
//                .filter(hostAndPort -> StringUtils.isNotEmpty(hostAndPort)
//                        && hostAndPort.contains(":"))
//                .map(hostAndPort -> new HostAndPort(
//                        hostAndPort.split(":")[0],
//                        Integer.valueOf(hostAndPort.split(":")[1])))
//                .collect(Collectors.toSet());
//        jedis = new JedisCluster(hostAndPortSet, config);
//        return jedis;
//    }

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5000);
        config.setMaxIdle(2000);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "192.168.200.201", 6379);
        Jedis jedis;
        int i = 0;
        try {
            for (; i < 100000; i++) {
                jedis = pool.getResource();
                jedis.hset("111", "222", "333");
                jedis.close();
                log.info(String.valueOf(i));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(String.valueOf(i));
        }
    }

    @Bean
    protected JedisPool init() {
        pool = new JedisPool(config(),
                redisProperties.getHost(),
                redisProperties.getPort());
        return pool;
    }
}
