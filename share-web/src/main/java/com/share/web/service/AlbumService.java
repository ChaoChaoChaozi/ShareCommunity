package com.share.web.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.common.service.HttpClientService;
import com.share.pojo.Album;
import com.share.pojo.Photo;
import com.share.pojo.Tag;
import com.share.pojo.User;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.vo.HttpResult;
import com.share.web.upload.UploadAlbum;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class AlbumService {

	public static final int ALBUM_STAUS_NORMAL = 0;
	public static final int ALBUM_STAUS_TOBERELEASED = 1; // 待发布

	public static String IMG_BASE_URL = Property.IMG_BASE_URL;

	@Autowired
	private HttpClientService client;

	@Autowired
	private UploadAlbum uploadAlbum;

	// 图片格式校验
	public String validataImg(MultipartFile img) {

		return null;
	}

	/**
	 * 获取图片的类型 url
	 * 
	 * @param img
	 * @return
	 */
	public String getImgType(MultipartFile img) {
		String contentType = img.getContentType();
		return contentType.substring(contentType.indexOf('/') + 1);
	}

	/**
	 * 
	 * @param user_id
	 * @param title
	 * @param desc
	 * @param status
	 * @param cover
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> newAlbum(int user_id, String title, String desc, int status, String cover)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Album album = new Album();
		album.setUser_id(user_id);
		album.setAlbum_title(title);
		album.setAlbum_desc(desc);
		album.setStatus(status);
		album.setCover(cover);
		String url = "http://album.share.com/album/saveAlbum";
		String jsonAlbum = ObjectUtil.mapper.writeValueAsString(album);
		String _id = client.doPostJson(url, jsonAlbum);
		Integer id = Integer.parseInt(_id);
		if (id != 0) {
			album.setId(id);
			map.put("album", album);
			map.put("status", Property.SUCCESS_ALBUM_CREATE);
		} else {
			map.put("status", Property.ERROR_ALBUM_CREATE);
		}
		return map;
	}

	public void saveImgToLocal(MultipartFile img, String key) {
		try {
			BufferedImage imgBuf = ImageIO.read(img.getInputStream());
			ImageIO.write(imgBuf, getImgType(img), new File("C://share-upload" + "/" + key));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传图片 url：http：//album.share.com/photo/upload/ 提交方式：post 参数：MultipartFile
	 * img ，Photo details
	 * 
	 * @param img
	 * @return String etag
	 * @throws Exception
	 */
	public Map<String, Object> uploadPhoto(MultipartFile img) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Photo details = new Photo();
		String key = UUID.randomUUID().toString();
		details.setKey(key);
		String etag = uploadAlbum.uploadPhoto(img, details);
		if (etag == null || etag.length() == 0) {
			map.put("status", Property.ERROR_PHOTO_CREATE);
			return map;
		} else {
			map.put("key", etag);
			map.put("link", IMG_BASE_URL + key);
			map.put("status", Property.SUCCESS_PHOTO_CREATE);
		}
		return map;
	}

	/**
	 * 新增图片，添加信息 url:http://album.share.com/album/savePhoto/ 提交方式：post 参数：Photo
	 * details
	 * 
	 * @param album_id
	 * @param img
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> newPhoto(int album_id, MultipartFile img, String desc) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Photo details = new Photo();
		String key = UUID.randomUUID().toString();
		details.setKey(key);
		details.setAlbum_id(album_id);
		details.setDesc(desc);
		String url = uploadAlbum.uploadPhoto(img, details);

		if (url == null || url.length() == 0) {

			map.put("status", Property.ERROR_PHOTO_CREATE);
			return map;
		} else {
			details.setKey(url);
			map.put("status", Property.SUCCESS_PHOTO_CREATE);
		}
		String url2 = "http://album.share.com/album/savePhoto";
		String jDetails = ObjectUtil.mapper.writeValueAsString(details);
		String _photo_id = client.doPostJson(url2, jDetails);
		Integer photo_id = Integer.parseInt(_photo_id);
		if (photo_id == 0) {
			map.put("status", Property.ERROR_PHOTO_CREATE);
			return map;
		}
		details.setId(photo_id);
		map.put("photo", details);
		return map;
	}

	/**
	 * 
	 * @param album
	 * @return
	 * @throws Exception
	 */
	public List<Tag> newPhotos(Album album) throws Exception {
		// save tag
		Map<String, Object> tagsmap = tagService.newTags(album.getAlbum_tags_list());
		album.setAlbum_tags_list((List<Tag>) tagsmap.get("tags"));
		updateAlbumInfo(album);
		// save relation
		for (Tag tag : (List<Tag>) tagsmap.get("tags")) {
			relationService.newRelation(RelationService.RELATION_TYPE_ALBUM, album.getId(), tag.getId());
		}
		List<Photo> photos = album.getPhotos();
		for (Photo photo : photos) {
			photo.setAlbum_id(album.getId());
			String url = "http://album.share.com/album/savePhoto";
			String jDetails = ObjectUtil.mapper.writeValueAsString(photo);
			String _photo_id = client.doPostJson(url, jDetails);
			Integer photo_id = Integer.parseInt(_photo_id);
			photo.setId(photo_id);
		}
		return (List<Tag>) tagsmap.get("tags");

	}

	/**
	 * 通过album_id，user_id查询相册
	 * url:http://album.share.com/get/albumuser/{album_id} 提交类型：post 参数：int
	 * album_id
	 * 
	 * @param album_id
	 * @param user_id
	 * @return int id
	 * @throws Exception
	 */
	public String checkUserOfAlbum(int album_id, int user_id) throws Exception {
		String url = "http://album.share.com/album/get_albumUser/" + album_id;
		String id = client.doGet(url);
		if (Integer.parseInt(id) != user_id) {
			return Property.ERROR_ALBUM_PERMISSIONDENIED;
		} else {
			return Property.SUCCESS_ALBUM_ALLOWED;
		}
	}

	/**
	 * 获取用户的待发布相册 url:http://album.share.com/get/album_id/ 提取方式：post 参数：int
	 * user_id
	 * 
	 * @param user_id
	 * @return Integer user_id
	 * @throws Exception
	 */
	public Integer getToBeReleasedAlbum(int user_id) throws Exception {
		String url = "http://album.share.com/album/get_albumId" + user_id;
		String id = client.doGet(url);
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return Integer.parseInt(id);
	}

	/**
	 * 通过相册id获取相册中的图片 url://http://album.share.com/get/photos_of_album/
	 * 提交方式：post 参数：int album_id
	 * 
	 * @param album_id
	 * @return List<Photo> photos
	 * @throws Exception
	 */
	public List<Photo> getPhotosOfAlbum(int album_id) throws Exception {
		String url = "http://album.share.com/album/getPhotos/" + album_id;
		String jsonPhoto = client.doGet(url);
		List<Photo> pList = null;
		JsonNode photo = ObjectUtil.mapper.readTree(jsonPhoto);
		if (photo.isArray() && photo.size() > 0) {
			pList = ObjectUtil.mapper.readValue(photo.traverse(),
					ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Photo.class));
		}
		return pList;
	}

	/**
	 * 更新相册的描述信息 url:http://album.share.com/album/update_albumDesc/ 提交类型：post
	 * 参数：int album_id, String album_desc,ALBUM_STAUS_NORMAL
	 * 
	 * @param album_id
	 * @param album_desc
	 * @return int effRows
	 * @throws Exception
	 * 
	 */
	public String updateAlbumDesc(int album_id, String album_desc) throws Exception {
		String url = "http://album.share.com/album/update_albumDesc/" + album_id + "/" + album_desc + "/"
				+ ALBUM_STAUS_NORMAL;
		String aDesc = client.doGet(url);
		int effRows = Integer.parseInt(aDesc);
		if (effRows == 1) {
			return Property.SUCCESS_ALBUM_UPDATE;
		} else {
			return Property.ERROR_ALBUM_UPDDESC;
		}
	}

	/**
	 * 通过photo_id更新照片的描述desc url：http://album.share.com/album/update_photoDesc/
	 * 提交方式：post 参数：photo.getId(), photo.getDesc() 无返回值
	 * 
	 * @param photos
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String updatePhotoDesc(List<Photo> photos) throws Exception {

		String url = "http://album.share.com/album/update_photoDesc";
		String json=ObjectUtil.mapper.writeValueAsString(photos);
		client.doPostJson(url, json);

		return Property.SUCCESS_ALBUM_UPDATE;
	}

	/**
	 * 更新相册的cover属性 url:http://album.share.com/album/update_cover/ 提交方式：post
	 * 参数：album_id, cover
	 * 
	 * @param album_id
	 * @param cover
	 * @return int
	 * @throws Exception
	 */
	public String updateAlbumCover(int album_id, String cover) throws Exception {
		String url = "http://album.share.com/album/update_albumCover/" + album_id + "/" + cover;
		String pCover = client.doGet(url);
		int effRows = Integer.parseInt(pCover);
		if (effRows == 1) {
			return Property.SUCCESS_ALBUM_UPDATE;
		} else {
			return Property.ERROR_ALBUM_UPDCOVER;
		}
	}

	/**
	 * 更新照片的数量 url:http://album.share.com/album/update_photos_count/ 提交方式：post
	 * 参数：album_id, count
	 * 
	 * @param album_id
	 * @param count
	 * @return int
	 * @throws Exception
	 */
	public String updatePhotosCount(int album_id, int count) throws Exception {
		String url = "http://album.share.com/album/update_photosCount/" + album_id + "/" + count;
		String pCount = client.doGet(url);
		int effRows = Integer.parseInt(pCount);
		if (effRows == 1) {
			return Property.SUCCESS_ALBUM_UPDATE;
		} else {
			return Property.ERROR_ALBUM_UPDCOVER;
		}

	}

	/**
	 * 通过album_id更新相册的各个信息 url：http://album.share.com/album/update_albumInfo
	 * 提交方式：post 参数：int album_id
	 * 
	 * @param album
	 * @return String
	 * @throws Exception
	 */
	public String updateAlbumInfo(Album album) throws Exception {
		String jsonAlbum = ObjectUtil.mapper.writeValueAsString(album);
		String url = "http://album.share.com/album/update_albumInfo/";
		client.doPostJson(url, jsonAlbum);
		return Property.SUCCESS_ALBUM_UPDATE;
	}

	/**
	 * 更新相册信息 url:http://album.share.com/album/update_info/{album_id} 提交方式：post
	 * 参数：album.album_id
	 * 
	 * @param album
	 * @return
	 */
	@Autowired
	private TagService tagService;
	@Autowired
	private RelationService relationService;

	public List<Tag> updateAlbum(Album album) throws Exception {
		// save tag
		Map<String, Object> tagsmap = tagService.newTags(album.getAlbum_tags_list());
		album.setAlbum_tags_list((List<Tag>) tagsmap.get("tags"));
		updateAlbumInfo(album);
		updatePhotoDesc(album.getPhotos());
		// save relation
		for (Tag tag : (List<Tag>) tagsmap.get("tags")) {
			relationService.newRelation(RelationService.RELATION_TYPE_ALBUM, album.getId(), tag.getId());
		}
		return (List<Tag>) tagsmap.get("tags");
	}

	/**
	 * 通过user_id获取对象album信息 url:http://album.share.com/album/get_album/{id}
	 * 提交方式：post 参数：int id
	 * 
	 * @param id
	 * @return List<Album> album
	 * @throws Exception
	 */
	public List<Album> getAlbumsOfUser(int id) throws Exception {
		String url = "http://album.share.com/album/get_albumsOfUser/" + id;
		String jsonAlbum = client.doGet(url);
		List<Album> aList = null;
		JsonNode photo = ObjectUtil.mapper.readTree(jsonAlbum);
		if (photo.isArray() && photo.size() > 0) {
			aList = ObjectUtil.mapper.readValue(photo.traverse(),
					ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Album.class));
		}
		return aList;
	}

	/**
	 * 通过id获取相册对象 url：http://album.share.com/get/album/{id} 提交方式：get 参数：int id
	 * 
	 * @param id
	 * @return Album album
	 * @throws Exception
	 */
	public Album getAlbum(int id) throws Exception {
		String url = "http://album.share.com/album/getAlbum/" + id;
		String jsonAlbum = client.doGet(url);
		Album album = ObjectUtil.mapper.readValue(jsonAlbum, Album.class);
		List<Photo> photos = getPhotosOfAlbum(id);
		album.setPhotos(photos);
		return album;
	}

	/**
	 * 通过id获取照片的key值 url：http://album.share.com/get/key_of_photo/{id} 提交方式：post
	 * 参数：int id
	 * 
	 * @param id
	 * @return String key
	 * @throws Exception
	 */
	public String getKeyofPhoto(int id) throws Exception {
		String url = "http://album.share.com/album/getKey/" + id;
		String key = client.doGet(url);
		return key;
	}

	/**
	 * 通过id获取相册的创建者对象 url:http://album.share.com/get/author_of_album/{id}
	 * 提交方式：post 参数：int id
	 * 
	 * @param id
	 * @return User user
	 * @throws Exception
	 */
	@Autowired
	private UserService userService;

	public User getAuthorOfALbum(int id) throws Exception {
		String url = "http://album.share.com/album/getAuthorOfAlbum/" + id;
		String userid = client.doGet(url);
		if (StringUtils.isNotEmpty(userid)) {
			int user_id = Integer.parseInt(userid);
			return userService.findById(user_id);
		} else {
			return null;
		}
	}

	/**
	 * 通过photo表id值查看album表中的user_id,不用多表联查
	 * url:http://album.share.com/album/get_photo_author/{id} 提交方式：post 参数：int
	 * id
	 * 
	 * @param id
	 * @return User user_
	 * @throws Exception
	 */
	public User getAuthorOfPhoto(int id) throws Exception {
		String url = "http://album.share.com/album/getAlbumContainPhoto/" + id;
		String jsonAlbum = client.doGet(url);
		Album album = ObjectUtil.mapper.readValue(jsonAlbum, Album.class);
		User user = new User();
		if (album != null) {
			user.setId(album.getUser_id());
		}
		return user;
	}

	/**
	 * 通过照片key删除在bucket的照片
	 * url:http://album.share.com/photo/delete_inBucket/{key} 提交方式：post
	 * 参数：String key 无返回值
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public String cropAvatar(String key, int x, int y, int width, int height) throws Exception {
		String classpath = AlbumService.class.getClassLoader().getResource("").getPath();
		try {
			File ori_img = new File("c://share-upload" + "/" + key);

			BufferedImage croped_img = ImageIO.read(ori_img);/*Thumbnails.of(ImageIO.read(ori_img)).sourceRegion(x, y, width, height)
					.size(200, 200).asBufferedImage();*/
			String img_type = key.split("\\.")[1];
			// convert bufferedimage to inputstream
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(croped_img, img_type, bos);

			// uploadAlbum.delPhotoInBucket(key);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * 通过id删除照片 url:http://album.share.com/photo/delete/{id} 提交方式：post 参数：int id
	 * 无返回值
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deletePhoto(int id) throws Exception {
		String url = "http://album.share.com/album/deletePhoto/" + id;
		client.doGet(url);
	}

}
