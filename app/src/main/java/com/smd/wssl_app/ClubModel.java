package com.smd.wssl_app;

public class ClubModel {
    private String club_name;
    private String amount_of_users;
    private String interest;
    private String club_image;

    public String getClub_image() {
        return club_image;
    }

    public void setClub_image(String club_image) {
        this.club_image = club_image;
    }

    public ClubModel(String club_name, String amount_of_users, String interest, String club_image) {
        this.club_name = club_name;
        this.amount_of_users = amount_of_users;
        this.interest = interest;
        this.club_image = club_image;
    }


    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public ClubModel(String club_name, String amount_of_users, String interest) {
        this.club_name = club_name;
        this.amount_of_users = amount_of_users;
        this.interest = interest;
    }

    public String getAmount_of_users() {
        return amount_of_users;
    }

    public void setAmount_of_users(String amount_of_users) {
        this.amount_of_users = amount_of_users;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
