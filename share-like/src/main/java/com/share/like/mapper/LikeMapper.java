package com.share.like.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface LikeMapper {

	int like(@Param("user_id")int user_id, @Param("object_type")int object_type, @Param("object_id")int object_id);

	int undolike(@Param("user_id")int user_id, @Param("object_type")int object_type, @Param("object_id")int object_id);

	int isLike(@Param("user_id")int user_id, @Param("object_type")int object_type, @Param("object_id")int object_id);

	long likersCount(int object_type, int object_id);

	int likeCount(int user_id);

	List<Integer> getLikers(@Param("object_type")int object_type, @Param("object_id")int object_id);

}
