package com.example.myapplicationisbetter.ui.loginpage;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface LoginView extends MvpView {
    void setButtonInputText(String s);

    void setSystemMassage(String s);

    void checkLoginAndPass();

    void blockButtonReset();

    void unblockButtonReset();
@StateStrategyType(SkipStrategy.class)
    void goInUserListPage();
}
