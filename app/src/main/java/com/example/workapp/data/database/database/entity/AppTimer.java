package com.example.workapp.data.database.database.entity;/*package com.example.workapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("workId")}, foreignKeys = @ForeignKey(entity = Work.class, parentColumns = "id", childColumns = "workId"))
public class AppTimer {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "timerId")
    private long id;

    @ColumnInfo(name = "workId")
    private String workId;

    @ColumnInfo(name = "timeOfTimerStart")
    private String startTime;

    @ColumnInfo(name = "timeOfTimerFinish")
    private String finishTime;

    @ColumnInfo(name = "startPause")
    private String startPause;

    @ColumnInfo(name = "finishPause")
    private String finishPause;

    @ColumnInfo(name = "elapsedTime")
    private String elapsedTime;

    @ColumnInfo(name = "timeInPause")
    private String timeInPause;

    public String getTimeInPause() {
        return timeInPause;
    }

    public void setTimeInPause(String timeInPause) {
        this.timeInPause = timeInPause;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    @Ignore
    private String name;

    public String getStartPause() {
        return startPause;
    }

    public void setStartPause(String startPause) {
        this.startPause = startPause;
    }

    public String getFinishPause() {
        return finishPause;
    }

    public void setFinishPause(String finishPause) {
        this.finishPause = finishPause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public long getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }
}
*/