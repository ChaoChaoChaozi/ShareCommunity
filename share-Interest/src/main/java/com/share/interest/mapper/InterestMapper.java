package com.share.interest.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.share.interest.model.Interest;
import com.share.pojo.Tag;

public interface InterestMapper {

	
	int interestInTag(@Param ("user_id")int user_id ,@Param("tag_id")int tag_id);

	int undoInterestInTag(@Param ("user_id")int user_id ,@Param("tag_id")int tag_id);

	List<Integer> getUsersInterestedInTag(@Param("tag_id")int tag_id);

	boolean hasInterestInTag(@Param ("user_id")int user_id ,@Param("tag_id")int tag_id);

	Map<Integer, Boolean> hasInterestInTags(@Param ("user_id") int user_id,@Param ("tags") List<Tag> tags);

	List<Tag> getTagsUserInterestedIn(@Param("user_id") int user_id);




}
