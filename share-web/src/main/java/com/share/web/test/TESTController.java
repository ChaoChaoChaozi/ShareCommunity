package com.share.web.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.share.pojo.Notification;
import com.share.util.Dic;
import com.share.web.service.NotificationService;

@RestController
public class TESTController {
	@Autowired
	private NotificationService service;
	@RequestMapping("/test1")
	public Integer test01() throws JsonProcessingException{
		Notification notification = new Notification();
		notification.setId(1);
		notification.setNotified_user(1);
		int doNotify = service.doNotify(notification);
		return doNotify;
		
	}
	@RequestMapping("/test2")
	public String test02() throws Exception{
		Map<String, Long> notificationsCount = service.getNotificationsCount(1234);
		System.out.println("test2:"+notificationsCount);
		return notificationsCount.toString();
	}
	@RequestMapping("/test3")
	public String test03() throws Exception{
		Notification notification = new Notification();
		notification.setId(1);
		notification.setNotified_user(1);
		service.refreshNotification(notification);
		return "secceed";
	}
	@RequestMapping("/test4")
	public List<Notification> test04() throws Exception{
		Notification notification = new Notification();
		notification.setId(1);
		notification.setNotified_user(1);
		List<Notification> notifications = service.getNotifications(1, 3);
		return notifications;
	}
}
