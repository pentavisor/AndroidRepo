package com.example.myapplicationisbetter.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import com.example.myapplicationisbetter.data.dao.LoginAndPassDao;
import com.example.myapplicationisbetter.data.dao.UserDao;
import com.example.myapplicationisbetter.data.dao.DaoTransaction;
import com.example.myapplicationisbetter.data.dao.UserPropertiesDao;
import com.example.myapplicationisbetter.data.models.LoginAndPassModel;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

@Database(entities = {LoginAndPassModel.class, UserDataModel.class, UserProperties.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LoginAndPassDao loginAndPassDao();
    public abstract UserDao userDao();
    public abstract UserPropertiesDao userPropertiesDao();
    public abstract DaoTransaction userDaoTransaction();

}
