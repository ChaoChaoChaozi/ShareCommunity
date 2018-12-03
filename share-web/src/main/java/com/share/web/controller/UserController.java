package com.share.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.pojo.Album;
import com.share.pojo.Post;
import com.share.pojo.User;
import com.share.web.service.AlbumService;
import com.share.web.service.FollowService;
import com.share.web.service.PostService;
import com.share.web.service.UserService;



@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("postService")
	private PostService postService;
	
	@Autowired
	@Qualifier("albumService")
	private AlbumService albumService;
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@RequestMapping("/{id}")
	public String collection(@PathVariable("id") int id, HttpSession session,Model model) throws Exception {	
		User me = (User) session.getAttribute("user");
		
		
		User user = userService.findById(id);
		model.addAttribute("u", user);
		try {
			model.addAttribute("follow", followService.isFollowing(me==null?0:me.getId(), id));
			model.addAttribute("counter", userService.getCounterOfFollowAndShortPost(user.getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Post> posts = postService.findPostsOfUser(id);
		model.addAttribute("posts", posts);
		List<Album> albums = albumService.getAlbumsOfUser(id);
		model.addAttribute("albums", albums);
		
		return "/user/index";
	}
}
