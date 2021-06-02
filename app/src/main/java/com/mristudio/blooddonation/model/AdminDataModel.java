package com.mristudio.massnundoa.model;

import com.google.firebase.auth.FirebaseUser;

public class AdminDataModel {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String userType;
    private Boolean isAdmin;

    public AdminDataModel() { }

    public AdminDataModel(String userId,String name, String email, String password, String userType, Boolean isAdmin ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.isAdmin = isAdmin;
        this.userId = userId;

    }



    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
