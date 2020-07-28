package com.example.workapp.data.network.model.timer;

import com.google.gson.annotations.SerializedName;

public final class TimerModel {
    @SerializedName("timerId")
    private long id;

    @SerializedName("workId")
    private String workId;

    @SerializedName("start")
    private String startTime;

    @SerializedName("finish")
    private String finishTime;

    @SerializedName("elapsedTime")
    private String elapsedTime;

    @SerializedName("timeInPause")
    private String timeInPause;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTimeInPause() {
        return timeInPause;
    }

    public void setTimeInPause(String timeInPause) {
        this.timeInPause = timeInPause;
    }
}