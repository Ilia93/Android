package com.example.workapp.data.network.model.comments;

import com.google.gson.annotations.SerializedName;

public final class CommentsModel {

    @SerializedName("commentId")
    private long id;
    @SerializedName("commentDate")
    private String time;
    @SerializedName("text")
    private String text;
    private String workId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }
}