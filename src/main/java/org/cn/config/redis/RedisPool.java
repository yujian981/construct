package org.cn.config.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisPool {


    private static JedisPool jedisPool;

    private static void RedisInit() {

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setMinIdle(100);
        config.setTestOnBorrow(true);   //借出时验证
        config.setTestOnReturn(true);   //返回后验证

        jedisPool = new JedisPool(config, "127.0.0.1", 6379);

    }

    static {
        RedisInit();
    }

    public static Jedis getInitRes() {

        return jedisPool.getResource();
    }

}
