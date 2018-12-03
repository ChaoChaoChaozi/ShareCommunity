package com.share.web.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.share.pojo.Event;
import com.share.pojo.Relation;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.StringToDate;
import com.share.web.redis.FeedDAO;
import com.share.web.search.IndexSearch;



@Service("feedService")
public class FeedService {

	public static final int FEED_COUNT_PER_PAGE = 10;
	public static final int FEED_COUNT = 200;	//feed缓存量
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@Autowired
	private FeedDAO feedDao;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;
	
	@Autowired
	@Qualifier("commentService")
	private CommentService commentService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;
	
	@Autowired
	private IndexSearch indexSearch;
	
	
	public void push(List<Integer> followers, int event_id) {
		if(followers != null && followers.size()!=0) {
			for(Integer follower: followers) {
				feedDao.save("feed:user:"+follower, event_id);
			}
		}
	}
	
	/**
	 * 缓存feed到对应标签列表序列中
	 * 
	 * @param tag_id
	 * @param event_id
	 */
	public void cacheFeed2Tag(int tag_id, int event_id) {
		feedDao.save("feed:tag:"+tag_id, event_id);
	}
	
	public void cacheFeeds2Tag(int tag_id, List<Integer> events_id) {
		feedDao.saveAll("feed:tag:"+tag_id, events_id);
	}
	
	private List<Integer> getEventIDs(int user_id, int start, int count) {
		return feedDao.fetch("feed:user:"+user_id, start, count);
	}
	
