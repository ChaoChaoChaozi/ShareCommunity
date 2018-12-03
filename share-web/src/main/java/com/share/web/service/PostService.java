package com.share.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Event;
import com.share.pojo.Post;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.ObjectUtil;
import com.share.util.Property;



@Service("postService")
public class PostService {

	public static final int POST_STATUS_PUB = 0;	//公开
	public static final int POST_STATUS_PRV = 1;	//私密
	public static final int POST_STATUS_SAVED = 2;	//保存
	public static final int POST_STATUS_EDIT = 3;	//编辑
	
	public static final int COMMENT_STATUS_ALLOWED = 0;		//允许评论
	public static final int COMMENT_STATUS_NOTALLOWED = 1;	//不允许评论
	
	public static final int POST_SUMMARY_LENGTH = 200;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	@Qualifier("eventService")
	private EventService eventService;

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	private HttpClientService client;
	
	/**
	 * @param author
	 * @param title
	 * @param content
	 * @param post_status
	 * @param comment_status
	 * @param param_tags
	 * @param post_cover
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> newPost(Integer author, String title, String content, 
						Integer post_status, Integer comment_status, String param_tags, String post_cover) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//1 field check
		if(author == null || 
		   title == null || title.length() == 0 ||
		   content == null || content.length() == 0) {
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}
		   
		
		if(post_status == null)
			post_status = POST_STATUS_PUB;
		if(post_status < 0 || post_status > 3) {
			map.put("status", Property.ERROR_POST_STATUS);
			return map;
		}
		
		if(comment_status == null)
			post_status = COMMENT_STATUS_ALLOWED;
		if(comment_status != 0 && comment_status != 1) {
			map.put("status", Property.ERROR_COMMENT_STATUS);
		}
		
		
		//2 save post
		Post post = new Post();
		post.setPost_author(author);
		post.setPost_title(title);
		post.setPost_excerpt(getSummary(content));
		post.setPost_content(content);
		post.setPost_status(post_status);
		post.setComment_status(comment_status);
		post.setLike_count(0);
		post.setShare_count(0);
		post.setComment_count(0);
		
		post.setPost_cover(post_cover);
		
		
		//3 save tags
		if(param_tags != null && param_tags.length() != 0) {	
			//此处会为tag建立index
			Map<String, Object> tagsmap = tagService.newTags(tagService.toList(param_tags));
			
			post.setPost_tags_list((List<Tag>)tagsmap.get("tags"));
			int id = savePost(post);
			post.setId(id);
			
			//4 save post tag relation
			for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
				Map<String, Object> relmap = relationService.newRelation(
											 RelationService.RELATION_TYPE_POST, 
											 post.getId(), 
											 tag.getId()
											 );
			}			
			map.put("tags", tagsmap.get("tags"));
		} else {
			int id = savePost(post);
			post.setId(id);
			map.put("tags", new ArrayList<Tag>());
		}
				
		map.put("post", post);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}
	/**
	 * 新建Post日志消息：
	 * http://post.share.com/post/newPost
	 * 类型：post
	 * 参数：Post post
	 * 返回值：id
	 * @param post
	 * @return
	 * @throws Exception 
	 */
	public int savePost(Post post) throws Exception{
		String url="http://post.share.com/post/save";
		String jsonData=ObjectUtil.mapper.writeValueAsString(post);
		String json= client.doPostJson(url, jsonData);
	
		return Integer.parseInt(json);
	}
	/**
	 *	根据ID寻找Post
	 *	http://post.share.com/post/findPostByID
	 *	类型：get
	 *	参数：int id
	 *	返回值：Post
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Post findPostByID(int id) throws Exception {
		String url="http://post.share.com/post/getPostByID?id="+id;
		String doGet = client.doGet(url);
		Post readValue = ObjectUtil.mapper.readValue(doGet, Post.class);
		return /*postDao.getPostByID(id)*/readValue;
	}
	
	public List<Post> findPostsByIDs(int[] ids) throws Exception {
		
		return null;
	}
	/**
	 *  根据userID寻找所有的Post
	 *	http://post.share.com/post/findPostsOfUser
	 *	类型：get
	 *	参数：int id -》userID
	 *	返回值：List<Post>
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public List<Post> findPostsOfUser(int id) throws Exception {
		String url="http://post.share.com/post/getPostsByUserID?id="+id;
		String doGet = client.doGet(url);
		List<Post> plist=null;
		JsonNode data=ObjectUtil.mapper.readTree(doGet);
		if(data.isArray()&&data.size()>0){
			plist=ObjectUtil.mapper.readValue(data.traverse(),
					ObjectUtil.mapper.getTypeFactory().
					constructCollectionType(List.class, Post.class));
		}
		return plist;
	}
	/**
	 *  根据userID寻找所有的Post
	 *	http://post.share.com/post/findPostsOfUser
	 *	类型：get
	 *	参数：int id -》userID
	 *	返回值：List<Post>
	 * @param id
	 * @param fromto
	 * @return
	 */
	public List<Post> findPostsOfUser(int id, Object[] fromto) {
		return null;
	}
	/** 根据userID寻找所有的Post
	 *	http://post.share.com/post/findPostsOfUser
	 *	类型：get
	 *	参数：int id -》userID
	 *	返回值：List<Post>
	 *
	 */
	public List<Post> findPostsOfUser(int id, String orderby, Object[] fromto) {
		
		return null;
	}
	/**
	 * 
	 * @param post_content
	 * @return
	 */
	public static String getSummary(String post_content) {
		if(post_content == null || post_content.length() == 0)
			return null;
		
		Document doc = Jsoup.parse(post_content);
		String text = doc.text().replaceAll("<script[^>]*>[\\d\\D]*?</script>", "");
		return text.substring(0, 
									  text.length() > POST_SUMMARY_LENGTH?
									  POST_SUMMARY_LENGTH:
									  text.length());
	}
	/**
	 * 根据Post id查找作者user id
	 * http://post.share.com/post/getAuthorOfPost
	 * 类型：get
	 * 参数：int id
	 *	返回值：user_id
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public User getAuthorOfPost(int id) throws Exception {
		String url="http://post.share.com/post/getAuthorOfPost?id="+id;
		String doGet=client.doGet(url);
		if(StringUtils.isEmpty(doGet)){
			return null;
		}
		Integer user_id=Integer.parseInt(doGet);
		
		//int user_id = postDao.getAuthorOfPost(id);
		return userService.findById(user_id);
	}
	/**
	 * 根据用户id，获取用户post的个数
	 * 类型：get
	 * 参数：user_id
	 * 返回值：long post的个数
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public long count(int user_id) throws Exception{
		String url="http://post.share.com/post/count?user_id="+user_id;
		String doGet=client.doGet(url);
		long count=Long.parseLong(doGet);
		return /*postDao.count(user_id)*/count;
	}
	/**
	 * 删除Post
	 * 根据id删除post
	 *  http://post.share.com/post/deletePost
	 *  类型：get
	 *  参数：int id postid
	 *  返回值：无
	 * @param id
	 * @throws Exception 
	 */
	public void deletePost(int id) throws Exception{
		//postDao.delete(id);
		String url="http://post.share.com/post/delete?id="+id;
		client.doGet(url);
		Event event = eventService.getEvent(Dic.OBJECT_TYPE_POST, id);
		if(event != null){
			eventService.delete(Dic.OBJECT_TYPE_POST, id);
			feedService.delete(event.getObject_type(), event.getObject_id());
		}
	}
}
