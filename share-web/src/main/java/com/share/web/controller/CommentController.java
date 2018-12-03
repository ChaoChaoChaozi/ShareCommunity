package com.share.web.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.share.pojo.Comment;
import com.share.pojo.Notification;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.Property;
import com.share.web.service.CommentService;
import com.share.web.service.NotificationService;
import com.share.web.service.PostService;
import com.share.web.service.UserService;


@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	@Qualifier("commentService")
	private CommentService commentService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;
	 
	@Autowired
	@Qualifier("postService")
	private PostService postService;
	
	@ResponseBody
	@RequestMapping("/{id}")
	public Map<String, Object> comment(@PathVariable("id") int id) throws Exception {
		Comment comment = commentService.findCommentByID(id);
		Map<String, Object> ret = new HashMap<String, Object>();
		if(comment == null) {
			ret.put("status", Property.ERROR);
		}else {
			ret.put("status", Property.SUCCESS);
			ret.put("comment", comment);
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	public Map<String, String> createComment(@RequestParam("comment_object_type") int comment_object_type,
											 @RequestParam("comment_object_id") int comment_object_id,
											 @RequestParam("comment_content") String comment_content,
											 @RequestParam("comment_parent") int comment_parent,
											 HttpSession session) throws Exception {
		User user = (User)session.getAttribute("user");
		User comment_parent_author = new User();
		if(comment_parent !=0 ){
			try {
				comment_parent_author = commentService.getCommentAuthor(comment_parent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Map<String, String> ret = commentService.newComment(comment_object_type, 
															comment_object_id, 
															user.getId(), 
															user.getUser_name(), 
															comment_content, 
															comment_parent,
															comment_parent_author.getId(),
															comment_parent_author.getUser_name());
		Notification notification =  new Notification(Dic.NOTIFY_TYPE_COMMENT,
													  Integer.parseInt(ret.get("id")),
													  comment_object_type,
													  comment_object_id,
													  userService.getAuthor(comment_object_type, comment_object_id).getId(),
													  user.getId()
													  );
		
		
		if(comment_parent!=0) {
			//reply notification
			notification.setNotify_type(Dic.NOTIFY_TYPE_COMMENT_REPLY);
			notification.setNotified_user(comment_parent_author.getId());
			notificationService.doNotify(notification);
		} else {
			//comment notification
			notificationService.doNotify(notification);
		}
		
		
		ret.put("avatar", userService.findById(user.getId()).getUser_avatar());
		ret.put("author_id", String.valueOf(user.getId()));
		ret.put("author_name", user.getUser_name());
		ret.put("reply_to_author", String.valueOf(comment_parent_author.getId()));
		ret.put("reply_to_authorname", comment_parent_author.getUser_name());
		return ret;
	}
	
	@RequestMapping(value="/{type}/{id}")
	public String getComments(@PathVariable("type") String type, @PathVariable("id") int id,Model model) throws Exception {
		
		model.addAttribute("comments", commentService.getComments(type, id));
		return "comment/index";
	}
	
	/**
	 * feed附属的comments
	 * @param type
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/attach/{type}/{id}")
	public String getAttachComments(@PathVariable("type") String type, @PathVariable("id") int id,Model model) throws Exception {
		
		model.addAttribute("comments", commentService.getComments(type, id, 0, 5));
		return "comment/attach_comments";
	}
	
}
