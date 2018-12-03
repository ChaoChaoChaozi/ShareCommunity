package com.share.like.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.share.like.mapper.LikeMapper;
import com.share.util.Dic;

@RestController
public class LikeController {
	private static final String LIKE_KEY = "like:"; // 缓存喜欢object的用户id
	@Autowired
	private LikeMapper likeMapper;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private SetOperations<String, Integer> setOps;

	// 用户喜欢
	/**
	 * 用户喜欢 返回值：无 参数类型 :get 参数列表: user_id,object_type,object_id
	 * http://like.share.com/like/like
	 * 
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @throws Exception
	 */
	@RequestMapping("like/like")
	public void like(int user_id, int object_type, int object_id) {
		likeMapper.like(user_id, object_type, object_id);
		setOps.add(LIKE_KEY + Dic.checkType(object_type) + ":" + object_id, user_id);
	}

	@RequestMapping("like/undoLike")
	public void undolike(int user_id, int object_type, int object_id) {
		int undolike = likeMapper.undolike(user_id, object_type, object_id);
		
		setOps.remove(LIKE_KEY + Dic.checkType(object_type) + ":" + object_id, user_id);
	}

	@RequestMapping("like/isLike")
	public Boolean isLike(int user_id, int object_type, int object_id) {
		return setOps.isMember(LIKE_KEY + Dic.checkType(object_type) + ":" + object_id, user_id);

	}

	@RequestMapping("like/likersCount")
	public long likersCount(int object_type, int object_id) {
		return setOps.size(LIKE_KEY + Dic.checkType(object_type) + ":" + object_id);
	}

	@RequestMapping("like/getLikers")
	public List<Integer> getLikers(int object_type, int object_id) {
		String key = LIKE_KEY + Dic.checkType(object_type) + ":" + object_id;
		List<Integer> likers = null;
		if (!redisTemplate.hasKey(key)) {
			likers = likeMapper.getLikers(object_type, object_id);
			setOps.add(key, (Integer[]) likers.toArray());

		}
		return likers;

	}

	@RequestMapping("like/likeCount")
	public int likeCount(int user_id) {
		return likeMapper.likeCount(user_id);
	}

}
