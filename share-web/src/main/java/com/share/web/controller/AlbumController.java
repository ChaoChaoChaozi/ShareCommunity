package com.share.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.share.pojo.Album;
import com.share.pojo.Photo;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.CookieFild;
import com.share.util.CookieUtils;
import com.share.util.Dic;
import com.share.util.MD5Util;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.web.service.AlbumService;
import com.share.web.service.EventService;
import com.share.web.service.FeedService;
import com.share.web.service.FollowService;
import com.share.web.service.InterestService;
import com.share.web.service.TagService;
import com.share.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/album")
public class AlbumController {

	@Autowired
	private JedisCluster redis;
	@Autowired
	private AlbumService albumService;

	@Autowired
	private EventService eventService;

	@Autowired
	private FeedService feedService;

	@Autowired
	private InterestService interestService;

	@Autowired
	private FollowService followService;

	@Autowired
	private UserService userService;

	/**
	 * 根据图片id进行搜索图片信息 整合相册信息
	 * 
	 * @param id
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/{id}/photos")
	public String album(@PathVariable("id") int id, HttpServletRequest req, Model model,HttpSession session) throws Exception {
		// 获取是否登陆
		User me = (User) session.getAttribute("user");
		Album album = albumService.getAlbum(id);
		model.addAttribute("album", album);
		User author = albumService.getAuthorOfALbum(id);
		model.addAttribute("u", author);
		model.addAttribute("follow", followService.isFollowing(me == null ? 0 : me.getId(), author.getId()));
		return "album/index";
	}

	@ResponseBody
	@RequestMapping("/{id}")
	public Album getAlbumInfo(@PathVariable("id") int id) throws Exception {
		Album album = albumService.getAlbum(id);
		System.out.println(album);
		return album;
	}

	/*
	 * 相册上传页面 指定album
	 */
	@RequestMapping(value = "/{album_id}/upload", method = RequestMethod.GET)
	public String albumUploadPage(@PathVariable("album_id") int id) {
		return "album/upload";
	}

	/*
	 * 相册上传页面 未指定album
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String albumUploadPage(HttpSession session, Model model) throws Exception {

		User user = (User) session.getAttribute("user");
		Integer album_id=null; 
		if(user!=null)
		album_id= albumService.getToBeReleasedAlbum(user.getId());
		if (album_id != null) {
			List<Photo> photos = albumService.getPhotosOfAlbum(album_id);
			session.setAttribute("album_id", album_id);
			// mav.addObject("photos", photos);
			model.addAttribute("photos", photos);
			session.setAttribute("photos", photos);
		} else {
			session.setAttribute("photos", new ArrayList<Photo>());
		}
		return "album/upload";
	}

	@ResponseBody
	@RequestMapping(value = "/delete/photo/{id}", method = RequestMethod.GET)
	public Map<String, Object> deletePhoto(@PathVariable("id") int id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		albumService.deletePhoto(id);
		map.put("status", Property.SUCCESS_PHOTO_DELETE);
		return map;
	}

	/*
	 * 上传图片到相册
	 */
	@ResponseBody
	@RequestMapping(value = "/{album_id}/upload/photo", method = RequestMethod.POST)
	public Map<String, Object> albumUpload(@PathVariable("album_id") int album_id,
			@RequestParam("uploader_input") MultipartFile img, HttpSession session) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		if (img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}
		User user = (User) session.getAttribute("user");
		// 检查相册是否属于用户
		if (!Property.SUCCESS_ALBUM_ALLOWED.equals(albumService.checkUserOfAlbum(album_id, user.getId()))) {
			map.put("status", Property.ERROR_ALBUM_PERMISSIONDENIED);
			return map;
		}
		// 上传图片
		Map<String, Object> photoMap = albumService.newPhoto(album_id, img, null);
		map.put("status", photoMap.get("status"));
		map.put("photo", photoMap.get("photo"));

		return map;
	}
	
	
	
