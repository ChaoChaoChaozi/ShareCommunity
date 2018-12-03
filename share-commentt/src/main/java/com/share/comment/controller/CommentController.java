package com.share.comment.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.comment.mapper.CommentMapper;
import com.share.pojo.Comment;
import com.share.pojo.User;
import com.share.util.ObjectUtil;

@RestController
public class CommentController {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@RequestMapping("/comment/save_newComment")
	public String save_newComment(@RequestBody String jsonComment) throws Exception{
		Comment comment=ObjectUtil.mapper.readValue(jsonComment, Comment.class);
		return commentMapper.newComment(comment)+"";
	}
	@RequestMapping("/comment/getCommentByID/{id}")
	public Comment getCommentByID(@PathVariable int id) throws Exception{
		return commentMapper.getCommentByID(id);
	}
	@RequestMapping("/comment/getCommentsOfPost/{id}/{offset}/{count}")
	public List<Comment> getCommentsOfPost(@PathVariable int id,@PathVariable int offset,@PathVariable int count) throws Exception{
		System.out.println(id+","+offset+","+count);
		return commentMapper.getCommentsOfPost(id,offset,count);
	}
	@RequestMapping("/comment/getCommentsOfPhoto/{id}/{offset}/{count}")
	public List<Comment> getCommentsOfPhoto(@PathVariable int id,@PathVariable int offset,@PathVariable int count) throws Exception{
		return commentMapper.getCommentsOfPhoto(id,offset,count);
	}
	@RequestMapping("/comment/getCommentsOfAlbum/{id}/{offset}/{count}")
	public List<Comment> getCommentsOfAlbum(@PathVariable int id,@PathVariable int offset,@PathVariable int count) throws Exception{
		return commentMapper.getCommentsOfAlbum(id,offset,count);
	}
	@RequestMapping("/comment/getCommentsOfShortPost/{id}/{offset}/{count}")
	public List<Comment> getCommentsOfShortPost(@PathVariable int id,@PathVariable int offset,@PathVariable int count) throws Exception{
		return commentMapper.getCommentsOfShortPost(id,offset,count);
	}
	@RequestMapping("/comment/getCommentAuthor/{comment_id}")
	public User getCommentAuthor(@PathVariable int comment_id) throws Exception{
		System.out.println("getCommentAuthor"+comment_id);
		Map<String, Object> comment = commentMapper.getCommentAuthor(comment_id);
		System.out.println(comment);
		User user = new User();
		user.setId((Integer)(comment.get("comment_author")));
		user.setUser_name((String)comment.get("comment_author_name"));
		return user;
	}
	@RequestMapping("/comment/commentsCount/{object_type}/{object_id}")
	public Integer commentsCount(@PathVariable int object_type,@PathVariable int object_id) throws Exception{
		return commentMapper.commentsCount(object_type,object_id);
	}
	
	
	
	
}
