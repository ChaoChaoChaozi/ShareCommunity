package com.share.notification.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.share.pojo.Notification;

public interface NotificationMapper {


	int save(Notification readValue);
	//void delete(int id);
	Notification get(int notification_id);
	List<Notification> getAllOfUser(int user_id);
	List<Notification> getNotificationsOfType(@Param("user_id")int user_id, @Param("notify_type")int notify_type);
	List<Notification> getNotificationsOfTypes(@Param("user_id")int user_id, @Param("notify_types")List<Integer> notify_types);		
	List<Map<String, Number>> getNotificationsCount(int user_id);
}
