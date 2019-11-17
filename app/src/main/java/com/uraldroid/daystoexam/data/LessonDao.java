package com.uraldroid.daystoexam.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.uraldroid.daystoexam.model.Lesson;

import java.util.List;

@Dao
public interface LessonDao {

    @Query("SELECT * FROM lesson")
    List<Lesson> getAll();

    @Query("SELECT * FROM lesson WHERE id = :id")
    Lesson getById(long id);

    @Insert
    void insert(Lesson lesson);

    @Update
    void update(Lesson lesson);

    @Delete
    void delete(Lesson lesson);

}
