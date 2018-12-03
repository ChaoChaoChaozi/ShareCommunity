package com.share.web.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.share.pojo.Notification;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.web.service.FollowService;
import com.share.web.service.NotificationService;
import com.share.web.service.UserService;



@Controller
@RequestMapping("/follow")
public class FollowController {
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;
	
	
	@ResponseBody
	@RequestMapping("/{following_user_id}")
	public Map<String, Object> follow(@PathVariable("following_user_id") int following_user_id,
									  HttpSession session) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = null;
		try {
			map = followService.newFollowing(user.getId(), 
																 user.getUser_name(), 
																 following_user_id, 
																 userService.findById(following_user_id).getUser_name());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Notification notification = new Notification(Dic.NOTIFY_TYPE_FOLLOW, 
												     0, 
												     Dic.OBJECT_TYPE_USER, 
												     following_user_id, 
												     following_user_id, 
												     user.getId());
		try {
			notificationService.doNotify(notification);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/undo/{following_user_id}")
	public Map<String, Object> undoFollow(@PathVariable("following_user_id") int following_user_id,
							   HttpSession session) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = null;
		try {
			map = followService.undoFollow(user.getId(),following_user_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	
}
