package com.share.notification.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.share.notification.mapper.NotificationMapper;
import com.share.pojo.Notification;
import com.share.util.Dic;
import com.share.util.ObjectUtil;

@Service
public class NotificationService {
	private static final String TABLE = "osf_notifications";

	private static final String NOTIFY_KEY = "notification:";

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Long> hashOps;
	@Autowired
	private NotificationMapper mapper;

	public void save(String jsonData) throws Exception {
		Notification readValue = ObjectUtil.mapper.readValue(jsonData, Notification.class);
		int secceed = mapper.save(readValue);
	}

	public Map<String, Long> getNotificationsCount(Integer user_id) {
		final Map<String, Long> notifications = new HashMap<String, Long>();

		if (!redisTemplate.hasKey(NOTIFY_KEY + user_id)) {
			initNotification(notifications);
			refreshNotifications(user_id, notifications);

		} else {
			for (String key : hashOps.keys(NOTIFY_KEY + user_id)) {
				notifications.put(key, hashOps.get(NOTIFY_KEY + user_id, key));
			}
		}
		return notifications;
	}

	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	public Notification get(int notification_id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Notification> getAllOfUser(int user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Notification> getNotificationsOfType(int user_id, int notify_type) {
		return mapper.getNotificationsOfType(user_id, notify_type);
	}

	public List<Notification> getNotificationsOfTypes(int user_id, List<Integer> notify_types) {
		return mapper.getNotificationsOfTypes(user_id, notify_types);
	}

	private void initNotification(Map<String, Long> notifications) {
		notifications.put("comment", 0L);
		notifications.put("comment_reply", 0L);
		notifications.put("follow", 0L);
		notifications.put("like", 0L);
		notifications.put("system", 0L);
	}
	// 刷新所有类型通知
	private void refreshNotifications(int user_id, Map<String, Long> notifications) {
		List<Map<String, Number>> notify_type_counts = mapper.getNotificationsCount(user_id);
		if (notify_type_counts != null && notify_type_counts.size() != 0) {
			for (Map<String, Number> notify_type_count : notify_type_counts) {
				// for(String type: notify_type_count.keySet()) {
				// System.out.println(type+":"+notify_type_count.get(type));
				// notifications.put(type, (Long)notify_type_count.get(type));
				// }
				notifications.put(Dic.toNotifyTypeDesc((Integer) notify_type_count.get("notify_type")),
						(Long) notify_type_count.get("count"));
			}
		}
		hashOps.putAll(NOTIFY_KEY + user_id, notifications);
	}

	public void refreshNotification(Notification notification) {
		Long count = hashOps.get(NOTIFY_KEY + notification.getNotified_user(),
				Dic.toNotifyTypeDesc(notification.getNotify_type()));
		if(count==null){
			count=0L;
		}
		hashOps.put(NOTIFY_KEY + notification.getNotified_user(), Dic.toNotifyTypeDesc(notification.getNotify_type()),
				count + 1);
	}


}
