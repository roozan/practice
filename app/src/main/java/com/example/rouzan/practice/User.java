package com.example.rouzan.practice;

import java.util.HashMap;

public class User {
    private String firstName,lastName,userName,email,password,category,profilePhotoUrl,userId;
    private HashMap<String,Boolean> followersList, followingList;
    Integer followersCount,followingCount;

    public HashMap<String, Boolean> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(HashMap<String, Boolean> followersList) {
        this.followersList = followersList;
    }

    public HashMap<String, Boolean> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(HashMap<String, Boolean> followingList) {
        this.followingList = followingList;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
