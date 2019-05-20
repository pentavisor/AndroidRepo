package com.example.myapplicationisbetter.ui.updatepage;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.loginpage.LoginView;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class UpdatePresenter extends MvpPresenter<UpdateView> {

    public void sendNewUserInDataBase(UserDataModel userDataModel, UserProperties userProperties) {
        App.getInstance()
                .getDatabase()
                .userDao()
                .getMaxFutureId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<UserDataModel>>() {
                    @Override
                    public void onSuccess(List<UserDataModel> id) {
                        if (id.size() == 0)
                            id.add(new UserDataModel(1, "", "", "", "", true, "", "", 0, "", 1));
                        Completable.fromAction(() -> {
                            // getViewState().setSystemText(id.get(0).toString());
                            userDataModel.future_id = id.get(0).future_id + 1;
                            userProperties.id = id.get(0).future_id + 1;
                            App.getInstance().getDatabase().userDaoTransaction().insertUserAndProperties(userDataModel, userProperties);
                        }).subscribeOn(Schedulers.single()).subscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // ...
                    }
                });
//        getViewState().setSystemText("Сохранение Успешно");
//        getViewState().goToUserList();
    }
}
