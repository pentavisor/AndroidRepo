package com.example.myapplicationisbetter.ui.updatepage;

import com.arellomobile.mvp.MvpView;

public interface UpdateView extends MvpView {
     void goToUserList();
     void setBirthdayText(String string);
     void setSystemText(String str);
}
