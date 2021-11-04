package com.mristudio.blooddonation.model;

public class RatingModel {
    private String userID;
    private String userCommet;
    private Float rating;

    public RatingModel() {
    }

    public RatingModel(String userID, String userCommet, Float rating) {
        this.userID = userID;
        this.userCommet = userCommet;
        this.rating = rating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserCommet() {
        return userCommet;
    }

    public void setUserCommet(String userCommet) {
        this.userCommet = userCommet;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
