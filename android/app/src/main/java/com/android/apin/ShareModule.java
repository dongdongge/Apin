package com.android.apin;

/**
 * Created by Administrator on 2017/7/4.
 *
 * 分享的携带参数的实体类
 */

public class ShareModule {

    public String title ;
    public String description;
    public String thumbImage;
    public String webpageUrl;

    public ShareModule(String title, String description, String thumbImage, String webpageUrl) {
        this.title = title;
        this.description = description;
        this.thumbImage = thumbImage;
        this.webpageUrl = webpageUrl;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public void setWebpageUrl(String webpageUrl) {
        this.webpageUrl = webpageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

}
