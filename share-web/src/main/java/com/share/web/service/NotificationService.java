package com.share.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Event;
import com.share.pojo.Notification;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.ObjectUtil;

@Service("notificationService")
public class NotificationService {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	@Qualifier("eventService")
	private EventService eventService;

	@Autowired
	private HttpClientService client;

	/**
	 * save notification
	 * 
	 * 保存Notification http://notification.share.com/notification/save 类型：post
	 * 请求参数：Notification notification 返回值：int 插入的返回值
	 * 
	 * @param notification
	 * @return notification id
	 * @throws JsonProcessingException
	 */
	public int doNotify(Notification notification) throws JsonProcessingException {
		// 利用httpclient doPostJson的方法,将对象转化成json字符串
		String jsonData = ObjectUtil.mapper.writeValueAsString(notification);
		String url = "http://notification.share.com/notification/save";
		try {
			client.doPostJson(url, jsonData);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 根据userid，获取用户通知个数
	 * http://notification.share.com/notification/getNotificationsCount 类型：get
	 * 参数：int user_id 返回值：Map<String, Long>
	 * 
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Long> getNotificationsCount(int user_id) throws Exception {

		String url = "http://notification.share.com/notification/getNotificationsCount/" + user_id;
		String jsondata = client.doGet(url);

		Map<String, Long> notifications = new HashMap<>();
		Map readValue = ObjectUtil.mapper.readValue(jsondata, notifications.getClass());
		notifications = readValue;
		// data.
		return notifications;

		// Map<String, Long> notifications =
		// notificationDao.getNotificationsCount(user_id);
		// Map<String, Integer> notifications_with_type = new HashMap<String,
		// Integer>();
		// for(Integer type: notifications.keySet()) {
		// notifications_with_type.put(Dic.toNotifyTypeDesc(type),
		// notifications.get(type));
		// }
	}

	/**
	 * 刷新Notification
	 * http://notification.share.com/notification/refreshNotification 类型：post
	 * 参数：Notification notification 返回值：无
	 * 
	 * @param notification
	 * @throws Exception
	 */
	public void refreshNotification(Notification notification) throws Exception {
		// notificationDao.refreshNotification(notification);
		// 利用httpclient doPostJson的方法,将对象转化成json字符串
		String jsonData = ObjectUtil.mapper.writeValueAsString(notification);
		String url = "http://notification.share.com/notification/refreshNotification";
		client.doPostJson(url, jsonData);
	}

	/**
	 * 通过用户id和通知类型，获取总的通知
	 * http://notification.share.com/notification/getNotifications post:get
	 * 参数：int user_id, int notify_type 返回值：List<Notification>
	 * 
	 * @param user_id
	 * @param notify_type
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public List<Notification> getNotifications(int user_id, int notify_type) throws Exception {

		List<Notification> notifications = null;

		if (notify_type == Dic.NOTIFY_TYPE_COMMENT) {
			String url = "http://notification.share.com/notification/getNotificationsOfTypes/" + user_id;
			String json = ObjectUtil.mapper
					.writeValueAsString(Arrays.asList(Dic.NOTIFY_TYPE_COMMENT, Dic.NOTIFY_TYPE_COMMENT_REPLY));
			String jsondata = client.doPostJson(url, json);
			JsonNode data = ObjectUtil.mapper.readTree(jsondata);
			if (data.isArray() && data.size() > 0) {
				notifications = ObjectUtil.mapper.readValue(data.traverse(),
						ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Notification.class));
			}
		} else {
			String url = "http://notification.share.com/notification/getNotificationsOfType?user_id=" + user_id
					+ "&notify_type=" + notify_type;
			String jsondata = client.doGet(url);
			JsonNode data = ObjectUtil.mapper.readTree(jsondata);
			if (data.isArray() && data.size() > 0) {
				notifications = ObjectUtil.mapper.readValue(data.traverse(),
						ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Notification.class));
			}
		}

		if (notifications != null) {
			for (Notification notification : notifications) {
				User user = userService.findById(notification.getNotifier());
				notification.setNotifier_name(user.getUser_name());
				notification.setNotifier_avatar(user.getUser_avatar());

				Event event = eventService.getEvent(notification.getObject_type(), notification.getObject_id());

				String object_title = null;
				if (Dic.OBJECT_TYPE_POST == notification.getObject_type()) {
					object_title = event.getTitle();
				} else if (Dic.OBJECT_TYPE_ALBUM == notification.getObject_type()) {
					object_title = event.getSummary();
				} else if (Dic.OBJECT_TYPE_SHORTPOST == notification.getObject_type()) {
					object_title = event.getSummary();
				}
				if (object_title != null) {
					int len = object_title.length();
					if (len > 20) {
						object_title = object_title.substring(0, 20);
					}
				}
				notification.setObject_title(object_title);
			}
		}
		return notifications;
	}
}
