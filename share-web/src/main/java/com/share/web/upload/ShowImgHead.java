package com.share.web.upload;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
/**
 * 动态生成图片  
 */
public class ShowImgHead {
	// {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"}
	private static String[] fontNames = { "宋体", "华文楷体", "黑体", "微软雅黑",  "楷体_GB2312" ,"华文新魏","华文隶书","PMingLiU"};
	// 可选字符
	//"23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	private static String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	// 背景色
	@SuppressWarnings("unused")
	private Color bgColor = new Color(255, 255, 255);
	// 基数(一个文字所占的空间大小)
	private int base = 25;
	// 图像宽度
	private int width = base * 4;
	// 图像高度
	private int height = base;
	// 文字个数
	private int len = 4;
	// 设置字体大小
	private int fontSize = 22;
	// 验证码上的文本
	private String text;

	private BufferedImage img = null;
	private Graphics2D g2 = null;

	/**
	 * 生成验证码图片
	 */
	public void drawImage(OutputStream outputStream) {
		// 1.创建图片缓冲区对象, 并设置宽高和图像类型
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 2.得到绘制环境
		g2 = (Graphics2D) img.getGraphics();
		// 3.开始画图
		// 设置背景色
	//	g2.setColor(bgColor);
		g2.setColor(new Color(getRandom(201, 255), getRandom(201, 250),getRandom(201, 250)));
		g2.fillRect(0, 0, width, height);

		StringBuffer sb = new StringBuffer();// 用来装载验证码上的文本

		for (int i = 0; i < len; i++) {
			// 设置画笔颜色 -- 随机
			// g2.setColor(new Color(255, 0, 0));
			g2.setColor(new Color(getRandom(0, 200), getRandom(0, 200),getRandom(0, 200)));

			// 设置字体
			g2.setFont(new Font(fontNames[getRandom(0, fontNames.length)], Font.BOLD, fontSize));

			// 旋转文字(-45~+45)
			int theta = getRandom(-45, 45);
			g2.rotate(theta * Math.PI / 180, 7 + i * base, height - 8);

			// 写字
			String code = codes.charAt(getRandom(0, codes.length())) + "";
			g2.drawString(code, 7 + i * base, height - 8);
			sb.append(code);
			g2.rotate(-theta * Math.PI / 180, 7 + i * base, height - 8);
		}

		this.text = sb.toString();

		// 画干扰线
		for (int i = 0; i < len + 3; i++) {
			// 设置画笔颜色 -- 随机
			// g2.setColor(new Color(255, 0, 0));
			g2.setColor(new Color(getRandom(0, 200), getRandom(0, 200),
					getRandom(0, 255)));
			g2.drawLine(getRandom(0, 120), getRandom(0, 30), getRandom(0, 120),
					getRandom(0, 30));
//			g2.drawOval(getRandom(0, 120), getRandom(0, 30), getRandom(0, 120),
//					getRandom(0, 30));
		}
		// 绘制边框
		g2.setColor(Color.GRAY);//设置灰色边框
		g2.drawRect(0, 0, width-2, height-2);
		
		// 4.保存图片到指定的输出流
		try {
			 ImageIO.write(this.img, "png", outputStream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			// 5.释放资源
			g2.dispose();
		}
	}
	/*
	 * 生成随机数的方法
	 */
	private static int getRandom(int start, int end) {
		Random random = new Random();
		return random.nextInt(end - start) + start;
	}
}
