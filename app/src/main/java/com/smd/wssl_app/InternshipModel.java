package com.smd.wssl_app;

public class InternshipModel {
    String name,pay,address,img,amount_of_users,link;

    public InternshipModel(String name, String pay, String address,  String amount_of_users,String link) {
        this.name = name;
        this.pay = pay;
        this.address = address;
        this.amount_of_users = amount_of_users;
        this.link = link;
    }

    public InternshipModel(String name, String pay, String address,  String amount_of_users) {
        this.name = name;
        this.pay = pay;
        this.address = address;
        this.amount_of_users = amount_of_users;

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAmount_of_users() {
        return amount_of_users;
    }

    public void setAmount_of_users(String amount_of_users) {
        this.amount_of_users = amount_of_users;
    }
}
