package com.mristudio.blooddonation.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("icon")
    @Expose
    private Integer icon;
    @SerializedName("sendername")
    @Expose
    private String sendername;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("tittle")
    @Expose
    private String tittle;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("receiver")
    @Expose
    private String receiver;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("postId")
    @Expose
    private String postId;


    public Data() {
    }

    public Data(String sender, Integer icon,String tittle,String sendername, String type, String msg,String receiver) {
        this.sender = sender;
        this.sendername = sendername;
        this.icon = icon;
        this.type = type;
        this.tittle = tittle;
        this.msg = msg;
        this.receiver = receiver;
    }

    public Data(String tittle, String type, String imageurl, String description, String address, String postId) {
        this.tittle = tittle;
        this.type = type;
        this.imageurl = imageurl;
        this.description = description;
        this.address = address;
        this.postId = postId;
    }

    public String getSender() {
        return sender;
    }

    public String getSendername() {
        return sendername;
    }

    public String getTittle() {
        return tittle;
    }

    public String getType() {
        return type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getDescription() {
        return description;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getaddress() {
        return address;
    }

    public String getPostId() {
        return postId;
    }

    public Integer getIcon() {
        return icon;
    }
}
