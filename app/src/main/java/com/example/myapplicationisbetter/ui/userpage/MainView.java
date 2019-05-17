package com.example.myapplicationisbetter.ui.userpage;

import com.arellomobile.mvp.MvpView;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

public interface MainView extends MvpView {
    void setStatus(String s);
    void setUserList(List<UserDataModel> list);
    void deleteUsers(List<UserDataModel> list);
    void deleteAllUsers();
    void goCreateUserPage();
    void setTextInFragment(UserDataModel userDataModel, UserProperties userProperties);
}
