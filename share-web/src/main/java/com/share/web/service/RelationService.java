package com.share.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Relation;
import com.share.pojo.Tag;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.vo.HttpResult;


@Service
public class RelationService {
	
	public static final int RELATION_TYPE_POST = 0;
	public static final int RELATION_TYPE_PHOTO = 1;
	public static final int RELATION_TYPE_ALBUM = 2;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private HttpClientService client;
	
	public Map<String, Object> newRelation(int object_type, int object_id, int tag_id) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String ,Object> map=new HashMap<String, Object>();
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		map.put("tag_id", tag_id);
		String url="http://relation.share.com/relation/save";
//		int id = relationDao.save(new Relation(object_type, object_id, tag_id));
		HttpResult result=client.doPost(url, map);
		String ids=result.getBody();
		
		Integer id=Integer.parseInt(ids);
		if(id != 0){
			Relation relation = new Relation();
			relation.setId(id);
			relation.setObject_type(object_type);
			relation.setObject_id(object_id);
			relation.setTag_id(tag_id);
			ret.put("relation", relation);
			ret.put("status", Property.SUCCESS_RELATION_CREATE);
		} else {
			ret.put("status", Property.ERROR_RELATION_CREATE);
		}
		return ret;
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 * @throws Exception 
	 */
	public List<Relation> getRelationsWithTag(String tag) throws Exception{
		List<Relation> relations = new ArrayList<Relation>();
		/*int tag_id = tagService.getID(tag);
		if(tag_id != 0) {
			//List<Relation> rels = relationDao.get(tag_id);
			if(rels != null)
				relations = rels;
		}*/
		
		String url="http://relation.share.com/relation/get?tag_id="+3;
		String jsondata=client.doGet(url);
		JsonNode data=ObjectUtil.mapper.readTree(jsondata);
		if(data.isArray()&&data.size()>0){
			relations = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Relation.class));
		}	
		
		return relations;
	}
	
	/**
	 * 获取有列表中tag的关联关系
	 * 
	 * @param tags
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public List<Relation> getRelationsInTags(List<Tag> tags) throws Exception{
		List<Relation> list=new ArrayList<Relation>();
		if(tags == null || tags.size() == 0)
			return new ArrayList<Relation>();
		
		List<String> tag_ids = new ArrayList<String>();
		for(Tag tag : tags) {
			tag_ids.add(String.valueOf(tag.getId()));
		}
		String url="http://relation.share.com/relation/getRelationsInTags";
		String json = ObjectUtil.mapper.writeValueAsString(tag_ids);
		String jsondata=client.doPostJson(url, json);
		JsonNode data=ObjectUtil.mapper.readTree(jsondata);
		if(data.isArray()&&data.size()>0){
			list = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Relation.class));
		}
		return list;
				/*relationDao.getRelationsInTags(tag_ids)*/
	}
	
}
