package com.share.web.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import com.share.mail.EMail;


/**
 * 邮件发送服务
 * @author chennan
 *
 */
@Service
public class MailService {
	@Autowired
	@Qualifier("javaMailSender")
	private JavaMailSenderImpl mail;
	@Value("${spring.mail.username}")
    private String sendName;//发送者

	public void sendMail(EMail email) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(sendName);
		String receiver = email.getEmail();
		simpleMailMessage.setTo(receiver);
		simpleMailMessage.setSubject(email.getSubject());
		simpleMailMessage.setText(email.getContent());
		simpleMailMessage.setSentDate(new Date());
		mail.send(simpleMailMessage);
	}
}
