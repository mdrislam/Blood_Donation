package com.mristudio.blooddonation.model;

public class CommentModel {
    private String tblId;
    private  String uId;
    private String commentMsg;

    public CommentModel() {
    }

    public CommentModel(String tblId, String uId, String commentMsg) {
        this.tblId = tblId;
        this.uId = uId;
        this.commentMsg = commentMsg;
    }

    public String getTblId() {
        return tblId;
    }

    public String getuId() {
        return uId;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "tblId='" + tblId + '\'' +
                ", uId='" + uId + '\'' +
                ", commentMsg='" + commentMsg + '\'' +
                '}';
    }
}
