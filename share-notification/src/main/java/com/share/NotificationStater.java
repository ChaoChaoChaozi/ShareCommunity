package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.notification.mapper")
public class NotificationStater {

	public static void main(String[] args) {
		SpringApplication.run(NotificationStater.class, args);
	}
}
