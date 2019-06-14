package com.example.myapplicationisbetter.ui.userpage;


import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;

import io.reactivex.Completable;

import com.example.myapplicationisbetter.data.models.UserProperties;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public static final int TEST_USER_PROFILE_DATA = 0;
    public static final String TEST_USER_VISIBLE = "test_user";
    public static final int TEST_USER_ID = -2;

    String str = "Шаловливая Aнастасия любит горячих парней, чувственных с ноткой азарта она погружается в таких людей как в дорогую вечернюю горячую ванну, в которой хочется согреться еще и еще.... ";
    UserDataModel firstUdm = new UserDataModel(TEST_USER_ID, "Анастасия", "Шаловливая", "asdasdasd", "", false, "", "", R.drawable.mustache, "", TEST_USER_PROFILE_DATA);
    UserDataModel PlusUdm = new UserDataModel(BindingAdapters.ADD_BUTTON_ID, "asdsd", "", "", "", true, "", "", R.drawable.circle_add, "", 0);

    private UserDataModel currentUser = null;
    private Activity activity;
    private UsersRecyclerViewAdapter adapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MainPresenter(Activity activity,UsersRecyclerViewAdapter adapter) {

        this.activity = activity;
        this.adapter = adapter;
    }

    public void goToCreateUserPage() {

        getViewState().goCreateUserPage();
    }


    public void userListInit() {
        disposables.add(
                App.getInstance().getDatabase().userDao().getAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<UserDataModel>>() {
                            @Override
                            public void accept(List<UserDataModel> users) throws Exception {
                                showListOfUsers(users);

                            }
                        })
        );
    }

    private void showListOfUsers(List<UserDataModel> users){
       users.add(0, PlusUdm);
        if (loadPrefs()) {
            users.add(1, firstUdm);
        }else{
            if(users.contains(currentUser))
                users.remove(currentUser);
        }
        getViewState().setUserList(users);
    }

    public void deleteUser(List<UserDataModel> users) {
        if (currentUser == null) return;
        if (currentUser.id == TEST_USER_ID){
            savePrefs();//save test user as deleted
            List<UserDataModel> someUsers = new ArrayList<>();
            someUsers.addAll(users);
            someUsers.remove(PlusUdm);
            someUsers.remove(firstUdm);
            showListOfUsers(someUsers);
        }else{
            Completable.fromAction(() -> {
                App.getInstance().getDatabase().userDao().deleteUser(currentUser);
            }).subscribeOn(Schedulers.single())
                    .subscribe();
        }
    }

//    public void deleteUser2(List<UserDataModel> users) {
//        adapter.removeItem(3);
//    }

    public void setTextInFeature(UserDataModel userDataModel) {
        try {
            currentUser = userDataModel.clone();
        } catch (Exception e) {}

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
                        }
                    });
        } else {
            getViewState().setText(userDataModel, null);
        }
    }

    private void savePrefs() {
        SharedPreferences sPref = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(TEST_USER_VISIBLE, "visible");
        ed.commit();
    }

    private boolean loadPrefs() {
        SharedPreferences sPref = activity.getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(TEST_USER_VISIBLE, "");
        if (savedText.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
