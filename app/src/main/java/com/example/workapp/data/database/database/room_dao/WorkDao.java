package com.example.workapp.data.database.database.room_dao;/*package com.example.workapp.database.room_dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workapp.database.entity.Work;
import com.example.workapp.database.querries.WorkWithComments;
import com.example.workapp.database.querries.WorkWithTimer;

import java.util.List;

@Dao
public interface WorkDao {

    @Query("SELECT*FROM work")
    List<Work> getall();

    @Query("SELECT * FROM work JOIN comment ON Work.id = Comment.workId")
    List<WorkWithComments> getWorkComments();

    @Query("SELECT * FROM work JOIN apptimer ON Work.id = AppTimer.workId")
    List<WorkWithTimer> getWorkAndTimer();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Work work);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Work work);

    @Delete
    void delete(Work work);
}
*/