package com.share.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Comment;
import com.share.pojo.Photo;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.ObjectUtil;
import com.share.util.Property;

import ch.qos.logback.core.net.server.Client;


@Service("commentService")
public class CommentService {

	public static final int COMMENT_TYPE_POST = 0;
	public static final int COMMENT_TYPE_PHOTO = 1;
	public static final int COMMENT_TYPE_ALBUM = 2;
	
	public static final String TYPE_POST = "post";
	public static final String TYPE_PHOTO = "photo";
	public static final String TYPE_ALBUM = "album";
	public static final String TYPE_SPOST = "spost";
	
	public static final int COUNT = 10;	//默认返回comment条数
	
	/**
	 *	添加新的信息，合成新的评论吧
	 * url:http://comment.share.com/comment/save_newComment/
	 * 提交方式：post
	 * 参数：Comment comment
	 * @param comment_object_type
	 * @param comment_object_id
	 * @param comment_author
	 * @param comment_author_name
	 * @param comment_content
	 * @param comment_parent
	 * @param comment_parent_author
	 * @param comment_parent_author_name
	 * @return 返回int id 
	 * @throws Exception 
	 */
	public Map<String, String> newComment(Integer comment_object_type, Integer comment_object_id,
							 Integer comment_author, String comment_author_name, 
							 String comment_content, Integer comment_parent, 
							 int comment_parent_author, String comment_parent_author_name) throws Exception{
		
		Map<String, String> ret = new HashMap<String, String>();
		if(comment_content == null || comment_content.length() == 0) {
			ret.put("status", Property.ERROR_COMMENT_EMPTY);
			return ret;
		}
		//不支持的评论类型
		if(Dic.checkType(comment_object_type) == null){
			ret.put("status", Property.ERROR_COMMENT_TYPE);
			return ret;
		}
		Comment comment = new Comment();
		comment.setComment_object_type(comment_object_type);
		comment.setComment_object_id(comment_object_id);
		comment.setComment_author(comment_author);
		comment.setComment_author_name(comment_author_name);
		comment.setComment_content(comment_content);
		comment.setComment_parent(comment_parent);
		comment.setComment_parent_author(comment_parent_author);
		comment.setComment_parent_author_name(comment_parent_author_name);
		String url="http://comment.share.com/comment/save_newComment";
		
		String jsonComment=ObjectUtil.mapper.writeValueAsString(comment);
		
		String new_id = client.doPostJson(url,jsonComment);
		int id=Integer.parseInt(new_id);
		ret.put("status", Property.SUCCESS_COMMENT_CREATE);
		ret.put("id", String.valueOf(id));
		return ret;
		
	}
	
	
	@Autowired
	private HttpClientService client;
	/**
	 * 通过用户id查看评论信息
	 * url：http://comment.share.com/query/comment/{id}
	 * 参数：int id
	 * 提交方式：get
	 * 返回值类型 Comment comment
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Comment findCommentByID(int id) throws Exception {
		String url="http://comment.share.com/comment/getCommentByID/"+id;
		String jsonCom = client.doGet(url);
		Comment comment = ObjectUtil.mapper.readValue
				(jsonCom, Comment.class);
		return comment;
	}
	
	public List<Comment> getComments(String type, int id) throws Exception {
		return getComments(type, id, 0, COUNT);
	}
	
	
	/**
	 * 获取评论集
	 * url：http://comment.share.com/get/comments
	 * post
	 *
	 * @param type
	 * @param id
	 * @param offset
	 * @param count
	 * @return
	 */
	@Autowired
	private UserService userService;
	@SuppressWarnings("unused")
	public List<Comment> getComments(String type, int id, int offset, int count) throws Exception {
		if(type == null || type.length() == 0)
			return null;
		
		List<Comment> comments=null;
		
		if(type.equals(TYPE_POST)) {
			String url="http://comment.share.com/comment/getCommentsOfPost/"
					+id+"/"+offset+"/"+count;
			String jsonComment = client.doGet(url);
			
			JsonNode comment = ObjectUtil.mapper.readTree(jsonComment);
			if(comment.isArray()&&comment.size()>0){
				comments=ObjectUtil.mapper.readValue(comment.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Comment.class));
			}
		
		} else if(type.equals(TYPE_PHOTO)) {
			String url="http://comment.share.com/comment/getCommentsOfPhoto/"
					+id+"/"+offset+"/"+count;
			String jsonComment = client.doGet(url);
			
			JsonNode comment = ObjectUtil.mapper.readTree(jsonComment);
			if(comment.isArray()&&comment.size()>0){
				comments=ObjectUtil.mapper.readValue(comment.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Comment.class));
			}
		
		} else if(type.equals(TYPE_ALBUM)){
			String url="http://comment.share.com/comment/getCommentsOfAlbum/"
					+id+"/"+offset+"/"+count;
			String jsonComment = client.doGet(url);
			
			JsonNode comment = ObjectUtil.mapper.readTree(jsonComment);
			if(comment.isArray()&&comment.size()>0){
				comments=ObjectUtil.mapper.readValue(comment.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Comment.class));
			}
		} else if(type.equals(TYPE_SPOST)){
			String url="http://comment.share.com/comment/getCommentsOfShortPost/"
					+id+"/"+offset+"/"+count;
			String jsonComment = client.doGet(url);
			
			JsonNode comment = ObjectUtil.mapper.readTree(jsonComment);
			if(comment.isArray()&&comment.size()>0){
				comments=ObjectUtil.mapper.readValue(comment.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Comment.class));
			}
		}
		
		//add avatars;
		if(comments != null && comments.size() !=0) {
			for(Comment comment: comments) {
				comment.setComment_author_avatar(userService.findById(comment.getComment_author()).getUser_avatar());
			}
		}
		
		return comments;
	}
	
	/**
	 * 通过comment_id获取comment_author,comment_author_name
	 * url:http://comment.share.com/comment/get_author/{id}
	 * 参数：int comment_id
	 * 返回值类型 User comment_parent_author
	 * @param comment_id
	 * @return
	 * @throws Exception 
	 */
	public User getCommentAuthor(int comment_id) throws Exception{
		String url="http://comment.share.com/comment/getCommentAuthor/"+comment_id;
		String jsonUser = client.doGet(url);
		User comment_parent_author = ObjectUtil.mapper.readValue(jsonUser, User.class);
		return comment_parent_author;
	}
	
	/**
	 * 查询评论数量
	 * url:http://comment.share.com/get/commentscount/{object_type}{object_id}
	 * @param object_type
	 * @param object_id
	 * @return int count
	 * @throws Exception 
	 */
	public Integer getCommentsCount(int object_type, int object_id) throws Exception {
		String url="http://comment.share.com/comment/commentsCount/"+object_type+"/"+object_id;
		String count = client.doGet(url);
		if(StringUtils.isNotEmpty(count))
		return Integer.parseInt(count);
		return 0;
	}
}
