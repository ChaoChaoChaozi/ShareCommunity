package com.share.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	@Autowired(required=false)
	private ShardedJedisPool pool;
	//set方法无超时
	//set方法有超时
	//exists方法
	//get方法
	//del
	
	public void set(String key,String value){
		ShardedJedis jedis = pool.getResource();
		jedis.set(key, value);
		pool.returnResource(jedis);
	}
	public void set(String key,String value,Integer seconds){
		ShardedJedis jedis = pool.getResource();
		jedis.set(key, value);
		jedis.expire(key, seconds);
		pool.returnResource(jedis);
	}
	public Boolean exists(String key){
		ShardedJedis jedis = pool.getResource();
		Boolean exists = jedis.exists(key);
		pool.returnResource(jedis);
		return exists;
	}
	public String get(String key){
		ShardedJedis jedis = pool.getResource();
		String value = jedis.get(key);
		pool.returnResource(jedis);
		return value;
	}
	public void del(String key){
		ShardedJedis jedis = pool.getResource();
		jedis.del(key);
		pool.returnResource(jedis);
	}
	
	
	
	
	
	
}
