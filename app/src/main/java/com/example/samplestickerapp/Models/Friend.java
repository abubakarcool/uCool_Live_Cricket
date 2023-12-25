package com.example.samplestickerapp.Models;

public class Friend {
    String profilePic, eemail, userName,userId;

    public Friend(){}

    public Friend(String profilePic, String eemail, String userName, String userId) {
        this.profilePic = profilePic;
        this.eemail = eemail;
        this.userName = userName;
        this.userId = userId;
    }

    public Friend(String eemail, String userName, String userId) {
        this.eemail = eemail;
        this.userName = userName;
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEemail() {
        return eemail;
    }

    public void setEemail(String eemail) {
        this.eemail = eemail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
