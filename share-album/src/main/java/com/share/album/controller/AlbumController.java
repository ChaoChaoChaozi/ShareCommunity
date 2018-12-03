package com.share.album.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.share.album.mapper.AlbumMapper;
import com.share.album.pojo.Album;
import com.share.album.pojo.Photo;
import com.share.util.ObjectUtil;

@RestController
public class AlbumController {
	
	public static final int ALBUM_STAUS_NORMAL = 0;
	public static final int ALBUM_STAUS_TOBERELEASED = 1; // 待发布
	@Autowired
	private AlbumMapper albumMapper;
	
	
	@RequestMapping("album/saveAlbum")
	public Integer saveAlbum(@RequestBody String jsonAlbum) throws Exception{
		Album album = (Album) ObjectUtil.mapper.readValue(jsonAlbum, Album.class);
		albumMapper.saveAlbum(album);
	
		return album.getId();
	}
	

	
	@RequestMapping("album/savePhoto")
	public Integer savePhoto(@RequestBody String jDetails) throws Exception{
		Photo photo = ObjectUtil.mapper.readValue(jDetails, Photo.class);
		albumMapper.savePhoto(photo);
		
		return photo.getId();
	}
	
	@RequestMapping("album/get_albumUser/{album_id}")
	public String getAlbumUser(@PathVariable int album_id){
		return albumMapper.getAlbumUser(album_id)+"";
	}
	
	@RequestMapping("album/get_albumId/{user_id}")
	public Integer getAlbumId(@PathVariable int user_id){
		int status=ALBUM_STAUS_TOBERELEASED;
		return albumMapper.getAlbumID(user_id,status);
	}
	
	
	@RequestMapping("album/getPhotos/{album_id}")
	public List<Photo> getPhotos(@PathVariable int album_id){
		return albumMapper.getPhotos(album_id);
	}
	
	@RequestMapping("album/update_albumDesc/{album_id}/{album_desc}")
	public String getPhotos(@PathVariable int album_id,@PathVariable String album_desc){
		int album_status=ALBUM_STAUS_NORMAL;
		return albumMapper.updateAlbumDesc(album_id,album_desc,album_status)+"";
	}
	
	@RequestMapping("album/update_photoDesc")
	public void updatePhotoDesc(@RequestBody String jsonData) throws Exception{
		List<Photo> pList = null;
		JsonNode photo = ObjectUtil.mapper.readTree(jsonData);
		if (photo.isArray() && photo.size() > 0) {
			pList = ObjectUtil.mapper.readValue(photo.traverse(),
					ObjectUtil.mapper.getTypeFactory().constructCollectionType(List.class, Photo.class));
		}
		if(pList!=null)
		for (Photo jsonNode : pList) {
			albumMapper.updatePhotoDesc(jsonNode.getId(),jsonNode.getDesc());
		}
		
		
	}
	
	@RequestMapping("album/update_albumCover/{album_id}/{cover}")
	public String updateAlbumCover(@PathVariable int album_id,@PathVariable String cover){
		return albumMapper.updateAlbumCover(album_id,cover)+"";
	}
	
	@RequestMapping("album/update_photosCount/{album_id}/{count}")
	public String updatePhotosCount(@PathVariable int album_id,@PathVariable int count){
		return albumMapper.updatePhotosCount(album_id,count)+"";
	}
	
	
	@RequestMapping("album/update_albumInfo")
	public void updateAlbumInfo(@RequestBody String jsonAlbum) throws Exception{
		
		Album album = ObjectUtil.mapper.readValue(jsonAlbum, Album.class);
		albumMapper.updateAlbumInfo(album);
	}
	
	@RequestMapping("album/get_albumsOfUser/{id}")
	public List<Album> getAlbumOfUser(@PathVariable int id){
		
		return albumMapper.getAlbumsOfUser(id);//*******返回值
	}
	
	@RequestMapping("album/getAlbum/{id}")
	public Album getAlbum(@PathVariable int id){
		Album album = albumMapper.getAlbum(id);
		
		List<Photo> photos = albumMapper.getPhotos(id);
		album.setPhotos(photos);
		return album;
	}
	
	@RequestMapping("album/getKey/{id}")
	public String getKey(@PathVariable int id){
		return albumMapper.getKey(id);
	}
	
	@RequestMapping("album/getAuthorOfAlbum/{id}")
	public Integer getAuthorOfAlbum(@PathVariable int id){
		
		Integer author = albumMapper.getAuthorOfAlbum(id);
		
		return author;
	}
	
	@RequestMapping("album/getAlbumContainPhoto/{id}")
	public Album getAlbumContainPhoto(@PathVariable int id){
		return albumMapper.getAlbumContainPhoto(id);
	}
	
	@RequestMapping("album/deletePhoto/{id}")
	public void deletePhoto(@PathVariable int id){
		albumMapper.deletePhoto(id);
	}
}
