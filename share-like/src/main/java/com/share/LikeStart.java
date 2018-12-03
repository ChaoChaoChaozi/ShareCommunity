package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.like.mapper")
public class LikeStart {
	public static void main(String[] args) {
		SpringApplication.run(LikeStart.class, args);
	}

}