	public List<Event> getFeeds(int user_id) throws Exception {
		return getFeeds(user_id, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeeds(int user_id, int count) throws Exception{
		List<Integer> event_ids = getEventIDs(user_id, 0, count-1);
		
		return decorateFeeds(user_id, event_ids);
	}
	
	private List<Event> decorateFeeds(int user_id, List<Integer> event_ids) throws Exception{
		List<Event> events = new ArrayList<Event>();
		//获取user的关注者和自己
		List<Integer> followingIDs = followService.getFollowingIDs(user_id);
		if(followingIDs==null){
		followingIDs=new ArrayList<>();
		followingIDs.add(user_id);
		}else{
			followingIDs.add(user_id);
		}
		if(followingIDs != null && followingIDs.size()!=0 ) {
			
			events = eventService.getEventsWithIDs(followingIDs);
			addUserInfo(events);
			updLikeCount(user_id, events);
			addCommentCount(events);
		}

		return events;
	}
	
	public List<Event> getFeedsOfPage(int user_id, int num) throws Exception {
		long all_feeds_count = feedDao.count("feed:user:"+user_id);
		List<Integer> event_ids = feedDao.fetch("feed:user:"+user_id, 
												FEED_COUNT_PER_PAGE*(num-1), 
												FEED_COUNT_PER_PAGE-1);
		return decorateFeeds(user_id, event_ids);
		
	}
	
	public List<Event> getFeedsOfPage(int user_id, int num, int feed_id) throws Exception {
		List<Integer> event_ids = new ArrayList<Integer>(); 
		int index = -1;
		long all_feeds_count = feedDao.count("feed:user:"+user_id);
		while(index == -1) {
			event_ids = feedDao.fetch("feed:user:"+user_id, 
					FEED_COUNT_PER_PAGE*(num-1)-1, 
					FEED_COUNT_PER_PAGE);
			
			if(FEED_COUNT_PER_PAGE*num >= all_feeds_count) {
				break;
			}
			
			num++;
			index = event_ids.indexOf(feed_id);
		}
		if(index != -1) {
			event_ids = event_ids.subList(index+1, event_ids.size());
		}
		return decorateFeeds(user_id, event_ids);
	}
	
	
	public List<Event> addUserInfo(List<Event> events) throws Exception {
		if(events == null || events.size() == 0)
			return events;
		for(Event event : events) {
			User user = userService.findById(event.getUser_id());
			event.setUser_name(user.getUser_name());
			event.setUser_avatar(user.getUser_avatar());
		}
		return events;
	}
	
	public void updLikeCount(int user_id, List<Event> events) throws Exception{
		if(events == null || events.size() == 0)
			return;
		for(Event event : events) {
			event.setLike_count((int)likeService.likersCount(event.getObject_type(), 
															 event.getObject_id()));
			event.setIs_like(likeService.isLike(user_id, 
												event.getObject_type(), 
												event.getObject_id()));
		}
	}
	
	public void addCommentCount(List<Event> events) throws Exception{
		if(events == null || events.size() == 0)
			return;
		for(Event event : events) {
			event.setComment_count(commentService.getCommentsCount(event.getObject_type(), 
															 	   event.getObject_id()));
		}
	}
	
	public void pull() {
		
	}
	
	public void delete(int user_id, int event_id) {
		feedDao.delete("feed:user:"+user_id, event_id);
	}

	/**
	 * 获取tag标签的feed
	 * 
	 * @param user_id
	 * @param tag_id
	 * @return
	 * @throws Exception 
	 */
	public List<Event> getFeedsByTag(int user_id, int tag_id) throws Exception {
		return getFeedsByTag(user_id, tag_id, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeedsByTag(int user_id, int tag_id, int count) throws Exception{
		List<Integer> event_ids = getEventIDsByTag(tag_id, 0, count-1);
		return decorateFeeds(user_id, event_ids);
	}
	
	public List<Event> getFeedsByTagOfPage(int user_id, int tag_id, int num) throws Exception {
		List<Integer> event_ids = feedDao.fetch("feed:tag:"+tag_id, 
												FEED_COUNT_PER_PAGE*(num-1), 
												FEED_COUNT_PER_PAGE-1);
		return decorateFeeds(user_id, event_ids);
		
	}
	
	private List<Integer> getEventIDsByTag(int tag_id, int start, int count) {
		return feedDao.fetch("feed:tag:"+tag_id, start, count);
	}
	
	/**
	 * feeds search
	 */
	public List<Event> getFeedsByTitleOrContentContains(String term) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		//List<Integer> event_ids = eventIndexService.findByTitleOrContent(term);
		List<Event> list=indexSearch.findByTitleOrContent(term, 0, 20);
		return list;
	}
	public List<Event> getFeedsByTitleOrContentContains(int user_id, String term) {		
		return getFeedsByTitleOrContentContains(user_id, term, 1);
	}
	
	public List<Event> getFeedsByTitleOrContentContains(String term, int page) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		//List<Integer> event_ids = eventIndexService.findByTitleOrContent(term, (page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		List<Event> list=indexSearch.findByTitleOrContent(term,(page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		return list;
	}
	public List<Event> getFeedsByTitleOrContentContains(int user_id, String term, int page) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		//List<Integer> event_ids = eventIndexService.findByTitleOrContent(term, (page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		List<Event> list=indexSearch.findByTitleOrContent(term,(page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		List<Event> list1=new ArrayList<>();
		for (Event event : list) {
			if(event.getUser_id()==user_id){
				list1.add(event);
			}
		}
		
		return list1;
	}
	
	/**
	 * feed推荐算法
	 * 这里只是简单实现, 可自己扩充
	 * @param user_id
	 * @return 推荐feed列表 - List<Event>
	 * @throws Exception 
	 */
	public List<Event> getRecommendFeeds(int user_id) throws Exception {
		return addUserInfo(eventService.getEventsHasPhoto(0, 20));
	}
	
	public List<Event> getRecommentFeedsOfPage(int user_id, int page) throws Exception {
		return addUserInfo(eventService.getEventsHasPhoto(FEED_COUNT_PER_PAGE*(page-1), FEED_COUNT_PER_PAGE-1));
	}
	
	public void coldStart(int user_id) throws Exception{
		if(feedDao.count("feed:user:"+user_id) != 0){
			return ;
		}
		
		List<Tag> tags_inted = interestService.getTagsUserInterestedIn(user_id);
		List<Relation> relations = relationService.getRelationsInTags(tags_inted);
		List<Event> events = eventService.getEventsWithRelations(relations);
		
		//no choose , fetch latest feeds default
		if(events == null || events.size() == 0){
			events = eventService.getEvents(0, FEED_COUNT_PER_PAGE);
		}
			
		List<Integer> events_id = new ArrayList<Integer>();
		for(Event event : events) {
			events_id.add(event.getId());
		}
		feedDao.saveAll("feed:user:"+user_id, events_id);

	}
}
