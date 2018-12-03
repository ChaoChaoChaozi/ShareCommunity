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
import com.share.web.service.EventService;
import com.share.web.service.FeedService;
import com.share.web.service.FollowService;
import com.share.web.service.InterestService;
import com.share.web.service.TagService;
import com.share.web.service.UserService;

@Controller
public class HomePage {

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

	@Autowired
	@Qualifier("tagService")
	private TagService tagService;

	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;

	@RequestMapping("/")
	public String showHomePage(HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "welcome";
		}
		try {
			model.addAttribute("counter", userService.getCounterOfFollowAndShortPost(user.getId()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Event> feeds = null;
		try {
			feeds = feedService.getFeeds(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("feeds", feeds);

		model.addAttribute("dic", new Dic());
		return "index";

	}

	@RequestMapping("/popup_usercard/{user_id}")
	public String getPopupUserCard(@PathVariable("user_id") String user_id, Model model)
			throws NumberFormatException, Exception {
		User user = userService.findById(Integer.valueOf(user_id));
		if (user != null) {
			model.addAttribute("u", user);
			try {
				model.addAttribute("counter", userService.getCounterOfFollowAndShortPost(Integer.valueOf(user_id)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "popup_usercard";
	}

	@RequestMapping("/page/{num}")
	public String nextPage(@PathVariable("num") String num_str, HttpSession session, Model model) {
		System.out.println(num_str);

		User user = (User) session.getAttribute("user");
		if (user == null) {
			return null;
		}

		int num = Integer.parseInt(num_str);
		List<Event> feeds = null;
		try {
			feeds = feedService.getFeedsOfPage(user.getId(), num);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("feeds", feeds);
		model.addAttribute("dic", new Dic());
		return "nextpage";
	}

	@RequestMapping("/welcome")
	public String welcome(HttpSession session, Model model) throws Exception {
		if (session.getAttribute("user") != null) {
			return "index";
		}

		List<Tag> tags_recommend = tagService.getRecommendTags(0);
		model.addAttribute("tags", tags_recommend);
		model.addAttribute("dic", new Dic());
		return "welcome";
	}

	@RequestMapping("/sidebar")
	public String sideBar(HttpSession session, Model model) throws Exception {
		// ModelAndView mav = new ModelAndView();
		// model.setViewName("sidebar");
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "sidebar";
		}

		List<User> rec_users = userService.getRecommendUsers(user == null ? 0 : user.getId(), 9);
		try {
			model.addAttribute("isFollowings", followService.isFollowing(user == null ? 0 : user.getId(), rec_users));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("popusers", rec_users);

		List<Tag> tags_recommend = tagService.getRecommendTags(user == null ? 0 : user.getId());
		model.addAttribute("poptags", tags_recommend);

		return "sidebar";
	}

	@RequestMapping("/guide")
	public String guide(HttpSession session, Model model) throws Exception {
		// ModelAndView mav = new ModelAndView();
		// mav.setViewName("guide");

		User user = (User) session.getAttribute("user");

		List<Tag> tags_recommend = tagService.getRecommendTags(user == null ? 0 : user.getId());
		model.addAttribute("tags", tags_recommend);
		try {
			model.addAttribute("isInterests",
					interestService.hasInterestInTags(user == null ? 0 : user.getId(), tags_recommend));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("dic", new Dic());
		return "guide";
	}

	/**
	 * 新用户兴趣选择之后 feed初始化
	 * 
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/guide/ok")
	public Map<String, Object> guideOk(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		try {
			feedService.coldStart(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	@RequestMapping("/followers")
	public String getFollowers(HttpSession session, Model model) {
		// ModelAndView mav = new ModelAndView();
		try {
			model.addAttribute("followers", userService
					.findAllbyIDs(followService.getFollowerIDs(((User) session.getAttribute("user")).getId())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mav.setViewName("follower");
		return "follower";
	}

	@RequestMapping("/followings")
	public String getFollowings(HttpSession session, Model model) {
		// ModelAndView mav = new ModelAndView();
		try {
			model.addAttribute("followings", userService
					.findAllbyIDs(followService.getFollowingIDs(((User) session.getAttribute("user")).getId())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mav.setViewName("following");
		return "following";
	}

	@RequestMapping("/404")
	public String pageNotFound() {
		return "404";
	}

	@RequestMapping("/500")
	public String error500() {
		return "500";
	}
}
