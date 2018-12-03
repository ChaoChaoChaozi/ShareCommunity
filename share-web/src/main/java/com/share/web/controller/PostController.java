package com.share.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.share.pojo.Post;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.Property;
import com.share.web.service.EventService;
import com.share.web.service.FeedService;
import com.share.web.service.FollowService;
import com.share.web.service.InterestService;
import com.share.web.service.LikeService;
import com.share.web.service.PostService;
import com.share.web.service.RelationService;
import com.share.web.service.TagService;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	@Qualifier("postService")
	private PostService postService;

	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;

	@Autowired
	@Qualifier("tagService")
	private TagService tagService;

	@Autowired
	@Qualifier("eventService")
	private EventService eventService;

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;

	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;

	@Autowired
	@Qualifier("followService")
	private FollowService followService;

	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;

	@RequestMapping("/{id}")
	public String post(@PathVariable("id") int id, HttpSession session, Model model) throws Exception {
		User me = (User) session.getAttribute("user");

		User author = postService.getAuthorOfPost(id);
		model.addAttribute("u", author);
		try {
			model.addAttribute("follow", followService.isFollowing(me == null ? 0 : me.getId(), author.getId()));
			model.addAttribute("is_like", likeService.isLike(me == null ? 0 : me.getId(), Dic.OBJECT_TYPE_POST, id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			model.addAttribute("post", postService.findPostByID(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "post/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createPost() {
		return "post/create";
	}

	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, Object> createPost(@RequestParam("title") String title, @RequestParam("content") String content,
			@RequestParam("post_status") int post_status, @RequestParam("comment_status") int comment_status,
			@RequestParam("tags") String param_tags, HttpSession session) {

		User user = (User) session.getAttribute("user");
		String post_cover = (String) session.getAttribute("post_cover");
		session.removeAttribute("post_cover");
		// 1 save post
		Map<String, Object> map = null;
		try {
			map = postService.newPost(user.getId(), title, content, post_status, comment_status, param_tags,
					post_cover);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = (String) map.get("status");
		Post post = (Post) map.get("post");

		List<Integer> followers = null;
		// 2 add event
		if (Property.SUCCESS_POST_CREATE.equals(status)) {
			int event_id = 0;
			try {
				event_id = eventService.newEvent(Dic.OBJECT_TYPE_POST, post);
				followers = followService.getFollowerIDs(user.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 3 push to followers
			if (followers==null) {
				followers=new ArrayList<>();
			}
			followers.add(user.getId());
			feedService.push(followers, event_id);

			// 4 push to users who follow the tags in the post
			List<Tag> tags = (ArrayList<Tag>) map.get("tags");
			// push to users who follow the tags
			Set<Integer> followers_set = new HashSet<Integer>();
			for (Tag tag : tags) {
				List<Integer> i_users = null;
				try {
					i_users = interestService.getUsersInterestedInTag(tag.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(i_users==null){
					i_users=new ArrayList<>();
				}
				for (Integer u : i_users) {
					if (u != user.getId())
						followers_set.add(u);
				}

				// cache feeds to tag list
				feedService.cacheFeed2Tag(tag.getId(), event_id);
			}
			feedService.push(new ArrayList<Integer>(followers_set), event_id);

		}
		return map;

	}

	@ResponseBody
	@RequestMapping("/delete/{id}")
	public Map<String, Object> deletePost(@PathVariable("id") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			postService.deletePost(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("status", Property.SUCCESS_POST_DELETE);
		return map;
	}

}
