package com.mristudio.blooddonation.model;

public class ImageSliderData {
    private String tablesId;
    private String imgUrl;
    private String  headline;
    private String  createBy;
    private String  createAt;

    public ImageSliderData() {
    }

    public ImageSliderData(String tablesId,String imgUrl, String headline, String createBy, String createAt) {
        this.imgUrl = imgUrl;
        this.headline = headline;
        this.createBy = createBy;
        this.createAt = createAt;
    }

    public String getTablesId() {
        return tablesId;
    }

    public void setTablesId(String tablesId) {
        this.tablesId = tablesId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
