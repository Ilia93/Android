package com.example.workapp.data.database.database.room_dao;/*package com.example.workapp.database.room_dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workapp.database.entity.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT*FROM comment")
    List<Comment> getall();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Comment comment);

    @Delete
    void delete(Comment comment);
}
*/
