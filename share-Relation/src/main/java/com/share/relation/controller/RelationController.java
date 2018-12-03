package com.share.relation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.relation.mapper.RelationMapper;
import com.share.relation.pojo.Relation;
import com.share.util.ObjectUtil;

@RestController
public class RelationController {
	@Autowired
	private RelationMapper relationMapper;
	@RequestMapping("relation/save")
	public Integer save(int object_type, int object_id, int tag_id) {
		Integer result=relationMapper.save(new Relation(object_type,  object_id, tag_id));
		
		return result;
	}

	@RequestMapping("relation/delete")
	public Integer delete() {
		return relationMapper.delete();
	}

	@RequestMapping("relation/get")
	public List<Relation> get(Integer tag_id) {
		return relationMapper.get(tag_id);
	}

	@RequestMapping("relation/getRelationsInTags")
	public List<Relation> getRelationsInTags(@RequestBody String jsonData) throws Exception {
		List<String> tag_ids=new ArrayList<>();
		JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			tag_ids = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
		}
		return relationMapper.getRelationsInTags(tag_ids);

	}

}
