package com.share.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.share.common.service.HttpClientService;
import com.share.pojo.ShortPost;
import com.share.util.Property;


@Service("shortPostService")
public class ShortPostService extends PostService{
	
	@Autowired
	private HttpClientService client;
	public Map<String, Object> newPost(Integer author, String content) throws Exception{		
		Map<String, Object> map = new HashMap<String, Object>();
		if(content == null || content.length() == 0){
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}
		ShortPost spost = new ShortPost();
		spost.setPost_author(author);
		spost.setPost_content(content);
		spost.setId(savePost(spost));
		map.put("spost", spost);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}
	/**
	 * url:  http://post.share.com/post/count
	 * @throws Exception 
	 */
	@Override
	public long count(int user_id) throws Exception{
		String url="http://post.share.com/post/scount?user_id="+user_id;
		String doGet = client.doGet(url);
		if(StringUtils.isNotEmpty(doGet))
		return Long.parseLong(doGet);
		return 0;
	}
}
