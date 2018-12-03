
package com.share.follow.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.follow.mapper.FollowMapper;
import com.share.follow.pojo.Follower;
import com.share.follow.pojo.Following;
import com.share.util.OSFUtils;
import com.share.util.ObjectUtil;

@RestController
public class FollowController{
	private static final String FOLLOWING_KEY = "following:user:";
	private static final String FOLLOWER_KEY = "follower:user:";
	
	private static final int FOLLOW_SCAN_COUNT = 10;
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate; 
	
	@Resource(name="redisTemplate")
	private SetOperations<String, Integer> setOps;
	@Autowired
	private FollowMapper followMapper;
	
	
	@RequestMapping("follow/saveFollowing")
	public Integer saveFollowing(Following following){
	   Integer saveFollowing = followMapper.saveFollowing(following);
	   setOps.add(FOLLOWING_KEY+following.getUser_id(), following.getFollowing_user_id());
	   return saveFollowing;
	}
	
	
	@RequestMapping("follow/saveFollower")
	public Integer saveFollower(Follower follower){
		Integer saveFollower =followMapper.saveFollower(follower);
		setOps.add(FOLLOWER_KEY+follower.getUser_id() ,follower.getFollower_user_id());

		return saveFollower;
	}
	
	@RequestMapping("follow/delFollowing")
	public Integer delFollowing(int following_user_id,int user_id){
		Following following = new Following();
		following.setUser_id(user_id);
		following.setFollowing_user_id(following_user_id);
		int effrows = followMapper.delFollowing(following);
		
		setOps.remove(FOLLOWING_KEY+following.getUser_id(),following.getFollowing_user_id());
		return effrows;
		
	}
	
	@RequestMapping("follow/delFollower")
	public Integer delFollower(int user_id,int follower_user_id){
		System.out.println(user_id+""+follower_user_id);
		Follower follower = new Follower();
		follower.setUser_id(user_id);
		follower.setFollower_user_id(follower_user_id);
		int effrows = followMapper.delFollower(follower);
		
		setOps.remove(FOLLOWER_KEY+follower.getUser_id(),follower.getFollower_user_id());
		return effrows;
		
	}
	
	@RequestMapping("follow/getFollowersCount")
	public long getFollowersCount(int user_id){
		return setOps.size(FOLLOWER_KEY+user_id);
	}
	
	@RequestMapping("follow/getFollowingsCount")
	public long getFollowingsCount(int user_id){
		return setOps.size(FOLLOWING_KEY+user_id);
	}
	
	@RequestMapping("follow/getFollowerIDs")
	public List<Integer> getFollowerIDs(int user_id){
		Set<Integer> members = setOps.members(FOLLOWER_KEY+user_id);
		return OSFUtils.toList(members);
	}
	@RequestMapping("follow/getFollowingIDs")
	public List<Integer> getFollowingIDs(int user_id){
		Set<Integer> members = setOps.members(FOLLOWING_KEY+user_id);
		return OSFUtils.toList(members);
	}
	@RequestMapping("follow/getFollowers")
	public List<Follower> getFollowers(int user_id) {	
		System.out.println(user_id);
		List<Follower> followers = followMapper.getFollowers(user_id);
		System.out.println("getFollowers:"+followers);
		return followers;
	}
	@RequestMapping("follow/getFollowings")
	public List<Following> getFollowings(int user_id) {
		System.out.println("user_id:"+user_id);
		List<Following> following = followMapper.getFollowings(user_id);
		System.out.println("getFollowerings:"+following);
		return following;
	}
	@RequestMapping("follow/hasFollowing")
	public Boolean hasFollowing(int user_a, int user_b) {
		Boolean member = setOps.isMember(FOLLOWING_KEY+user_a, user_b);
		System.out.println(member);
		return member;
	}
	
	@RequestMapping("follow/isFollowingUsers/{user_id}")
	public List<Integer> isFollowingUsers(@PathVariable("user_id")Integer user_id,@RequestBody String json) throws Exception {
		List<Integer> following_ids=null;
		JsonNode data=ObjectUtil.mapper.readTree(json);
		if(data.isArray()&&data.size()>0){
			following_ids = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Integer.class));
		}
		System.out.println("following_ids"+following_ids);
		List<Integer> list=followMapper.isFollowingUsers(user_id,following_ids);
		System.out.println(list);
		return list;
	}

}