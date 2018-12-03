package com.share.relation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.share.relation.pojo.Relation;

public interface RelationMapper {
	Integer save(Relation relation);

	Integer delete() ;

	List<Relation> get(int tag_id) ;

	List<Relation> getRelationsInTags(@Param("tag_ids")List<String> tag_ids) ;
}

