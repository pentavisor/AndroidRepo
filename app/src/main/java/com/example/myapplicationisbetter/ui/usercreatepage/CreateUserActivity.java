package com.example.myapplicationisbetter.ui.usercreatepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.andexert.library.RippleView;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.userpage.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateUserActivity extends MvpAppCompatActivity implements CreateUserView {
    @InjectPresenter
    CreateUserPresenter createUserPresenter;

    @BindView(R.id.secret_code)
    RippleView secretCodeView;
    @BindView(R.id.sensor_number)
    RippleView sensorNumberView;
    @BindView(R.id.Birthday)
    TextView birthdayText;
    @BindView(R.id.lastname)
    EditText lastName;

    @BindView(R.id.sportSwith)
    Switch sportSw;
    @BindView(R.id.flowerSwith)
    Switch flowerSw;
    @BindView(R.id.mushroomSwith)
    Switch mushroomSw;
    @BindView(R.id.crazySwith)
    Switch crazySw;

    @BindView(R.id.save_button)
    Button saveButton;
//    @BindView(R.id.spinner)
//    Spinner spinner;

    UserProperties userProperties;
    UserDataModel userDataModel;

    ArrayAdapter<String> sexChoice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);

        userProperties = new UserProperties(0, false, false, false, false);
        userDataModel = new UserDataModel(0, "", "", "", "", false, "0000", "1111", R.drawable.mustache, "", 0);

        sexChoice = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sex_choice));
        createUserPresenter.calendarInit();

        sportSw.setOnCheckedChangeListener((c, b) -> userProperties.sport = b);
        flowerSw.setOnCheckedChangeListener((c, b) -> userProperties.flowers = b);
        mushroomSw.setOnCheckedChangeListener((c, b) -> userProperties.mushrooms = b);
        crazySw.setOnCheckedChangeListener((c, b) -> userProperties.funnyCat = b);


        saveButton.setOnClickListener((x) -> {

            userDataModel.lastName = lastName.getText().toString();
            userDataModel.imageLink = R.drawable.gears;
            createUserPresenter.initDataQueryToRandomUser(userDataModel, userProperties);
        });


        birthdayText.setOnClickListener(v -> {
            if ((createUserPresenter.date != null) & (createUserPresenter.myCalendar != null))
                new DatePickerDialog(CreateUserActivity.this, createUserPresenter.date, createUserPresenter.myCalendar
                        .get(Calendar.YEAR), createUserPresenter.myCalendar.get(Calendar.MONTH),
                        createUserPresenter.myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        sensorNumberView.setOnClickListener(v -> showAddSensorNumberWindow(CreateUserActivity.this));

        secretCodeView.setOnClickListener(v -> showAddSecretCodeWindow(CreateUserActivity.this));


   }

    private void showAddSensorNumberWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(App.getInstance().getResources().getString(R.string.set_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_sensor_number))
                .setView(taskEditText)
                .setPositiveButton(App.getInstance().getResources().getString(R.string.put_one), (dialog1, which) -> userDataModel.sensorNumber = String.valueOf(taskEditText.getText()))
                .setNegativeButton(App.getInstance().getResources().getString(R.string.cancel), null)
                .create();
        dialog.show();
    }

    private void showAddSecretCodeWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(App.getInstance().getResources().getString(R.string.check_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_secret_code))
                .setView(taskEditText)
                .setPositiveButton(App.getInstance().getResources().getString(R.string.put_one), (dialog1, which) -> userDataModel.sensorSecretCode = String.valueOf(taskEditText.getText()))
                .setNegativeButton(App.getInstance().getResources().getString(R.string.cancel), null)
                .create();
        dialog.show();
    }

    @Override
    public void setBirthdayText(String text) {
        birthdayText.setText(text);
        userDataModel.birthday = text;
    }

    @Override
    public void setSystemText(String str) {
        Toast toast = Toast.makeText(App.getInstance().getApplicationContext(),
                str, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void goToUserList() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}

