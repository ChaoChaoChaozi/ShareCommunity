package com.share.sso.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.share.sso.pojo.User;


public interface UserMapper {
	

	void InsertToken(User user);

	User findByUsername(String username);

	User findByEmail(String Email);

	User getUserByID(int id);

	void updateActivationKey(@Param("id")int id, @Param("activationKey")String activationKey);

	void activateUser( @Param("activationKey")String activationKey, @Param("id")int id);

	User getUser(@Param("user_activationKey") String string, @Param("key")Object[] args);

	List<User> getUsers(int count);

	void updateAvatar(@Param("user_id")int user_id, @Param("avatar")String avatar);

	void updateUsernameAndDesc(@Param("id")int id, @Param("user_name")String user_name,@Param("user_desc") String user_desc);

	void updateResetPwdKey(@Param("email")String email,@Param("key") String key);

	String isAllowedResetPwd(@Param("email")String email);

	Integer updatePassword(@Param("user_email")String user_email, @Param("user_pwd")String user_pwd);

	int register(User user);

	List<User> getUsersByIDs(@Param("pList")List<Integer> pList);

	List<User> getUserofUsername(@Param("username")String username);

	

}
