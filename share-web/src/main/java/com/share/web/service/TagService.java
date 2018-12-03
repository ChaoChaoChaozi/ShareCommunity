package com.share.web.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Event;
import com.share.pojo.Tag;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.web.search.IndexSearch;


@Service
public class TagService {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RelationService relationService;

	@Autowired
	private FeedService feedService;
	@Autowired
	private HttpClientService client;
	
	
	@Autowired
	private IndexSearch indexSearch;

	private String check(String tag) {
		if(tag == null || tag.length() == 0) {
			return Property.ERROR_TAG_EMPTY;
		}
		return Property.SUCCESS_TAG_FORMAT;
	}
	
	public static List<Tag> toList(String tags) {
		if(tags == null || tags.length() == 0)
			return new ArrayList<Tag>();
		String[] tag_and_id_strs = tags.split(" ");
		List<Tag> tag_list = new ArrayList<Tag>();
		for(String tag : tag_and_id_strs) {
			String[] tag_and_id = tag.split(":");
			Tag t = new Tag();
			if(tag_and_id.length > 1) {
				t.setId(Integer.valueOf(tag.split(":")[1]) );
			}
			t.setTag(tag.split(":")[0]);
			
			tag_list.add(t);
		}
		
		return tag_list;
	}
	
	
//	public static String toString(List<String> tags) {
//		if(tags == null || tags.size() == 0)
//			return null;
//		StringBuffer buffer = new StringBuffer();
//		for(String tag: tags) {
//			buffer.append(tag+" ");
//		}
//		return buffer.toString();
//	}
	
	public static String toString(List<Tag> tags) {
		if(tags == null || tags.size() == 0)
			return null;
		StringBuffer buffer = new StringBuffer();
		for(Tag tag: tags) {
			buffer.append(tag.getTag()+":"+tag.getId()+" ");
		}
		return buffer.toString();
	}
	/**
	 * 新建标签
	 * 请求url：http://tag.share.com/tag/getTagID?+tag
	 * 请求方法：get
	 * 请求参数：String tag
	 * 返回值：Map<String, Object>
	 * @param tag
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> newTag(String tag) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		String status = check(tag);
		ret.put("status", status);
		if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
			return ret;
		}
		String url="http://tag.share.com/tag/getTagID?tag="+tag;
		String jsonData=client.doGet(url);
		int id=Integer.parseInt(jsonData);
		//int id = tagDao.getTagID(tag);
		if(id != 0) {
			Tag tg = new Tag();
			tg.setId(id);
			tg.setTag(tag);
			ret.put("tag", tg);
			return ret;
		}
		 url="http://tag.share.com/tag/save?tag="+tag;
		 jsonData=client.doGet(url);
		 id=Integer.parseInt(jsonData);
		//id = tagDao.save(new Tag(tag));
		if(id != 0) {
			Tag tg = new Tag();
			tg.setId(id);
			tg.setTag(tag);
			ret.put("tag", tg);
		}
		return ret;
	}
	
//	@Transactional
	public Map<String, Object> newTags(List<Tag> tags) throws Exception {
				
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Tag> taglist = new ArrayList<Tag>();
		ret.put("tags", taglist);
		
		if(tags == null || tags.size() == 0) {
			ret.put("status", Property.SUCCESS_TAG_CREATE);
			return ret;
		}
		
		for(Tag tag: tags) {
			String status = check(tag.getTag());
			if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
				return ret;
			}
			String url="http://tag.share.com/tag/getTagID?tag="+tag.getTag();
			String jsonData=client.doGet(url);
			Integer id=Integer.parseInt(jsonData);
			//Integer id = tagDao.getTagID(tag.getTag());
			if(id != null&&id!=0) {
				Tag tg = new Tag();				
				tg.setId(id);
				tg.setTag(tag.getTag());
				taglist.add(tg);
				continue;
			}
			 url="http://tag.share.com/tag/save";
			 String json=ObjectUtil.mapper.writeValueAsString(tag);
			 jsonData=client.doPostJson(url, json);
			 id=Integer.parseInt(jsonData);			
			if(id != null) {
				Tag tg = new Tag();	
				tg.setId(id);
				tg.setTag(tag.getTag());
				taglist.add(tg);
				//index tag
				//tagIndexService.add(tg);
			}
		}
		ret.put("status", Property.SUCCESS_TAG_CREATE);
		return ret;
	}
	/**
	 * 获取标签的ID
	 * 请求url：http://tag.share.com/tag/getID
	 * 请求方式：get
	 * 请求参数：String tag
	 * 返回值 int
	 * @param tag
	 * @return
	 * @throws Exception 
	 */
	public int getID(String tag) throws Exception{
		String url = "http://tag.share.com/tag/getTagID?tag="+tag;
		
		String doGet = client.doGet(url);
		return Integer.parseInt(doGet)/*tagDao.getTagID(tag)*/;
	}
	/**
	 * 搜索标签
	 * 请求url：http://tag.share.com/tag/searchTag
	 * 请求方式 ：get
	 * 请求参数：String term
	 * 返回数据：List<tag>
	 * @param term
	 * @return
	 * @throws Exception 
	 * 此方法未完成
	 */
	public List<Tag> searchTag(String term) throws Exception {
		List<Event> listE=indexSearch.findByTitleOrContent(term, 0, 20);
		List<Tag> listT=new ArrayList<>();
		for (Event event : listE) {
			Tag tag=new Tag();
			tag.setCover(event.getTitle());
			tag.setTag(event.getSummary().substring(0,5));
		}
		
		return listT;
	}
	
