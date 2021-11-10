package com.mristudio.blooddonation.model;

import java.io.Serializable;

public class Donation implements Serializable {
    private boolean status;
    private String userId;
    private String tableId;
    private String hospitalName;
    private String addressofHospital;
    private String donateDate;
    private String dayOfMonth;
    private String monthName;

    public Donation() {
    }

    public Donation(boolean status, String userId, String tableId, String hospitalName, String addressofHospital, String donateDate, String dayOfMonth, String monthName) {
        this.status = status;
        this.userId = userId;
        this.tableId = tableId;
        this.hospitalName = hospitalName;
        this.addressofHospital = addressofHospital;
        this.donateDate = donateDate;
        this.dayOfMonth = dayOfMonth;
        this.monthName = monthName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
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

    public String getDonateDate() {
        return donateDate;
    }

    public void setDonateDate(String donateDate) {
        this.donateDate = donateDate;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "userId='" + userId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", addressofHospital='" + addressofHospital + '\'' +
                ", donateDate='" + donateDate + '\'' +
                ", dayOfMonth='" + dayOfMonth + '\'' +
                ", monthName='" + monthName + '\'' +
                '}';
    }
}
