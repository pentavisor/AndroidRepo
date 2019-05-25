package com.example.myapplicationisbetter.ui.userpage;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.data.models.UserDataModel;

import io.reactivex.Completable;

import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {


    public final int TEST_USER_PROFILE_DATA = 0;

    public void userListInit() {

        App.getInstance().getDatabase().userDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> getViewState().setUserList(employees));
    }

    public void deleteUser(UserDataModel userDataModel) {
        Completable.fromAction(() -> {
            App.getInstance().getDatabase().userDao().deleteUser(userDataModel);
        }).subscribeOn(Schedulers.single()).subscribe();
    }

    public void setTextInFeature(UserDataModel userDataModel) {
        if (userDataModel.future_id != TEST_USER_PROFILE_DATA) {
            App.getInstance()
                    .getDatabase()
                    .userPropertiesDao()
                    .getItemOnId(userDataModel.future_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<UserProperties>>() {
                        @Override
                        public void onSuccess(List<UserProperties> userProperties) {
                            getViewState().setText(userDataModel, userProperties.get(0));
                        }

                        @Override
                        public void onError(Throwable e) {
                            // ...
                        }
                    });
        } else {
            getViewState().setText(userDataModel, null);
        }
    }
}
