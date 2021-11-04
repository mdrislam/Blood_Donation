package com.mristudio.blooddonation.notification;

public class Data {
    private String user;
    private int icon;
    private String body;
    private String tittle;
    private String sented;

    public Data() {
    }

    public Data(String user, int icon, String body, String tittle, String sented) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.tittle = tittle;
        this.sented = sented;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
