package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.event.mapper")
public class StarterEvent {
	public static void main(String[] args) {
		SpringApplication.run(StarterEvent.class, args);
	}
}
