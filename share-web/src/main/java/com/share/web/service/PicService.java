package com.share.web.service;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.share.util.UUIDUtil;
import com.share.util.UploadUtil;
import com.share.vo.PicUploadResult;



@Service
public class PicService {
	public PicUploadResult uploadPic(MultipartFile uploadFile) {	
		PicUploadResult result=new PicUploadResult();
		/*
		 * 1 判断后缀
		 * 2 判断木马
		 * 3 生成路径
		 * 4 生成url
		 * 5 存盘
		 * 6返回
		 * 异常一旦出现,不是有木马就是其他问题,返回result对象
		 * error=1
		 */
		//获取文件原名称 
		String oldFileName=uploadFile.getOriginalFilename();
		//截取后缀,从最后一个"."开始到名称末尾截取
		//菊花.jpg
		String extName=oldFileName.substring(oldFileName.lastIndexOf("."));
		//判断是否是jpg,png,gif一员,正则表达式的分组
		if(!extName.matches("^.(jpg|png|gif)$")){
			result.setError(1);
			return result;
		}
		//判断木马,数据中,如果有宽和高,就证明是图片,如果没有就不是图片
		try{//利用imageIO判断是否为图片数据
			BufferedImage image=ImageIO.read(uploadFile.getInputStream());
			result.setHeight(image.getHeight()+"");//获取图片的高
			result.setWidth(image.getWidth()+"");
			//生成路径,存储到磁盘的路径,和url虚拟路径都需要一个共同的内容
			//dir /upload/4/d/5/d/3/d/3/d/
			String dir=UploadUtil.getUploadPath(oldFileName, "/upload");
			//根据传递测upload生成一个头,
			//根据原文件名称生成三列的一个多级文件路径
			///upload/4/d/5/d/3/d/3/d/
			//生成存盘的文件夹路径, 
			//可以用System.getProperties("user.dir")
			String path="C://jt-upload/"+dir+"/";//文件夹,路径
			//生成磁盘的路径文件夹 File
			File _dir=new File(path);//如果已经生成了路径
			if(!_dir.exists()){//不存在路径,才生成磁盘路径
				_dir.mkdirs(); 
			}
			//文件名称不能使用原文件,会总是重名
			String fileName=UUIDUtil.getUUID()+extName;
			//存盘,将数据输出到
			uploadFile.transferTo(new File(path+fileName));
			//error默认0不用动,width height url?????
			result.setUrl("http://image.jt.com"+dir+"/"+fileName);
			return result;
			//   /upload/c/7/4/1/4/2/3/2/a99e691b-88d4-43a2-ac12-82ec54db123d_738f47e2-9605-46aa-b647-fc8dca814074.jpg
		}catch(Exception e){
			result.setError(1);
			return result;
		}
	}
}