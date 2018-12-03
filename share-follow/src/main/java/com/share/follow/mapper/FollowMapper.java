
package com.share.follow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.share.follow.pojo.Follower;
import com.share.follow.pojo.Following;


public interface FollowMapper{

	Integer saveFollowing(Following following);

	Integer saveFollower(Follower follower);

	Integer delFollower(Follower follower);

	List<Following> getFollowings(int user_id);

	List<Follower> getFollowers(int user_id);

	//Integer delFollower(int user_id, int user_id2);

	Integer delFollowing(Following following);

	List<Integer> isFollowingUsers(@Param("user_id")int user_id, @Param("following_ids")List<Integer> following_ids);


	
}