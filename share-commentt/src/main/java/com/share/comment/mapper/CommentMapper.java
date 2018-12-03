package com.share.comment.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.share.pojo.Comment;

public interface CommentMapper {

	int newComment(Comment comment);

	Comment getCommentByID(int id);

	List<Comment> getCommentsOfPost(@Param("id")int id, @Param("offset")int offset, @Param("count")int count);

	List<Comment> getCommentsOfPhoto(@Param("id")int id, @Param("offset")int offset, @Param("count")int count);

	List<Comment> getCommentsOfAlbum(@Param("id")int id, @Param("offset")int offset, @Param("count")int count);

	List<Comment> getCommentsOfShortPost(@Param("id")int id, @Param("offset")int offset, @Param("count")int count);

	Map<String, Object> getCommentAuthor(int comment_id);

	Integer commentsCount(@Param("object_type")int object_type, @Param("object_id")int object_id);
	
}
