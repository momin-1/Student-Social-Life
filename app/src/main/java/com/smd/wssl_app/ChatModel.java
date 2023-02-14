package com.smd.wssl_app;

public class ChatModel {
    String sender;
    String message;
    String img_url;
    String groupname;


    public ChatModel(String sender, String message, String img_url) {
        this.sender = sender;
        this.message = message;
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatModel(String sender,  String message) {
        this.sender = sender;
        this.message = message;
    }
}
