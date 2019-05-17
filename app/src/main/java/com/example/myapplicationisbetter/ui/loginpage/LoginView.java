package com.example.myapplicationisbetter.ui.loginpage;

import com.arellomobile.mvp.MvpView;

public interface LoginView extends MvpView {
    void setButtonInputText(String s);

    void setSystemMassage(String s);

    void checkLoginAndPass();

    void blockButtonReset();

    //--view page control---------
    void goInUserListPage();
}
