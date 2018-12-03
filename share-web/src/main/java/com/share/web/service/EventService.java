package com.share.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Album;
import com.share.pojo.Event;
import com.share.pojo.Photo;
import com.share.pojo.Post;
import com.share.pojo.Relation;
import com.share.pojo.ShortPost;
import com.share.util.Dic;
import com.share.util.ObjectUtil;
import com.share.vo.HttpResult;


@Service
public class EventService {
	
@Autowired
private HttpClientService client;
	/**
	 * 根据object_type的类型，将obj强转为对应类型，获取对应表中数据，封装Event事件
	 * url:http://event.share.com/event/savePhotoEvent/{object_type}/{obj}
	 * 提交方式：Get
	 * @param int object_type
	 * @param Object obj
	 * @return Event
	 * @throws Exception 
	 */
	private Event toEvent(int object_type, Object obj) throws Exception {
		Event event = new Event();
		if(Dic.OBJECT_TYPE_POST == object_type) {
			Post post = (Post)obj;
			event.setObject_type(Dic.OBJECT_TYPE_POST);
			event.setObject_id(post.getId());
			event.setUser_id(post.getPost_author());
			event.setTitle(post.getPost_title());
			event.setSummary(post.getPost_excerpt());
			event.setContent(post.getPost_cover());
			event.setLike_count(post.getLike_count());
			event.setShare_count(post.getShare_count());
			event.setComment_count(post.getComment_count());
			event.setTags_list(post.getPost_tags_list());
			
		} else if(Dic.OBJECT_TYPE_ALBUM == object_type) {
			Album album = (Album)obj;
			event.setObject_type(Dic.OBJECT_TYPE_ALBUM);
			event.setObject_id(album.getId());
			event.setUser_id(album.getUser_id());
			event.setTitle(album.getCover());
			event.setSummary(album.getAlbum_desc());
			
			List<Photo> photos = album.getPhotos();
			StringBuffer keys = new StringBuffer();
			if(photos!=null)
			for(Photo photo:photos) {
				keys.append(photo.getKey()+":");
			}
			event.setContent(keys.toString());
			event.setLike_count(0);
			event.setShare_count(0);
			event.setComment_count(0);
			event.setTags_list(album.getAlbum_tags_list());
			
		} 
		 else if(Dic.OBJECT_TYPE_SHORTPOST == object_type){
			ShortPost spost = (ShortPost) obj;
			event.setObject_type(Dic.OBJECT_TYPE_SHORTPOST);
			event.setObject_id(spost.getId());
			event.setSummary(spost.getPost_content());
			event.setUser_id(spost.getPost_author());
			event.setLike_count(spost.getLike_count());
			event.setShare_count(spost.getShare_count());
			event.setComment_count(spost.getComment_count());
		}
		return event;
	}
		
	/**
	 * @throws Exception 
	 * 调用(int object_type, Object obj)方法 ，保存event，并添加到索引
	 * url:http://event.share.com/event/newEvent/{object_type}/{obj}
	 * 请求方式：GET
	 * @param int object_type
	 * @param Object obj
	 * @return int
	 * @throws  	
	 */
	public int newEvent(int object_type, Object obj) throws Exception {
		
		Event event = toEvent(object_type, obj);
		String jsonData=ObjectUtil.mapper.writeValueAsString(event);
		String url="http://event.share.com/event/save";
		String doGet = client.doPostJson(url,jsonData);
		int event_id = Integer.parseInt(doGet);
		return event_id;
		//return 0;
	}
	
