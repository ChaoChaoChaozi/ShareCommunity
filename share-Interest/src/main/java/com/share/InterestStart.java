package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.interest.mapper")
public class InterestStart {
	public static void main(String[] args) {
		SpringApplication.run(InterestStart.class, args);
	}

	
}
