package com.example.samplestickerapp.Models;

public class Users {
    String profilePic, userName, eemail, password, userId, lastMessage, status, online;

    public Users(String profilePic, String userName, String eemail, String password, String userId, String lastMessage) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.eemail = eemail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public Users(String profilePic, String userName, String eemail, String password, String userId, String lastMessage, String status) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.eemail = eemail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    public Users(String profilePic, String userName, String eemail, String password, String userId, String lastMessage, String status, String online) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.eemail = eemail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.status = status;
        this.online = online;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Users(){} ////Empty constructor

    public Users( String userName, String eemail, String password, String userId) { //signUp constructor
        this.userName = userName;
        this.eemail = eemail;
        this.password = password;
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEemail() {
        return eemail;
    }

    public void setEemail(String eemail) {
        this.eemail = eemail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
