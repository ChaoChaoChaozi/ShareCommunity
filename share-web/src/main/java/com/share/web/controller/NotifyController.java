package com.share.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.pojo.User;
import com.share.util.Dic;
import com.share.web.service.NotificationService;

@Controller
@RequestMapping("/notifications")
public class NotifyController {
	
	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;
	
	
	@RequestMapping("/comment")
	public String comment(HttpSession session,Model model) {
		
		User user = (User) session.getAttribute("user");
		model.addAttribute("dic", new Dic());
		try {
			model.addAttribute("notis", notificationService.getNotifications(user.getId(), Dic.NOTIFY_TYPE_COMMENT));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "notifications/comment";
	}
	
	@RequestMapping("/like")
	public String like(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("dic", new Dic());
		try {
			model.addAttribute("notis", notificationService.getNotifications(user.getId(), Dic.NOTIFY_TYPE_LIKE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "notifications/like";
	}
	
	@RequestMapping("/follow")
	public String follow(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("dic", new Dic());
		try {
			model.addAttribute("notis", notificationService.getNotifications(user.getId(), Dic.NOTIFY_TYPE_FOLLOW));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "notifications/follow";
	}

	@RequestMapping("/system")
	public String system(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("dic", new Dic());
		try {
			model.addAttribute("notis", notificationService.getNotifications(user.getId(), Dic.NOTIFY_TYPE_SYSTEM));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "notifications/system";
	}
	
}
