package com.share.web.search;

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
import org.springframework.stereotype.Service;

import com.share.pojo.Event;
import com.share.pojo.User;
import com.share.util.StringToDate;
import com.share.web.service.UserService;
@Service
public class IndexSearch {

	@Autowired
	private TransportClient esClient;
	@Autowired
	private UserService userService;
	
	public List<Event> findByTitleOrContent(String term,int start,int size){
	MatchQueryBuilder query = QueryBuilders.matchQuery("summary", term).operator(Operator.OR);
	SearchResponse response = esClient.prepareSearch("shareevent").setQuery(query).setFrom(start).setSize(size).get();
	SearchHits hits = response.getHits();
	List<Event> list=new ArrayList<Event>();
	for (SearchHit searchHit : hits) {
		Event event=new Event();
		event.setId(Integer.parseInt(searchHit.getSource().get("id").toString()));
		event.setObject_type(Integer.parseInt(searchHit.getSource().get("object_type").toString()));
		event.setObject_id(Integer.parseInt(searchHit.getSource().get("object_id").toString()));
		String date=searchHit.getSource().get("ts").toString();
		String da=date.substring(0, date.indexOf("."));
		da=da.replace("T", " ");
		System.out.println(da);
		event.setTs(StringToDate.getDateOfString(da));
		event.setUser_id(Integer.parseInt(searchHit.getSource().get("user_id").toString()));
		
			Object obj=searchHit.getSource().get("title");
			if(obj!=null)
		event.setTitle(searchHit.getSource().get("title").toString());
		event.setSummary(searchHit.getSource().get("summary").toString());
		Object object = searchHit.getSource().get("content");
		if(object!=null)
		event.setContent(object.toString());
		User user=new User();
		try {
			user = userService.findById(event.getUser_id());
		} catch (Exception e) {
		}
		event.setUser_avatar(user.getUser_avatar());
		event.setUser_name(user.getUser_name());
		
		list.add(event);
	}
	return list;
	}
}
