package com.example.myapplicationisbetter.ui.usercreatepage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.LoginAndPassModel;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.example.myapplicationisbetter.ui.userpage.UserAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState

public class CreateUserPresenter extends MvpPresenter<CreateUserView> {

    public final Calendar myCalendar = Calendar.getInstance();
    public DatePickerDialog.OnDateSetListener date;

    public void calendarInit() {
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        getViewState().setBirthdayText(sdf.format(myCalendar.getTime()));
    }

    private void nextChainImage(UserDataModel userDataModel, UserProperties userProperties, Context context) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String u = "https://randomuser.me/api/?inc=picture";
                    URL randomUserEndPoint = new URL(u);
                    HttpsURLConnection myConnection = (HttpsURLConnection) randomUserEndPoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReaderForName = new JsonReader(responseBodyReader);
                        jsonReaderForName.beginObject();
                        while (jsonReaderForName.hasNext()) {
                            String key = jsonReaderForName.nextName();
                            if (key.equals("results")) {
                                jsonReaderForName.beginArray();
                                while (jsonReaderForName.hasNext()) {
                                    jsonReaderForName.beginObject();
                                    while (jsonReaderForName.hasNext()) {
                                        String key2 = jsonReaderForName.nextName();
                                        if (key2.equals("picture")) {
                                            jsonReaderForName.beginObject();
                                            while (jsonReaderForName.hasNext()) {
                                                String key3 = jsonReaderForName.nextName();
                                                if (key3.equals("large")) {
                                                    userDataModel.imageName = jsonReaderForName.nextString();
                                                    sendNewUserInDataBase(userDataModel, userProperties);

                                                } else {
                                                    jsonReaderForName.nextString();
                                                }
                                            }
                                        } else {
                                            jsonReaderForName.nextString();
                                        }
                                    }
                                }
                                break;
                            } else {
                                jsonReaderForName.skipValue();
                            }
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveDateInDateBase(UserDataModel userDataModel, UserProperties userProperties, Context context) {

        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorSecretCode)) {
            getViewState().setSystemText("Неверный секретный код, попробуйте еще");
            return;
        }
        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorNumber)) {
            getViewState().setSystemText("Неверный номер сенсора, попробуйте еще");
            return;
        }
        if (!Pattern.matches("(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]{2,}", userDataModel.lastName)) {
            getViewState().setSystemText("Неверная фамилия, попробуйте еще");
            return;
        }
        if (!MyHelper.isNetworkConnected()) {
            getViewState().setSystemText("Включите доступ к интернету пожалуйста");
            return;
        }
        //get user name

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String u = "https://randomuser.me/api/?inc=name";
                    URL randomUserEndPoint = new URL(u);
                    HttpsURLConnection myConnection = (HttpsURLConnection) randomUserEndPoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReaderForName = new JsonReader(responseBodyReader);
                        jsonReaderForName.beginObject();
                        while (jsonReaderForName.hasNext()) {
                            String key = jsonReaderForName.nextName();
                            if (key.equals("results")) {
                                jsonReaderForName.beginArray();
                                while (jsonReaderForName.hasNext()) {
                                    jsonReaderForName.beginObject();
                                    while (jsonReaderForName.hasNext()) {
                                        String key2 = jsonReaderForName.nextName();
                                        if (key2.equals("name")) {
                                            jsonReaderForName.beginObject();
                                            while (jsonReaderForName.hasNext()) {
                                                String key3 = jsonReaderForName.nextName();
                                                if (key3.equals("first")) {
                                                    userDataModel.firstName = jsonReaderForName.nextString();
                                                    nextChainImage(userDataModel, userProperties, context);
                                                } else {
                                                    jsonReaderForName.nextString();
                                                }
                                            }
                                        } else {
                                            jsonReaderForName.nextString();
                                        }
                                    }
                                }
                                break;
                            } else {
                                jsonReaderForName.skipValue();
                            }
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
        getViewState().setSystemText("Сохранение Успешно");
        getViewState().goToUserList();
    }


}

