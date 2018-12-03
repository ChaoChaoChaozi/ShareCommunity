package com.share.notification.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.notification.service.NotificationService;
import com.share.pojo.Notification;
import com.share.util.ObjectUtil;

@RestController
public class NotificationController {

	@Autowired
	private NotificationService service;
	
	@RequestMapping("notification/save")
	public void save(@RequestBody String jsonData){
		try {
			service.save(jsonData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("notification/getNotificationsCount/{user_id}") 
	public Map<String,Long> getNotificationsCount(@PathVariable("user_id")Integer user_id){
	//	List<Map<String,Long>>  list=new ArrayList<>();
		Map<String, Long> notificationsCount = service.getNotificationsCount(user_id);
	//	list.add( notificationsCount);
		return notificationsCount;
	}
	
	@RequestMapping("notification/refreshNotification")
	public void refreshNotification(@RequestBody String notification){
		Notification readValue = null;
		try {
			readValue = ObjectUtil.mapper.readValue(notification, Notification.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.refreshNotification(readValue);
	}
	
	@RequestMapping("notification/getNotificationsOfTypes/{user_id}")
	public List<Notification> getNotificationsOfTypes(@PathVariable("user_id")Integer user_id,@RequestBody String list) throws  Exception{
		List<Integer> notify_types=null;
		JsonNode data = ObjectUtil.mapper.readTree(list);
		if (data.isArray() && data.size() > 0) {
			notify_types = ObjectUtil.mapper.readValue(data.traverse(),
					ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
		}
		
		return service.getNotificationsOfTypes(user_id,notify_types);
	}
	@RequestMapping("notification/getNotificationsOfType")
	public List<Notification> getNotificationsOfType(Integer user_id,Integer notify_type) throws  Exception{
	
		return service.getNotificationsOfType(user_id,notify_type);
	}
	
	
}
