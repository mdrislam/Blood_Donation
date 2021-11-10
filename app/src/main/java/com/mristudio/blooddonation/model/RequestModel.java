package com.mristudio.blooddonation.model;

import java.io.Serializable;
import java.util.List;

public class RequestModel implements Serializable {
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

    private String type;





    public RequestModel() {
    }

    public RequestModel(String tblId, String uId, String requestMessage, String imagesUrl, String gender, String date, String time, String userProfileName, String userProfileImageUrl, String postDateTime, String type) {
        this.tblId = tblId;
        this.uId = uId;
        this.requestMessage = requestMessage;
        this.imagesUrl = imagesUrl;
        this.gender = gender;
        this.date = date;
        this.time = time;
        this.userProfileName = userProfileName;
        this.userProfileImageUrl = userProfileImageUrl;
        this.postDateTime = postDateTime;
        this.type = type;
    }

    public RequestModel(String tblId, String uId, String notifyId, String bloodGroup, String hospitalName, String addressofHospital, String requestMessage, String district, String imagesUrl, String cause, String gender, String date, String time, Integer units, boolean isUrgent, String userProfileName, String userProfileImageUrl, String postDateTime, String type) {
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

        this.userProfileName = userProfileName;
        this.userProfileImageUrl = userProfileImageUrl;
        this.postDateTime = postDateTime;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "tblId='" + tblId + '\'' +
                ", uId='" + uId + '\'' +
                ", notifyId='" + notifyId + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
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
                ", postDateTime='" + postDateTime + '\'' +
                '}';
    }
}
