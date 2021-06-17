package com.mristudio.blooddonation.model;

public class PostRequestModel {
    private String uId;
    private String hName;
    private String sheduledTime;
    private String sheduledDate;
    private String bloodGroup;
    private String addressOfHospital;
    private String relationship;
    private String contactNumber;
    private boolean status;
    private String areaOfCity;
    private String areaOfSubCity;
    private boolean isUrgent;

    public PostRequestModel() {
    }

    public PostRequestModel(String uId, String hName, String sheduledTime, String sheduledDate, String bloodGroup, String addressOfHospital, String relationship, String contactNumber, boolean status, String areaOfCity, String areaOfSubCity, boolean isUrgent) {
        this.uId = uId;
        this.hName = hName;
        this.sheduledTime = sheduledTime;
        this.sheduledDate = sheduledDate;
        this.bloodGroup = bloodGroup;
        this.addressOfHospital = addressOfHospital;
        this.relationship = relationship;
        this.contactNumber = contactNumber;
        this.status = status;
        this.areaOfCity = areaOfCity;
        this.areaOfSubCity = areaOfSubCity;
        this.isUrgent = isUrgent;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String getSheduledTime() {
        return sheduledTime;
    }

    public void setSheduledTime(String sheduledTime) {
        this.sheduledTime = sheduledTime;
    }

    public String getSheduledDate() {
        return sheduledDate;
    }

    public void setSheduledDate(String sheduledDate) {
        this.sheduledDate = sheduledDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddressOfHospital() {
        return addressOfHospital;
    }

    public void setAddressOfHospital(String addressOfHospital) {
        this.addressOfHospital = addressOfHospital;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAreaOfCity() {
        return areaOfCity;
    }

    public void setAreaOfCity(String areaOfCity) {
        this.areaOfCity = areaOfCity;
    }

    public String getAreaOfSubCity() {
        return areaOfSubCity;
    }

    public void setAreaOfSubCity(String areaOfSubCity) {
        this.areaOfSubCity = areaOfSubCity;
    }

    public boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }
}
