package com.example.myapplicationisbetter.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;


import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.querymodels.UsersAndTheirPropreties;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_list")
    Flowable<List<UserDataModel>> getAll();

    @Query("SELECT * FROM user_list WHERE id IN (:userIds)")
    List<UserDataModel> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user_list WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    UserDataModel findByName(String first, String last);

    @Query("SELECT * FROM user_list WHERE first_name LIKE :first LIMIT 1")
    Maybe<UserDataModel> findByNameAsin(String first);

    @Insert
    void insertAll(List<UserDataModel> users);

    @Insert
    void insertOne(UserDataModel user);

    @Update
    void update(UserDataModel user);

    @Delete
    void deleteUser(UserDataModel user);

    @Query("DELETE FROM user_list")
    void deleteAll();

    @Query("SELECT * FROM user_list ORDER BY id DESC LIMIT 1")
    Single<List<UserDataModel>> getMaxFutureId();

}
