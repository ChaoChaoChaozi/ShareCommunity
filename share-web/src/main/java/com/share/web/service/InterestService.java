package com.share.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Interest;
import com.share.pojo.Tag;
import com.share.util.ObjectUtil;
import com.share.vo.HttpResult;


@Service("interestService")
public class InterestService {
	
	@Autowired
	private HttpClientService client ;
	
	
	/**
	 * 关注tag
	 * * url:http://interest.share.com/interest/interestInTag
	 * 请求参数：int user_id, int tag_id
	 * 返回值：interestDao.saveInterest(interest) int
	 * 返回值类型：void
	 * 请求方式：GET
	 * @param user_id
	 * @param tag_id
	 * @throws Exception 
	 */
	public void interestInTag(int user_id, int tag_id) throws Exception {
		//Interest interest = new Interest(user_id, tag_id)
		//interestDao.saveInterest(interest);
		String url="http://interest.share.com/interest/interestInTag?user_id="+user_id+"&tag_id="+tag_id;
		client.doGet(url);
		/*Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("tag_id", tag_id);
		client.doGet(url,map);*/	
	}
	
	
	/**
	 * 撤销关注tag
	 * url:http://interest.share.com/interest/undoInterestInTag
	 * 请求参数：int user_id, int tag_id
	 * 返回值：interestDao.delInterest(user_id, tag_id)
	 * 返回值类型：void
	 * 请求方式：GET
	 * @param user_id
	 * @param tag_id
	 * @throws Exception 
	 */
	public void undoInterestInTag(int user_id, int tag_id) throws Exception{
		String url="http://interest.share.com/interest/undoInterestInTag?user_id="+user_id+"&tag_id="+tag_id;
		/*Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("tag_id", tag_id);
		client.doGet(url, map);*/
		client.doGet(url);
		
		//Interest interest = new Interest(user_id, tag_id);
		//interestDao.delInterest(interest);
	}
	
	/**
	 * 获取关注tag_id的用户列表
	 * url:http://interest.share.com/interest/getUsersInterestedInTag
	 * 请求参数： int tag_id
	 * 返回值：interestDao.getUsersInterestInTag(int tag_id)
	 * 返回值类型：void
	 * 请求方式：GET
	 * @param tag_id
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> getUsersInterestedInTag(int tag_id) throws Exception {
		String url="http://interest.share.com/interest/getUsersInterestedInTag/"+tag_id;
		/*Map<String, Object> map=new HashMap<String, Object>();
		map.put("tag_id", tag_id);*/
		List<Integer> cList=null;
		try{
			String jsonData=client.doGet(url);//cartList的json;
			
			JsonNode data=ObjectUtil.mapper.readTree(jsonData);
			if(data.isArray()&&data.size()>0){
				cList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Integer.class));
			}
		}catch(NullPointerException e){
			return cList;
		}
		return cList;
				/*interestDao.getUsersInterestInTag(tag_id)*/
	}
	
	/**
	 * 判断用户对tag是否已经关注
	 *url:http://interest.share.com/interest/hasInterestInTag
	 * 请求参数： int user_id, int tag_id
	 * 返回值：interestDao.hasInterestInTag(user_id, tag_id)
	 * 返回值类型：void
	 * 请求方式：GET
	 * @param user_id
	 * @param tag_id
	 * @return
	 * @throws Exception 
	 */
	public boolean hasInterestInTag(int user_id, int tag_id) throws Exception {
		String url="http://interest.share.com/interest/hasInterestInTag/"+user_id+tag_id;
		/*Map<String, Object>map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("tag_id", tag_id);*/
		String doGet=client.doGet(url);
		
		return false;
				/*interestDao.hasInterestInTag(user_id, tag_id)*/
	}
	

	/**
	 * 判断用户对列表中的tag是否已经关注
	 * url:http://interest.share.com/interest/hasInterestInTags
	 * 请求参数： int user_id, int tag_id
	 * 返回值：result
	 * 返回值类型：Map
	 * 请求方式：GET
	 * @param user_id
	 * @param tags
	 * @return
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Map<Integer, Boolean> hasInterestInTags(int user_id, List<Tag> tags) throws JsonParseException, JsonMappingException, Exception{
	if(tags == null || tags.size() == 0 ){
			return null;
		}
		Map<Integer, Boolean> result = new TreeMap<Integer, Boolean>();
		List<Integer> tag_ids = new ArrayList<Integer>();
		for(Tag tag: tags){
			tag_ids.add(tag.getId());
			result.put(tag.getId(), false);
		}
		/*List<Integer> interested_tags = interestDao.hasInterestInTags(user_id, tag_ids);
		for(int i=0; i<interested_tags.size(); i++) {
			result.put(interested_tags.get(i), true);
		}*/
		String url="http://interest.share.com/interest/hasInterestInTags/"+user_id+tags;
		/*Map<String, Object> map=new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("tag_ids", tag_ids);*/
		List<Integer> cList=null;
		try{
			String jsonData=client.doGet(url);//cartList的json;
			
			JsonNode data=ObjectUtil.mapper.readTree(jsonData);
			if(data.isArray()&&data.size()>0){
				cList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Integer.class));
			}
		}catch(NullPointerException e){
			return null;
		}
		for(int i=0; i<cList.size(); i++) {
			result.put(cList.get(i), true);
		}
		return result;
	}
	
	/**
	 * 获取用户关注的tag列表
	 * url:http://interest.share.com/interest/getTagsUserInterestedIn
	 * 请求参数： int user_id
	 * 返回值：interestDao.getTagsUserInterestedIn(user_id)
	 * 返回值类型：List<Tag>
	 * 请求方式：GET
	 * @param user_id
	 * @param tag_id
	 * @return
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<Tag> getTagsUserInterestedIn(int user_id) throws Exception{
		String url="http://interest.share.com/interest/getTagsUserInterestedIn/"+user_id;
		/*Map<String, Object>map=new HashMap<String, Object>();
		map.put("user_id", user_id);*/
		List<Tag> cList=null;
		try{
			String jsonData=client.doGet(url);//cartList的json;
			
			JsonNode data=ObjectUtil.mapper.readTree(jsonData);
			if(data.isArray()&&data.size()>0){
				cList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Tag.class));
			}
		}catch(NullPointerException e){
			return cList;
		}
		return cList;
		
		
				/*interestDao.getTagsUserInterestedIn(user_id)*/
	}
}