	/**
	 * 获取有tag的event
	 * 需重构，迁移到feed或event
	 * @param tag
	 * @return
	 * @throws Exception 
	 */
	public List<Event> getWithTag(String tag) throws Exception {
		List<Event> events = eventService.getEventsWithRelations(relationService.getRelationsWithTag(tag));
		feedService.addUserInfo(events);
		return events;
	}
	
	
	/**
	 * 获取推荐tag
	 * 简单实现，获取有cover的tag
	 * 请求url：http://tag.share.com/tag/getRecommendTags
	 * 请求参数：int user_id
	 * 返回数值： List<Tag>
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public List<Tag> getRecommendTags(int user_id) throws Exception{
		String url ="http://tag.share.com/tag/getTagsHasCover";
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("user_id", user_id);
		String jsonData = client.doGet(url, map);
		//TODO解析listjson编程对象List<Product>
		List<Tag> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Tag.class));
		}

		return pList/*tagDao.getTagsHasCover()*/;
	}
	/**
	 * 通过id获取标签
	 * 请求url：http://tag.share.com/tag/getTagByID
	 * 请求方式：get
	 * 请求参数：int id
	 * 返回Tag
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Tag getTagByID(int id) throws Exception {
		String url="http://tag.share.com/tag/getTagByID?id="+id;
		
		String jsonData = client.doGet(url);
		Tag tag=ObjectUtil.mapper.readValue(jsonData, Tag.class);
		return tag/*tagDao.getTagByID(id)*/;
	}
	/**
	 * 通过IDList来获取标签
	 * 请求url：http://tag.share.com/tag/getTagsByIDs
	 * 请求方式：post
	 * 请求参数：List<Integer> ids
	 * 返回：List<Tag>
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	public List<Tag> getTagsByIDs(List<Integer> ids) throws Exception {
		
		
		
		List<String> ids_str = new ArrayList<String>();
		for(int i=0; i<ids.size(); i++) {
			ids_str.add(String.valueOf(ids.get(i)));
		}
		
		String url="http://tag.share.com/tag/getTags";
		String jsonData=ObjectUtil.mapper.writeValueAsString(ids_str);
		String json= client.doPostJson(url, jsonData);
		List<Tag> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(json);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Tag.class));
		}
		return pList/*tagDao.getTags(ids_str)*/;
	}
}