	/**
	 * 根据start和step，获取s_events表中的数据  select * from osf_events limit #{param1},#{param2}
	 * url:http://event.share.com/event/getEvents/{start}/{step}
	 * 请求方式： GET
	 * @param int start
	 * @param int step
	 * @return List<Event>
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<Event> getEvents(int start, int step) throws JsonParseException, JsonMappingException, Exception {
		String url="http://event.share.com/event/getEvents?start="+start+"&step="+step;
		String result = client.doGet(url);
		List<Event> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(result);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Event.class));
		}
		return pList;
		//return null;
	}

	/**
	 *  根据relation关系(object_type, object_id)查询event
	 * url:http://event.share.com/event/getEventsWithRelations/{relations}
	 * 请求方式： GET
	 * @param List<Relation> relations
	 * @return List<Event> 
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	public List<Event> getEventsWithRelations(List<Relation> relations) throws Exception {
		List<Event> events = new ArrayList<Event>();
		if(relations != null && relations.size() != 0) {
			Map<Integer, List<Integer>> category = new HashMap<Integer, List<Integer>>();
			for(Relation relation : relations) {
				if(!category.containsKey(relation.getObject_type())) {
					category.put(relation.getObject_type(), new ArrayList<Integer>());
				}
				category.get(relation.getObject_type()).add(relation.getObject_id());
			}
//			events = eventDao.getEventsWithRelations(category);
			
			List<String> category1 = new ArrayList<>();
			for(Entry<Integer, List<Integer>> entry : category.entrySet()){
				String key=entry.getKey()+"";
				String value=ObjectUtil.mapper.writeValueAsString(entry.getValue());
				category1.add(key+"#"+ value);
			}
			
			String url= "http://event.share.com/event/getEventsWithRelations";
			String value1=ObjectUtil.mapper.writeValueAsString(category1);
			String body = client.doPostJson(url, value1);
			
			JsonNode data=ObjectUtil.mapper.readTree(body);
			if(data.isArray()&&data.size()>0){
				events = ObjectUtil.mapper.readValue(data.traverse(),
						ObjectUtil.mapper.getTypeFactory().
						constructCollectionType(List.class, Event.class));
			}
		}
		return events;
		//return events;
	}
	
	/**
	 * 获取含有图片的Event	 
	 * url:http://event.share.com/event/getEventsHasPhoto/{start}/{step}
	 * 请求方式： GET
	 * @param int start 
	 * @param int step
	 * @return List<Event>
	 * @throws Exception 
	 */
	public List<Event> getEventsHasPhoto(int start, int step) throws Exception {
		String url = "http://event.share.com/event/getEventsHasPhoto?start="+start+"&step="+step;
		String doGet = client.doGet(url);
		List<Event> pList=null;
		try {
			JsonNode data=ObjectUtil.mapper.readTree(doGet);
			if(data.isArray()&&data.size()>0){
				pList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Event.class));
			}
		} catch (Exception e) {
			pList=null;
		}
		return pList;
	}
	
	/**
	 * 根据object_type和object_id获取Event	 
	 * url:http://event.share.com/event/getEvent/{object_type}/{object_id}
	 * 请求方式： GET
	 * @param int object_type 
	 * @param int object_id
	 * @return Event
	 * @throws Exception 
	 */
	public Event getEvent(int object_type, int object_id) throws Exception{
		String url="http://event.share.com/event/getEvent";
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		String doGet = client.doGet(url,map);
		if(StringUtils.isNotEmpty(doGet)){
		Event event=ObjectUtil.mapper.readValue(doGet,Event.class);
		return event;
		}
		return null;
	
	}
	
	
	/**
	 * 根据event_ids获取Event	 
	 * url:http://event.share.com/event/getEventsWithIDs/{event_ids}
	 * 请求方式： GET
	 * @param int object_type 
	 * @param int object_id
	 * @return List<Event>
	 * @throws Exception 
	 * @throws  
	 * @throws  
	 */
	public List<Event> getEventsWithIDs(List<Integer> event_ids) throws  Exception {
		String url="http://event.share.com/event/getEventsWithIDs";
		String json=ObjectUtil.mapper.writeValueAsString(event_ids);
		String body = client.doPostJson(url, json);
		List<Event> pList=null;
		if(StringUtils.isNotEmpty(body)){
	
		JsonNode data=ObjectUtil.mapper.readTree(body);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, Event.class));
		}
		}
		return pList;
	}
	/**
	 * 根据event_ids获取Event,取前 count 个
	 * url:http://event.share.com/event/getEventsOfUser/{event_ids}
	 * 请求方式： GET
	 * @param int user_id 
	 * @param int count
	 * @return List<Event>
	 * @throws Exception 
	 * @throws  
	 * @throws  
	 */
	public List<Event> getEventsOfUser(int user_id, int num) throws  Exception{
		String url = "http://event.share.com/event/getEventsOfUser?user_id="+user_id+"&num="+num;
		String doGet = client.doGet(url);
		List<Event> pList=null;
		try {
			JsonNode data=ObjectUtil.mapper.readTree(doGet);
			if(data.isArray()&&data.size()>0){
				pList = ObjectUtil.mapper.readValue(data.traverse(),
			    ObjectUtil.mapper.getTypeFactory().
			    constructCollectionType(List.class, Event.class));
			}
		} catch (Exception e) {
		}
		return pList;
	}
		
	/**
	 * 根据id删除时间Event
	 * url:http://event.share.com/event/delete/{id}
	 * 请求方式： GET
	 * @param int id 
	 * @return void
	 * @throws Exception 
	 */
	public void delete(int id) throws Exception{
		String url="http://event.share.com/event/delete?id="+id;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		String doGet = client.doGet(url,map);
		
	}
	/**
	 * 根据object_type和object_id删除时间Event
	 * url:http://event.share.com/event/delete/{object_type}/{object_id}
	 * 请求方式： GET
	 * @param int id 
	 * @param int object_id 
	 * @return void
	 * @throws Exception 
	 */
	public void delete(int object_type, int object_id) throws Exception{
		String url="http://event.share.com/event/delete2?id="+object_type+"object_id"+object_id;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("object_type", object_type);
		map.put("object_id", object_id);
		String doGet = client.doGet(url,map);
	}
	
	
}
