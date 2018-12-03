package com.share.vo;

public class PicUploadResult {
    private Integer error=0;		//图片上传错误不能抛出，抛出就无法进行jsp页面回调，所以设置这个标识，0表示无异常，1代表异常
    private String url;             //应该是浏览器能够解析的具体页面路径  相对路径http://image.jt/1212.jpg 
    								//本地路径:存放图片路径     D:\jt-upload\images\2017\06\03
    private String width;
    private String height;          //如果认为的不指定,则使用的是图片自己的尺寸

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
    
    

}
