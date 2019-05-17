package com.example.myapplicationisbetter.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_list",indices = {@Index(value = {"future_id"},unique = true)})
public class UserDataModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "google_map_link")
    public String googleMapLink;

    @ColumnInfo(name = "birthday")
    public String birthday;

    @ColumnInfo(name = "sex")
    public Boolean sex;

    @ColumnInfo(name = "sensor_number")
    public String sensorNumber;

    @ColumnInfo(name = "sensor_secret_code")
    public String sensorSecretCode;

    @ColumnInfo(name = "imageLink")
    public int imageLink;

    @ColumnInfo(name = "imageName")
    public String imageName;

    @ColumnInfo(name = "future_id")
    public int future_id;




    public UserDataModel(int id, String firstName, String lastName, String googleMapLink, String birthday, Boolean sex, String sensorNumber, String sensorSecretCode, int imageLink,String imageName, int future_id ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.googleMapLink = googleMapLink;
        this.birthday = birthday;
        this.sex = sex;
        this.sensorNumber = sensorNumber;
        this.sensorSecretCode = sensorSecretCode;
        this.imageLink = imageLink;
        this.imageName = imageName;
        this.future_id = future_id;
    }
}
