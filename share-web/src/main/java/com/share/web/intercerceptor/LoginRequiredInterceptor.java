package com.share.web.intercerceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.share.common.service.HttpClientService;
import com.share.pojo.User;
import com.share.util.CookieFild;
import com.share.util.CookieUtils;
import com.share.util.ObjectUtil;
import com.share.util.Property;
import com.share.vo.SysResult;
import com.share.web.redis.FeedDAO;
import com.share.web.service.NotificationService;
/**
 * 所有拦截器
 * @author chennan
 *
 */
public class LoginRequiredInterceptor implements HandlerInterceptor{

	/**
	 * 通知服务类
	 */
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private HttpClientService client;
	@Autowired
	private FeedDAO redis;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user=null;
	/*	String userJson = CookieUtils.getCookieValue(request, CookieFild.USER_lOGIN);
		if(StringUtils.isNotEmpty(userJson)){//曾经登录过
			//到sso连接redis获取userJson
			String url="http://sso.jt.com/user/query/"+userJson;
			String jsonData=client.doGet(url);//获取sysresult的json
			SysResult result=ObjectUtil.mapper.readValue(jsonData, SysResult.class);
			String userJson1=(String) result.getData();
			if(userJson1!=null&&userJson1!=""&&userJson1!="null"){//真正登录是正常的
				//userNode解析userId,放到request域中
				user=ObjectUtil.mapper.readValue(userJson1, User.class);
			}
		}*/
		HttpSession session=request.getSession();
		user=(User) session.getAttribute("user");
		if(user == null){
			String contextPath = request.getContextPath();
			String servletPath = request.getServletPath();
			session.setAttribute("lastvisit", contextPath+servletPath);
		    response.sendRedirect("/welcome");
			return false;
		} else {
			session.setAttribute("user", user);
			session.setAttribute("notifications", notificationService.getNotificationsCount(user.getId()));
			return true;
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
