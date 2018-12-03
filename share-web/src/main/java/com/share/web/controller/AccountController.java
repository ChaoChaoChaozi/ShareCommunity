package com.share.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.share.mail.EMail;
import com.share.pojo.User;
import com.share.util.CookieFild;
import com.share.util.Property;
import com.share.util.VerifyCode;
import com.share.vo.Result;
import com.share.web.service.MailService;
import com.share.web.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	/**
	 * 检测是否登陆
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpSession session) {
		if (session.getAttribute("user") != null)
			return "redirect:/";

		return "account/login";
	}
	/**
	 * 发送邮箱验证码
	 * @param userEmail
	 * @return
	 */
	
	@RequestMapping(value="/sendEmailAck")
	@ResponseBody
	public Map<String, String> sendEmailAck(String email,HttpSession session){
		System.out.println("发送验证码！"+email);
		EMail eMail = new EMail();
		String randomCode = VerifyCode.getRandomCode();
		session.setAttribute("emailAck", randomCode);
		eMail.setContent(email+"您正在注册Let' Share 空间分享网站，您的验证码是："+randomCode);
		eMail.setEmail(email);
		eMail.setSubject("Let' Share 空间分享");
		Map<String, String> map=new HashMap<>();
		try{
			mailService.sendMail(eMail);
			map.put("status","1");
		}catch(Exception e){
			map.put("status","0");
		}
		
		
	
		return map;
	}
	/**
	 * 跳转到个人信息修改页面
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/setting/info", method = RequestMethod.GET)
	public String settingInfoPage(HttpSession session) {
		return "account/setting/info";
	}

	/**
	 * 成功
	 * @param user_name
	 * @param user_desc
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setting/info", method = RequestMethod.POST)
	public Map<String, Object> settingInfo(@RequestParam("user_name") String user_name,
			@RequestParam("user_desc") String user_desc, HttpSession session) throws Exception {
		// 从session中获取user
		User me = (User) session.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();

		String status = null;
		// 判断用户名是否为空
		if (user_name == null || user_name.length() == 0) {
			user_name = me.getUser_name();
		} else {
			// 查看修改的用户名在数据库中是否已存在
			User user = userService.findByUsername(user_name);
			// 用户名存在
			if (user != null) {
				status = Property.ERROR_USERNAME_EXIST;
				map.put("status", status);
				return map;
			}
		}

		// 用户名不存在
		status = Property.ERROR_USERNAME_NOTEXIST;
		// 根据用户id修改用户名和签名
		userService.updateUsernameAndDesc(me.getId(), user_name, user_desc);
		// 更新session状态
		me.setUser_name(user_name);
		me.setUser_desc(user_desc);

		map.put("status", status);
		return map;
	}

	/**
	 * 跳转到头像修改页面
	 * 
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/setting/avatar")
	public String settingAvatar(HttpSession session, Model mode) {
		return "account/setting/avatar";
	}

	/**
	 * 跳转到安全页面
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/setting/security")
	public String settingSecurity() {
		return "account/setting/security";
	}

	
	/**
	 * 修改密码
	 * 
	 * @param key
	 * @param email
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetpwd")
	public String resetpwdPage(@RequestParam("key") String key, @RequestParam("email") String email,
			HttpSession session, Model model) throws Exception {
		// set user login
		User user = userService.findByEmail(email);
		session.setAttribute("user", user);

		String status = null;
		
		status = Property.SUCCESS_PWD_RESET_ALLOWED;
	
		model.addAttribute("status", status);
		// model放入值进行判断是否成功
		model.addAttribute("SUCCESS_PWD_RESET_ALLOWED", Property.SUCCESS_PWD_RESET_ALLOWED);
		model.addAttribute("ERROR_PWD_RESET_NOTALLOWED", Property.ERROR_PWD_RESET_NOTALLOWED);
		return "account/resetpwd";
	}

	/**
	 * 重置密码
	 * 
	 * @param password
	 * @param cfm_pwd
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
	public Map<String, Object> resetpwd(@RequestParam("password") String password,
			@RequestParam("cfm_pwd") String cfm_pwd, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");

		map.put("status", userService.resetPassword(user.getUser_email(), password, cfm_pwd));
		return map;
	}

	/**
	 * 修改密码
	 * 
	 * @param old_pwd
	 * @param new_pwd
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/changepwd", method = RequestMethod.POST)
	public Map<String, Object> changepwd(@RequestParam("old_pwd") String old_pwd,
			@RequestParam("new_pwd") String new_pwd, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		map.put("status", userService.changePassword(user.getUser_email(), old_pwd, new_pwd));
		return map;
	}

	/**
	 * 发送邮件
	 * 
	 * @param session
	 * @return
	 * @throws Exception 
	 */

	@ResponseBody
	@RequestMapping(value = "/send_resetpwd_email")
	public Map<String, Object> sendResetPwdEmail(HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		EMail email=new EMail();
		email.setEmail(user.getUser_email());
		String randomCode = VerifyCode.getRandomCode();
		session.setAttribute("emailAck", randomCode);
		email.setContent(user.getUser_email()+"您正在修改Let' Share 空间分享网站密码，您的验证码是："+randomCode);
		email.setSubject("Let' Share 空间分享");
		mailService.sendMail(email);
		 
		map.put("status", Property.SUCCESS_EMAIL_RESETPWD_SEND);
		return map;
	}

	/**
	 * 登陆
	 * 
	 * @param email
	 * @param password
	 * @param session
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) throws Exception {
		
		Map<String, Object> ret = userService.login(email, password);
		String status = ret.get("status")+"";
		if (Property.SUCCESS_ACCOUNT_LOGIN.equals(status)) {
			session.setAttribute("user", (User) ret.get("user"));
		}
		return ret;
	}

	/**
	 * 跳转注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "account/register";
	}

	/**
	 * 注册
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @param cfmPwd
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, String> register(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("cfmPwd") String cfmPwd,
			@RequestParam("emailAck") String emailAck,HttpSession session) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(emailAck.equals(session.getAttribute("emailAck"))){
		String status = userService.register(username, email, password, cfmPwd, map);
		map.put("status", status);
		}else{
		map.put("status", "002002");}
		return map;
	}

	private void initStatus(Model mav) {
		mav.addAttribute("ERROR_ACCOUNT_ACTIVATION_NOTEXIST", Property.ERROR_ACCOUNT_ACTIVATION_NOTEXIST);
		mav.addAttribute("ERROR_ACCOUNT_ACTIVATION_EXPIRED", Property.ERROR_ACCOUNT_ACTIVATION_EXPIRED);
		mav.addAttribute("ERROR_ACCOUNT_ACTIVATION", Property.ERROR_ACCOUNT_ACTIVATION);
	}

	/**
	 * 跳转激活账户页面
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping("/activation/mail/send")
	public String actication(@RequestParam("email") String email, Model model) {
		ModelAndView mav = new ModelAndView();

		initStatus(model);
		model.addAttribute("email", email);
		return "account/activation";
	}

	/**
	 * 重新发送激活短信
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/activation/mail/resend")
	public Map<String, String> acticationMailResend(@RequestParam("email") String email) throws Exception {
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, Object> map = userService.updateActivationKey(email);
		ret.put("status", (String) map.get("status"));
		if (Property.SUCCESS_ACCOUNT_ACTIVATION_KEY_UPD.equals((String) map.get("status"))) {
			// mailService.sendAccountActivationEmail(email,
			// (String)map.get("activationKey"));
			ret.put("status", Property.SUCCESS_ACCOUNT_ACTIVATION_EMAIL_RESEND);
		}
		return ret;
	}

	/**
	 * 确认是否处于活跃状态
	 * 
	 * @param key
	 * @param email
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/activation/{key}")
	public String activation(@PathVariable("key") String key, @RequestParam("email") String email, HttpSession session,
			Model model) throws Exception {

		String status = null;
		try {
			status = userService.activateUser(email, URLDecoder.decode(key, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (Property.SUCCESS_ACCOUNT_ACTIVATION.equals(status) || Property.ERROR_ACCOUNT_EXIST.equals(status)) {
			User user = userService.findByEmail(email);
			session.setAttribute("user", user);
			return "/guide";
		} else {
			model.addAttribute("status", status);
			model.addAttribute("email", email);
			model.addAttribute("ERROR_ACCOUNT_ACTIVATION_NOTEXIST", Property.ERROR_ACCOUNT_ACTIVATION_NOTEXIST);
			model.addAttribute("ERROR_ACCOUNT_ACTIVATION_EXPIRED", Property.ERROR_ACCOUNT_ACTIVATION_EXPIRED);
			model.addAttribute("ERROR_ACCOUNT_ACTIVATION", Property.ERROR_ACCOUNT_ACTIVATION);
			return "account/activation";
		}

	}

	@RequestMapping("/completeinfo")
	public String completeUserInfo(HttpSession session) {
		return "/account/setting/info";
	}

	/**
	 * 注销
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "account/login";
	}
}
