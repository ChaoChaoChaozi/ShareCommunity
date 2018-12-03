package com.share.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Follower;
import com.share.pojo.Following;
import com.share.pojo.User;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.vo.HttpResult;


@Service
public class FollowService {
@Autowired
private HttpClientService client;


	/**
	 * 
	 * @param user_id
	 * @param user_name
	 * @param following_user_id
	 * @param following_user_name
	 * @return 
	 * @throws Exception 
	 */
	public Map<String, Object> newFollowing(int user_id, String user_name, int following_user_id, String following_user_name) throws Exception {
		Map<String, Object> followingMap = new HashMap<String, Object>();
		Map<String,Object> followerMap =new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		Following following = new Following();
		following.setUser_id(user_id);
		following.setUser_name(user_name);
		following.setFollowing_user_id(following_user_id);
		following.setFollowing_user_name(following_user_name);
		//int id = followDao.saveFollowing(following);
		
		String url1="http://follow.share.com/follow/saveFollowing";
		followingMap.put("user_id", user_id);
		followingMap.put("user_name", user_name);
		followingMap.put("following_user_id", following_user_id);
		followingMap.put("following_user_name", following_user_name);
		HttpResult id1= client.doPost(url1, followingMap);
		
		String body1 = id1.getBody();
		try{
		if(Integer.parseInt(body1) == 0) {
			map.put("status", Property.ERROR_FOLLOW); 
			return map;
		}}catch(Exception e){
			System.out.println("Integer.parseInt(body1)出错");
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUser_id(following_user_id);
		follower.setUser_name(following_user_name);
		follower.setFollower_user_id(user_id);
		follower.setFollower_user_name(user_name);
		//followDao.saveFollower(follower);
		String url2="http://follow.share.com/follow/saveFollower";
		followerMap.put("user_id", following_user_id);
		followerMap.put("user_name",following_user_name);
		followerMap.put("follower_user_id", user_id);
		followerMap.put("following_user_name", user_name);
		HttpResult id2 = client.doPost(url2, followerMap);
		String body2=id2.getBody();
		try {
			if(Integer.parseInt(body2)== 0) {
				map.put("status", Property.ERROR_FOLLOW);
				return map;
			}
		} catch (Exception e) {
			System.out.println("Integer.parseInt(body2)报错");
		}
		map.put("follower", follower);
		map.put("following", following);
		map.put("status", Property.SUCCESS_FOLLOW);
		
		return map;
	}
	
	/**
	 * 通过int user_id, int following_user_id返回取消的关注者
	 * url：url:http://follow.share.com/follow/delFollowing
	 * 
	 * 请求参数：int user_id, int following_user_id
	 * 返回值：map
	 * 返回类型 Map
	 * 请求类型：GET
	 * @param user_id
	 * @param following_user_id
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> undoFollow(int user_id, int following_user_id) throws Exception {
		Map<String, Object> followingMap = new HashMap<String, Object>();
		Map<String,Object> followerMap =new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		Following following = new Following();
		following.setUser_id(user_id);
		following.setFollowing_user_id(following_user_id);
		String url1 = "http://follow.share.com/follow/delFollowing";
		followingMap.put("user_id",user_id );
		followingMap.put("following_user_id", following_user_id);
		HttpResult id1 = client.doPost(url1, followingMap);
		String body1 = id1.getBody();
		if(Integer.parseInt(body1)== 0) {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
			return map;
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUser_id(following_user_id);
		follower.setFollower_user_id(user_id);
		String url2="http://follow.share.com/follow/delFollower";
		followerMap.put("user_id",following_user_id);
		followerMap.put("follower_user_id",user_id);
		HttpResult id2 = client.doPost(url2, followerMap);
		String body2 = id2.getBody();
		if(Integer.parseInt(body2)> 0) {
			map.put("status", Property.SUCCESS_FOLLOW_UNDO);
			map.put("follower", follower);
		} else {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
		}
		return map;
	}
	
	/**
	 * 根据user_id 获取粉丝的数量
	 * url：http://follow.share.com/follow/followerscount
	 * 请求参数:int user_id
	 * 返回值类型：long
	 * 返回值是：followDao.getFollowersCount(user_id)
	 * 请求方式：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public long followersCount(int user_id) throws Exception {
		String url ="http://follow.share.com/follow/getFollowersCount?user_id="+user_id;
		String result = client.doGet(url);
	
		if(StringUtils.isNotEmpty(result))
		return Long.parseLong(result);
		return 0;
	}
	
	/**
	 * 根据user_id 获取关注的的数量
	 * url：http://follow.share.com/follow/followingsCount
	 * 请求参数:int user_id
	 * 返回值类型：long 
	 * 返回值是：followDao.getFollowingsCount(user_id)
	 * 请求方式：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public long followingsCount(int user_id) throws Exception {
		String url ="http://follow.share.com/follow/getFollowingsCount?user_id="+user_id;
		String result = client.doGet(url);
		return Long.parseLong(result);
	}
	
	/**
	 * 根据user_Id 获取粉丝的ID
	 * url：http://follow.share.com/follow/getFollowerIDs
	 * 返回值类型  List<Integer>
	 * 返回值是：followDao.getFollowerIDs(user_id)
	 * 请求类型：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> getFollowerIDs(int user_id) throws  Exception {
		String url ="http://follow.share.com/follow/getFollowerIDs?user_id="+user_id;
		String result = client.doGet(url);
		List<Integer> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(result);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Integer.class));
		}
		return pList;
		//return /*followDao.getFollowerIDs(user_id)*/null;
	}
	
