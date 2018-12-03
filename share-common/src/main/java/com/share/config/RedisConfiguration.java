package com.share.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class RedisConfiguration {
		//从配置文件读取数据
	@Value("${spring.redis.nodes:null}")//默认是"null"不是null关键字
	private String nodes;
	@Value("${spring.redis.pool.max-idle:1}")
	private Integer maxIdle;
	@Value("${spring.redis.pool.min-idle:0}")
	private Integer minIdle;
	@Value("${spring.redis.pool.max-active:1}")
	private Integer maxTotal;//对应配置文件时max-active
	@Value("${spring.redis.pool.max-wait:1}")
	private Integer maxWait;
	@Value("${spring.redis.timeout:0}")
	private Integer timeout;
	
	//利用参数返回JedisPoolConfig对象
	public JedisPoolConfig getConfig(){
		JedisPoolConfig config=new JedisPoolConfig();
		//设定参数
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setMaxTotal(maxTotal);
		config.setMaxWaitMillis(maxWait);
		return config;
	}
	//创建返回jedis分片连接池对象,并且由框架维护管理
	@Bean
	public ShardedJedisPool getPool(){
		//获取infoList 
		List<JedisShardInfo> infoList=
				new ArrayList<JedisShardInfo>();
		//处理字符串nodes
		if(!("null".equals(nodes))){
		String[] hostAndPorts =nodes.split(",");
		for (String node : hostAndPorts) {
			//每个node格式192.168.198.40:6379
			String[] hostAndPort=node.split(":");
			//生成一个info对象,添加到list中
			JedisShardInfo info=
			new JedisShardInfo(hostAndPort[0], 
					Integer.parseInt(hostAndPort[1]), timeout);
			infoList.add(info);
		}
		//用config对象和list对象生产jedis分片连接池
		ShardedJedisPool pool=new ShardedJedisPool(getConfig(),infoList);
		return pool;
		}
		return null;
	}
}






















