package com.share.sso.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.sso.mapper.UserMapper;
import com.share.sso.pojo.User;
import com.share.util.MD5Util;
import com.share.util.ObjectUtil;
import com.share.util.Property;


@RestController
public class UserController {
	public static final int USERNAME_MAXLEN = 20;
	public static final int STATUS_USER_NORMAL = 0;				//正常
	public static final int STATUS_USER_INACTIVE = 1;			//待激活
	public static final int STATUS_USER_LOCK = 2;				//锁定
	public static final int STATUS_USER_CANCELLED = 3;			//注销
	@Autowired
	private UserMapper userMapper;
	
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate; 

	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Object> mapOps;
	
	/**
	 * 在redis中插入user，和token
	 * @param user
	 * @param token
	 */
	@RequestMapping("/user/insertToken")
	public void insertToken(User user,String token){
		mapOps.put("tokens:", token, user);
	}
	
	
	/*
	 * 从redis中删除user
	 * 
	 */
	@RequestMapping("/user/delToken")
	public void delToken(String token){
		mapOps.delete("tokens:", token);
	}
	
	
	
	/*
	 * 判断redis中是否存在Token
	 * 请求url：http://sso.share.com/user/containsToken
	 * 请求方法：get
	 * 请求参数：String token
	 * 返回值：boolean
	 */
	@RequestMapping("/user/containsToken")
	public boolean containsToken(String token){
		return mapOps.hasKey("tokens:", token);
	}
	
	
	
	/**
	 * 通过token查找user
	 * 
	 */
	@RequestMapping("/user/getUserByToken")
	public User findUserByToken(String token){	
		return (User) mapOps.get("tokens:", token);
	}
	@RequestMapping("/user/getUserofUsername")
	public List<User> getUserofUsername(String username){
		return userMapper.getUserofUsername(username);
	}
	
	//通过用户名找用户
	//请求参数:String username
	// 返回值类型 User
	@RequestMapping("/user/getUserByUsername")
	public User findByUsername(String username){
		
		return userMapper.findByUsername(username);
	}
	
	
	/**
	 * 通过邮箱寻找Uesr
	 * 请求url：http://sso.share.com/user/getUserByEmail
	 * 请求方法：get
	 * 请求参数：String email
	 */
	@RequestMapping("/user/getUserByEmail")
	public User getUserByEmail(String email){
		User user=userMapper.findByEmail(email);
		
		return user;
	}
	
	
	/**
	 * 通过ID寻找Uesr
	 * 请求url：http://sso.share.com/user/getUserByID
	 * 请求方法：get
	 * 请求参数：int id
	 * 返回值类型： User
	 */
	@RequestMapping("/user/getUserByID")
	public User getUserByID(Integer id){
		User userByID = userMapper.getUserByID(id);
		 return userByID;
	}
	
	
	/**
	 * 通过IDList寻找UesrList
	 * 请求url：http://sso.share.com/user/getUsersByIDs
	 * 请求方法：post
	 * 请求参数：List<Integer> ids
	 * 返回值类型： List<User>
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/user/getUsersByIDs")
	public List<User> getUsersByIDs(@RequestBody String ids) throws  Exception{
		List<Integer> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(ids);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Integer.class));
		}
		if(pList==null||pList.size()==0)return null;
		List<User> user=userMapper.getUsersByIDs(pList);
	
		return user;

	}
	
	
	
	@RequestMapping("/user/register")
	public String register(User user){
		return userMapper.register(user)+"";
	}
	
	
	
	/*
	 * 更新/user/updateActivationKey
	 */
	@RequestMapping("/user/updateActivationKey")
	public String updateActivationKey(User user){
		String activationKey ="123";
		userMapper.updateActivationKey(user.getId(),activationKey);
		return activationKey;
	}
	
	
	
	@RequestMapping("/user/activateUser")
	public String activateUser(String email,String key){
		User user=getUserByEmail(email);
		if(user == null)
			return Property.ERROR_ACCOUNT_ACTIVATION_NOTEXIST;
		else {
			
			if(user.getUser_status() == STATUS_USER_INACTIVE ){
				if(user.getUser_activationKey().equals(key)){
					user.setUser_activationKey(null);
					user.setUser_status(STATUS_USER_NORMAL);
					
					int id=user.getId();
					String activationKey=user.getUser_activationKey();
					
					userMapper.activateUser(activationKey,id);
				}else {
					return Property.ERROR_ACCOUNT_ACTIVATION_EXPIRED;
				}
			} else{
				if(user.getUser_status() == STATUS_USER_NORMAL){
					return Property.ERROR_ACCOUNT_EXIST;
				} else{
					return Property.ERROR_ACCOUNT_ACTIVATION;
				}
				
			}
		}
		return Property.SUCCESS_ACCOUNT_ACTIVATION;
	}
	
	
	@RequestMapping("/user/getUser")
	public User findByActivationKey(String key){
		User user=userMapper.getUser("user_activationKey", new Object[]{key});
		return user;
	}
	
	
	//获取推荐用户ID
	@RequestMapping("user/getUsers")
	public List<User> getRecommendUsersID(Integer count){
		if(count==null)return null;
		List<User> puser=userMapper.getUsers(count);
		return puser;
	}
	
	
	@RequestMapping("/user/updateAvatar")
	public void updateAvatar(int user_id,String avatar){
		userMapper.updateAvatar(user_id,avatar);
		User user = (User)mapOps.get("user", "user:"+user_id);
		if(user == null) {
			user =getUserByID(user_id);
		}
		user.setUser_avatar(avatar);
		mapOps.put("user", "user:"+user_id, user);
	}
	
	
	/**
	 * 更新用户名和描述
	 * 请求url：http://sso.share.com/user/updateUsernameAndDesc
	 * 请求方法：get
	 * 请求参数：int user_id, String username, String desc
	 * 返回数据：无 
	 */
	
	@RequestMapping("user/updateUsernameAndDesc")
	public void updateUsernameAndDesc(User user){
		userMapper.updateUsernameAndDesc(user.getId(),user.getUser_name(),user.getUser_desc());
	}
	
	@RequestMapping("/user/updateResetPwdKey")
	public void updateResetPwdKey(String email,String key){
		userMapper.updateResetPwdKey(email,key);
	}
	
	@RequestMapping("/user/isAllowedResetPwd")
	public String isAllowedResetPwd(String email){
		return userMapper.isAllowedResetPwd(email);
	}
	
	@RequestMapping("/user/updatePassword")
	public Integer updatePassword(String user_email,String user_pwd){
		
		int s=userMapper.updatePassword(user_email,user_pwd);
		
		return s;
	}
	
}






