	/**
	 * 根据user_id 获取关注者的id
	 * url：http://follow.share.com/follow/getFollowingIDs
	 * 返回值类型  List<Integer>
	 * 返回值是：followDao.getFollowingIDs(user_id)
	 * 请求类型：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> getFollowingIDs(int user_id) throws Exception {
		String url ="http://follow.share.com/follow/getFollowingIDs?user_id="+user_id;
		String result = client.doGet(url);
		List<Integer> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(result);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Integer.class));
		}
		return pList;
		//return /*followDao.getFollowingIDs(user_id)*/null;
	}
	
	/**通过user_id 获取粉丝
	 * url：http://follow.share.com/follow/getFollowers
	 * 返回值类型  List<Integer>
	 * 返回值是：followDao.getFollowers(user_id)
	 * 请求类型：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public List<Follower> getFollowers(int user_id) throws Exception {
		String url ="http://follow.share.com/follow/getFollowers?user_id="+user_id;

		String result = client.doGet(url);
		List<Follower> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(result);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Follower.class));
		}
		return pList;
		//return /*followDao.getFollowers(user_id)*/null;
	}
	
	
	/**通过user_id 获取关注者
	 * url：http://follow.share.com/follow/getFollowings
	 * 返回值类型  List<Integer>
	 * 返回值是：followDao.getFollowings(user_id)
	 * 请求类型：GET
	 * @param user_id
	 * @return
	 * @throws Exception 
	 
	 */
	public List<Following> getFollowings(int user_id) throws Exception {
		String url ="http://follow.share.com/follow/getFollowings?user_id="+user_id;
		String result = client.doGet(url);
		List<Following> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(result);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Following.class));
		}
		 return pList;
		//return /*followDao.getFollowings(user_id)*/null;
	}
	
	
	/**
	 * 
	 * 通过 int user_a, int user_b判断是否是偶像
	 * url：http://follow.share.com/follow/hasFollowing
	 * 返回值类型  boolean
	 * 返回值是：followDao.hasFollowing(user_a, user_b)
	 * 请求类型：GET
	 * 注意：//这个方法可能有问题
            boolean empty = doGet.isEmpty();  
	 * @param user_a
	 * @param user_b
	 * @return 
	 * @throws Exception 
	 */
	public boolean isFollowing(int user_a, int user_b) throws Exception{

		String url ="http://follow.share.com/follow/hasFollowing?user_a="+user_a+"&user_b="+user_b;
        String doGet = client.doGet(url);
        //这个方法可能有问题
		return new Boolean(doGet);
	}
	
	
	/**
	 * 通过 int user_a, int user_b判断是否是 粉丝
	 * url：http://follow.share.com/follow/isFollowingUsers
	 * 返回值类型  Map
	 * 返回值是：result
	 * 请求类型：GET
	 * @param user_a
	 * @param user_b
	 * @return
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	public Map<Integer, Boolean> isFollowing(int user_id, List<User> users) throws Exception{
		if(users == null || users.size() == 0) {
			return null;
		}
		Map<Integer, Boolean> result = new TreeMap<Integer, Boolean>();
		List<Integer> users_id = new ArrayList<Integer>();
		for(User user: users) {
			users_id.add(user.getId());
			result.put(user.getId(), false);
		}												
		//List<Integer> following_users = followDao.isFollowingUsers(user_id, users_id);
		String url="http://follow.share.com/follow/isFollowingUsers/"+user_id;
		String json=ObjectUtil.mapper.writeValueAsString(users_id);
		String doPost = client.doPostJson(url, json);
		
		List<Integer> following_users=null;
		JsonNode data=ObjectUtil.mapper.readTree(doPost);
		if(data.isArray()&&data.size()>0){
			following_users = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Integer.class));
		}
		if(following_users!=null)
		for(int i=0; i<following_users.size(); i++) {
			result.put(following_users.get(i), true);
		}
		return result;
	}
}
