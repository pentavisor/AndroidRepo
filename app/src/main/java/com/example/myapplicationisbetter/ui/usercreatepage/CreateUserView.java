package com.example.myapplicationisbetter.ui.usercreatepage;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface CreateUserView extends MvpView {
    void setBirthdayText(String text);
    void setSystemText(String text);
    void goToUserList();
}
