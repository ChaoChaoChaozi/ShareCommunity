package com.share.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.share.pojo.Notification;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.Property;
import com.share.web.service.LikeService;
import com.share.web.service.NotificationService;


@Controller
@RequestMapping("/like")
public class LikeController {

	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;
	
	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;
	
	@ResponseBody
	@RequestMapping("/do")
	public Map<String, String> like(@RequestParam("author") int author,
									@RequestParam("object_type") int object_type,
					  				@RequestParam("object_id") int object_id,
					  				HttpSession session){
		User me = (User) session.getAttribute("user");
				
		try {
			likeService.like(me.getId(), object_type, object_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("status", Property.SUCCESS_LIKE);
		
		Notification notification = new Notification(Dic.NOTIFY_TYPE_LIKE, 
													 0, 
													 object_type, 
													 object_id, 
													 author, 
													 me.getId()
													 );
		try {
			notificationService.doNotify(notification);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping("/undo")
	public Map<String, String> undolike(@RequestParam("object_type") int object_type,
										@RequestParam("object_id") int object_id,
										HttpSession session){
		User me = (User) session.getAttribute("user");
		try {
			likeService.undoLike(me.getId(), object_type, object_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("status", Property.SUCCESS_LIKE_UNDO);
		return ret;
	}
	
}
