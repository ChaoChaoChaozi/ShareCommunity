package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.album.mapper")
public class StarterAlbum {
	public static void main(String[] args) {
		SpringApplication.run(StarterAlbum.class, args);
	}
}
