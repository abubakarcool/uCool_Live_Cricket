package com.example.samplestickerapp.Models;

public class MessageModel {
    String uId, message, messageId, status, imageURL;
    long timestamp;
    boolean imageMsg;

    public MessageModel(String uId, String message, long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
    public MessageModel(){}

    public MessageModel(String uId, String message, String messageId, String status, long timestamp) {
        this.uId = uId;
        this.message = message;
        this.messageId = messageId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isImageMsg() {
        return imageMsg;
    }

    public void setImageMsg(boolean imageMsg) {
        this.imageMsg = imageMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
