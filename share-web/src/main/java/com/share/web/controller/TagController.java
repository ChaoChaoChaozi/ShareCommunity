package com.share.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.share.pojo.Event;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.Property;
import com.share.web.service.FeedService;
import com.share.web.service.InterestService;
import com.share.web.service.TagService;



@Controller
@RequestMapping("/tag")
public class TagController {

	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@RequestMapping("/{tag_id}")
	public String getFeedsByTag(@PathVariable("tag_id") int tag_id, HttpSession session,Model model) throws Exception {

		
		
		
		Tag tag = tagService.getTagByID(tag_id);
		if(tag == null) {
			
			return "404";
		}
		
		model.addAttribute("tag", tag.getTag());
		model.addAttribute("id", tag.getId());
		
		User user = (User)session.getAttribute("user");
		if(user != null) {
			try {
				model.addAttribute("isInterest", 
							  interestService.hasInterestInTag(user.getId(), tag_id));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			model.addAttribute("isInterest", false);
		}
		
		List<Event> feeds = null;
		try {
			feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag_id, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("feeds", feeds);
		model.addAttribute("dic", new Dic());
		return "tag/index";
	}
	
	@RequestMapping("/{tag_id}/page/{page}")
	public String getFeedsByTagOfPage(@PathVariable("tag_id") int tag_id, 
											@PathVariable("page") int page,
											HttpSession session,Model model) throws Exception {

		
		
		
		Tag tag = tagService.getTagByID(tag_id);
		if(tag == null) {
			
			return "404";
		}
		
		User user = (User) session.getAttribute("user");
		
		List<Event> feeds = null;
		try {
			feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag_id, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("feeds", feeds);
		model.addAttribute("dic", new Dic());
		return "nextpage";
	}
	
	/**
	 * 对某个标签感兴趣
	 */
	@ResponseBody
	@RequestMapping("/{tag_id}/interest")
	public Map<String, Object> interest(@PathVariable("tag_id") int tag_id, HttpSession session) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		User user = (User) session.getAttribute("user");
		try {
			interestService.interestInTag(user.getId(), tag_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		ret.put("status", Property.SUCCESS_INTEREST);
		return ret;
	}
	
	
	/**
	 * 
	 */
	@ResponseBody
	@RequestMapping("/{tag_id}/undointerest")
	public Map<String, Object> undoInterest(@PathVariable("tag_id") int tag_id, HttpSession session) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		User user = (User) session.getAttribute("user");
		try {
			interestService.undoInterestInTag(user.getId(), tag_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ret.put("status", Property.SUCCESS_INTEREST_UNDO);
		return ret;
	}
	
	
	
}
