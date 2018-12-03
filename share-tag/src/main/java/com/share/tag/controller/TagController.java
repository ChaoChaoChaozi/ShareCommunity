package com.share.tag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.tag.mapper.TagMapper;
import com.share.tag.pojo.Tag;
import com.share.util.ObjectUtil;

@RestController
public class TagController {
	@Autowired
	private TagMapper tagMapper;

	@RequestMapping("tag/getTagID")
	public Integer getTagID(String tag) {
		Integer id=tagMapper.queryTagID(tag);
		if(id==null){
			return 0;
		}
		return id;
	}
	@RequestMapping("tag/save")
	public Integer save(@RequestBody String jsonData) throws Exception{
		
		Tag tag = ObjectUtil.mapper.readValue(jsonData, Tag.class);
		int id=tagMapper.saveTag(tag);
		return id;
		
	}
	@RequestMapping("tag/getTagsHasCover")
	public List<Tag> getTagsHasCover(){
		List<Tag> list=tagMapper.getTags();
		return list;
		
	}
	@RequestMapping("tag/getTagByID")
	public Tag  getTagByID(int id){
		Tag tag=tagMapper.queryTag(id);
		return tag;
	}
	@RequestMapping("tag/getTags")
	public List<Tag> getTags(@RequestBody String jsonData) throws Exception{
		List<String> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, String.class));
		}
		
		List<Tag> list=tagMapper.queryTags(pList);
		
		return list;
		
	}
	

	
	
}
