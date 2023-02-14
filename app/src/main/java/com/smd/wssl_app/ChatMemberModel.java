package com.smd.wssl_app;

public class ChatMemberModel {
    String imgurl,name,title,status;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChatMemberModel(String imgurl, String name, String title, String status) {
        this.imgurl = imgurl;
        this.name = name;
        this.title = title;
        this.status = status;
    }
}
