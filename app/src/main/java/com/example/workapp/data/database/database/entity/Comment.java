package com.example.workapp.data.database.database.entity;/*package com.example.workapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index("workId")},foreignKeys = @ForeignKey(entity = Work.class, parentColumns = "id", childColumns = "workId"))
public class Comment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "commentId")
    private long id;

    @ColumnInfo(name = "commentDate")
    private String time;

    @ColumnInfo(name = "commentText")
    private String text;

    @ColumnInfo(name = "workId")
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
}*/
