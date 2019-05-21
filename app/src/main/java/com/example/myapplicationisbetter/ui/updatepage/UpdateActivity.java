package com.example.myapplicationisbetter.ui.updatepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.userpage.MainActivity;

import java.util.Calendar;

public class UpdateActivity extends MvpAppCompatActivity implements UpdateView {

    @InjectPresenter
    UpdatePresenter updatePresenter;

    RippleView secretCodeView;
    RippleView sensorNumberView;

    EditText lastName;
    TextView birthday;
    Spinner spinner;

    Switch sportSw;
    Switch flowersSw;
    Switch mushroomsSw;
    Switch crazySw;

    Button startButton;

    UserDataModel userDataModel;
    UserProperties userProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Bundle arguments = getIntent().getExtras();
        userDataModel = (UserDataModel) arguments.get("currentUserDataModel");
        userProperties = (UserProperties) arguments.get("currentUserProperties");
        arguments.clear();
        updatePresenter.calendarInit();

        startButton = (Button) findViewById(R.id.save_button);

        lastName = (EditText) findViewById(R.id.lastname);
        birthday = (TextView) findViewById(R.id.birthday);
        spinner = (Spinner) findViewById(R.id.spinner);
        secretCodeView = (RippleView) findViewById(R.id.secret_code);
        sensorNumberView = (RippleView) findViewById(R.id.sensor_number);

        sportSw = (Switch) findViewById(R.id.sportSwith);
        flowersSw = (Switch) findViewById(R.id.flowerSwith);
        mushroomsSw = (Switch) findViewById(R.id.mushroomSwith);
        crazySw = (Switch) findViewById(R.id.crazySwith);

        sportSw.setChecked(userProperties.sport);
        flowersSw.setChecked(userProperties.flowers);
        mushroomsSw.setChecked(userProperties.mushrooms);
        crazySw.setChecked(userProperties.funnyCat);

        lastName.setText(userDataModel.lastName);
        birthday.setText(userDataModel.birthday);
        if (userDataModel.sex) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }


        sportSw.setOnCheckedChangeListener((c,b)-> userProperties.sport = b );
        flowersSw.setOnCheckedChangeListener((c,b)-> userProperties.flowers = b );
        mushroomsSw.setOnCheckedChangeListener((c,b)-> userProperties.mushrooms = b );
        crazySw.setOnCheckedChangeListener((c,b)-> userProperties.funnyCat = b );

        sensorNumberView.setOnClickListener(v -> showAddSensorNumberWindow(UpdateActivity.this));

        secretCodeView.setOnClickListener(v -> showAddSecretCodeWindow(UpdateActivity.this));
        birthday.setOnClickListener(v -> {
            if ((updatePresenter.date != null) & (updatePresenter.myCalendar != null))
                new DatePickerDialog(UpdateActivity.this, updatePresenter.date, updatePresenter.myCalendar
                        .get(Calendar.YEAR), updatePresenter.myCalendar.get(Calendar.MONTH),
                        updatePresenter.myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        startButton.setOnClickListener((x) -> {
            userDataModel.birthday = birthday.getText().toString();
            userDataModel.lastName = lastName.getText().toString();
            if (spinner.getSelectedItemPosition() == 1) {
                userDataModel.sex = true;
            } else {
                userDataModel.sex = false;
            }
            updatePresenter.sendNewUserInDataBase(userDataModel, userProperties);
        });
    }

    private void showAddSensorNumberWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Добвьте датчик")
                .setMessage("Введите номер датчика")
                .setView(taskEditText)
                .setPositiveButton("Добавить", (dialog1, which) -> userDataModel.sensorNumber = String.valueOf(taskEditText.getText()))
                .setNegativeButton("Отмена", null)
                .create();
        dialog.show();
    }

    private void showAddSecretCodeWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Проверка датчика")
                .setMessage("Введите секретный код")
                .setView(taskEditText)
                .setPositiveButton("Добавить", (dialog1, which) -> userDataModel.sensorSecretCode = String.valueOf(taskEditText.getText()))
                .setNegativeButton("Отмена", null)
                .create();
        dialog.show();
    }

    @Override
    public void goToUserList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void setBirthdayText(String string){
        birthday.setText(string);
    }

    @Override
    public void setSystemText(String str){
        Toast toast = Toast.makeText(App.getInstance().getApplicationContext(),
                str, Toast.LENGTH_SHORT);
        toast.show();
    }}
