package com.share.album.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.multipart.MultipartFile;

import com.share.album.pojo.Album;
import com.share.album.pojo.Photo;

public interface AlbumMapper {

	Integer saveAlbum(Album album);
	
	Integer savePhoto(Photo photo);

	String getKey(int id);
	
	Integer getAlbumUser(int id);
	
	Integer getAlbumID(@Param("user_id")int user_id, @Param("status") int status);
	
	Integer updateAlbumInfo(Album album);
	
	Integer updateAlbumDesc(@Param("album_id")int album_id, @Param("album_desc")String album_desc, @Param("album_status")int album_status);
	
	Integer updateAlbumCover(@Param("album_id")int album_id, @Param("cover")String cover);
	
	
	Integer updatePhotosCount(@Param("album_id")int album_id, @Param("count")int count);
	

	Integer updatePhotoDesc(@Param("photo_id")int photo_id, @Param("photo_desc")String photo_desc);
	
	Album getAlbum(int id);
	
	List<Album> getAlbumsOfUser(int id);

	List<Photo> getPhotos(int album_id);
	
	Integer getAuthorOfAlbum(int id);
	
	Album getAlbumContainPhoto(int photo_id);
	
	Integer deletePhoto(int id);
	
}
