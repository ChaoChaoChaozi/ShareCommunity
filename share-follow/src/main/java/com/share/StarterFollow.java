/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.share;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.share.follow.mapper")
public class StarterFollow{
	public static void main(String[] args) {
		SpringApplication.run(StarterFollow.class, args);
	}
}