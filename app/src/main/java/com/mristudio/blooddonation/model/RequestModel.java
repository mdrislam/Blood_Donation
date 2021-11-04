package com.mristudio.blooddonation.model;

import java.util.List;

public class RequestModel {
    private String tblId;
    private String uId;
    private String notifyId;
    private String bloodGroup;
    private String hospitalName;
    private String addressofHospital;
    private String requestMessage;
    private String district;
    private String imagesUrl;
    private String cause;
    private String gender;
    private String date;
    private String time;
    private Integer units;
    private boolean isUrgent;
    private Integer totalAccept;
    private Integer totalDonate;
    private String userProfileName;
    private String userProfileImageUrl;
    private String postDateTime;
    private List<String> lovesList;
    private List<String> viewsList;


    public RequestModel() {
    }

    public RequestModel(String tblId, String uId, String notifyId, String bloodGroup, String hospitalName,String addressofHospital, String requestMessage, String district, String imagesUrl, String cause, String gender, String date, String time, Integer units, boolean isUrgent, Integer totalAccept, Integer totalDonate, String userProfileName, String userProfileImageUrl,String postDateTime) {
        this.tblId = tblId;
        this.uId = uId;
        this.notifyId = notifyId;
        this.bloodGroup = bloodGroup;
        this.hospitalName = hospitalName;
        this.addressofHospital = addressofHospital;
        this.requestMessage = requestMessage;
        this.district = district;
        this.imagesUrl = imagesUrl;
        this.cause = cause;
        this.gender = gender;
        this.date = date;
        this.time = time;
        this.units = units;
        this.isUrgent = isUrgent;
        this.totalAccept = totalAccept;
        this.totalDonate = totalDonate;
        this.userProfileName = userProfileName;
        this.userProfileImageUrl = userProfileImageUrl;
        this.postDateTime = postDateTime;
    }

    public String getTblId() {
        return tblId;
    }

    public void setTblId(String tblId) {
        this.tblId = tblId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddressofHospital() {
        return addressofHospital;
    }

    public void setAddressofHospital(String addressofHospital) {
        this.addressofHospital = addressofHospital;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getcause() {
        return cause;
    }

    public void setcause(String cause) {
        this.cause = cause;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public Integer getTotalAccept() {
        return totalAccept;
    }

    public void setTotalAccept(Integer totalAccept) {
        this.totalAccept = totalAccept;
    }

    public Integer getTotalDonate() {
        return totalDonate;
    }

    public void setTotalDonate(Integer totalDonate) {
        this.totalDonate = totalDonate;
    }

    public String getUserProfileName() {
        return userProfileName;
    }

    public void setUserProfileName(String userProfileName) {
        this.userProfileName = userProfileName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "tblId='" + tblId + '\'' +
                ", uId='" + uId + '\'' +
                ", notifyId='" + notifyId + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", addressofHospital='" + addressofHospital + '\'' +
                ", requestMessage='" + requestMessage + '\'' +
                ", district='" + district + '\'' +
                ", imagesUrl='" + imagesUrl + '\'' +
                ", cause='" + cause + '\'' +
                ", gender='" + gender + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", units=" + units +
                ", isUrgent=" + isUrgent +
                ", totalAccept=" + totalAccept +
                ", totalDonate=" + totalDonate +
                ", userProfileName='" + userProfileName + '\'' +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                ", lovesList=" + lovesList +
                ", viewsList=" + viewsList +
                '}';
    }

    public List<String> getLovesList() {
        return lovesList;
    }

    public void setLovesList(List<String> lovesList) {
        this.lovesList = lovesList;
    }

    public List<String> getViewsList() {
        return viewsList;
    }

    public void setViewsList(List<String> viewsList) {
        this.viewsList = viewsList;
    }
}
