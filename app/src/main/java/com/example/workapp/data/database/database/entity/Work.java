package com.example.workapp.data.database.database.entity;/*package com.example.workapp.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Work {
    public Work() {
        id = UUID.randomUUID().toString();
    }

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "workName")
    private String name;

    @ColumnInfo(name = "isArchived")
    private boolean isCompleted;

    private int typeId;

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}*/