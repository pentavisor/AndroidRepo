package com.example.myapplicationisbetter.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.data.querymodels.UsersAndTheirPropreties;

import java.util.List;
@Dao
public abstract class DaoTransaction {

    @Update
    public abstract void updateUserDataModel(UserDataModel userDataModel);
    @Update
    public abstract void updateUserProperties(UserProperties properties);
    @Transaction
    public void updateUserAndProperties(UserDataModel userDataModel, UserProperties properties) {
        updateUserDataModel(userDataModel);
        updateUserProperties(properties);
    }


    @Insert
    public abstract void insertUserDataModel(UserDataModel userDataModel);
    @Insert
    public abstract void insertUserProperties(UserProperties properties);
    @Transaction
    public void insertUserAndProperties(UserDataModel userDataModel, UserProperties properties) {
        insertUserDataModel(userDataModel);
        insertUserProperties(properties);
    }

    @Transaction
    @Query("SELECT * from user_list")
    public abstract List<UsersAndTheirPropreties> usersAndTheirPropreties();
}
