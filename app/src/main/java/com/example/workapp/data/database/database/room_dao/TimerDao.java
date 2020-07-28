package com.example.workapp.data.database.database.room_dao;/*package com.example.workapp.database.room_dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workapp.database.entity.AppTimer;

import java.util.List;

@Dao
public interface TimerDao {
    @Query("SELECT*FROM apptimer")
    List<AppTimer> getall();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppTimer appTimer);

    @Update
    void update(AppTimer appTimer);

    @Delete
    void delete(AppTimer appTimer);
}
*/