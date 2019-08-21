package org.cn.config.redis;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    public static String get(String key){

        Jedis initResGet= null;
        String result = null;

        try {
            initResGet = RedisPool.getInitRes();
            result = initResGet.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (initResGet==null){
                initResGet.close();
                initResGet=null;
            }
        }
              return result;
    }

    public static void del(String key){

        Jedis initResDel =null;

        try {
            initResDel = RedisPool.getInitRes();
            initResDel.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (initResDel==null){
                initResDel.close();
                initResDel=null;
            }
        }
    }

    public static  void set(String key,int time){

        Jedis initResSet =null;

        try {
            initResSet = RedisPool.getInitRes();
            initResSet.expire(key,time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initResSet.close();
            initResSet=null;
        }
    }

    public static void  set(String key,String value){

        Jedis initResGet=null;
        try {
            initResGet = RedisPool.getInitRes();

            initResGet.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (initResGet==null){
                initResGet.close();
                initResGet=null;
            }
        }

    }

    public static void  set(String key,String value,int time){

        Jedis initResGet=null;
        try {
            initResGet = RedisPool.getInitRes();

            initResGet.setex(key,time,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (initResGet==null){
                initResGet.close();
                initResGet=null;
            }
        }

    }

}
