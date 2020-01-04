package com.example.savepasswordoffline;

public class UserModelData {
    public String userID, userName, userEmail, userCreateDate, userModifyDate, userIsVerified;

    public UserModelData(String userID, String userName, String userEmail, String userCreateDate, String userModifyDate, String userIsVerified){
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userCreateDate = userCreateDate;
        this.userModifyDate = userModifyDate;
        this.userIsVerified = userIsVerified;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserCreateDate() {
        return userCreateDate;
    }

    public String getUserModifyDate() {
        return userModifyDate;
    }

    public String getUserIsVerified() {
        return userIsVerified;
    }
}
