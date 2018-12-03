package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.tag.mapper")
public class StarterTag {
	public static void main(String[] args) {
		SpringApplication.run(StarterTag.class, args);
	}
}
