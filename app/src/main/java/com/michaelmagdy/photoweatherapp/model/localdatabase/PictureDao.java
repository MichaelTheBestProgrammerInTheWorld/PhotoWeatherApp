package com.michaelmagdy.photoweatherapp.model.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PictureDao {

    @Insert
    void insert(Picture picture);

    @Query("SELECT * FROM Picture")
    LiveData<List<Picture>> getAllPictures();
}
