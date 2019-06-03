package com.example.myapplicationisbetter.ui.userpage;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

public interface MainView extends MvpView {
    void goCreateUserPage();
    void setText(UserDataModel userDataModel, UserProperties userProperties);
}
