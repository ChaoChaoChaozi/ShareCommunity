 package com.share.web.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.User;
import com.share.util.Dic;
import com.share.util.MD5Util;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.vo.HttpResult;
import com.share.web.search.IndexSearch;


@Service
public class UserService {


	public static final int USERNAME_MAXLEN = 20;
	public static final int STATUS_USER_NORMAL = 0;				//正常
	public static final int STATUS_USER_INACTIVE = 1;			//待激活
	public static final int STATUS_USER_LOCK = 2;				//锁定
	public static final int STATUS_USER_CANCELLED = 3;			//注销
	
	public static final String DEFAULT_USER_AVATAR = "default-avatar.jpg";
	

	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private ShortPostService shortPostService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private AlbumService albumService;
	@Autowired
	private HttpClientService client;
	


	
	private boolean ValidateEmail(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException e) {
			result = false;
		}
		return result;
	}
	
//	public String confirmPwd(String user_name, String user_pwd) {
//		if(user_pwd == null || user_name.length() == 0)
//			return Property.ERROR_PWD_EMPTY;
//		String pwd = userDao.getPwdByUsername(user_name);
//		if(pwd.equals(user_pwd)) 
//			return null;
//		else
//			return Property.ERROR_PWD_DIFF;
//			
//	}
	/**
	 * 新建user在redis中
	 * 	http://sso.share.com/user/insertToken
	 * 	post:
	 * 	参数：User
	 * 	无返回值
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public String  newToken(User user) throws Exception {
		String token = UUID.randomUUID().toString();
		String url="http://sso.share.com/user/insertToken";
		//利用httpclient doPostJson的方法,将对象转化成json字符串
		String jsonData=ObjectUtil.mapper.writeValueAsString(user);
		jsonData=ObjectUtil.mapper.writeValueAsString(token);
		client.doPostJson(url, jsonData);
		return token;
	}
	/**
	 * 从redis中删除user
	 * http://sso.share.com/user/delToken
	 * get：
	 * 参数：token
	 * 返回值：无
	 * @param token
	 * @throws Exception 
	 */
	public void delToken(String token) throws Exception {
		String url="http://sso.share.com/user/delToken?token="+token;
		client.doGet(url);
		//userDao.delToken(token);
	}
	/**
	 * 判断redis中是否存在Token
	 * 请求url：http://sso.share.com/user/containsToken
	 * 请求方法：getgetUserByEmail
	 * 请求参数：String token
	 * 返回值：boolean
	 * 
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	public boolean checkToken(String token) throws Exception {
		String url="http://sso.share.com/user/containsToken?token="+token;
		String doGet = client.doGet(url);
		
		return doGet.isEmpty();
		/*userDao.containsToken(token)*/
	}
	/**
	 * 通过token找User
	 * 请求url：http://sso.share.com/user/getUserByToken
	 * 请求方法：get
	 * 请求参数 :String token
	 * 返回值类型：User
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	public User findUserByToken(String token) throws Exception {
		String url="http://sso.share.com/user/getUserByToken?token="+token;
		String doGet=client.doGet(url);
		 User user=ObjectUtil.mapper.readValue(doGet, User.class);
		return /*userDao.getUserByToken(token)*/user;
	}
	/**
	 * 通过用户名找用户
	 * 请求url：http://sso.share.com/user/getUserByUsername
	 * 请求方法：get
	 * 请求参数：String username
	 * 返回值类型：User
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	public User findByUsername(String username) throws Exception {
		String url="http://sso.share.com/user/getUserByUsername?username="+username;
		User user;
		try {
			String jsonData=client.doGet(url);
			user = ObjectUtil.mapper.readValue(jsonData, User.class);
		} catch (Exception e) {
			user=null;
		}
	//	User user = userDao.getUserByUsername(username);
//		if(user != null){
//			addAvatar(user);
//		}
		return user;
	}
	/**
	 * 通过邮箱寻找Uesr
	 * 请求url：http://sso.share.com/user/getUserByEmail
	 * 请求方法：get
	 * 请求参数：String email
	 * 返回值类型： User
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	public User findByEmail(String email) throws Exception {
		String url="http://sso.share.com/user/getUserByEmail?email="+email;
		
		User user;
		try {
			String json  = client.doGet(url);
			user = ObjectUtil.mapper.readValue(json, User.class);
		} catch (Exception e) {
			user=null;
		}
		
		return user;
	}

	/**
	 * 通过ID寻找Uesr
	 * 请求url：http://sso.share.com/user/getUserByID
	 * 请求方法：get
	 * 请求参数：int id
	 * 返回值类型： User
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public User findById(int id) throws Exception {
		String url="http://sso.share.com/user/getUserByID?id="+id;
		String json = client.doGet(url);
		User user=ObjectUtil.mapper.readValue(json, User.class);
		//User user = userDao.getUserByID(id);
//		if(user != null) {
//			addAvatar(user);
//		}
		return user;
	}
	/**
	 * 通过IDList寻找UesrList
	 * 请求url：http://sso.share.com/user/getUsersByIDs
	 * 请求方法：post
	 * 请求参数：List<Integer> ids
	 * 返回值类型： List<User>
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public List<User> findAllbyIDs(List<Integer> ids) throws Exception {
		String url="http://sso.share.com/user/getUsersByIDs";
		String idds=ObjectUtil.mapper.writeValueAsString(ids);
		String json = client.doPostJson(url, idds);
		if(StringUtils.isNotEmpty(json)){
		List<User> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(json);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, User.class));
		}
		return pList;
		}else{
			return null;
		}
	}
	
	private void addAvatar(User user) {
		user.setUser_avatar(user.getUser_avatar());
	}
	
	
	/**
	 * 登录
	 * 请求url：http://sso.share.com/user/getPwdByEmail
	 * 请求参数：String email, String password
	 * 请求方法：get
	 * 返回数据：Map<String, Object>
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> login(String email, String password) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		//1 empty check
		if(email == null || email.length() <= 0) {
			ret.put("status", Property.ERROR_EMAIL_EMPTY);
			return ret;
		}
			
		if(password == null || password.length() <= 0){
			ret.put("status", Property.ERROR_PWD_EMPTY);
			return ret;
		}
			
		
		//2 ValidateEmail 
		if(!ValidateEmail(email)) {
			ret.put("status", Property.ERROR_EMAIL_FORMAT);
			return ret;
		}

		//3 email exist?
		User user = findByEmail(email);
		if(user == null) {
			ret.put("status", Property.ERROR_EMAIL_NOT_REG);
			return ret;
		}
		
		//4 check user status
		if(STATUS_USER_NORMAL != user.getUser_status()) {
			ret.put("status", user.getUser_status());
			return ret;
		}
		//5 password validate
		
		if(user.getUser_pwd().equals(MD5Util.md5(password))) {
			ret.put("status", Property.SUCCESS_ACCOUNT_LOGIN);
			ret.put("user", user);
			return ret;
		}else{
			ret.put("status", Property.ERROR_PWD_DIFF);
			return ret;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public String register(String username, String email, String password, String conformPwd, Map<String, String> map) throws Exception {
		//1 empty check
		if(email == null || email.length() <= 0)
			return Property.ERROR_EMAIL_EMPTY;
		else{
			//4 ValidateEmail
			if(!ValidateEmail(email))
				return Property.ERROR_EMAIL_FORMAT;
			
			//5 email exist?
			User user = findByEmail(email);
			if(user != null) {
							
				//6 user status check
				if(STATUS_USER_NORMAL == user.getUser_status())
					return Property.ERROR_ACCOUNT_EXIST;
				else if(STATUS_USER_INACTIVE == user.getUser_status()){
					map.put("activationKey", "123");
					return Property.ERROR_ACCOUNT_INACTIVE;
				}
				else if(STATUS_USER_LOCK == user.getUser_status())
					return Property.ERROR_ACCOUNT_LOCK;
				else if(STATUS_USER_CANCELLED == user.getUser_status()) 
					return Property.ERROR_ACCOUNT_CANCELLED;
			}			
		}
		
		if(username == null || username.length() == 0) 
			return Property.ERROR_USERNAME_EMPTY;
		else {
			//username exist check
			if(findByUsername(username) != null) {
				return Property.ERROR_USERNAME_EXIST;
			}
		}
		
		
		if(password == null || password.length() <= 0)
			return Property.ERROR_PWD_EMPTY;
		
		if(conformPwd == null || conformPwd.length() <= 0)
			return Property.ERROR_CFMPWD_EMPTY;
				
		//2 pwd == conformPwd ?
		if(!password.equals(conformPwd))
			return Property.ERROR_CFMPWD_NOTAGREE;
		String activationKey = "123";	
		Map<String, Object> map1=new HashMap<>();
		map1.put("user_name", username);
		//md5加密
		map1.put("user_pwd", MD5Util.md5(password));
		map1.put("user_email", email);
		map1.put("user_status", STATUS_USER_NORMAL);
		//设置默认头像
		String default_user_img="/defaultimg/";
		int imgRan=(int)Math.random()*10;
		
		map1.put("user_avatar", default_user_img+imgRan+".jpg");
		map1.put("user_activationKey", activationKey);
		
		String url="http://sso.share.com/user/register";
		
		HttpResult result=client.doPost(url, map1);
		String success=result.getBody();
		map.put("id", success);
		map.put("activationKey", "123");
		return Property.SUCCESS_ACCOUNT_REG;
		
	}
	/**
	 * 请求url：http://sso.share.com/user/updateActivationKey
	 * 请求方法：post
	 * 请求参数：User user
	 * 返回值： status
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> updateActivationKey(String email) throws Exception{
		//1 check user status
		User user = findByEmail(email);
		String status = null;
		if(user == null){
			status = Property.ERROR_EMAIL_NOT_REG;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user.getId());
		
		if(STATUS_USER_INACTIVE == user.getUser_status()){
			
			String jsonData=ObjectUtil.mapper.writeValueAsString(user);
			String url="http://sso.share.com/user/updateActivationKey";
			String activationKey=client.doPostJson(url, jsonData);
			status = Property.SUCCESS_ACCOUNT_ACTIVATION_KEY_UPD;
			map.put("activationKey", activationKey);
		} else {
			if(STATUS_USER_NORMAL == user.getUser_status())
				status = Property.ERROR_ACCOUNT_EXIST; //已激活
			else if(STATUS_USER_CANCELLED == user.getUser_status()) 
				status = Property.ERROR_ACCOUNT_CANCELLED;
			//status = Property.ERROR_ACCOUNT_ACTIVATION;
		}
		map.put("status", status);
		
		return map;
	}
	/**
	 * 请求url：http://sso.share.com/user/activateUser
	 * 请求方法：get
	 * 请求参数：String email String key
	 * 返回值：无
	 * @param email key
	 * @return
	 * @throws Exception 
	 */
	public String activateUser(String email, String key) throws Exception {
		User user = findByEmail(email);
		String url="http://sso.share.com/user/activateUser";
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("email", email);
		map.put("key", key);
		HttpResult result=client.doPost(url, map);
		String status=result.getBody();
		return status;
	}
	
	public String findActivationKeyOfUser(int id) {
		return null;
	}
	
	/**
	 * 根据用户活跃状态寻找用户
	 * http://sso.share.com/user/getUser
	 * 请求方法：get
	 * 请求参数：String key
	 * 返回数据：User
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public User findByActivationKey(String key) throws Exception {
		String  url ="http://sso.share.com/user/getUser?key="+key;
		String jsonData = client.doGet(url);
		User user=ObjectUtil.mapper.readValue(jsonData, User.class);
		return /*userDao.getUser("user_activationKey", new Object[]{key})*/user;
	}
	/**
	 * 通过邮箱获取状态
	 * 请求url：http://sso.share.com/user/getUserStatus
	 * 请求方法：get
	 * 请求参数：String email
	 * 返回数据：int
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	public int getStatus(String email) throws Exception {
		String  url ="http://sso.share.com/user/getUserByEmail?email="+email;
		String jsonData = client.doGet(url);
		User user=ObjectUtil.mapper.readValue(jsonData, User.class);
	//	User user = userDao.getUserByEmail(email);
		
		return /*user.getUserStatus()*/user.getUser_status();
	}
	
	/**
	 * 推荐用户
	 * 请求url：http://sso.share.com/user/getUsers
	 * 请求方法：get
	 * 请求参数：int user_id, int count
	 * 返回数据：List<User>
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	public List<User> getRecommendUsers(int user_id, int count) throws Exception{
		//to do recommend logic
		String url="http://sso.share.com/user/getUsers?count="+count;
		String jsonData=client.doGet(url);
		List<User> pList=null;
		
		if(StringUtils.isNotEmpty(jsonData)){
			JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, User.class));
		}
		}
		
		Iterator<User> it = pList.iterator();
		while(it.hasNext()){
			User user = it.next();
			if(user.getId() == user_id) {
				it.remove();
				continue;
			}
			//addAvatar(user);
		}
		return pList;
	}
	/**
	 * 获取推荐用户ID
	 * 请求url：http://sso.share.com/user/getUsers
	 * 请求方法：get
	 * 请求参数：int user_id, int count
	 * 返回数据：List<Integer>
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public List<Integer> getRecommendUsersID(int user_id, int count) throws Exception {
		String url="http://sso.share.com/user/getUsers?count="+count;
		
		String jsonData=client.doGet(url);
	
		List<User> pList=null;
		JsonNode data=ObjectUtil.mapper.readTree(jsonData);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, User.class));
		}

		List<Integer> ids =  new ArrayList<Integer>();
		Iterator<User> it = pList.iterator();
		while(it.hasNext()){
			User user = it.next();
			if(user.getId() == user_id) {
				it.remove();
				continue;
			}
			ids.add(user.getId());
		}
		return ids;
	}
	
	
	
	public Map<String, Long> getCounterOfFollowAndShortPost(int user_id) throws Exception{
		Map<String, Long> counter = new HashMap<String, Long>();
		counter.put("follower", followService.followersCount(user_id));
		counter.put("following", followService.followingsCount(user_id));
		counter.put("spost", shortPostService.count(user_id));
		return counter;
	}
	/**
	 * 修改avatar
	 * 请求url：http://sso.share.com/user/updateAvatar
	 * 请求方法：get
	 * 请求参数：int user_id, String avatar
	 * 返回数据：String
	 * @throws Exception 
	 */
	public String changeAvatar(int user_id, String avatar) throws Exception {
		String url="http://sso.share.com/user/updateAvatar?user_id="+user_id+"&avatar="+avatar;
		client.doGet(url);
		//userDao.updateAvatar(user_id, avatar);
		return Property.SUCCESS_AVATAR_CHANGE;
	}
	/**
	 * 更新用户名和描述
	 * 请求url：http://sso.share.com/user/updateUsernameAndDesc
	 * 请求方法：get
	 * 请求参数：int user_id, String username, String desc
	 * 返回数据：无
	 * @param user_id
	 * @param username
	 * @param desc
	 * @throws Exception 
	 */
	public void updateUsernameAndDesc(int user_id, String username, String desc) throws Exception{
		String url="http://sso.share.com/user/updateUsernameAndDesc";
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", user_id);
		map.put("user_name", username);
		map.put("user_desc", desc);
		client.doPost(url, map);
		//userDao.updateUsernameAndDesc(user_id, username, desc);
	}
	
	/**
	 * 根据邮箱更新密码
	 * 请求url：http://sso.share.com/user/updateResetPwdKey
	 * 请求方法：get
	 * 请求参数：String email
	 * 返回数据：key
	 * return reset password key
	 */
	public String updateResetPwdKey(String email) throws Exception{
		String key = MD5Util.md5(email);
		String url = "http://sso.share.com/user/updateResetPwdKey?user_email="+email+"&key="+key;
		client.doGet(url);
		return key;
	}
	

	/**
	 * 重置密码
	 * 请求url：http://sso.share.com/user/updatePassword
	 * 		  http://sso.share.com/user/updateResetPwdKey
	 * 请求方法：get
	 * 请求参数：String email, String password, String cfm_pwd
	 * 返回数据：wu
	 */
	public String resetPassword(String email, String password, String cfm_pwd){
		if( password == null || password.length() == 0){
			return Property.ERROR_PWD_EMPTY;
		}
		if(cfm_pwd == null || cfm_pwd.length()==0){
			return Property.ERROR_CFMPWD_EMPTY;
		}
			 
		if(!password.equals(cfm_pwd)) {
			return Property.ERROR_CFMPWD_NOTAGREE;
		}
		password=MD5Util.md5(password);
		String url ="http://sso.share.com/user/updatePassword?user_email="+email+"&user_pwd="+password;
	//	userDao.updateResetPwdKey(email, null);
		String url2 ="http://sso.share.com/user/updateResetPwdKey?user_email="+email+"&key="+222;
		String doGet1 = null;
		String doGet2 = null;
		try {
			 doGet1 = client.doGet(url);
			 doGet2 = client.doGet(url2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		if (Integer.parseInt(doGet1)>0&&Integer.parseInt(doGet2)>0) {
			return Property.SUCCESS_PWD_RESET;
		}else{
			return Property.ERROR_PWD_RESET;
		}	
	}
	
	/**
	 * 修改密码
	 * 请求url：http://sso.share.com/user/updatePassword
	 * 请求方法：get
	 * 请求参数：String email, String old_pwd, String new_pwd
	 * 返回数据：String
	 */
	public String changePassword(String email, String old_pwd, String new_pwd) throws Exception{
		if( old_pwd == null || old_pwd.length() == 0){
			return Property.ERROR_PWD_EMPTY;
		}
		if( new_pwd == null || new_pwd.length() == 0){
			return Property.ERROR_PWD_EMPTY;
		}
		if(new_pwd.equals(old_pwd)){
			return Property.ERROR_CFMPWD_SAME;
		}
		User user = findByEmail(email);
		
		String current_pwd = user.getUser_pwd();
		if(!current_pwd.equals(MD5Util.md5(old_pwd))){
			return Property.ERROR_PWD_NOTAGREE;
		}
		new_pwd=MD5Util.md5(new_pwd);
		String url ="http://sso.share.com/user/updatePassword?user_email="+email+"&user_pwd="+new_pwd;
		String s= client.doGet(url);
		
		if (Integer.parseInt(s)>0) {
			return Property.SUCCESS_PWD_CHANGE;
		}else{
			return Property.ERROR_PWD_RESET;
		}
		
	}
	
	public User getAuthor(int object_type, int object_id) throws Exception{
		if(object_type == Dic.OBJECT_TYPE_POST){
			return postService.getAuthorOfPost(object_id);
		} else if(object_type == Dic.OBJECT_TYPE_ALBUM){
			return albumService.getAuthorOfALbum(object_id);
		} else if(object_type == Dic.OBJECT_TYPE_SHORTPOST) {
			return shortPostService.getAuthorOfPost(object_id);
		} else if(object_type == Dic.OBJECT_TYPE_PHOTO) {
			return albumService.getAuthorOfPhoto(object_id);
		} else {
			return new User();
		}
	}
	
	
	public List<User> searchUserByName(String username) throws Exception {
		String url="http://sso.share.com/user/getUserofUsername?username="+username;
		String doGet = client.doGet(url);
		List<User> pList=null;
		if(StringUtils.isNotEmpty(doGet)){
		JsonNode data=ObjectUtil.mapper.readTree(doGet);
		if(data.isArray()&&data.size()>0){
			pList = ObjectUtil.mapper.readValue(data.traverse(),
		    ObjectUtil.mapper.getTypeFactory().
		    constructCollectionType(List.class, User.class));
		}
		}
		return pList;	
	}
}
