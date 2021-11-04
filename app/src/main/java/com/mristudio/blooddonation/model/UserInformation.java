package com.mristudio.blooddonation.model;

import java.util.List;

public class UserInformation {
    private String userId;
    private Boolean isAdmin;
    private String userType;
    private String uname;
    private String email;
    private String password;
    private String name;
    private String phonNo;
    private String altPhoneNo;
    private String socialLink;
    private String bloodGroup;
    private boolean weaight;
    private String userGender;
    private String religion;
    private String address;
    private String police_station;
    private String district;
    private String birthday;
    private String last_donateDate;
    private String userProfilePicture;
    private String token;
    private String activeStatus;
    private List<RatingModel> ratingModelList;

    public UserInformation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonNo() {
        return phonNo;
    }

    public void setPhonNo(String phonNo) {
        this.phonNo = phonNo;
    }

    public String getAltPhoneNo() {
        return altPhoneNo;
    }

    public void setAltPhoneNo(String altPhoneNo) {
        this.altPhoneNo = altPhoneNo;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public boolean getWeaight() {
        return weaight;
    }

    public void setWeaight(boolean weaight) {
        this.weaight = weaight;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPolice_station() {
        return police_station;
    }

    public void setPolice_station(String police_station) {
        this.police_station = police_station;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLast_donateDate() {
        return last_donateDate;
    }

    public void setLast_donateDate(String last_donateDate) {
        this.last_donateDate = last_donateDate;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<RatingModel> getRatingModelList() {
        return ratingModelList;
    }

    public void setRatingModelList(List<RatingModel> ratingModelList) {
        this.ratingModelList = ratingModelList;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "userId='" + userId + '\'' +
                ", isAdmin=" + isAdmin +
                ", userType='" + userType + '\'' +
                ", userName='" + uname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phonNo='" + phonNo + '\'' +
                ", altPhoneNo='" + altPhoneNo + '\'' +
                ", socialLink='" + socialLink + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", weaight=" + weaight +
                ", userGender='" + userGender + '\'' +
                ", religion='" + religion + '\'' +
                ", address='" + address + '\'' +
                ", police_station='" + police_station + '\'' +
                ", district='" + district + '\'' +
                ", birthday='" + birthday + '\'' +
                ", last_donateDate='" + last_donateDate + '\'' +
                ", userProfilePicture='" + userProfilePicture + '\'' +
                ", token='" + token + '\'' +
                ", ratingModelList=" + ratingModelList +
                '}';
    }
}
