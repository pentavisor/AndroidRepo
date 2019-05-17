package com.example.myapplicationisbetter.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserPropertiesDao {
    @Insert
    void insertAll(List<UserProperties> properties);
    @Insert
    void insertOne(UserProperties properties);
    @Update
    void updateAll(List<UserProperties> properties);
    @Update
    void updateOne(UserProperties properties);
    @Query("SELECT * FROM user_properties")
    List<UserProperties> getAll();
    @Query("SELECT * FROM user_properties WHERE prop_id LIKE :id")
    Single<List<UserProperties>> getItemOnId(int id);
    @Delete
    void deleteAll(List<UserProperties> properties);
    @Delete
    void deleteOne(UserProperties properties);
}
