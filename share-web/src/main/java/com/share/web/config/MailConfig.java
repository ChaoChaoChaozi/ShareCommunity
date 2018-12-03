package com.share.web.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class MailConfig {
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.username}")
	private String account;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String isAuth;
	@Value("${spring.mail.smtp.timeout}")
	private String outTime;
 
	@Bean(name = "javaMailSender")
	public JavaMailSenderImpl getMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);
		javaMailSender.setUsername(account);
		javaMailSender.setPassword(password);
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", isAuth);
		properties.put("mail.smtp.timeout", outTime);
		javaMailSender.setJavaMailProperties(properties);
		return javaMailSender;
	}

	
}
