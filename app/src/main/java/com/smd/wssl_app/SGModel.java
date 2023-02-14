package com.smd.wssl_app;

public class SGModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getAmount_of_users() {
        return amount_of_users;
    }

    public void setAmount_of_users(String amount_of_users) {
        this.amount_of_users = amount_of_users;
    }

    public SGModel(String name, String longname, String amount_of_users) {
        this.name = name;
        this.longname = longname;
        this.amount_of_users = amount_of_users;
    }

    String name,longname,amount_of_users;
}
