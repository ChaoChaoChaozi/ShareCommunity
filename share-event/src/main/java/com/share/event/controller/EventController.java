package com.share.event.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.event.mapper.EventMapper;
import com.share.event.pojo.Event;
import com.share.util.ObjectUtil;

@RestController
public class EventController {
	@Autowired
	private EventMapper eventMapper;

	@RequestMapping("/event/save")
	public int save(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException{
		Event event=com.share.util.ObjectUtil.mapper.readValue(json, Event.class);
		return eventMapper.save(event);
	}
	@RequestMapping("/event/getEventsWithRelations")
	public List<Event> getEventsWithRelations(@RequestBody String category) throws Exception{
		List<String> events = new ArrayList<>();
		JsonNode data=ObjectUtil.mapper.readTree(category);
		if(data.isArray()&&data.size()>0){
			events = ObjectUtil.mapper.readValue(data.traverse(),
					ObjectUtil.mapper.getTypeFactory().
					constructCollectionType(List.class, String.class));
		}
		Map<Integer, List<Integer>> category1 = new HashMap<Integer, List<Integer>>();
		for (String str : events) {
			
			int key=Integer.parseInt(str.split("#")[0]);
			String value=str.split("#")[1];
			System.out.println(key+","+value);
			List<Integer> e= new ArrayList<>();
			JsonNode d=ObjectUtil.mapper.readTree(value);
			if(d.isArray()&&d.size()>0){
				e = ObjectUtil.mapper.readValue(d.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Integer.class));
			}
			category1.put(key, e);
		}
		if( category1 == null ||  category1.size() == 0) {
			return new ArrayList<Event>();
		}
		System.out.println(category1);
		List<Event> events1=eventMapper.getEventsWithRelations(category1);
		return events1;
	}
	@RequestMapping("/event/getEventsHasPhoto")
	public List<Event> getEventsHasPhoto(int start, int step) throws JsonParseException, JsonMappingException, IOException{
		System.out.println(start+","+step);
		return eventMapper.getEventsHasPhoto(start,step);
	}
	@RequestMapping("/event/getEvents")
	public List<Event> getEvents(int start, int step) throws JsonParseException, JsonMappingException, IOException{
		List<Event> events = eventMapper.getEvents(start,step);
		System.out.println(events);
		return events;
	}
	@RequestMapping("/event/getEventsWithIDs")
	public List<Event> getEventsWithIDs(@RequestBody String jsonData) throws Exception{
		List<Integer> events = new ArrayList<>();
		System.out.println(jsonData);
		JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			events = ObjectUtil.mapper.readValue(data.traverse(),
					ObjectUtil.mapper.getTypeFactory().
					constructCollectionType(List.class, Integer.class));
		}
		
		System.out.println("/event/getEventsWithIDs");
		List<Event> list=eventMapper.getEventsWithIDs(events);
		System.out.println(list);
		
		
		return list;
	}
	@RequestMapping("/event/getEventsOfUser")
	public List<Event> getEventsOfUser(int user_id, int num) {
		return eventMapper.getEventsOfUser(user_id,num);
	}
	@RequestMapping("/event/getEvent")
	public Event getEvent(int object_type, int object_id) {
		return eventMapper.getEvent(object_type,object_id);
	}
	@RequestMapping("/event/delete")
	public int delete(int id) {
		return eventMapper.delete(id);
	}
	@RequestMapping("/event/delete2")
	public int deleteByObject(int object_type, int object_id) {
		return eventMapper.deleteByObject(object_type,object_id);
	}
}
