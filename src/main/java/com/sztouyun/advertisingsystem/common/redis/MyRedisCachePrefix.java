package com.sztouyun.advertisingsystem.common.redis;

import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by szty on 2018/8/21.
 */
public class MyRedisCachePrefix implements RedisCachePrefix {
    private final RedisSerializer serializer;
    private final String delimiter;

    public MyRedisCachePrefix() {
        this("");
    }

    public MyRedisCachePrefix(String delimiter) {
        this.serializer = new StringRedisSerializer();
        this.delimiter = delimiter;
    }

    public byte[] prefix(String cacheName) {
        if(org.springframework.util.StringUtils.isEmpty(delimiter))
            return this.serializer.serialize(cacheName.concat("_"));
        return this.serializer.serialize(this.delimiter.concat("_"+cacheName+"_"));
    }
}
