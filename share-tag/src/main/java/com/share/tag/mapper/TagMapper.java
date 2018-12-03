package com.share.tag.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.share.tag.pojo.Tag;

public interface TagMapper {

	Integer queryTagID(String tag);

	Integer saveTag(Tag tag);

	List<Tag> getTags();

	Tag queryTag(int id);

	List<Tag> queryTags(@Param("tgs_id")List<String> tgs_id);

	

	

}
