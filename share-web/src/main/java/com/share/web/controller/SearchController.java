package com.share.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.share.pojo.Event;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.web.service.EventService;
import com.share.web.service.FeedService;
import com.share.web.service.FollowService;
import com.share.web.service.InterestService;
import com.share.web.service.TagService;
import com.share.web.service.UserService;



@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@RequestMapping("/feed")
	public String searchFeed(@RequestParam("term") String term, HttpSession session,Model model) {
		System.out.println(term);
		User me = (User) session.getAttribute("user");
		model.addAttribute("feeds", feedService.getFeedsByTitleOrContentContains(term));
		model.addAttribute("dic", new Dic());
		model.addAttribute("term", term);
		return "search/feed";
	}
	
	@RequestMapping("/feed/page/{num}")
	public String searchFeedOfPage(@PathVariable("num") int num, 
										 @RequestParam("term") String term,
										 HttpSession session,Model model) {
		System.out.println(term);
		
		User me = (User) session.getAttribute("user");

		model.addAttribute("feeds", feedService.getFeedsByTitleOrContentContains(me==null?0:me.getId(), term, num));
		model.addAttribute("dic", new Dic());
		model.addAttribute("term", term);
		return "nextpage";
	}	
	
	@RequestMapping("/tag")
	public String searchTag(@RequestParam("term") String term, HttpSession session,Model model) throws Exception {
		User me = (User) session.getAttribute("user");
		
		List<Tag> tags = tagService.searchTag(term);
		model.addAttribute("tags", tags);
		Map<Integer, List<Event>> feeds = new TreeMap<Integer, List<Event>>();
		try {
			model.addAttribute("isInterests", interestService.hasInterestInTags(me==null?0:me.getId(), tags));
			for(Tag tag: tags){
				feeds.put(tag.getId(), feedService.getFeedsByTag(0, tag.getId(), 3));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		model.addAttribute("feeds", feeds);
		model.addAttribute("term", term);
		model.addAttribute("dic", new Dic());
		return "search/tag";
	}

	@RequestMapping("/user")
	public String searchUser(@RequestParam("term") String term, HttpSession session,Model model) throws Exception {
		User me = (User) session.getAttribute("user");
		
		List<User> users = userService.searchUserByName(term);
		Map<User, List<Event>> feeds = new HashMap<User, List<Event>>();
		try {
			model.addAttribute("isFollowings", followService.isFollowing(me==null?0:me.getId(), users));
			if(users!=null)
			for(User user: users){
				feeds.put(user, eventService.getEventsOfUser(user.getId(), 3));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		model.addAttribute("feeds", feeds);		
		model.addAttribute("term", term);
		model.addAttribute("dic", new Dic());
		return "search/user";
	}
	
}
