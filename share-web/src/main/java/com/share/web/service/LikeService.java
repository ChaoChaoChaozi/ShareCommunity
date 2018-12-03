package com.share.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.util.ObjectUtil;


@Service("likeService")
public class LikeService {
	
	@Autowired
	private HttpClientService client ;
	
	
	/**
	 * 用户喜欢
	 * 返回值：无
	 * 参数类型 :get
	 * 参数列表: user_id,object_type,object_id
	 * http://like.share.com/like/like
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @throws Exception 
	 */
	public void like(int user_id, int object_type, int object_id) throws Exception{
		//likeDao.like(user_id, object_type, object_id);
		String url="http://like.share.com/like/like";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id",user_id);
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		client.doGet(url, map);
	}
	/**
	 * 用户不喜欢
	 * 返回值:无
	 * 参数类型:get
	 * 参数列表:user_id,object_type,object_id
	 * http://like.share.com/like/undoLike
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @throws Exception 
	 */
	public void undoLike(int user_id, int object_type, int object_id) throws Exception{
		//likeDao.undoLike(user_id, object_type, object_id);
		String url="http://like.share.com/like/undoLike";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		client.doGet(url, map);
		
	}
	
	/**
	 * 判断用户是否喜欢某个对象
	 * 参数类型:get
	 * 返回值:boolean islike
	 * 参数列表: user_id,object_type,object_id
	 * http://like.share.com/like/isLike
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @return
	 * @throws Exception 
	 */
	public boolean isLike(int user_id, int object_type, int object_id) throws Exception {
		String url="http://like.share.com/like/isLike";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		String doGet = client.doGet(url, map);
		
		return ObjectUtil.mapper.readValue(doGet,Boolean.class);
				/*likeDao.isLike(user_id, object_type, object_id)*/
	}
	
	/**
	 * 返回喜欢某个对象的用户数量
	 * 返回值:long likerscount
	 * 参数列表:object_type,object_id
	 *  http://like.share.com/like/likersCount
	 * 参数类型:get
	 * @param object_type
	 * @param object_id
	 * @return
	 * @throws Exception 
	 */
	public long likersCount(int object_type, int object_id) throws Exception {
		String url="http://like.share.com/like/likersCount";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		String doGet = client.doGet(url, map);
		return Long.parseLong(doGet);
				/*likeDao.likersCount(object_type, object_id)*/
	}
	
	/**
	 * 返回喜欢某个对象的用户ID列表
	 * 返回值 List<Integer>
	 * http://like.share.com/like/likers
	 * 参数类型:get
	 * 参数列表:object_type
	 * @param object_type,object_id
	 * @param object_id
	 * @return
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<Integer> likers(int object_type, int object_id) throws Exception{
		String url="http://like.share.com/like/getLikers";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		List<Integer> cList=null;
		try{
			String jsonData=client.doGet(url,map);//cartList的json;
			
			JsonNode data=ObjectUtil.mapper.readTree(jsonData);
			if(data.isArray()&&data.size()>0){
				cList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Integer.class));
			}
		}catch(Exception e){
			return cList;
		}
		return cList;
		
				/*likeDao.getLikers(object_type, object_id)*/
	}
	
	/**
	 * 用户喜欢的对象数量(部分对象类型)
	 * 返回值: int
	 *  http://like.share.com/like/likeCount
	 * 参数类型: get
	 * 参数列表:user_id
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public int likeCount(int user_id) throws Exception{
		String url="http://like.share.com/like/likeCount";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		String doGet = client.doGet(url,map);
		return Integer.parseInt(doGet);
	}
}