//*************************************************
	private Album toAlbum(String params) throws Exception {
		Album album = new Album();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(params);

			album.setAlbum_desc(root.path("album_desc").textValue());

			JsonNode photos = root.path("photos");
			if (photos.size() > 0) {
				album.setCover(albumService.getKeyofPhoto(Integer.parseInt(photos.get(0).path("id").textValue())));

				List<Photo> photos2upd = new ArrayList<Photo>();
				album.setPhotos(photos2upd);
				for (int i = 0; i < photos.size(); i++) {
					int photo_id = Integer.parseInt(photos.get(i).path("id").textValue());
					String photo_desc = photos.get(i).path("desc").textValue();
					Photo photo = new Photo();
					photo.setId(photo_id);
					photo.setDesc(photo_desc);
					photos2upd.add(photo);

					System.out.println("photo_id:" + photo_id + " desc:" + photo_desc);
				}
				album.setLike_count(photos2upd.size());
			}

			JsonNode tags = root.path("tags");
			if (tags.size() > 0) {
				List<Tag> tag_list = new ArrayList<Tag>();
				album.setAlbum_tags_list(tag_list);
				album.setAlbum_tags(TagService.toString(tag_list));
				for (int i = 0; i < tags.size(); i++) {
					Tag t = new Tag();
					t.setTag(tags.get(i).textValue());
					tag_list.add(t);
				}
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return album;
	}

	/*
	 * 创建相册
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, Object> createAlbum(@RequestBody String params, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		System.out.println(params);

		Album album = toAlbum(params);
		if (album.getLike_count() == 0) {
			map.put("status", Property.ERROR_ALBUM_EMPTY);
			return map;
		}

		
		User user = (User) session.getAttribute("user");
		List<Album> list = albumService.getAlbumsOfUser(user.getId());
		album.setId(list.get(list.size()-1).getId()); 
		album.setUser_id(user.getId());
		album.setPhotos((List<Photo>) session.getAttribute("photos"));
		List<Tag> tags = albumService.updateAlbum(album);

		int event_id = eventService.newEvent(Dic.OBJECT_TYPE_ALBUM, album);

		// push to users who follow u
		List<Integer> followers = followService.getFollowerIDs(user.getId());
		if(followers!=null)
		followers.add(user.getId());
		else {followers=new ArrayList<>(); followers.add(user.getId());}
		feedService.push(followers, event_id);

		// push to users who follow the tags
		Set<Integer> followers_set = new HashSet<Integer>();
		for (Tag tag : tags) {
			List<Integer> i_users = interestService.getUsersInterestedInTag(tag.getId());
			if(i_users!=null)
			for (Integer u : i_users) {
				if (u != user.getId())
					followers_set.add(u);
			}

			// cache feeds to tag list
			feedService.cacheFeed2Tag(tag.getId(), event_id);
		}
		feedService.push(new ArrayList<Integer>(followers_set), event_id);

		map.put("album", album);
		map.put("status", Property.SUCCESS_ALBUM_UPDATE);
		return map;
	}

	/*
	 * 未指定相册 临时创建相册
	 */
	@ResponseBody
	@RequestMapping(value = "/upload/photo", method = RequestMethod.POST)
	public Map<String, Object> uploadPhoto(@RequestParam("uploader_input") MultipartFile img, HttpSession session) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}

		User user = (User) session.getAttribute("user");
		Integer album_id = (Integer) session.getAttribute("album_id");
		if(user==null){
			user=new User();
		}
		// 创建临时相册
		if (album_id == null || album_id == 0) {
			Map<String, Object> albumMap = albumService.newAlbum(user.getId(), null, null,
					AlbumService.ALBUM_STAUS_TOBERELEASED, null);
			if (!Property.SUCCESS_ALBUM_CREATE.equals(albumMap.get("status"))) {
				map.put("status", albumMap.get("status"));
				return map;
			}
			album_id = ((Album) albumMap.get("album")).getId();
			session.setAttribute("album_id", album_id);
		}
		// 上传图片
		Map<String, Object> photoMap = albumService.newPhoto(album_id, img, null);
		Photo photo = (Photo) photoMap.get("photo");

		List<Photo> photos = (List<Photo>) session.getAttribute("photos");
		if (photos == null) {
			photos = new ArrayList<Photo>();
			session.setAttribute("photots", photos);
		}
		photos.add(photo);
		map.put("status", photoMap.get("status"));
		map.put("id", photo.getId());
		map.put("key", photo.getKey());
		return map;
	}

	/*
	 * post 中图片上传
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/upload/postphoto", method = RequestMethod.POST)
	public Map<String, Object> postPhotoUpload(@RequestParam("uploader_input") MultipartFile img, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		if (img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}

		// upload photo
		map = albumService.uploadPhoto(img);
		// set post cover
		session.setAttribute("post_cover", map.get("key"));
		return map;
	}

	/**
	 * 上传头像
	 * 
	 * @param img
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/upload/avatar", method = RequestMethod.POST)
	public Map<String, Object> avatarUpload(@RequestParam("avatar_file") MultipartFile img, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		if (img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}

		// upload photo
		map = albumService.uploadPhoto(img);

		// save to local
		albumService.saveImgToLocal(img, (String) map.get("key"));
		System.out.println("上传头像："+map.get("key"));
		session.setAttribute("temp_avatar", map.get("key"));

		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/cropavatar", method = RequestMethod.POST)
	public Map<String, Object> cropAvatar(@RequestParam("x") int x, @RequestParam("y") int y,
			@RequestParam("width") int width, @RequestParam("height") int height, HttpSession session) throws Exception {

		// System.out.println("x:"+x+" y:"+y + " width:"+width+ "
		// height:"+height);
		Map<String, Object> map = new HashMap<String, Object>();

		String key = (String) session.getAttribute("temp_avatar");
		if (key == null || key.length() == 0) {
			map.put("status", Property.ERROR_AVATAR_CHANGE);
			return map;
		}

		String avatar_img = albumService.cropAvatar(key, x, y, width, height);
		
		String status = userService.changeAvatar(((User) session.getAttribute("user")).getId(), avatar_img);
		if (Property.SUCCESS_AVATAR_CHANGE.equals(status)) {
			// update session
			((User) session.getAttribute("user")).setUser_avatar(avatar_img);
		}

		map.put("status", status);
		return map;

	}

}
