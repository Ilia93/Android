package com.example.workapp.presentation.screen.main;

public class ActivityTemplatesModel {
    private String activityDescription;
    private int workImage;

    public ActivityTemplatesModel(String activityDescription, int workImage) {
        this.activityDescription = activityDescription;
        this.workImage = workImage;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public int getWorkImage() {
        return workImage;
    }

    public void setWorkImage(int workImage) {
        this.workImage = workImage;
    }
}
