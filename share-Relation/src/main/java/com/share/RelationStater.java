package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.relation.mapper")
public class RelationStater{
	public static void main(String[] args) {
		SpringApplication.run(RelationStater.class, args);
	}
}