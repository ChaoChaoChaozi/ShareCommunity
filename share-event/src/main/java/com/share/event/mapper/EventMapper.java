package com.share.event.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.share.event.pojo.Event;

public interface EventMapper {

	int save(Event event);

	List<Event> getEventsHasPhoto(@Param(value="start")int start, @Param(value="step")int step);

	List<Event> getEvents(@Param(value="start")int start, @Param(value="step")int step);

	List<Event> getEventsOfUser(@Param(value="user_id") int user_id, @Param(value="num") int num);

	int delete(int id);

	int deleteByObject(@Param(value="object_type")int object_type, @Param(value="object_id")int object_id);

	List<Event> getEventsWithIDs(@Param("event_ids") List<Integer> events);

	List<Event> getEventsWithRelations(Map<Integer, List<Integer>> category1);

	Event getEvent(@Param(value="object_type")int object_type, @Param(value="object_id")int object_id);

}
