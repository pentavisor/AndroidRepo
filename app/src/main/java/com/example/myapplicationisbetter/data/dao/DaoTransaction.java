package com.example.myapplicationisbetter.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.data.querymodels.UsersAndTheirPropreties;

import java.util.List;
@Dao
public abstract class DaoTransaction {
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
