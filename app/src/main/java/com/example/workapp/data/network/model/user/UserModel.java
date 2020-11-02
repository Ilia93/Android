package com.example.workapp.data.network.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class UserModel {

    @SerializedName("userId")
    private String userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userSecondName")
    private String userSecondName;
    @SerializedName("userAge")
    private String userAge;
    @SerializedName("userGender")
    private String userGender;
    @SerializedName("userWeight")
    private String userWeight;
    @SerializedName("objectId")
    private String userObjectId;

    public UserModel() {
        userId = UUID.randomUUID().toString();
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }
}
