package com.example.rouzan.practice;

import java.util.ArrayList;

public class Post {
    String foodName,categoryName,foodImageUrl,postUploaderId,postId;
    long postUploadTime;
    int postPoint,tastedCount,notTastedCount;
    ArrayList<String> categoryList;


    public int getTastedCount() {
        return tastedCount;
    }

    public void setTastedCount(int tastedCount) {
        this.tastedCount = tastedCount;
    }

    public int getNotTastedCount() {
        return notTastedCount;
    }

    public void setNotTastedCount(int notTastedCount) {
        this.notTastedCount = notTastedCount;
    }

    public String getPostUploaderId() {
        return postUploaderId;
    }

    public void setPostUploaderId(String postUploaderId) {
        this.postUploaderId = postUploaderId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getPostUploadTime() {
        return postUploadTime;
    }

    public void setPostUploadTime(long postUploadTime) {
        this.postUploadTime = postUploadTime;
    }

    public int getPostPoint() {
        return postPoint;
    }

    public void setPostPoint(int postPoint) {
        this.postPoint = postPoint;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFoodName() {
        return foodName;

    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
