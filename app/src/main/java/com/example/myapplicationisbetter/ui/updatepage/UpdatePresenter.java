package com.example.myapplicationisbetter.ui.updatepage;

import android.app.DatePickerDialog;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class UpdatePresenter extends MvpPresenter<UpdateView> {

    public final Calendar myCalendar = Calendar.getInstance();
    public DatePickerDialog.OnDateSetListener date;

    public void calendarInit() {
        date = (view, year, monthOfYear, dayOfMonth) -> {
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

    public void sendNewUserInDataBase(UserDataModel userDataModel, UserProperties userProperties) {

        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorSecretCode)) {
            getViewState().setSystemText("Неверный секретный код, попробуйте еще в рамках символов [0-9a-zA-Z]");
            return;
        }
        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}", userDataModel.sensorNumber)) {
            getViewState().setSystemText("Неверный номер сенсора, попробуйте еще в рамках символов [0-9a-zA-Z]");
            return;
        }
        if (!Pattern.matches("(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]{2,}", userDataModel.lastName)) {
            getViewState().setSystemText("Неверная фамилия, попробуйте еще в рамках символов [a-zA-Z]");
            return;
        }

        Completable.fromAction(() -> {
            App.getInstance().getDatabase().userDaoTransaction().updateUserAndProperties(userDataModel, userProperties);
        }).subscribeOn(Schedulers.single()).subscribe();
        getViewState().goToUserList();
    }


}
