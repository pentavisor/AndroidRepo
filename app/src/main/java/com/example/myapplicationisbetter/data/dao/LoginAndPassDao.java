package com.example.myapplicationisbetter.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.myapplicationisbetter.data.models.LoginAndPassModel;


import io.reactivex.Maybe;

@Dao
public interface LoginAndPassDao {
    @Query("SELECT * FROM login_and_pass  LIMIT 1")
    Maybe<LoginAndPassModel> findFirstEntry();

    @Query("DELETE FROM login_and_pass")
    void deleteAll();


    @Insert
    void insertAll(LoginAndPassModel users);

}
