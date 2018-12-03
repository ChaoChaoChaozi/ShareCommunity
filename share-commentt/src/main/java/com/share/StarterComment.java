package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.comment.mapper")
public class StarterComment {
	public static void main(String[] args) {
		SpringApplication.run(StarterComment.class, args);
	}
}
