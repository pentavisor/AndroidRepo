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

public class CreateUserActivity extends MvpAppCompatActivity implements CreateUserView {
    @InjectPresenter
    CreateUserPresenter createUserPresenter;

    RippleView secretCodeView;
    RippleView sensorNumberView;

    ImageView imagePhoto;
    TextView birthdayText;
    TextView firstName;
    EditText lastName;

    Switch sportSw;
    Switch flowerSw;
    Switch mushroomSw;
    Switch crazySw;

    UserProperties userProperties;
    UserDataModel userDataModel;

    Spinner spinner;
    ArrayAdapter<String> sexChoise;
    Button saveButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        saveButton =(Button) findViewById(R.id.save_button);

        birthdayText = (TextView) findViewById(R.id.Birthday);
        secretCodeView = (RippleView) findViewById(R.id.secret_code);
        sensorNumberView = (RippleView) findViewById(R.id.sensor_number);
        imagePhoto = (ImageView) findViewById(R.id.createPhotoImage);
        firstName = (TextView) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        spinner = (Spinner) findViewById(R.id.spinner);

        sportSw =(Switch) findViewById(R.id.sportSwith);
        flowerSw =(Switch) findViewById(R.id.flowerSwith);
        mushroomSw =(Switch) findViewById(R.id.mushroomSwith);
        crazySw =(Switch) findViewById(R.id.crazySwith);



        userProperties = new UserProperties(0,false,false,false,false);
        userDataModel = new UserDataModel(0,"","","","",false,"0000","1111", R.drawable.mustache,"",0);


        sexChoise = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sex_choice));
        spinner.setAdapter(sexChoise);
        createUserPresenter.calendarInit();


        sportSw.setOnCheckedChangeListener((c,b)-> userProperties.sport = b );
        flowerSw.setOnCheckedChangeListener((c,b)-> userProperties.flowers = b );
        mushroomSw.setOnCheckedChangeListener((c,b)-> userProperties.mushrooms = b );
        crazySw.setOnCheckedChangeListener((c,b)-> userProperties.funnyCat = b );


        saveButton.setOnClickListener((x)-> {

            userDataModel.lastName = lastName.getText().toString();
            userDataModel.firstName = lastName.getText().toString();// переделать в соответствии с тз
            userDataModel.imageLink = R.drawable.gears;
            createUserPresenter.saveDateInDateBase(userDataModel,userProperties,this);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               switch (i){
                   case (1):userDataModel.sex = true;
                   break;
                   case (2):userDataModel.sex = false;
                   break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
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

    private void showAddSensorNumberWindow (Context c) {
        final EditText taskEditText = new EditText (c);
        AlertDialog dialog = new AlertDialog.Builder (c)
                .setTitle(App.getInstance().getResources().getString(R.string.set_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_sensor_number))
                .setView (taskEditText)
                .setPositiveButton(App.getInstance().getResources().getString(R.string.put_one), (dialog1, which) -> userDataModel.sensorNumber = String.valueOf(taskEditText.getText()))
                .setNegativeButton(App.getInstance().getResources().getString(R.string.cancel), null)
                .create();
        dialog.show();
    }

    private void showAddSecretCodeWindow (Context c) {
        final EditText taskEditText = new EditText (c);
        taskEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        AlertDialog dialog = new AlertDialog.Builder (c)
                .setTitle(App.getInstance().getResources().getString(R.string.check_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_secret_code))
                .setView (taskEditText)
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
    public void setSystemText(String str){
        Toast toast = Toast.makeText(App.getInstance().getApplicationContext(),
                str, Toast.LENGTH_SHORT);
                toast.show();
    }
    @Override
    public void goToUserList(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

