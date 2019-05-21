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

        Completable.fromAction(() -> {
            App.getInstance().getDatabase().userDaoTransaction().updateUserAndProperties(userDataModel, userProperties);
        }).subscribeOn(Schedulers.single()).subscribe();
        getViewState().goToUserList();
    }


}
