package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.sso.mapper")
public class StarterSSO {
	public static void main(String[] args) {
		SpringApplication.run(StarterSSO.class,args);
	}
}
