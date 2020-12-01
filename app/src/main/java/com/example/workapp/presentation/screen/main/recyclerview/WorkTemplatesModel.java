package com.example.workapp.presentation.screen.main.recyclerview;

public class WorkTemplatesModel {
    private String activityDescription;
    private int workImage;

    public WorkTemplatesModel(String activityDescription, int workImage) {
        this.activityDescription = activityDescription;
        this.workImage = workImage;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public int getWorkImage() {
        return workImage;
    }
}