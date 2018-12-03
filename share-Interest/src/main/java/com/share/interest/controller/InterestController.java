package com.share.interest.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.interest.mapper.InterestMapper;
import com.share.interest.model.Interest;
import com.share.pojo.Tag;

@RestController
public class InterestController {
	public static String TABLE_INTEREST = "s_interests";
	public static String TABLE_TAG 		= "s_tags";
	
	private static final String INTEREST_KEY = "tag:interest:user:"; //用户关注的tag

	@Autowired
	private InterestMapper interestMapper;
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate; 
	
	@Resource(name="redisTemplate")
	private SetOperations<String, Integer> setOps;
	private Interest interest;
	//关注tag
	@RequestMapping("interest/interestInTag")
	public void interestInTag(int user_id ,int tag_id){
		
		int i=interestMapper.interestInTag(user_id,tag_id);
		
		//setOps.add(INTEREST_KEY+interest.getUser_id(), interest.getTag_id());
	}
	
	//撤销关注tag
	@RequestMapping("interest/undoInterestInTag")
	public void undoInterestInTag(int user_id, int tag_id){
		interestMapper.undoInterestInTag(user_id,tag_id);
	}
	
	//获取关注tag_id的用户列表
	@RequestMapping("interest/getUsersInterestedInTag/{tag_id}")
	public List<Integer> getUsersInterestedInTag(@PathVariable int tag_id){
	
		
		return interestMapper.getUsersInterestedInTag(tag_id);	
	}
	
	//判断用户对tag是否已经关注
	@RequestMapping("interest/hasInterestInTag/{user_id}/{tag_id}")
	public Boolean hasInterestInTag(@PathVariable int user_id, @PathVariable int tag_id){
		return interestMapper.hasInterestInTag(user_id,tag_id);
		
	}
	//判断用户对列表中的tag是否已经关注
	@RequestMapping("interest/hasInterestInTags/{user_id}/{tags}")
	public Map<Integer,Boolean> hasInterestInTags(@PathVariable int user_id,@PathVariable List<Tag> tags){
		return interestMapper.hasInterestInTags(user_id, tags);
		
	}
	//获取用户关注的tag列表
	@RequestMapping("interest/getTagsUserInterestedIn/{user_id}")
	public List<Tag> getTagsUserInterestedIn(@PathVariable int user_id){
		return interestMapper.getTagsUserInterestedIn(user_id);	
	}
}

