package com.share.util;

/**
 * 验证码生成器
 * @author chennan
 *
 */
public class VerifyCode {
	private static final String[] codes={"1","2","3","4","5","6","7","8","9","0"};
	/**
	 * 获取随机验证码
	 * @return
	 */
	public static String getRandomCode(){
		String code="";
		for (int i=0;i<6;i++) {
			int temp=(int) (Math.random()*10);
			code+=codes[temp];
		}
		return code;
	}
}
