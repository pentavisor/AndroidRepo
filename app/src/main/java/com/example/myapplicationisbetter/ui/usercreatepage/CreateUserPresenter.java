package com.example.myapplicationisbetter.ui.usercreatepage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserInetInform;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.MyHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState

public class CreateUserPresenter extends MvpPresenter<CreateUserView> {

    public final Calendar myCalendar = Calendar.getInstance();
    public DatePickerDialog.OnDateSetListener date;


    public void calendarInit() {
        date = (view, year, monthOfYear, dayOfMonth ) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        getViewState().setBirthdayText(sdf.format(myCalendar.getTime()));
    }

    public void initDataQueryToRandomUser(UserDataModel userDataModel, UserProperties userProperties) {

//        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorSecretCode)) {
//            getViewState().setSystemText(App.getInstance().getResources().getString(R.string.wrong_secret_code));
//            return;
//        }
//        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorNumber)) {
//            getViewState().setSystemText(App.getInstance().getResources().getString(R.string.wrong_sensor_number));
//            return;
//        }
//        if (!Pattern.matches("(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]{2,}", userDataModel.lastName)) {
//            getViewState().setSystemText(App.getInstance().getResources().getString(R.string.wrong_last_name));
//            return;
//        }
        if (!MyHelper.isNetworkConnected()) {
            getViewState().setSystemText(App.getInstance().getResources().getString(R.string.internet_error));
            return;
        }


        AsyncTask.execute(() -> {
            try {

                String u = "https://randomuser.me/api/?inc=name,picture&nat=us";
                URL randomUserEndPoint = new URL(u);
                HttpsURLConnection myConnection = (HttpsURLConnection) randomUserEndPoint.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    UserInetInform randomUserAPIParser = RandomUserAPIParser.goParse(responseBody);
                    userDataModel.firstName =randomUserAPIParser.getFirstNameInet();
                    userDataModel.imageName =randomUserAPIParser.getPhotoInet();
                    userDataModel.sex =randomUserAPIParser.getSex();
                    sendNewUserInDataBase(userDataModel, userProperties);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendNewUserInDataBase(UserDataModel userDataModel, UserProperties userProperties) {
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
        //getViewState().setSystemText(App.getInstance().getResources().getString(R.string.saving_was_done));
        getViewState().goToUserList();
    }


}

