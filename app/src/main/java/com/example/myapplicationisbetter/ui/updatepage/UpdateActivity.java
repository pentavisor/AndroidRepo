package com.example.myapplicationisbetter.ui.updatepage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;

public class UpdateActivity extends MvpAppCompatActivity implements UpdateView {

    @InjectPresenter
    UpdatePresenter updatePresenter;

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
        userDataModel =(UserDataModel)arguments.get("currentUserDataModel");
        userProperties =(UserProperties)arguments.get("currentUserProperties");
        arguments.clear();


        lastName = (EditText) findViewById(R.id.lastname);
        birthday = (TextView) findViewById(R.id.birthday);
        spinner =(Spinner) findViewById(R.id.spinner);

        sportSw =(Switch) findViewById(R.id.sportSwith);
        flowersSw =(Switch) findViewById(R.id.flowerSwith);
        mushroomsSw =(Switch) findViewById(R.id.mushroomSwith);
        crazySw =(Switch) findViewById(R.id.crazySwith);

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


//        startButton.setOnClickListener((x)-> {
//
//            userDataModel.lastName = lastName.getText().toString();
//            userDataModel.firstName = lastName.getText().toString();// переделать в соответствии с тз
//            userDataModel.imageLink = R.drawable.gears;
//            updatePresenter.sendNewUserInDataBase(userDataModel,userProperties);
//        });
    }
}
