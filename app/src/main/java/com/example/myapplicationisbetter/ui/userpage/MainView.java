package com.example.myapplicationisbetter.ui.userpage;

import com.arellomobile.mvp.MvpView;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

public interface MainView extends MvpView {
    void setUserList(List<UserDataModel> list);
    void deleteUsers(List<UserDataModel> list);
    void deleteAllUsers();
    void goCreateUserPage();
    void setText(UserDataModel userDataModel, UserProperties userProperties);
}
