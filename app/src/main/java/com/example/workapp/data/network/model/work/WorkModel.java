package com.example.workapp.data.network.model.work;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public final class WorkModel {

    @SerializedName("workId")
    private String id;

    @SerializedName("workName")
    private String name;

    @SerializedName("isArchived")
    private boolean isArchived;

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @SerializedName("objectId")
    private String objectId;

    public WorkModel() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isArchived;
    }

    public void setCompleted(boolean completed) {
        isArchived = completed;
    }
}
