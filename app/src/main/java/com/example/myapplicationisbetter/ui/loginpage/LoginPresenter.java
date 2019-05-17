package com.example.myapplicationisbetter.ui.loginpage;

import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;

import java.util.regex.Pattern;

import com.example.myapplicationisbetter.data.models.LoginAndPassModel;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

    LoginAndPassModel firstQueryLoginAndPassModel;

    private CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenter() {

        disposables.add(
                App.getInstance().getDatabase().loginAndPassDao().findFirstEntry()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableMaybeObserver<LoginAndPassModel>() {
                            @Override
                            public void onSuccess(LoginAndPassModel loginAndPassModel) {
                                loginAndPassModel = new LoginAndPassModel(loginAndPassModel.login, loginAndPassModel.password);
                                setMessageInText(loginAndPassModel);
                                //открыть кнопку входа и резет
                                disposables.dispose();
                            }

                            @Override
                            public void onError(Throwable e) {
                                // ...
                                disposables.dispose();
                            }

                            @Override
                            public void onComplete() {
                                LoginAndPassModel loginAndPassModel = null;
                                setMessageInText(loginAndPassModel);
                                //открыть кнопку входа
                                disposables.dispose();
                            }
                        }));
    }

    public void btnSet() {

        getViewState().checkLoginAndPass();
    }

    public void loginInit(String login, String pass) {
        if (!Pattern.matches("^((8|\\+7)[\\- ]?)?(\\(?9\\d{2}\\)?[\\- ]?)?[\\d\\- ]{7}$", login)) {
            Toast toast = Toast.makeText(App.getInstance().getApplicationContext(),
                    App.getInstance().getResources().getString(R.string.wron_login), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", pass)) {
            Toast toast = Toast.makeText(App.getInstance().getApplicationContext(),
                    App.getInstance().getResources().getString(R.string.wron_pass), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (firstQueryLoginAndPassModel == null) {
            Completable.fromAction(() -> {
                LoginAndPassModel loginAndPassModel = new LoginAndPassModel(login, pass);
                App.getInstance().getDatabase().loginAndPassDao().insertAll(loginAndPassModel);
            }).subscribeOn(Schedulers.single()).subscribe();

            getViewState().goInUserListPage();
        } else {
            if ((firstQueryLoginAndPassModel.login.equals(login)) & (firstQueryLoginAndPassModel.password.equals(pass))) {
                getViewState().goInUserListPage();
            } else {
                getViewState().setSystemMassage(App.getInstance().getResources().getString(R.string.wrong_login_or_pass));
            }

        }
    }

    public void btnReset() {
        Completable.fromAction(() ->
                App.getInstance().getDatabase().loginAndPassDao().deleteAll()
        ).subscribeOn(Schedulers.single()).subscribe();
        firstQueryLoginAndPassModel = null;
        getViewState().blockButtonReset();
        getViewState().setSystemMassage(App.getInstance().getResources().getString(R.string.reset_was_done));

    }

    public void setMessageInText(LoginAndPassModel loginAndPassModel) {
        if (loginAndPassModel == null) {
            getViewState().setSystemMassage(App.getInstance().getResources().getString(R.string.entry_was_not_found));
            getViewState().blockButtonReset();
        } else {
            getViewState().setSystemMassage(App.getInstance().getResources().getString(R.string.entry_was_found));
        }
    }

    public void destroySubscribes() {
        disposables.clear();
    }


}
