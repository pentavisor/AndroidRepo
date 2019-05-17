package com.example.myapplicationisbetter.data.querymodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

public class UsersAndTheirPropreties {
    @Embedded
    public UserDataModel userDataModel;

    @Relation(parentColumn = "id", entityColumn = "prop_id")
    public List<UserProperties> properties;


}
