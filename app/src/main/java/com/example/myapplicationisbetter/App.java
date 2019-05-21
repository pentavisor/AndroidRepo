package com.example.myapplicationisbetter;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.myapplicationisbetter.data.AppDatabase;
import com.squareup.leakcanary.LeakCanary;

public final class App extends Application {

    private static App instance;

    private AppDatabase database;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
//        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries()
//                .build();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, App.getInstance().getResources().getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();
        LeakCanary.install(this);
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() { return database; }
}
