package com.share.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.share.pojo.ShortPost;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.web.service.EventService;
import com.share.web.service.FeedService;
import com.share.web.service.FollowService;
import com.share.web.service.ShortPostService;
import com.share.web.service.UserService;



@Controller
@RequestMapping("/spost")
public class ShortPostController {

	@Autowired
	@Qualifier("shortPostService")
	private ShortPostService shortPostService;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Map<String, Object> createPost(@RequestParam("content") String content,
										  HttpSession session) throws Exception{
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = shortPostService.newPost(user.getId(), content);
		
		ShortPost spost = (ShortPost) map.get("spost");	
		int event_id = 0;
		try {
			event_id = eventService.newEvent(Dic.OBJECT_TYPE_SHORTPOST, spost);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Integer> followers = new ArrayList<Integer>();
		try {
			followers = followService.getFollowerIDs(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(followers==null)followers = new ArrayList<Integer>();
		followers.add(user.getId());
		feedService.push(followers, event_id);
		
		map.put("avatar", userService.findById(user.getId()).getUser_avatar());
		map.put("author_name", user.getUser_name());
		
		return map;
	}
}
