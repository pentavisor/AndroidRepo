package com.example.myapplicationisbetter.ui.usercreatepage;

import com.arellomobile.mvp.MvpView;

public interface CreateUserView extends MvpView {
    void setBirthdayText(String text);
    void setSystemText(String text);
    void goToUserList();
}
