package com.example.myapplicationisbetter.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "user_properties",
        foreignKeys = @ForeignKey(entity = UserDataModel.class,
                parentColumns = "future_id",
                childColumns = "prop_id",
                onDelete = CASCADE,
                onUpdate = CASCADE))
public class UserProperties implements Serializable {
    @PrimaryKey()
    @ColumnInfo(name = "prop_id")
    public int id;

    @ColumnInfo(name = "sport")
    public boolean sport;

    @ColumnInfo(name = "flowers")
    public boolean flowers;

    @ColumnInfo(name = "mushrooms")
    public boolean mushrooms;

    @ColumnInfo(name = "first_name")
    public boolean funnyCat;

    public UserProperties(int id, boolean sport, boolean flowers, boolean mushrooms, boolean funnyCat) {
        this.id = id;
        this.sport = sport;
        this.flowers = flowers;
        this.mushrooms = mushrooms;
        this.funnyCat = funnyCat;
    }


}
