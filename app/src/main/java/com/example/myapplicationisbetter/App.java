package com.example.myapplicationisbetter;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.myapplicationisbetter.data.AppDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public final class App extends Application {

    private static App instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, App.getInstance().getResources().getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.downloader(new OkHttp3Downloader(getApplicationContext(), Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }


}
